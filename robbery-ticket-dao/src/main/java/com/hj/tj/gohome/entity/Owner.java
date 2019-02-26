package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("owner")
public class Owner {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 微信账号
     */
    private String wxAccount;

    /**
     * 微信昵称
     */
    private String wxNickname;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;


    /**
     * 状态,0未删除,1已删除
     */
    private Integer status;

    /**
     * 创建日期
     */
    private Date createdAt;

    /**
     * 更新日期
     */
    private Date updatedAt;

}
