package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.tj.gohome.entity.Owner;
import com.hj.tj.gohome.enums.BaseStatusEnum;
import com.hj.tj.gohome.mapper.OwnerMapper;
import com.hj.tj.gohome.service.OwnerService;
import com.hj.tj.gohome.utils.StringUtil;
import com.hj.tj.gohome.vo.requestVo.OwnerInsertReqObj;
import com.hj.tj.gohome.vo.requestVo.OwnerReqObj;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author tangj
 * @description
 * @since 2018/10/9 14:35
 */
@Service
public class OwnerServiceImpl implements OwnerService {

    @Resource
    private OwnerMapper ownerMapper;

    @Override
    public List<Owner> listOwner(OwnerReqObj ownerReqObj) {
        QueryWrapper<Owner> ownerQueryWrapper = genQueryWrapper(ownerReqObj);

        return ownerMapper.selectList(ownerQueryWrapper);
    }

    @Override
    public Integer saveOwner(OwnerInsertReqObj ownerInsertReqObj) {
        QueryWrapper<Owner> ownerQueryWrapper = new QueryWrapper<>();
        ownerQueryWrapper.eq("wx_account", ownerInsertReqObj.getWxAccount());

        List<Owner> owners = ownerMapper.selectList(ownerQueryWrapper);

        Owner owner = new Owner();
        BeanUtils.copyProperties(ownerInsertReqObj, owner);
        owner.setUpdatedAt(new Date());

        if (CollectionUtils.isEmpty(owners) && Objects.isNull(ownerInsertReqObj.getId())) {
            owner.setCreatedAt(new Date());
            owner.setStatus(BaseStatusEnum.UN_DELETE.getValue());
            if (Objects.isNull(owner.getGender())) {
                owner.setGender(0);
            }

            ownerMapper.insert(owner);
        } else {
            owner.setId(ownerInsertReqObj.getId());

            if (!CollectionUtils.isEmpty(owners)) {
                owner.setId(owners.get(0).getId());
            }

            ownerMapper.updateById(owner);
        }

        return owner.getId();
    }

    private QueryWrapper genQueryWrapper(OwnerReqObj ownerReqObj) {
        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();

        if (!CollectionUtils.isEmpty(ownerReqObj.getIdList())) {
            queryWrapper.in("id", ownerReqObj.getIdList());
        }

        if (StringUtil.isNotBlank(ownerReqObj.getPhone())) {
            queryWrapper.like("phone", ownerReqObj.getPhone());
        }

        if (StringUtil.isNotBlank(ownerReqObj.getWxAccount())) {
            queryWrapper.like("wx_account", ownerReqObj.getWxAccount());
        }

        if (StringUtil.isNotBlank(ownerReqObj.getWxNickName())) {
            queryWrapper.like("wx_nickname", ownerReqObj.getWxNickName());
        }

        queryWrapper.eq("status", BaseStatusEnum.UN_DELETE.getValue());

        return queryWrapper;
    }
}
