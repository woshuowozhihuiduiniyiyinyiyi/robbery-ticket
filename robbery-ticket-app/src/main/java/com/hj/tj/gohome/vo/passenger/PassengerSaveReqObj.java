package com.hj.tj.gohome.vo.passenger;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class PassengerSaveReqObj {

    /**
     * id
     */
    private Integer id;

    /**
     * 旅客名称
     */
    @NotBlank(message = "乘客名称不能为空")
    private String name;

    /**
     * 乘客类型
     */
    @Min(value = 0, message = "乘客类型不正确")
    @Max(value = 2, message = "乘客类型不正确")
    private Integer type;

    /**
     * 证件类型
     */
    @Min(value = 0, message = "证件类型不正确")
    @Max(value = 3, message = "证件类型不正确")
    private Integer idCardType;

    /**
     * 身份证号
     */
    @Pattern(regexp = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)",
            message = "身份证号错误")
    private String idCard;

    /**
     * 学生票相关信息
     */
    private PassengerStudentReqObj passengerStudentReqObj;
}
