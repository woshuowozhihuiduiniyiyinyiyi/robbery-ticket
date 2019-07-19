package com.hj.tj.gohome.vo.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class SpeedCommentSaveParam {

    /**
     * 评论id
     */
    private Integer id;

    /**
     * 父评论id
     */
    private Integer parentId;

    /**
     * 动态id
     */
    private Integer speedDynamicId;

    /**
     * 内容
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;

    /**
     * 图片列表
     */
    private List<String> pictureList;
}
