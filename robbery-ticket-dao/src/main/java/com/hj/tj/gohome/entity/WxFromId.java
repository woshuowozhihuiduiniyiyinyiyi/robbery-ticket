package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("wx_form_id")
@Data
public class WxFromId {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 微信的appId
     */
    private String appId;

    /**
     * 微信formId
     */
    private String formId;

    /**
     * 业主Id
     */
    private Integer ownerId;

    /**
     * 是否已经使用
     */
    private Integer hasUse;

    /**
     * 过期时间
     */
    private Date expireDate;

    /**
     * 0已删除，1未删除
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
