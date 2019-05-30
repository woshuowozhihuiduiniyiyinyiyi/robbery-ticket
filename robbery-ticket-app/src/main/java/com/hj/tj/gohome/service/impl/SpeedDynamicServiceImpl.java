package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.tj.gohome.entity.Owner;
import com.hj.tj.gohome.entity.SpeedDynamic;
import com.hj.tj.gohome.enums.BaseStatusEnum;
import com.hj.tj.gohome.enums.SpeedDynamicHasTopEnum;
import com.hj.tj.gohome.mapper.SpeedDynamicMapper;
import com.hj.tj.gohome.service.OwnerService;
import com.hj.tj.gohome.service.SpeedDynamicService;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicResult;
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

    @Override
    public List<SpeedDynamicResult> dynamicTopList(Integer areaId) {
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
            SpeedDynamicResult speedDynamicResult = new SpeedDynamicResult();
            BeanUtils.copyProperties(speedDynamic, speedDynamicResult);

            Owner owner = ownerMap.get(speedDynamic.getOwnerId());
            if (Objects.nonNull(owner)) {
                speedDynamicResult.setWxNickName(owner.getWxNickname());
                speedDynamicResult.setAvatarUrl(owner.getAvatarUrl());
            }

            speedDynamicResult.setPictureList(new ArrayList<>());
            if (!StringUtils.isEmpty(speedDynamic.getPicture())) {
                String[] pictureList = speedDynamic.getPicture().split("`");
                speedDynamicResult.setPictureList(Arrays.asList(pictureList));
            }

            resultList.add(speedDynamicResult);
        }

        return resultList;
    }
}
