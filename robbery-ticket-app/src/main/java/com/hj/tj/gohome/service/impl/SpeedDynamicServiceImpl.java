package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.entity.Owner;
import com.hj.tj.gohome.entity.SpeedDynamic;
import com.hj.tj.gohome.entity.SpeedPraise;
import com.hj.tj.gohome.enums.BaseStatusEnum;
import com.hj.tj.gohome.enums.SpeedDynamicHasTopEnum;
import com.hj.tj.gohome.enums.SpeedPraiseDataTypeEnum;
import com.hj.tj.gohome.enums.StatusEnum;
import com.hj.tj.gohome.mapper.OwnerMapper;
import com.hj.tj.gohome.mapper.SpeedDynamicMapper;
import com.hj.tj.gohome.mapper.SpeedPraiseMapper;
import com.hj.tj.gohome.service.OwnerService;
import com.hj.tj.gohome.service.SpeedDynamicService;
import com.hj.tj.gohome.service.SpeedPraiseService;
import com.hj.tj.gohome.utils.OwnerContextHelper;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicDetailResult;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicParam;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicResult;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicSaveParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpeedDynamicServiceImpl implements SpeedDynamicService {

    @Resource
    private SpeedDynamicMapper speedDynamicMapper;

    @Resource
    private OwnerService ownerService;

    @Resource
    private OwnerMapper ownerMapper;

    @Resource
    private SpeedPraiseService speedPraiseService;

    @Override
    public List<SpeedDynamicResult> listTopSpeedDynamic(Integer areaId) {
        List<SpeedDynamic> speedDynamics = speedDynamicMapper.selectList(Wrappers.<SpeedDynamic>query().lambda()
                .eq(SpeedDynamic::getSpeedAreaId, areaId)
                .eq(SpeedDynamic::getHasTop, SpeedDynamicHasTopEnum.TOP.getValue())
                .gt(SpeedDynamic::getTopExpire, new Date())
                .eq(SpeedDynamic::getStatus, StatusEnum.UN_DELETE.getStatus()));
        if (CollectionUtils.isEmpty(speedDynamics)) {
            return new ArrayList<>();
        }

        List<Integer> ownerIds = speedDynamics.stream().map(SpeedDynamic::getOwnerId).collect(Collectors.toList());
        List<Owner> owners = ownerService.selectByIds(ownerIds);
        Map<Integer, Owner> ownerMap = owners.stream().collect(Collectors.toMap(Owner::getId, o -> o));

        List<SpeedDynamicResult> resultList = new ArrayList<>();
        for (SpeedDynamic speedDynamic : speedDynamics) {
            SpeedDynamicResult speedDynamicResult = genSpeedDynamicResult(speedDynamic, ownerMap);
            resultList.add(speedDynamicResult);
        }

        return resultList;
    }

    @Override
    public PageInfo<SpeedDynamicResult> listSpeedDynamic(SpeedDynamicParam speedDynamicParam) {
        LambdaQueryWrapper<SpeedDynamic> speedDynamicQueryWrapper = Wrappers.<SpeedDynamic>query().lambda();
        if (Objects.nonNull(speedDynamicParam.getAreaId())) {
            speedDynamicQueryWrapper.eq(SpeedDynamic::getSpeedAreaId, speedDynamicParam.getAreaId());
        }
        if (Objects.equals(speedDynamicParam.getHasMy(), 1)) {
            speedDynamicQueryWrapper.eq(SpeedDynamic::getOwnerId, OwnerContextHelper.getOwnerId());
        }
        speedDynamicQueryWrapper.eq(SpeedDynamic::getStatus, BaseStatusEnum.UN_DELETE.getValue());
        speedDynamicQueryWrapper.orderByDesc(SpeedDynamic::getPostTime);

        PageHelper.startPage(speedDynamicParam.getPage().getPage(), speedDynamicParam.getPage().getSize());
        List<SpeedDynamic> speedDynamics = speedDynamicMapper.selectList(speedDynamicQueryWrapper);
        if (CollectionUtils.isEmpty(speedDynamics)) {
            return new PageInfo<>();
        }

        List<Integer> ownerIds = speedDynamics.stream().map(SpeedDynamic::getOwnerId).collect(Collectors.toList());
        List<Owner> owners = ownerService.selectByIds(ownerIds);
        Map<Integer, Owner> ownerMap = owners.stream().collect(Collectors.toMap(Owner::getId, o -> o));

        PageInfo<SpeedDynamic> pageInfo = new PageInfo<>(speedDynamics);

        List<SpeedDynamicResult> speedDynamicResults = new ArrayList<>(speedDynamics.size());
        for (SpeedDynamic speedDynamic : speedDynamics) {
            speedDynamicResults.add(genSpeedDynamicResult(speedDynamic, ownerMap));
        }

        PageInfo<SpeedDynamicResult> resultPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, resultPageInfo);
        resultPageInfo.setList(speedDynamicResults);

        return resultPageInfo;
    }

    @Override
    public SpeedDynamicDetailResult findById(Integer id) {
        SpeedDynamic speedDynamic = speedDynamicMapper.selectById(id);
        if (Objects.isNull(speedDynamic)) {
            return new SpeedDynamicDetailResult();
        }

        Owner owner = ownerMapper.selectById(speedDynamic.getOwnerId());

        return genSpeedDynamicDetailResult(speedDynamic, owner);
    }

    @Override
    public Integer insert(SpeedDynamicSaveParam speedDynamicSaveParam) {
        SpeedDynamic speedDynamic = new SpeedDynamic();
        speedDynamic.setContent(speedDynamicSaveParam.getContent());
        speedDynamic.setSpeedAreaId(speedDynamicSaveParam.getSpeedAreaId());
        speedDynamic.setOwnerId(OwnerContextHelper.getOwnerId());
        speedDynamic.setCreator(OwnerContextHelper.getOwnerId().toString());
        speedDynamic.setUpdater(OwnerContextHelper.getOwnerId().toString());

        if (!CollectionUtils.isEmpty(speedDynamicSaveParam.getPictureList())) {
            speedDynamic.setPicture(String.join("`", speedDynamicSaveParam.getPictureList()));
        }

        speedDynamicMapper.insert(speedDynamic);

        return speedDynamic.getId();
    }

    @Override
    public PageInfo<SpeedDynamicResult> loginDynamicList(SpeedDynamicParam speedDynamicParam) {
        PageInfo<SpeedDynamicResult> speedDynamicResultPageInfo = this.listSpeedDynamic(speedDynamicParam);

        List<SpeedDynamicResult> speedDynamicResults = speedDynamicResultPageInfo.getList();
        if (CollectionUtils.isEmpty(speedDynamicResults)) {
            return speedDynamicResultPageInfo;
        }

        List<Integer> dynamicIds = speedDynamicResults.stream().map(SpeedDynamicResult::getId).collect(Collectors.toList());
        List<SpeedPraise> speedPraises = speedPraiseService.listByDataIdAndType(OwnerContextHelper.getOwnerId(), dynamicIds,
                SpeedPraiseDataTypeEnum.DYNAMIC.getType());

        if (CollectionUtils.isEmpty(speedPraises)) {
            return speedDynamicResultPageInfo;
        }

        Map<Integer, SpeedPraise> speedPraiseMap = speedPraises.stream().collect(Collectors.toMap(SpeedPraise::getDataId, s -> s, (s1, s2) -> s1));
        for (SpeedDynamicResult speedDynamicResult : speedDynamicResults) {
            if (Objects.nonNull(speedPraiseMap.get(speedDynamicResult.getId()))) {
                speedDynamicResult.setHasPraise(true);
            }
        }

        return speedDynamicResultPageInfo;
    }

    @Override
    public SpeedDynamicDetailResult loginFindById(Integer id) {
        SpeedDynamicDetailResult speedDynamicDetailResult = this.findById(id);
        if (Objects.isNull(speedDynamicDetailResult.getId())) {
            return speedDynamicDetailResult;
        }

        List<SpeedPraise> speedPraises = speedPraiseService.listByDataIdAndType(OwnerContextHelper.getOwnerId(), Collections.singletonList(id),
                SpeedPraiseDataTypeEnum.DYNAMIC.getType());
        if (!CollectionUtils.isEmpty(speedPraises)) {
            speedDynamicDetailResult.setHasPraise(true);
        }


        return speedDynamicDetailResult;
    }

    private SpeedDynamicDetailResult genSpeedDynamicDetailResult(SpeedDynamic speedDynamic, Owner owner) {
        SpeedDynamicDetailResult speedDynamicDetailResult = new SpeedDynamicDetailResult();
        BeanUtils.copyProperties(speedDynamic, speedDynamicDetailResult);

        if (Objects.nonNull(owner)) {
            speedDynamicDetailResult.setAvatarUrl(owner.getAvatarUrl());
            speedDynamicDetailResult.setWxNickName(owner.getWxNickname());
        }

        if (!StringUtils.isEmpty(speedDynamic.getPicture())) {
            String[] pictureArr = speedDynamic.getPicture().split("`");
            speedDynamicDetailResult.setPictureList(Arrays.asList(pictureArr));
        }

        return speedDynamicDetailResult;
    }

    private SpeedDynamicResult genSpeedDynamicResult(SpeedDynamic speedDynamic,
                                                     Map<Integer, Owner> ownerMap) {
        SpeedDynamicResult speedDynamicResult = new SpeedDynamicResult();
        BeanUtils.copyProperties(speedDynamic, speedDynamicResult);

        if (!StringUtils.isEmpty(speedDynamic.getPicture())) {
            String[] pictureArr = speedDynamic.getPicture().split("`");
            speedDynamicResult.setPictureList(Arrays.asList(pictureArr));
        }

        Owner owner = ownerMap.get(speedDynamic.getOwnerId());
        if (Objects.nonNull(owner)) {
            speedDynamicResult.setWxNickName(owner.getWxNickname());
            speedDynamicResult.setAvatarUrl(owner.getAvatarUrl());
        }

        return speedDynamicResult;
    }
}
