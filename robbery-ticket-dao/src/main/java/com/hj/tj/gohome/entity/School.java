package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("school")
@Data
public class School {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学校编码
     */
    private String schoolCode;

    /**
     * 学校名称
     */
    private String schoolName;

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
