package com.hj.tj.gohome.vo.dynamic;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2019/5/30 17:33
 */
@Data
public class SpeedDynamicSaveParam {

    /**
     * 父动态id
     */
    private Integer parentId;

    /**
     * 内容
     */
    @NotEmpty(message = "发布内容不能为空")
    private String content;

    /**
     * 图片列表
     */
    private List<String> pictureList;

    /**
     * 地区id
     */
    private Integer speedAreaId;
}
