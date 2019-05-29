package com.hj.tj.gohome.vo.dynamic;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SpeedDynamicResult {

    /**
     * 动态id
     */
    private Integer id;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 微信昵称
     */
    private String wxNickName;

    /**
     * 发布时间
     */
    private Date postTime;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片列表
     */
    private List<String> pictureList;

}
