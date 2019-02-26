package com.hj.tj.gohome.vo.responseVO;

import com.hj.tj.gohome.vo.requestVo.OwnerResObj;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderResObj {
    private Integer id;

    private OwnerResObj ownerInfo;

    /**
     * 出发时间
     */
    private Date departureDate;
    private String departureDateStr;

    /**
     * 用户期待出发时间
     */
    private String expectDate;

    /**
     * 出发地
     */
    private String origin;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 车次
     */
    private String trainNumber;

    /**
     * 座位
     */
    private String seat;

    /**
     * 乘客信息
     */
    private List<PassengerResObj> passengerList;

    /**
     * 价格
     */
    private Double price;

    /**
     * 收益
     */
    private Double profit;

    /**
     * 收益
     */
    private Double robbingPrice;

    /**
     * 服务费=价格-收益
     */
    private Double servicePrice;

    /**
     * 抢票人员
     */
    private Integer robbingTicketUserId;
    private String robbingTicketUserName;

    /**
     * 抢票状态
     */
    private Integer status;
    private String statusStr;

    /**
     * 接单人员
     */
    private Integer portalUserId;
    private String portalUserName;

    /**
     * 创建时间
     */
    private Date createdAt;
    private String createdAtStr;

}
