package com.hj.tj.gohome.service;

import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicParam;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicResult;

import java.util.List;

public interface SpeedDynamicService {

    /**
     * 置顶列表
     *
     * @param areaId
     * @return
     */
    List<SpeedDynamicResult> listTopSpeedDynamic(Integer areaId);

    /**
     * 列表
     * @param speedDynamicParam
     * @return
     */
    PageInfo<SpeedDynamicResult> listSpeedDynamic(SpeedDynamicParam speedDynamicParam);
}
