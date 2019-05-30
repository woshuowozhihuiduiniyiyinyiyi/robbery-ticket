package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.service.SpeedDynamicService;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/speed/dynamic")
public class SpeedDynamicController {

    @Resource
    private SpeedDynamicService dynamicService;

    @GetMapping("/list/top/{areaId}")
    public ResponseEntity<List<SpeedDynamicResult>> dynamicTopList(@PathVariable("areaId") Integer areaId) {
        return ResponseEntity.ok(dynamicService.dynamicTopList(areaId));
    }


}
