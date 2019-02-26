package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.config.handler.ServiceException;
import com.hj.tj.gohome.config.handler.ServiceExceptionEnum;
import com.hj.tj.gohome.service.PortalUserService;
import com.hj.tj.gohome.vo.requestVo.LoginReqObj;
import com.hj.tj.gohome.vo.responseVO.LoginResObj;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author tangj
 * @description
 * @since 2018/10/10 15:59
 */
@RestController
@RequestMapping("/api")
public class PortalUserController {

    @Resource
    private PortalUserService portalUserService;

    @PostMapping("/portalUser/login")
    public ResponseEntity login(@Validated @RequestBody LoginReqObj loginReqObj) {
        LoginResObj loginResObj = portalUserService.login(loginReqObj.getAccount(), loginReqObj.getPassword());
        if (StringUtils.isEmpty(loginResObj)) {
            throw new ServiceException(ServiceExceptionEnum.USER_LOGIN_ERROR);
        }

        return ResponseEntity.ok(loginResObj);
    }

    @GetMapping("/auth/portalUser/list")
    public ResponseEntity listPortalUserResObj() {
        return ResponseEntity.ok(portalUserService.listPortalUserResObj());
    }
}
