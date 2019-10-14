package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.config.handler.ServiceException;
import com.hj.tj.gohome.config.handler.ServiceExceptionEnum;
import com.hj.tj.gohome.entity.*;
import com.hj.tj.gohome.enums.SpeedPraiseDataTypeEnum;
import com.hj.tj.gohome.enums.StatusEnum;
import com.hj.tj.gohome.mapper.SpeedCommentMapper;
import com.hj.tj.gohome.mapper.SpeedDynamicMapper;
import com.hj.tj.gohome.mapper.SpeedPraiseMapper;
import com.hj.tj.gohome.service.OwnerService;
import com.hj.tj.gohome.service.SpeedPraiseService;
import com.hj.tj.gohome.service.WxTemplateMsgService;
import com.hj.tj.gohome.utils.OwnerContextHelper;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicReplyResult;
import com.hj.tj.gohome.vo.praise.SpeedPraiseMeParam;
import com.hj.tj.gohome.vo.praise.SpeedPraiseMeResult;
import com.hj.tj.gohome.vo.praise.SpeedPraiseSaveParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpeedPraiseServiceImpl implements SpeedPraiseService {

    @Resource
    private SpeedPraiseMapper speedPraiseMapper;

    @Resource
    private SpeedDynamicMapper speedDynamicMapper;

    @Resource
    private SpeedCommentMapper speedCommentMapper;

    @Resource
    private OwnerService ownerService;

    @Resource
    private WxTemplateMsgService wxTemplateMsgService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Integer save(SpeedPraiseSaveParam speedPraiseSaveParam) {
        QueryWrapper<SpeedPraise> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", OwnerContextHelper.getOwnerId())
                .eq("data_id", speedPraiseSaveParam.getDataId())
                .eq("data_type", speedPraiseSaveParam.getDataType())
                .eq("status", StatusEnum.UN_DELETE.getStatus());
        Integer count = speedPraiseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException(ServiceExceptionEnum.PRAISE_ALREADY_EXISTS);
        }

        SpeedPraise speedPraise = new SpeedPraise();
        BeanUtils.copyProperties(speedPraiseSaveParam, speedPraise);
        speedPraise.setPostTime(new Date());
        speedPraise.setCreator(OwnerContextHelper.getOwnerId().toString());
        speedPraise.setStatus(StatusEnum.UN_DELETE.getStatus());
        speedPraise.setOwnerId(OwnerContextHelper.getOwnerId());

        if (Objects.equals(SpeedPraiseDataTypeEnum.DYNAMIC.getType(), speedPraiseSaveParam.getDataType())) {
            SpeedDynamic speedDynamic = speedDynamicMapper.selectById(speedPraiseSaveParam.getDataId());
            if (Objects.nonNull(speedDynamic)) {
                wxTemplateMsgService.addNewMsg(OwnerContextHelper.getOwnerId(), speedDynamic.getOwnerId());
                speedPraise.setPraiseOwnerId(speedPraise.getOwnerId());
            }
        } else {
            SpeedComment speedComment = speedCommentMapper.selectById(speedPraiseSaveParam.getDataId());
            if (Objects.nonNull(speedComment)) {
                wxTemplateMsgService.addNewMsg(OwnerContextHelper.getOwnerId(), speedComment.getOwnerId());
                speedPraise.setPraiseOwnerId(speedComment.getOwnerId());
            }
        }

        speedPraiseMapper.insert(speedPraise);

        if (Objects.equals(SpeedPraiseDataTypeEnum.DYNAMIC.getType(), speedPraiseSaveParam.getDataType())) {
            speedDynamicMapper.addPraiseNum(speedPraiseSaveParam.getDataId());
        }

        if (Objects.equals(SpeedPraiseDataTypeEnum.COMMENT.getType(), speedPraiseSaveParam.getDataType())) {
            speedCommentMapper.addPraiseNum(speedPraiseSaveParam.getDataId());
        }

        return speedPraise.getId();
    }

    @Override
    public List<SpeedPraise> listByDataIdAndType(Integer ownerId, List<Integer> dataIds, Integer dataType) {
        QueryWrapper<SpeedPraise> speedPraiseQueryWrapper = new QueryWrapper<>();
        speedPraiseQueryWrapper.eq("owner_id", ownerId)
                .in("data_id", dataIds)
                .eq("data_type", dataType)
                .eq("status", StatusEnum.UN_DELETE.getStatus());

        return speedPraiseMapper.selectList(speedPraiseQueryWrapper);
    }

    @Override
    public PageInfo<SpeedPraiseMeResult> listPraiseMe(SpeedPraiseMeParam speedPraiseMeParam) {
        QueryWrapper<SpeedPraise> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("praise_owner_id", OwnerContextHelper.getOwnerId())
                .eq("status", StatusEnum.UN_DELETE.getStatus())
                .eq("data_type", SpeedPraiseDataTypeEnum.DYNAMIC.getType());

        PageHelper.startPage(speedPraiseMeParam.getPage().getPage(), speedPraiseMeParam.getPage().getSize(), "post_time desc");

        List<SpeedPraise> speedPraises = speedPraiseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(speedPraises)) {
            return new PageInfo<>();
        }

        PageInfo<SpeedPraise> speedPraisePageInfo = new PageInfo<>(speedPraises);

        List<Integer> ownerIds = speedPraises.stream().map(SpeedPraise::getOwnerId).collect(Collectors.toList());
        List<Owner> owners = ownerService.selectByIds(ownerIds);
        Map<Integer, Owner> ownerMap = owners.stream().collect(Collectors.toMap(Owner::getId, o -> o));

        List<Integer> dynamicIds = speedPraises.stream().map(SpeedPraise::getDataId).collect(Collectors.toList());
        List<SpeedDynamic> speedDynamics = speedDynamicMapper.selectBatchIds(dynamicIds);
        Map<Integer, SpeedDynamic> speedDynamicMap = speedDynamics.stream().collect(Collectors.toMap(SpeedDynamic::getId, s -> s));

        List<SpeedPraiseMeResult> resultList = new ArrayList<>();
        for (SpeedPraise speedPraise : speedPraises) {
            resultList.add(genSpeedPraiseMeResult(speedPraise, ownerMap, speedDynamicMap));
        }

        PageInfo<SpeedPraiseMeResult> resultPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(speedPraisePageInfo, resultPageInfo);
        resultPageInfo.setList(resultList);

        return resultPageInfo;
    }

    private SpeedPraiseMeResult genSpeedPraiseMeResult(SpeedPraise speedPraise,
                                                       Map<Integer, Owner> ownerMap,
                                                       Map<Integer, SpeedDynamic> speedDynamicMap) {
        SpeedPraiseMeResult speedPraiseMeResult = new SpeedPraiseMeResult();
        speedPraiseMeResult.setContent("赞了这条抢票");
        speedPraiseMeResult.setId(speedPraise.getId());
        speedPraiseMeResult.setOwnerId(speedPraise.getOwnerId());
        speedPraiseMeResult.setPostTime(speedPraise.getPostTime());

        if (Objects.nonNull(ownerMap.get(speedPraise.getOwnerId()))) {
            Owner owner = ownerMap.get(speedPraise.getOwnerId());
            speedPraiseMeResult.setAvatarUrl(owner.getAvatarUrl());
            speedPraiseMeResult.setWxNickName(owner.getWxNickname());
        }

        if (Objects.nonNull(speedDynamicMap.get(speedPraise.getDataId()))) {
            SpeedDynamic speedDynamic = speedDynamicMap.get(speedPraise.getDataId());
            SpeedDynamicReplyResult speedDynamicReplyResult = new SpeedDynamicReplyResult();
            speedDynamicReplyResult.setContent(speedDynamic.getContent());
            speedDynamicReplyResult.setId(speedDynamic.getId());

            if (!StringUtils.isEmpty(speedDynamic.getPicture())) {
                speedDynamicReplyResult.setPictureList(Arrays.asList(speedDynamic.getPicture().split("`")));
            }

            speedPraiseMeResult.setSpeedDynamicReplyResult(speedDynamicReplyResult);
        }

        return speedPraiseMeResult;
    }
}
