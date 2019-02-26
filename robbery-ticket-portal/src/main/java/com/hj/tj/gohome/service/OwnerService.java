package com.hj.tj.gohome.service;

import com.hj.tj.gohome.entity.Owner;
import com.hj.tj.gohome.vo.requestVo.OwnerInsertReqObj;
import com.hj.tj.gohome.vo.requestVo.OwnerReqObj;

import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2018/10/9 14:35
 */
public interface OwnerService {

    List<Owner> listOwner(OwnerReqObj ownerReqObj);

    Integer saveOwner(OwnerInsertReqObj ownerInsertReqObj);
}
