package com.hj.tj.gohome.controller;

import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.service.SpeedDynamicService;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicDetailResult;
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
        return ResponseEntity.ok(dynamicService.listSpeedDynamic(speedDynamicParam));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<SpeedDynamicDetailResult> dynamicDetail(@PathVariable("id") Integer id) {
        SpeedDynamicDetailResult speedDynamicDetailResult = dynamicService.findById(id);
        return ResponseEntity.ok(speedDynamicDetailResult);
    }
}
