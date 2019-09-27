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

    @PostMapping("/owner/login/{appId}")
    public ResponseEntity login(@RequestBody WxLoginReqObj wxLoginReqObj,
                                @NotBlank(message = "appid 不能为空") @PathVariable("appId") String appId) throws Exception {
        WxLoginResObj wxLoginResObj = ownerService.login(wxLoginReqObj, appId);

        return ResponseEntity.ok(wxLoginResObj);
    }

    @GetMapping("/owner/refresh/{appId}/{code}")
    public ResponseEntity refreshToken(@NotBlank(message = "微信code 不能为空") @PathVariable("code") String code,
                                       @NotBlank(message = "微信appid 不能为空") @PathVariable("appId") String appId) throws Exception{
        String sid = ownerService.refreshToken(code, appId);

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, sid).build();
    }

    @GetMapping("/owner/create/tourist")
    public ResponseEntity createTourist() {
        String sid = ownerService.createTourist();

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, sid).build();
    }

}
