package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.service.RobbingTicketUserService;
import com.hj.tj.gohome.vo.requestVo.RobbingTicketUserReqObj;
import com.hj.tj.gohome.vo.responseVO.RobbingTicketUserResObj;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2018/10/17 15:05
 */
@RestController
@RequestMapping("/api/auth")
public class RobbingTicketUserController {

    @Resource
    private RobbingTicketUserService robbingTicketUserService;

    @PostMapping("/robbing/ticket/user/list")
    public ResponseEntity listRobbingTicketUser(@Validated @RequestBody RobbingTicketUserReqObj robbingTicketUserReqObj) {
        List<RobbingTicketUserResObj> robbingTicketUserResObjs = robbingTicketUserService.listRobbingTicketUserResObj(robbingTicketUserReqObj);

        return ResponseEntity.ok(robbingTicketUserResObjs);
    }

}
