package com.hj.tj.gohome.vo.dynamic;

import com.hj.tj.gohome.utils.Page;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tangj
 * @description
 * @since 2019/5/30 11:14
 */
@Data
public class SpeedDynamicParam {

    private Integer areaId;

    /**
     * 是否是我的动态
     */
    private Integer hasMy;

    private Page page = new Page();
}
