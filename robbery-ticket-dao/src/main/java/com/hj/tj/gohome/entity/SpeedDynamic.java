package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("speed_dynamic")
public class SpeedDynamic {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发动态的用户id
     */
    private Integer ownerId;

    /**
     * 所属地区id
     */
    private Integer speedAreaId;

    /**
     * 是否置顶,0否1是
     */
    private Integer hasTop;

    /**
     * 置顶过期时间
     */
    private Date topExpire;

    /**
     * 内容
     */
    private String content;

    /**
     * 发布时间
     */
    private Date postTime;

    /**
     * 评论数
     */
    private Integer commentNum;

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
     * 根id
     */
    private Integer rootId;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 回复用户id
     */
    private Integer repeatOwnerId;

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
