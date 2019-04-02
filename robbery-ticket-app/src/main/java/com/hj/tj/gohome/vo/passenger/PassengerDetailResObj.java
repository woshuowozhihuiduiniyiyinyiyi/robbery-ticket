package com.hj.tj.gohome.vo.passenger;

import lombok.Data;

/**
 * @author tangj
 * @description
 * @since 2019/4/2 17:00
 */
@Data
public class PassengerDetailResObj {

    /**
     * 乘客id
     */
    private Integer id;

    /**
     * 乘客名称
     */
    private String name;

    /**
     * 乘客类型
     */
    private Integer type;
    private String typeStr;

    /**
     * 乘客证件类型
     */
    private Integer idCardType;
    private String idCardTypeStr;

    /**
     * 乘客证件
     */
    private String idCard;

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

}
