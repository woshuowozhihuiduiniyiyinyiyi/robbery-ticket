package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.vo.dynamic.DynamicResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth/speed/dynamic")
public class SpeedDynamicController {

    @GetMapping("/list/top/{areaId}")
    public ResponseEntity<List<DynamicResult>> dynamicTopList(@PathVariable("areaId") Integer areaId){

        return ResponseEntity.ok().build();
    }
}
