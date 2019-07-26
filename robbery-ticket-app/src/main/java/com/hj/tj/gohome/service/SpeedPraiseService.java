package com.hj.tj.gohome.service;

import com.hj.tj.gohome.entity.SpeedPraise;
import com.hj.tj.gohome.vo.praise.SpeedPraiseSaveParam;

import java.util.List;

public interface SpeedPraiseService {

    /**
     * 点赞数据
     *
     * @param speedPraiseSaveParam
     * @return
     */
    Integer save(SpeedPraiseSaveParam speedPraiseSaveParam);

    /**
     * 根据数据id列表和类型来获取点赞列表
     *
     * @param dataIds
     * @param dataType
     * @return
     */
    List<SpeedPraise> listByDataIdAndType(Integer ownerId, List<Integer> dataIds, Integer dataType);
}
