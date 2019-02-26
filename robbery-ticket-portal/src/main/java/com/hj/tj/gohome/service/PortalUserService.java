package com.hj.tj.gohome.service;

import com.hj.tj.gohome.entity.PortalUser;
import com.hj.tj.gohome.vo.responseVO.LoginResObj;
import com.hj.tj.gohome.vo.responseVO.PortalUserResObj;

import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2018/10/10 10:35
 */
public interface PortalUserService {

    /**
     * 根据sid 来获取用户
     *
     * @param sid
     * @return
     */
    PortalUser getPortalUserBySid(String sid);

    /**
     * 根据用户名和密码来获取用户
     *
     * @param account
     * @param password
     * @return
     */
    LoginResObj login(String account, String password);

    /**
     * 获取用户列表
     *
     * @return
     */
    List<PortalUserResObj> listPortalUserResObj();

}
