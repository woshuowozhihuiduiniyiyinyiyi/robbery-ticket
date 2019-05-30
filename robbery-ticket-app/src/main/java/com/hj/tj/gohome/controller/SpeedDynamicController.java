package com.hj.tj.gohome.controller;

import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.service.SpeedDynamicService;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicParam;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicResult;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/speed/dynamic")
public class SpeedDynamicController {

    @Resource
    private SpeedDynamicService dynamicService;

    @GetMapping("/list/top/{areaId}")
    public ResponseEntity<List<SpeedDynamicResult>> dynamicTopList(@PathVariable("areaId") Integer areaId) {
        return ResponseEntity.ok(dynamicService.listTopSpeedDynamic(areaId));
    }

    @PostMapping("/list")
    public ResponseEntity<PageInfo<SpeedDynamicResult>> dynamicList(@Validated @RequestBody SpeedDynamicParam speedDynamicParam) {
        PageInfo<SpeedDynamicResult> speedDynamicResultPageInfo = dynamicService.listSpeedDynamic(speedDynamicParam);
        return ResponseEntity.ok(speedDynamicResultPageInfo);
    }

}
