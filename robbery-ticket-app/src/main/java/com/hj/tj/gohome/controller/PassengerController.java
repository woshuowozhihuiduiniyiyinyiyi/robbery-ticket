package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.service.PassengerService;
import com.hj.tj.gohome.vo.passenger.PassengerDetailResObj;
import com.hj.tj.gohome.vo.passenger.PassengerSaveReqObj;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class PassengerController {

    @Resource
    private PassengerService passengerService;

    @PostMapping("/passenger/save")
    public ResponseEntity savePassenger(@Valid @RequestBody PassengerSaveReqObj passengerSaveReqObj) {
        Integer passengerId = passengerService.savePassenger(passengerSaveReqObj);

        return ResponseEntity.ok(passengerId);
    }

    @GetMapping("/passenger/list")
    public ResponseEntity listPassenger() {
        return ResponseEntity.ok(passengerService.listPassenger());
    }

    @GetMapping("/passenger/detail/{id}")
    public ResponseEntity<PassengerDetailResObj> getPassengerDetail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(passengerService.getPassengerDetail(id));
    }

}
