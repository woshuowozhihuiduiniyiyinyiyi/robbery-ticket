package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.service.StationService;
import com.hj.tj.gohome.vo.station.StationInfoResObj;
import com.hj.tj.gohome.vo.station.TrainInfoReqObj;
import com.hj.tj.gohome.vo.station.TrainInfoResObj;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @PostMapping("/train/info/list")
    public ResponseEntity<List<TrainInfoResObj>> getTrainInfoList(@Validated @RequestBody TrainInfoReqObj trainInfoReqObj) {
        List<TrainInfoResObj> stationInfoResObjs = stationService.listTrainInfo(trainInfoReqObj);
        return ResponseEntity.ok(stationInfoResObjs);
    }

    @GetMapping("/station/info/list")
    public ResponseEntity<List<StationInfoResObj>> getStationInfoList(@Validated @NotBlank String trainName) {
        if (Objects.isNull(trainName)) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        List<StationInfoResObj> stationInfoResObjs = stationService.listStationInfo(trainName);
        return ResponseEntity.ok(stationInfoResObjs);
    }
}
