package com.hj.tj.gohome.vo.passenger;

import lombok.Data;

@Data
public class PassengerStudentReqObj {

    /**
     * 乘客id
     */
    private Integer passengerId;

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
