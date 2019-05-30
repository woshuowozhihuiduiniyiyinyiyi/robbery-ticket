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

    @NotNull(message = "地区id不能为空")
    private Integer areaId;

    private Page page = new Page();
}
