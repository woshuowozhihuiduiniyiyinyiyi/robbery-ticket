package com.hj.tj.gohome.service;

import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicDetailResult;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicParam;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicResult;
import com.hj.tj.gohome.vo.dynamic.SpeedDynamicSaveParam;

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
     *
     * @param speedDynamicParam
     * @return
     */
    PageInfo<SpeedDynamicResult> listSpeedDynamic(SpeedDynamicParam speedDynamicParam);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    SpeedDynamicDetailResult findById(Integer id);

    /**
     * 发布动态或者回复
     *
     * @param speedDynamicSaveParam
     * @return
     */
    Integer insert(SpeedDynamicSaveParam speedDynamicSaveParam);

    /**
     * 登录后动态列表
     *
     * @param speedDynamicParam
     * @return
     */
    PageInfo<SpeedDynamicResult> loginDynamicList(SpeedDynamicParam speedDynamicParam);

    /**
     * 登录后详情
     *
     * @param id
     * @return
     */
    SpeedDynamicDetailResult loginFindById(Integer id);
}
