package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.service.WxFormIdService;
import com.hj.tj.gohome.vo.form.WxFormIdParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class WxFormIdController {

    @Resource
    private WxFormIdService wxFormIdService;

    @PostMapping("/form/id/save")
    public ResponseEntity formIdSave(@Validated @RequestBody WxFormIdParam wxFormIdParam) {
        wxFormIdService.wxFormIdSave(wxFormIdParam);

        return ResponseEntity.ok().build();
    }
}
