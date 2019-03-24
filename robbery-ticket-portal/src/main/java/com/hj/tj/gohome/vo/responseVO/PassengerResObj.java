package com.hj.tj.gohome.vo.responseVO;

import lombok.Data;

/**
 * @author tangj
 * @description
 * @since 2018/10/15 14:51
 */
@Data
public class PassengerResObj {

    /**
     * 乘客id
     */
    private Integer passengerId;

    /**
     * 乘客姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 乘客类型
     */
    private Integer type;
    private String typeStr;

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
}
