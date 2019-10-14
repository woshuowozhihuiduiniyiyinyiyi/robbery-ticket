package com.hj.tj.gohome.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hj.tj.gohome.config.WxMaConfiguration;
import com.hj.tj.gohome.config.WxMaProperties;
import com.hj.tj.gohome.entity.Order;
import com.hj.tj.gohome.entity.Owner;
import com.hj.tj.gohome.entity.WxFormId;
import com.hj.tj.gohome.entity.WxTemplateMsg;
import com.hj.tj.gohome.enums.WxFormIdHasUseEnum;
import com.hj.tj.gohome.enums.WxTemplateMsgHasPushEnum;
import com.hj.tj.gohome.enums.WxTemplateMsgPushTypeEnum;
import com.hj.tj.gohome.mapper.OwnerMapper;
import com.hj.tj.gohome.mapper.WxFormIdMapper;
import com.hj.tj.gohome.mapper.WxTemplateMsgMapper;
import com.hj.tj.gohome.service.WxTemplateMsgService;
import com.hj.tj.gohome.utils.DateUtil;
import com.hj.tj.gohome.utils.OwnerContextHelper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@EnableConfigurationProperties(WxMaProperties.class)
public class WxTemplateMsgServiceImpl implements WxTemplateMsgService {

    @Resource
    private WxMaProperties properties;

    @Resource
    private OwnerMapper ownerMapper;

    @Resource
    private WxFormIdMapper wxFormIdMapper;

    @Resource
    private WxTemplateMsgMapper wxTemplateMsgMapper;

    private static final String NEW_ORDER_TEMPLATE = "newOrder";
    private static final String SPEED_DYNAMIC_TEMPLATE = "speedDynamic";

    private boolean sendMsg(List<String> keywordList,
                           String formId,
                           String toUserOpenId,
                           String appId,
                           String templateId,
                           String page) {
        List<WxMaTemplateData> msgList = new ArrayList<>();
        for (int i = 0; i < keywordList.size(); i++) {
            String keywordStr = "keyword" + (i + 1);
            msgList.add(new WxMaTemplateData(keywordStr, keywordList.get(i)));
        }

        try {
            WxMaConfiguration.getMaService(appId).getMsgService().sendTemplateMsg(
                    WxMaTemplateMessage.builder()
                            .templateId(templateId)
                            .formId(formId)
                            .data(msgList)
                            .toUser(toUserOpenId)
                            .page(page)
                            .build());
        } catch (WxErrorException e) {
            log.error("send speed template msg error. toUserOpenId:{}, formId:{}, keywordList:{}, e:{}", toUserOpenId, formId, keywordList, e);
            return false;
        }

        return true;
    }

    @Override
    public boolean sendNewOrderMsg(Order order) {
        Owner owner = ownerMapper.selectById(order.getOwnerId());
        if (Objects.isNull(owner) || StringUtils.isEmpty(owner.getAppId())) {
            return false;
        }

        List<String> keywordList = new ArrayList<>();
        keywordList.add(owner.getWxNickname());
        keywordList.add(DateUtil.DateToString(new Date()));
        keywordList.add("您有新的订单，用户Id:" + owner.getId() + ",用户微信昵称:" + owner.getWxNickname() +
                ", 用户出发站:" + order.getOrigin() + ", 用户目的站:" + order.getDestination());

        List<Owner> portalOwners = ownerMapper.selectList(Wrappers.<Owner>query().lambda().gt(Owner::getPortalUserId, 0));
        if (CollectionUtils.isEmpty(portalOwners)) {
            return false;
        }

        for (Owner portalOwner : portalOwners) {
            List<WxFormId> wxFormIds = wxFormIdMapper.selectList(Wrappers.<WxFormId>query().lambda()
                    .eq(WxFormId::getOwnerId, portalOwner.getId())
                    .eq(WxFormId::getHasUse, WxFormIdHasUseEnum.NOT_USE.getValue())
                    .ge(WxFormId::getExpireDate, new Date())
                    .orderByAsc(WxFormId::getExpireDate));
            if (CollectionUtils.isEmpty(wxFormIds)) {
                continue;
            }

            String templateId = "";
            for (WxMaProperties.Config config : properties.getConfigs()) {
                if (config.getAppid().equals(owner.getAppId())) {
                    templateId = config.getMsgTemplateMap().get(NEW_ORDER_TEMPLATE);
                    break;
                }
            }

            if (!StringUtils.isEmpty(templateId)) {
                WxFormId wxFormId = wxFormIds.get(0);
                wxFormId.setHasUse(WxFormIdHasUseEnum.USED.getValue());
                wxFormIdMapper.updateById(wxFormId);

                return sendMsg(keywordList, wxFormId.getFormId(), portalOwner.getOpenId(), owner.getAppId(), templateId, "pages/index/index");
            }
        }

        return false;
    }

