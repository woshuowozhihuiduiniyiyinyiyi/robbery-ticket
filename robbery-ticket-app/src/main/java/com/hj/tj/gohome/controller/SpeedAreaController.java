package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.service.SpeedAreaService;
import com.hj.tj.gohome.vo.area.SpeedAreaResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2019/5/25 17:51
 */
@RestController
@RequestMapping("/api/speed/area")
public class SpeedAreaController {

    @Resource
    private SpeedAreaService speedAreaService;

    @GetMapping("/list")
    public ResponseEntity<List<SpeedAreaResult>> speedAreaResult() {
        return ResponseEntity.ok(speedAreaService.listSpeedArea());
    }
}
