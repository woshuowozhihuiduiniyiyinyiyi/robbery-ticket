package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("`order`")
public class Order {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /***
     * 始发地
     */
    private String origin;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 用记指定的出发日期
     */
    private String expectDate;

    /**
     * 抢票人员实际抢到的票的出发日期
     */
    private Date departureDate;

    /**
     * 车次
     */
    private String trainNumber;

    /**
     * 座位号
     */
    private String seat;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 收益
     */
    private Integer profit;

    /**
     * 订单完成时间
     */
    private Date completeDate;

    /**
     * 业主id
     */
    private Integer ownerId;

    /**
     * 接单人员
     */
    private Integer portalUserId;

    /**
     * 抢票人员
     */
    private Integer robbingTicketUserId;

    /**
     * 订单联系手机号
     */
    private String phone;

    /**
     * 是否接受上车补票，N不接受，Y接受
     */
    private String canBuyTicketLater;

    /**
     * 抢票状态，0已删除，1抢票中，2交易成功，3交易关闭
     */
    private Integer status;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 修改人
     */
    private String updater;

    /**
     * 修改时间
     */
    private Date updatedAt;
}
