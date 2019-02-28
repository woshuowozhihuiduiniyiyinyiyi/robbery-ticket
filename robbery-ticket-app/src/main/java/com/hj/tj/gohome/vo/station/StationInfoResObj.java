package com.hj.tj.gohome.vo.station;

import lombok.Data;

import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2019/2/28 9:46
 */
@Data
public class StationInfoResObj {

    /**
     * 开车时间
     */
    private String fromTime;

    /**
     * 到达时间
     */
    private String toTime;

    /**
     * 历经时长
     */
    private String usedTime;

    /**
     * 是否当日到达
     */
    private String hasToday;

    /**
     * 出发站
     */
    private String fromCity;

    /**
     * 到达站
     */
    private String toCity;

    /**
     * 起始站
     */
    private String beginCity;

    /**
     * 终点站
     */
    private String endCity;

    /**
     * 车次
     */
    private String trainNumber;

    /**
     * 余票信息
     */
    private List<String> ticketInfo;
}
