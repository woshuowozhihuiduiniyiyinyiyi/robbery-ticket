package com.hj.tj.gohome.service;

import com.hj.tj.gohome.vo.dynamic.SpeedDynamicResult;

import java.util.List;

public interface SpeedDynamicService {

    /**
     * 置顶列表
     *
     * @param areaId
     * @return
     */
    List<SpeedDynamicResult> dynamicTopList(Integer areaId);
}
