package com.hj.tj.gohome.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.hj.tj.gohome.config.WxMaConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/wx/portal/{appId}")
@Slf4j
public class WxPortalController {

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@PathVariable("appId") String appId,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        log.info("接收到来自微信服务器的认证消息：signature = [{}], timestamp = [{}], nonce = [{}], echostr = [{}]",
                signature, timestamp, nonce, echostr);

        WxMaService wxMaService = WxMaConfiguration.getMaService(appId);
        if (Objects.isNull(wxMaService)) {
            return "非法请求";
        }

        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        if (wxMaService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "非法请求";
    }

}
