package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("speed_comment")
public class SpeedComment {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 动态id
     */
    private Integer speedDynamicId;

    /**
     * 评论用户id
     */
    private Integer ownerId;

    /**
     * 回复用户id
     */
    private Integer replyOwnerId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 回复数
     */
    private Integer replyNum;

    /**
     * 点赞数
     */
    private Integer praiseNum;

    /**
     * 分享数
     */
    private Integer shareNum;

    /**
     * 图片
     */
    private String picture;

    /**
     * 提交时间
     */
    private Date postTime;

    /**
     * 根id，评论的根id为0
     */
    private Integer rootId;

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
