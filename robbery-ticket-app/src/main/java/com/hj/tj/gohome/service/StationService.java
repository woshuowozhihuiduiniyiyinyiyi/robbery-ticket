package com.hj.tj.gohome.service;

import com.hj.tj.gohome.vo.station.StationInfoReqObj;
import com.hj.tj.gohome.vo.station.StationInfoResObj;

import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2019/2/28 11:07
 */
public interface StationService {

    /**
     * 根据站点信息查询12306 车次相关信息
     *
     * @param stationInfoReqObj
     * @return
     */
    List<StationInfoResObj> listStationInfo(StationInfoReqObj stationInfoReqObj);
}
