package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderReqObj {
    /**
     * id
     */
    private Integer id;
    private List<Integer> idList;

    /**
     * 创建时间
     */
    private Date createdAtMin;
    private Date createdAtMax;

    /**
     * 抢票时间
     */
    private Date departureDateMin;
    private Date departureDateMax;

    /**
     * 用户期待的出发时间
     */
    private Date expectDateMin;
    private Date expectDateMax;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 业主微信昵称
     */
    private String ownerWxNickName;

    /**
     * 微信号
     */
    private String ownerWxAccount;

    /**
     * 旅客名称
     */
    private String passengerName;

    /**
     * 旅客身份证号
     */
    private String passengerIdCard;

    /**
     * 抢票人员
     */
    private String robbingUserName;

    /**
     * 分页信息
     */
    private Page page = new Page();
}
