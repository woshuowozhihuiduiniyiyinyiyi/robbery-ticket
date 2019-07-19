package com.hj.tj.gohome.controller;

import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.config.handler.ServiceException;
import com.hj.tj.gohome.config.handler.ServiceExceptionEnum;
import com.hj.tj.gohome.service.SpeedCommentService;
import com.hj.tj.gohome.vo.comment.SpeedCommentParam;
import com.hj.tj.gohome.vo.comment.SpeedCommentResult;
import com.hj.tj.gohome.vo.comment.SpeedCommentSaveParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@Validated
public class SpeedCommentController {

    @Resource
    private SpeedCommentService speedCommentService;

    @PostMapping("/speed/comment/list")
    public ResponseEntity<PageInfo<SpeedCommentResult>> speedCommentList(@Validated @RequestBody SpeedCommentParam speedCommentParam) {
        return ResponseEntity.ok(speedCommentService.listSpeedComment(speedCommentParam));
    }

    @PostMapping("/auth/speed/comment/save")
    public ResponseEntity<Integer> speedCommentSave(@Validated @RequestBody SpeedCommentSaveParam speedCommentSaveParam) {
        if (Objects.isNull(speedCommentSaveParam.getId()) && Objects.isNull(speedCommentSaveParam.getSpeedDynamicId())
                && Objects.isNull(speedCommentSaveParam.getParentId())) {
            throw new ServiceException(ServiceExceptionEnum.SYS_ERROR);
        }

        return ResponseEntity.ok(speedCommentService.save(speedCommentSaveParam));
    }
}
