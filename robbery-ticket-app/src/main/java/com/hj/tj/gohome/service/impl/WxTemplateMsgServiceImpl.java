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
import com.hj.tj.gohome.enums.WxFormIdHasUseEnum;
import com.hj.tj.gohome.mapper.OwnerMapper;
import com.hj.tj.gohome.mapper.WxFormIdMapper;
import com.hj.tj.gohome.service.WxTemplateMsgService;
import com.hj.tj.gohome.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

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

    @Override
    public boolean sendMsg(List<String> keywordList,
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
            log.error("send speed template msg error. toUserOpenId:{}, formId:{}, keywordList:{}", toUserOpenId, formId, keywordList);
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
        keywordList.add("您有新的订单，用户Id:" + owner.getId() + ",用户微信昵称:" + owner.getWxNickname() +
                ", 用户出发站:" + order.getOrigin() + ", 用户目的站:" + order.getDestination());
        keywordList.add(DateUtil.DateToString(new Date()));
        keywordList.add("已提交");

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
                    templateId = config.getMsgTemplateMap().get("newOrder");
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
}
