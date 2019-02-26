package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author tangj
 * @description
 * @since 2019/2/25 11:43
 */
@Data
@TableName("robbing_ticket_user")
public class RobbingTicketUser {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 抢票人员名称
     */
    private String name;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 联系手机号
     */
    private String phone;

    /**
     * 状态，0已删除，1未删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 修改时间
     */
    private Date updatedAt;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String updater;
}
