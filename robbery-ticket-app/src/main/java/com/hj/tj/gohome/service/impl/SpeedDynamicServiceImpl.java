package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.entity.Owner;
import com.hj.tj.gohome.entity.SpeedDynamic;
import com.hj.tj.gohome.enums.BaseStatusEnum;
import com.hj.tj.gohome.enums.SpeedDynamicHasTopEnum;
import com.hj.tj.gohome.mapper.OwnerMapper;
import com.hj.tj.gohome.mapper.SpeedDynamicMapper;
import com.hj.tj.gohome.service.OwnerService;
import com.hj.tj.gohome.service.SpeedDynamicService;
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

    @Override
    public List<SpeedDynamicResult> listTopSpeedDynamic(Integer areaId) {
        QueryWrapper<SpeedDynamic> speedDynamicQueryWrapper = new QueryWrapper<>();
        speedDynamicQueryWrapper.eq("speed_area_id", areaId)
                .eq("has_top", SpeedDynamicHasTopEnum.TOP.getValue())
                .gt("top_expire", new Date())
                .eq("status", BaseStatusEnum.UN_DELETE.getValue());

        List<SpeedDynamic> speedDynamics = speedDynamicMapper.selectList(speedDynamicQueryWrapper);
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
        QueryWrapper<SpeedDynamic> speedDynamicQueryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(speedDynamicParam.getAreaId())) {
            speedDynamicQueryWrapper.eq("speed_area_id", speedDynamicParam.getAreaId());
        }
        if (Objects.equals(speedDynamicParam.getHasMy(), 1)) {
            speedDynamicQueryWrapper.eq("owner_id", OwnerContextHelper.getOwnerId());
        }
        speedDynamicQueryWrapper.eq("status", BaseStatusEnum.UN_DELETE.getValue());

        PageHelper.startPage(speedDynamicParam.getPage().getPage(), speedDynamicParam.getPage().getSize(),
                "post_time desc");
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

    private SpeedDynamicResult genSpeedDynamicResult(SpeedDynamic speedDynamic, Map<Integer, Owner> ownerMap) {
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
