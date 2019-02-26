package com.hj.tj.gohome.vo.responseVO;

import lombok.Data;

/**
 * @author tangj
 * @description
 * @since 2018/10/11 15:17
 */
@Data
public class LoginResObj {

    private String token;
    private Integer userId;
    private String userName;
    private String phone;
    private String gender;

}
