package com.hj.tj.gohome.vo.dynamic;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpeedDynamicReplyResult {

    /**
     * 动态id
     */
    private Integer id = 0;

    /**
     * 动态内容
     */
    private String content = "";

    /**
     * 动态图片列表
     */
    private List<String> pictureList = new ArrayList<>();
}
