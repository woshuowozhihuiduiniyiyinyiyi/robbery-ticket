package com.hj.tj.gohome.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.config.WxMaConfiguration;
import com.hj.tj.gohome.config.handler.ServiceException;
import com.hj.tj.gohome.config.handler.ServiceExceptionEnum;
import com.hj.tj.gohome.service.SpeedDynamicService;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicDetailResult;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicParam;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicResult;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicSaveParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class SpeedDynamicController {

    @Resource
    private SpeedDynamicService dynamicService;

    @GetMapping("/speed/dynamic/list/top/{areaId}")
    public ResponseEntity<List<SpeedDynamicResult>> dynamicTopList(@PathVariable("areaId") Integer areaId) {
        return ResponseEntity.ok(dynamicService.listTopSpeedDynamic(areaId));
    }

    @PostMapping("/speed/dynamic/list")
    public ResponseEntity<PageInfo<SpeedDynamicResult>> dynamicList(@Validated @RequestBody SpeedDynamicParam speedDynamicParam) {
        return ResponseEntity.ok(dynamicService.listSpeedDynamic(speedDynamicParam));
    }

    @PostMapping("/auth/speed/dynamic/list")
    public ResponseEntity<PageInfo<SpeedDynamicResult>> loginDynamicList(@Validated @RequestBody SpeedDynamicParam speedDynamicParam) {
        return ResponseEntity.ok(dynamicService.loginDynamicList(speedDynamicParam));
    }

    @GetMapping("/auth/speed/dynamic/detail/{id}")
    public ResponseEntity<SpeedDynamicDetailResult> loginDynamicDetail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(dynamicService.loginFindById(id));
    }

    @GetMapping("/speed/dynamic/detail/{id}")
    public ResponseEntity<SpeedDynamicDetailResult> dynamicDetail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(dynamicService.findById(id));
    }

    @PostMapping("/auth/speed/dynamic/save/{appId}")
    public ResponseEntity dynamicSave(@Validated @RequestBody SpeedDynamicSaveParam speedDynamicSaveParam,
                                      @PathVariable("appId") String appId) {
        WxMaService maService = WxMaConfiguration.getMaService(appId);
        boolean hasSec = maService.getSecCheckService().checkMessage(speedDynamicSaveParam.getContent());
        if (hasSec) {
            throw new ServiceException(ServiceExceptionEnum.CONTENT_HAS_SEC);
        }

        return ResponseEntity.ok(dynamicService.insert(speedDynamicSaveParam));
    }
}
