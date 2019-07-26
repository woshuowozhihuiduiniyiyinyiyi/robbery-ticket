package com.hj.tj.gohome.vo.comment;

import com.hj.tj.gohome.vo.dynamic.SpeedDynamicReplyResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SpeedCommentMyReplyResult {

    /**
     * 回复id
     */
    private Integer id;

    /**
     * 评论用户
     */
    private Integer ownerId = 0;

    /**
     * 微信昵称
     */
    private String wxNickName = "";

    /**
     * 用户头像
     */
    private String avatarUrl = "";

    /**
     * 回复内容
     */
    private String content = "";

    /**
     * 回复图片
     */
    private List<String> pictureList = new ArrayList<>();

    /**
     * 评论时间
     */
    private Date postTime;

    /**
     * 动态信息
     */
    private SpeedDynamicReplyResult speedDynamicReplyResult = new SpeedDynamicReplyResult();
}
