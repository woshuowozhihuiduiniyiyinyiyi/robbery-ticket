package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author tangj
 * @description
 * @since 2019/5/25 16:55
 */
@Data
@TableName("speed_area")
public class SpeedArea {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 最大置顶数
     */
    private Integer maxTopNum;

    /**
     * 地区名
     */
    private String name;

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
