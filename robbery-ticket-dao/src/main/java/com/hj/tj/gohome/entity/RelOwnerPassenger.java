package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("rel_owner_passenger")
public class RelOwnerPassenger {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 业主id
     */
    private Integer ownerId;

    /**
     * 旅客id
     */
    private Integer passengerId;

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
     * 创建者
     */
    private String creator;

    /**
     * 修改者
     */
    private String updater;
}
