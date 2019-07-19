package com.hj.tj.gohome.vo.comment;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SpeedCommentResult {

    /**
     * 评论id
     */
    private Integer id;

    /**
     * 动态id
     */
    private Integer speedDynamicId;

    /**
     * 用户
     */
    private Integer ownerId;
    private String wxNickName = "";

    /**
     * 用户头像
     */
    private String avatarUrl = "";

    /**
     * 发布时间
     */
    private Date postTime;

    /**
     * 内容
     */
    private String content = "";

    /**
     * 点赞数
     */
    private Integer praiseNum = 0;

    /**
     * 回复数
     */
    private Integer replyNum = 0;

    /**
     * 分享数
     */
    private Integer shareNum = 0;

    /**
     * 图片列表
     */
    private List<String> pictureList = new ArrayList<>();

    /**
     * 回复列表
     */
    private List<SpeedCommentReplyResult> replyResults = new ArrayList<>();
}
