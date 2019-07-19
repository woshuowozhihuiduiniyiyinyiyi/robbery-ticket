package com.hj.tj.gohome.service;

import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.vo.comment.SpeedCommentParam;
import com.hj.tj.gohome.vo.comment.SpeedCommentResult;

public interface SpeedCommentService {

    /**
     * 评论列表
     *
     * @param speedCommentParam
     * @return
     */
    PageInfo<SpeedCommentResult> listSpeedComment(SpeedCommentParam speedCommentParam);
}
