package com.hj.tj.gohome.vo.station;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tangj
 * @description
 * @since 2019/2/28 10:42
 */
@Data
public class StationInfoReqObj {
    /**
     * 列表出发日期
     */
    @NotNull(message = "列车出发日期不能为空")
    private String trainDate;

    /**
     * 出发站
     */
    @NotBlank(message = "列车出发站不能为空")
    private String fromStation;

    /**
     * 到达站
     */
    @NotBlank(message = "列表到达站不能为空")
    private String toStation;
}
