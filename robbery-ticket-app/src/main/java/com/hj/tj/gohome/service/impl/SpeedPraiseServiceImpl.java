package com.hj.tj.gohome.service.impl;

import com.hj.tj.gohome.entity.SpeedPraise;
import com.hj.tj.gohome.enums.SpeedPraiseDataTypeEnum;
import com.hj.tj.gohome.enums.StatusEnum;
import com.hj.tj.gohome.mapper.SpeedCommentMapper;
import com.hj.tj.gohome.mapper.SpeedDynamicMapper;
import com.hj.tj.gohome.mapper.SpeedPraiseMapper;
import com.hj.tj.gohome.service.SpeedPraiseService;
import com.hj.tj.gohome.utils.OwnerContextHelper;
import com.hj.tj.gohome.vo.praise.SpeedPraiseSaveParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class SpeedPraiseServiceImpl implements SpeedPraiseService {

    @Resource
    private SpeedPraiseMapper speedPraiseMapper;

    @Resource
    private SpeedDynamicMapper speedDynamicMapper;

    @Resource
    private SpeedCommentMapper speedCommentMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Integer save(SpeedPraiseSaveParam speedPraiseSaveParam) {
        SpeedPraise speedPraise = new SpeedPraise();
        BeanUtils.copyProperties(speedPraiseSaveParam, speedPraise);
        speedPraise.setPostTime(new Date());
        speedPraise.setCreator(OwnerContextHelper.getOwnerId().toString());
        speedPraise.setStatus(StatusEnum.UN_DELETE.getStatus());

        speedPraiseMapper.insert(speedPraise);

        if (Objects.equals(SpeedPraiseDataTypeEnum.DYNAMIC.getType(), speedPraiseSaveParam.getDataType())) {
            speedDynamicMapper.addPraiseNum(speedPraiseSaveParam.getDataId());
        }

        if (Objects.equals(SpeedPraiseDataTypeEnum.COMMENT.getType(), speedPraiseSaveParam.getDataType())) {
            speedCommentMapper.addPraiseNum(speedPraiseSaveParam.getDataId());
        }

        return speedPraise.getId();
    }
}
