package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author tangj
 * @description
 * @since 2019/2/28 18:04
 */
@TableName("station")
@Data
public class Station {
    /**
     * id
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 列表编号
     */
    private String number;

    /**
     * 列车名称
     */
    private String name;

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
