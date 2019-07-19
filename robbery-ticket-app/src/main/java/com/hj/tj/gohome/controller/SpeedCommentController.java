package com.hj.tj.gohome.controller;

import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.service.SpeedCommentService;
import com.hj.tj.gohome.vo.comment.SpeedCommentParam;
import com.hj.tj.gohome.vo.comment.SpeedCommentResult;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class SpeedCommentController {

    @Resource
    private SpeedCommentService speedCommentService;

    @PostMapping("/speed/comment/list")
    public ResponseEntity<PageInfo<SpeedCommentResult>> speedCommentList(@Validated @RequestBody SpeedCommentParam speedCommentParam) {
        return ResponseEntity.ok(speedCommentService.listSpeedComment(speedCommentParam));
    }
}
