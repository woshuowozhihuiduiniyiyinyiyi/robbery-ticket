package com.hj.tj.gohome.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.config.WxMaConfiguration;
import com.hj.tj.gohome.config.handler.ServiceException;
import com.hj.tj.gohome.config.handler.ServiceExceptionEnum;
import com.hj.tj.gohome.service.SpeedCommentService;
import com.hj.tj.gohome.vo.comment.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@Slf4j
public class SpeedCommentController {

    @Resource
    private SpeedCommentService speedCommentService;

    @PostMapping("/speed/comment/list")
    public ResponseEntity<PageInfo<SpeedCommentResult>> speedCommentList(@Validated @RequestBody SpeedCommentParam speedCommentParam) {
        return ResponseEntity.ok(speedCommentService.listSpeedComment(speedCommentParam));
    }

    @PostMapping("/auth/speed/comment/save/{appId}")
    public ResponseEntity<Integer> speedCommentSave(@Validated @RequestBody SpeedCommentSaveParam speedCommentSaveParam,
                                                    @PathVariable("appId") String appId) {
        WxMaService maService = WxMaConfiguration.getMaService(appId);

        boolean hasSec = maService.getSecCheckService().checkMessage(speedCommentSaveParam.getContent());
        if (!hasSec) {
            throw new ServiceException(ServiceExceptionEnum.CONTENT_HAS_SEC);
        }

        if (Objects.isNull(speedCommentSaveParam.getId()) && Objects.isNull(speedCommentSaveParam.getSpeedDynamicId())
                && Objects.isNull(speedCommentSaveParam.getParentId())) {
            throw new ServiceException(ServiceExceptionEnum.SYS_ERROR);
        }

        return ResponseEntity.ok(speedCommentService.save(speedCommentSaveParam));
    }

    @PostMapping("/auth/speed/comment/my/reply")
    public ResponseEntity<PageInfo<SpeedCommentMyReplyResult>> speedCommentMyReplyList(@Validated @RequestBody SpeedCommentMyReplyParam speedCommentMyReplyParam) {
        PageInfo<SpeedCommentMyReplyResult> resultPageInfo = speedCommentService.speedCommentMyReplyList(speedCommentMyReplyParam);
        return ResponseEntity.ok(resultPageInfo);
    }
}
