package com.hj.tj.gohome.service;

import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.vo.comment.*;

public interface SpeedCommentService {

    /**
     * 评论列表
     *
     * @param speedCommentParam
     * @return
     */
    PageInfo<SpeedCommentResult> listSpeedComment(SpeedCommentParam speedCommentParam);

    /**
     * 新增或更新评论
     *
     * @param speedCommentSaveParam
     * @return
     */
    Integer save(SpeedCommentSaveParam speedCommentSaveParam);

    /**
     * 我的信息列表
     *
     * @param speedCommentMyReplyParam
     * @return
     */
    PageInfo<SpeedCommentMyReplyResult> speedCommentMyReplyList(SpeedCommentMyReplyParam speedCommentMyReplyParam);
}
