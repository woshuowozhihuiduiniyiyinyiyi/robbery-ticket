package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.tj.gohome.config.handler.ServiceException;
import com.hj.tj.gohome.config.handler.ServiceExceptionEnum;
import com.hj.tj.gohome.config.jwt.TokenHelper;
import com.hj.tj.gohome.entity.PortalUser;
import com.hj.tj.gohome.enums.BaseStatusEnum;
import com.hj.tj.gohome.enums.GenderEnum;
import com.hj.tj.gohome.mapper.PortalUserMapper;
import com.hj.tj.gohome.service.PortalUserService;
import com.hj.tj.gohome.utils.DateUtil;
import com.hj.tj.gohome.vo.responseVO.LoginResObj;
import com.hj.tj.gohome.vo.responseVO.PortalUserResObj;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author tangj
 * @description
 * @since 2018/10/10 10:37
 */
@Service
public class PortalUserServiceImpl implements PortalUserService {

    @Resource
    private PortalUserMapper portalUserMapper;

    @Resource
    private TokenHelper tokenHelper;

    @Override
    public PortalUser getPortalUserBySid(String sid) {
        QueryWrapper<PortalUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid).eq("status", BaseStatusEnum.UN_DELETE.getValue());

        List<PortalUser> portalUsers = portalUserMapper.selectList(queryWrapper);

        if (CollectionUtils.isEmpty(portalUsers)) {
            return null;
        }

        return portalUsers.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LoginResObj login(String account, String password) {
        QueryWrapper<PortalUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account).eq("password", password)
                .eq("status", BaseStatusEnum.UN_DELETE.getValue());
        PortalUser portalUser = portalUserMapper.selectOne(queryWrapper);
        if (Objects.isNull(portalUser)) {
            throw new ServiceException(ServiceExceptionEnum.USER_LOGIN_ERROR);
        }

        LoginResObj loginResObj = genLoginResObj(portalUser);

        return loginResObj;
    }

    @Override
    public List<PortalUserResObj> listPortalUserResObj() {
        QueryWrapper<PortalUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", BaseStatusEnum.UN_DELETE.getValue());

        List<PortalUser> portalUsers = portalUserMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(portalUsers)) {
            return null;
        }

        List<PortalUserResObj> portalUserResObjs = new ArrayList<>();
        for (PortalUser portalUser : portalUsers) {
            PortalUserResObj portalUserResObj = new PortalUserResObj();
            BeanUtils.copyProperties(portalUser, portalUserResObj);

            portalUserResObjs.add(portalUserResObj);
        }

        return portalUserResObjs;
    }

    private LoginResObj genLoginResObj(PortalUser portalUser) {
        LoginResObj loginResObj = new LoginResObj();

        loginResObj.setUserId(portalUser.getId());
        loginResObj.setUserName(portalUser.getName());
        loginResObj.setPhone(portalUser.getPhone());

        loginResObj.setGender(Objects.equals(GenderEnum.MAN.getValue(), portalUser.getGender()) ?
                GenderEnum.MAN.getDescription() : GenderEnum.WOMAN.getDescription());

        String sid = tokenHelper.generate(portalUser.getId(), portalUser.getName());
        loginResObj.setToken(sid);

        return loginResObj;
    }
}
