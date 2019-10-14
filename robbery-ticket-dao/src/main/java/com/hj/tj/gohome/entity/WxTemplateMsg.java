package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("wx_template_msg")
@Data
public class WxTemplateMsg {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 业主id
     */
    private Integer ownerId;

    /**
     * 推送业主id
     */
    private Integer pushOwnerId;

    /**
     * 是否已经推送
     */
    private Integer hasPush;

    /**
     * 推送类型，0加速留言，1加速回复，2加速点赞
     */
    private Integer pushType;

    /**
     * 推送次数
     */
    private Integer pushCount;

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
