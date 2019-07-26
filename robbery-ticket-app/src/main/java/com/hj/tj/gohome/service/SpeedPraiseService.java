package com.hj.tj.gohome.service;

import com.hj.tj.gohome.vo.praise.SpeedPraiseSaveParam;

public interface SpeedPraiseService {

    /**
     * 点赞数据
     *
     * @param speedPraiseSaveParam
     * @return
     */
    Integer save(SpeedPraiseSaveParam speedPraiseSaveParam);
}
