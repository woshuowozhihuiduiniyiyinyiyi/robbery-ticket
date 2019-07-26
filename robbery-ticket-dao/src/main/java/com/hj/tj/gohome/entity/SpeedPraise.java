package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class SpeedPraise {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据id，可能是动态id，也可能是评论或者回复id
     */
    private Integer dataId;

    /**
     * 数据类型，0为动态，1为评论
     */
    private Integer dataType;

    /**
     * 点赞时间
     */
    private Date postTime;

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
