package com.hj.tj.gohome.vo.station;

import lombok.Data;

/**
 * @author tangj
 * @description
 * @since 2019/3/1 9:37
 */
@Data
public class StationInfoResObj {

    /**
     * id
     */
    private Integer id;

    /**
     * 车站名称，如：北京
     */
    private String name;

    /**
     * 车站编码，用于查询
     */
    private String number;
}
