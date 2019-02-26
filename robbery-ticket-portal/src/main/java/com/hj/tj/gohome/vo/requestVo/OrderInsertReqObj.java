package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class OrderInsertReqObj {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Integer id;

    /**
     * 起点
     */
    @NotBlank(message = "出发地点不能为空")
    private String origin;

    /**
     * 目的地
     */
    @NotBlank(message = "目的地不能为空")
    private String destination;

    /**
     * 用户期望出发日期
     */
    private String expectDate;

    /**
     * 出发日期
     */
    private Date departureDate;

    /**
     * 车次
     */
    @NotBlank(message = "车次不能为空")
    private String trainNumber;

    /**
     * 座位
     */
    @NotBlank(message = "座位不能为空")
    private String seat;

    /**
     * 旅客信息
     */
    @NotEmpty(message = "旅客信息不能为空")
    private List<PassengerInsertReqObj> passengerList;

    /**
     * 接章价格
     */
    @NotNull(message = "接单价格不能为空")
    @Min(value = 0, message = "接单价格最低为0")
    private Double price;

    /**
     * 接单人员
     */
    @NotNull(message = "接单人员不能为空")
    private Integer portalUserId;

    /**
     * 抢票用户
     */
    private Integer robbingTicketUserId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 抢票价格
     */
    private Double robbingPrice;

    /**
     * 订单联系手机号
     */
    @NotBlank(message = "订单联系手机号不能为空")
    private String phone;

    /**
     * 业主id
     */
    @NotNull(message = "业主id不能为空")
    private Integer ownerId;
}
