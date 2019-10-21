package com.hj.tj.gohome.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hj.tj.gohome.config.WxMaConfiguration;
import com.hj.tj.gohome.config.handler.ServiceException;
import com.hj.tj.gohome.config.handler.ServiceExceptionEnum;
import com.hj.tj.gohome.config.jwt.TokenHelper;
import com.hj.tj.gohome.consts.OwnerConstants;
import com.hj.tj.gohome.entity.Owner;
import com.hj.tj.gohome.enums.StatusEnum;
import com.hj.tj.gohome.mapper.OwnerMapper;
import com.hj.tj.gohome.service.OwnerService;
import com.hj.tj.gohome.vo.login.WxLoginReqObj;
import com.hj.tj.gohome.vo.login.WxLoginResObj;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Resource
    private OwnerMapper ownerMapper;

    @Resource
    private TokenHelper tokenHelper;

    @Override
    public WxLoginResObj login(WxLoginReqObj wxLoginReqObj, String appId) throws Exception {
        WxMaService wxMaService = WxMaConfiguration.getMaService(appId);
        if (Objects.isNull(wxMaService)) {
            throw new ServiceException(ServiceExceptionEnum.APP_ID_ERROR);
        }

        WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(wxLoginReqObj.getCode());

        WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(session.getSessionKey(),
                wxLoginReqObj.getEncryptedData(), wxLoginReqObj.getIv());

        if (Objects.isNull(userInfo)) {
            throw new ServiceException(ServiceExceptionEnum.WX_GET_USER_ERROR);
        }

        Owner owner = ownerMapper.selectOne(Wrappers.<Owner>query().lambda()
                .eq(Owner::getOpenId, userInfo.getOpenId())
                .eq(Owner::getStatus, StatusEnum.UN_DELETE.getStatus()));
        if (Objects.isNull(owner)) {
            owner = new Owner();
            BeanUtils.copyProperties(userInfo, owner);

            owner.setWxNickname(userInfo.getNickName());
            owner.setGender(Integer.parseInt(userInfo.getGender()));
            owner.setAppId(appId);
            ownerMapper.insert(owner);
        } else {
            owner.setWxNickname(userInfo.getNickName());
            owner.setGender(Integer.parseInt(userInfo.getGender()));
            ownerMapper.updateById(owner);
        }

        WxLoginResObj wxLoginResObj = new WxLoginResObj();
        wxLoginResObj.setId(owner.getId());
        wxLoginResObj.setNickname(owner.getWxNickname());
        wxLoginResObj.setAvatarUrl(owner.getAvatarUrl());

        wxLoginResObj.setToken(tokenHelper.generate(owner.getId(), OwnerConstants.FROM_MINIAPP, owner.getWxNickname()));

        return wxLoginResObj;
    }

    @Override
    public String refreshToken(String code, String appId) throws Exception {
        WxMaService wxMaService = WxMaConfiguration.getMaService(appId);
        if (Objects.isNull(wxMaService)) {
            throw new ServiceException(ServiceExceptionEnum.APP_ID_ERROR);
        }

        WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);

        Owner owner = ownerMapper.selectOne(Wrappers.<Owner>query().lambda().eq(Owner::getOpenId, session.getOpenid()));
        if (Objects.isNull(owner)) {
            throw new ServiceException(ServiceExceptionEnum.OWNER_NOT_EXISTS);
        }

        return tokenHelper.generate(owner.getId(), "miniapp", owner.getWxNickname());
    }

    @Override
    public String createTourist() {
        Owner owner = new Owner();
        owner.setCreatedAt(new Date());
        ownerMapper.insert(owner);

        return tokenHelper.generate(owner.getId(), "h5", "");
    }

    @Override
    public List<Owner> selectByIds(List<Integer> ownerIds) {
        if (CollectionUtils.isEmpty(ownerIds)) {
            return new ArrayList<>();
        }

        return ownerMapper.selectList(Wrappers.<Owner>query().lambda().in(Owner::getId, ownerIds));
    }
}
