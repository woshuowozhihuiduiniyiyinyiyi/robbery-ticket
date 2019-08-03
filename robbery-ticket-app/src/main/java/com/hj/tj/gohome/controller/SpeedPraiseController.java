package com.hj.tj.gohome.controller;

import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.service.SpeedPraiseService;
import com.hj.tj.gohome.vo.praise.SpeedPraiseMeParam;
import com.hj.tj.gohome.vo.praise.SpeedPraiseMeResult;
import com.hj.tj.gohome.vo.praise.SpeedPraiseSaveParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class SpeedPraiseController {

    @Resource
    private SpeedPraiseService speedPraiseService;

    @PostMapping("/auth/speed/praise/save")
    public ResponseEntity<Integer> speedPraiseSave(@Validated @RequestBody SpeedPraiseSaveParam speedPraiseSaveParam) {
        return ResponseEntity.ok(speedPraiseService.save(speedPraiseSaveParam));
    }

    @PostMapping("/auth/speed/praise/me/list")
    public ResponseEntity<PageInfo<SpeedPraiseMeResult>> speedPraiseMeList(@Validated @RequestBody SpeedPraiseMeParam speedPraiseMeParam) {
        return ResponseEntity.ok(speedPraiseService.listPraiseMe(speedPraiseMeParam));
    }
}
