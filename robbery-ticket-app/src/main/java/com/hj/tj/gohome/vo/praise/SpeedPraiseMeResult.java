package com.hj.tj.gohome.vo.praise;

import com.hj.tj.gohome.vo.dynamic.SpeedDynamicReplyResult;
import lombok.Data;

import java.util.Date;

@Data
public class SpeedPraiseMeResult {

    /**
     * 点赞id
     */
    private Integer id;

    /**
     * 点赞用户
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
     * 点赞内容
     */
    private String content = "";

    /**
     * 点赞时间
     */
    private Date postTime;

    /**
     * 动态信息
     */
    private SpeedDynamicReplyResult speedDynamicReplyResult = new SpeedDynamicReplyResult();
}
