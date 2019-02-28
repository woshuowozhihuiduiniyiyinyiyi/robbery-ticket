package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.service.StationService;
import com.hj.tj.gohome.vo.station.StationInfoReqObj;
import com.hj.tj.gohome.vo.station.StationInfoResObj;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2019/2/28 9:30
 */
@RestController
@RequestMapping("/api")
public class StationController {

    @Resource
    private StationService stationService;

    @PostMapping("/station/info/list")
    public ResponseEntity<List<StationInfoResObj>> getStationInfoList(@Validated @RequestBody StationInfoReqObj stationInfoReqObj) {
        List<StationInfoResObj> stationInfoResObjs = stationService.listStationInfo(stationInfoReqObj);
        return ResponseEntity.ok(stationInfoResObjs);
    }
}
