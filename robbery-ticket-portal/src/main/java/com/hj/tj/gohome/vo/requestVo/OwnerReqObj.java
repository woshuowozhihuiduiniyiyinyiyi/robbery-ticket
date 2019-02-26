package com.hj.tj.gohome.vo.requestVo;

import lombok.Data;

import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2018/10/9 15:43
 */
@Data
public class OwnerReqObj {

    private List<Integer> idList;

    private String wxAccount;

    private String wxNickName;

    private String phone;
}
