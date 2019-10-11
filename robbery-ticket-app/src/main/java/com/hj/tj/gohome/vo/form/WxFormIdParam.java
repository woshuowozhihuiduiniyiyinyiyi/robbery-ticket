package com.hj.tj.gohome.vo.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WxFormIdParam {

    @NotBlank(message = "小程序id不能为空")
    private String appId;

    @NotBlank(message = "formId不能为空")
    private String formId;
}
