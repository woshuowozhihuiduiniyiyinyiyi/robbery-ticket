package com.hj.tj.gohome.service;

import com.hj.tj.gohome.entity.Owner;
import com.hj.tj.gohome.vo.login.WxLoginReqObj;
import com.hj.tj.gohome.vo.login.WxLoginResObj;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

public interface OwnerService {

    /**
     * 微信登录
     *
     * @param wxLoginReqObj
     * @return
     */
    WxLoginResObj login(WxLoginReqObj wxLoginReqObj, String appId) throws Exception;

    /**
     * 刷新token
     *
     * @param code 微信返回的code
     * @return
     */
    String refreshToken(String code, String appId) throws Exception;

    /**
     * 生成游客
     *
     * @return
     */
    String createTourist();

    /**
     * 根据id列表获取客户信息
     */
    List<Owner> selectByIds(List<Integer> ownerIds);
}
