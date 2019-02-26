package com.hj.tj.gohome.vo.responseVO;

import lombok.Data;

/**
 * @author tangj
 * @description
 * @since 2018/11/7 11:57
 */
@Data
public class OrderStatisticDataResObj {

    private Integer totalOrderCount;

    private Integer robbingCount;

    private Integer successCount;

    private Double totalProfit;

}
