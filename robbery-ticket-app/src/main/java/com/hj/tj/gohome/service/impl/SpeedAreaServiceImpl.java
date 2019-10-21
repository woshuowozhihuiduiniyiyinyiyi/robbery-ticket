package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hj.tj.gohome.entity.SpeedArea;
import com.hj.tj.gohome.enums.StatusEnum;
import com.hj.tj.gohome.mapper.SpeedAreaMapper;
import com.hj.tj.gohome.service.SpeedAreaService;
import com.hj.tj.gohome.vo.area.SpeedAreaResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2019/5/25 17:28
 */
@Service
public class SpeedAreaServiceImpl implements SpeedAreaService {

    @Resource
    private SpeedAreaMapper speedAreaMapper;

    @Override
    public List<SpeedAreaResult> listSpeedArea() {
        List<SpeedArea> speedAreas = speedAreaMapper.selectList(Wrappers.<SpeedArea>query().lambda()
                .eq(SpeedArea::getStatus, StatusEnum.UN_DELETE.getStatus()));
        if (CollectionUtils.isEmpty(speedAreas)) {
            return new ArrayList<>();
        }

        List<SpeedAreaResult> resultList = new ArrayList<>();
        for(SpeedArea speedArea: speedAreas){
            SpeedAreaResult areaResult = new SpeedAreaResult();
            BeanUtils.copyProperties(speedArea, areaResult);

            resultList.add(areaResult);
        }

        return resultList;
    }
}