    @Override
    public Integer pushMsg() {
        List<WxTemplateMsg> wxTemplateMsgList = wxTemplateMsgMapper.selectList(Wrappers.<WxTemplateMsg>query().lambda()
                .eq(WxTemplateMsg::getHasPush, WxTemplateMsgHasPushEnum.NOT_PUSH.getValue())
                .le(WxTemplateMsg::getPushCount, 3));
        if (CollectionUtils.isEmpty(wxTemplateMsgList)) {
            log.info("[action = `WxTemplateMsgServiceImpl.pushMsg`. have no message need push.]");
            return 0;
        }

        Map<Integer, Owner> ownerMap = getOwnerMap(wxTemplateMsgList);
        if (CollectionUtils.isEmpty(ownerMap)) {
            log.info("[action = `WxTemplateMsgServiceImpl.pushMsg`. ownerMap is empty.]");
            updatePushCount(wxTemplateMsgList);
            return 0;
        }

        log.info("[action = `WxTemplateMsgServiceImpl.pushMsg`. push start.wxTemplateMsgList:{}]", wxTemplateMsgList);
        Integer count = 0;
        for (WxTemplateMsg wxTemplateMsg : wxTemplateMsgList) {
            Owner owner = ownerMap.get(wxTemplateMsg.getOwnerId());
            Owner pushOwner = ownerMap.get(wxTemplateMsg.getPushOwnerId());
            if (Objects.isNull(owner) || Objects.isNull(pushOwner)) {
                wxTemplateMsg.setPushCount(wxTemplateMsg.getPushCount() + 1);
                wxTemplateMsgMapper.updateById(wxTemplateMsg);
                continue;
            }

            List<String> keywordList = new ArrayList<>();
            keywordList.add(owner.getWxNickname());
            keywordList.add(DateUtil.DateToString(wxTemplateMsg.getCreatedAt()));
            WxTemplateMsgPushTypeEnum wxTemplateMsgPushTypeEnum = WxTemplateMsgPushTypeEnum.getByValue(wxTemplateMsg.getPushType());
            if (Objects.nonNull(wxTemplateMsgPushTypeEnum)) {
                keywordList.add(wxTemplateMsgPushTypeEnum.getDescription());
            }

            List<WxFormId> wxFormIds = wxFormIdMapper.selectList(Wrappers.<WxFormId>query().lambda()
                    .eq(WxFormId::getOwnerId, wxTemplateMsg.getPushOwnerId())
                    .eq(WxFormId::getHasUse, WxFormIdHasUseEnum.NOT_USE.getValue())
                    .ge(WxFormId::getExpireDate, new Date())
                    .orderByAsc(WxFormId::getExpireDate));
            if (CollectionUtils.isEmpty(wxFormIds)) {
                wxTemplateMsg.setPushCount(wxTemplateMsg.getPushCount() + 1);
                wxTemplateMsgMapper.updateById(wxTemplateMsg);
                continue;
            }

            String templateId = "";
            for (WxMaProperties.Config config : properties.getConfigs()) {
                if (config.getAppid().equals(owner.getAppId())) {
                    templateId = config.getMsgTemplateMap().get(SPEED_DYNAMIC_TEMPLATE);
                    break;
                }
            }

            if (StringUtils.isEmpty(templateId)) {
                log.error("[action = `WxTemplateMsgServiceImpl.pushMsg`. templateId config error.]");
                return 0;
            }

            WxFormId wxFormId = wxFormIds.get(0);
            boolean sendSuccess = sendMsg(keywordList, wxFormId.getFormId(), pushOwner.getOpenId(), pushOwner.getAppId(), templateId, "pages/index/index");
            if (sendSuccess) {
                wxTemplateMsg.setHasPush(WxTemplateMsgHasPushEnum.PUSHED.getValue());
                count += 1;
            }
            wxTemplateMsg.setPushCount(wxTemplateMsg.getPushCount() + 1);
            wxTemplateMsg.setUpdatedAt(new Date());
            wxTemplateMsgMapper.updateById(wxTemplateMsg);

            wxFormId.setHasUse(WxFormIdHasUseEnum.USED.getValue());
            wxFormId.setUpdatedAt(new Date());
            wxFormIdMapper.updateById(wxFormId);
        }

        return count;
    }

    @Override
    public void addNewMsg(Integer ownerId, Integer pushOwnerId) {
        WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
        wxTemplateMsg.setHasPush(WxTemplateMsgHasPushEnum.NOT_PUSH.getValue());
        wxTemplateMsg.setUpdatedAt(new Date());
        wxTemplateMsg.setOwnerId(ownerId);
        wxTemplateMsg.setPushOwnerId(pushOwnerId);
        wxTemplateMsg.setCreator(OwnerContextHelper.getOwnerId().toString());
        wxTemplateMsg.setUpdater(OwnerContextHelper.getOwnerId().toString());

        wxTemplateMsgMapper.insert(wxTemplateMsg);
    }

    /**
     * 更新推送次数+1
     *
     * @param wxTemplateMsgList 模板消息列表
     */
    private void updatePushCount(List<WxTemplateMsg> wxTemplateMsgList) {
        for (WxTemplateMsg wxTemplateMsg : wxTemplateMsgList) {
            wxTemplateMsg.setPushCount(wxTemplateMsg.getPushCount() + 1);
            wxTemplateMsgMapper.updateById(wxTemplateMsg);
        }
    }

    /**
     * 获取业主Map
     *
     * @param wxTemplateMsgList 模板消息列表
     * @return 业主Map
     */
    private Map<Integer, Owner> getOwnerMap(List<WxTemplateMsg> wxTemplateMsgList) {
        List<Integer> ownerIds = wxTemplateMsgList.stream().map(WxTemplateMsg::getOwnerId).collect(Collectors.toList());
        List<Integer> pushOwnerIds = wxTemplateMsgList.stream().map(WxTemplateMsg::getPushOwnerId).collect(Collectors.toList());
        ownerIds.addAll(pushOwnerIds);
        List<Owner> owners = ownerMapper.selectList(Wrappers.<Owner>query().lambda().in(Owner::getId, ownerIds));
        if (CollectionUtils.isEmpty(owners)) {
            return new HashMap<>();
        }

        return owners.stream().collect(Collectors.toMap(Owner::getId, o -> o));
    }
}
