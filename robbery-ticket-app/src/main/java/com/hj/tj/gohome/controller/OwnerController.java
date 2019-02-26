package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.service.OwnerService;
import com.hj.tj.gohome.vo.login.WxLoginReqObj;
import com.hj.tj.gohome.vo.login.WxLoginResObj;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api")
@Validated
public class OwnerController {

    @Resource
    private OwnerService ownerService;

    @PostMapping("/owner/login")
    public ResponseEntity login(@RequestBody WxLoginReqObj wxLoginReqObj) throws Exception {
        WxLoginResObj wxLoginResObj = ownerService.login(wxLoginReqObj);

        return ResponseEntity.ok(wxLoginResObj);
    }

    @GetMapping("/owner/refresh/{code}")
    public ResponseEntity refreshToken(@NotBlank(message = "微信code 不能为空") @PathVariable("code") String code) throws Exception {
        String sid = ownerService.refreshToken(code);

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, sid).build();
    }

    @GetMapping("/owner/create/tourist")
    public ResponseEntity createTourist() throws Exception {
        String sid = ownerService.createTourist();

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, sid).build();
    }

}
