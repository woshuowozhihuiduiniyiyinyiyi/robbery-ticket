package com.hj.tj.gohome.service.impl;

import com.hj.tj.gohome.entity.WxFormId;
import com.hj.tj.gohome.enums.BaseStatusEnum;
import com.hj.tj.gohome.enums.WxFormIdHasUseEnum;
import com.hj.tj.gohome.mapper.WxFormIdMapper;
import com.hj.tj.gohome.service.WxFormIdService;
import com.hj.tj.gohome.utils.DateUtil;
import com.hj.tj.gohome.utils.OwnerContextHelper;
import com.hj.tj.gohome.vo.form.WxFormIdParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class WxFormIdServiceImpl implements WxFormIdService {

    @Resource
    private WxFormIdMapper wxFormIdMapper;

    @Override
    public void wxFormIdSave(WxFormIdParam wxFormIdParam) {
        WxFormId wxFromId = new WxFormId();
        BeanUtils.copyProperties(wxFormIdParam, wxFromId);

        wxFromId.setCreatedAt(new Date());
        wxFromId.setCreator(OwnerContextHelper.getOwnerId().toString());
        wxFromId.setExpireDate(DateUtil.addDate(new Date(), 6));
        wxFromId.setHasUse(WxFormIdHasUseEnum.NOT_USE.getValue());
        wxFromId.setOwnerId(OwnerContextHelper.getOwnerId());
        wxFromId.setStatus(BaseStatusEnum.UN_DELETE.getValue());
        wxFromId.setUpdatedAt(new Date());
        wxFromId.setUpdater(OwnerContextHelper.getOwnerId().toString());

        wxFormIdMapper.insert(wxFromId);
    }
}
