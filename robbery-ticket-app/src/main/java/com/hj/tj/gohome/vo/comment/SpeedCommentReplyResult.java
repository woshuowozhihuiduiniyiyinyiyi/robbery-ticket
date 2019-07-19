package com.hj.tj.gohome.vo.comment;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpeedCommentReplyResult {

    /**
     * 回复id
     */
    private Integer id;

    /**
     * 评论用户
     */
    private Integer ownerId = 0;
    private String wxNickName = "";

    /**
     * 回复用户
     */
    private Integer replyOwnerId = 0;
    private String replyWxNickName = "";

    /**
     * 回复图片
     */
    private List<String> pictureList = new ArrayList<>();
}
