package com.hj.tj.gohome.service;

import com.hj.tj.gohome.vo.station.StationInfoResObj;
import com.hj.tj.gohome.vo.station.TrainInfoReqObj;
import com.hj.tj.gohome.vo.station.TrainInfoResObj;

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
    List<TrainInfoResObj> listTrainInfo(TrainInfoReqObj stationInfoReqObj);

    /**
     * 根据车站名称模糊查询列车
     *
     * @param trainName
     * @return
     */
    List<StationInfoResObj> listStationInfo(String trainName);
}
