package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("passenger_student")
@Data
public class PassengerStudent {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 乘客id
     */
    private Integer passengerId;

    /**
     * 学校id
     */
    private Integer schoolId;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 学制
     */
    private Integer educationalSystem;

    /**
     * 入学年份
     */
    private Integer enterYear;

    /**
     * 优惠段始
     */
    private String discountStart;

    /**
     * 优惠段终
     */
    private String discountEnd;

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
