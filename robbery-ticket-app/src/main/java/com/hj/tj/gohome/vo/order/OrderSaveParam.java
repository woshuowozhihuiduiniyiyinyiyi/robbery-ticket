package com.hj.tj.gohome.vo.order;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class OrderSaveParam {

    /**
     * id
     */
    private Integer id;

    /**
     * 出发地
     */
    @NotBlank(message = "出发地不能为空")
    private String origin;

    /**
     * 目的地
     */
    @NotBlank(message = "目的地不能为空")
    private String destination;

    /**
     * 用记指定的出发日期
     */
    @NotBlank(message = "出发日期不能为空")
    private String expectDate;

    /**
     * 车次
     */
    @NotBlank(message = "车次不能为空")
    private String trainNumber;

    /**
     * 座位号
     */
    @NotBlank(message = "座位号不能为空")
    private String seat;

    /**
     * 乘车人列表
     */
    @NotEmpty(message = "乘车人不能为空")
    private List<Integer> passengerIdList;

    /**
     * 订单联系手机号
     */
    @Pattern(regexp = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$", message = "手机号输入错误")
    private String phone;

    /**
     * 微信号
     */
    private String wxAccount;

    /**
     * 是否接受上车补票，N不接受，Y接受
     */
    private String canBuyTicketLater = "N";

    /**
     * 抢票价格
     */
    private Double price;
}
