package com.hj.tj.gohome.vo.praise;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SpeedPraiseSaveParam {

    /**
     * 数据id，可能是动态id，也可能是评论id
     */
    @NotNull(message = "点赞数据id不能为空")
    private Integer dataId;

    /**
     * 数据类型，0为动态，1为评论
     */
    @NotNull(message = "点赞数据类型不能为空")
    private Integer dataType;
}
