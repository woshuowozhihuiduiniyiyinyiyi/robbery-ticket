package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.service.OrderService;
import com.hj.tj.gohome.vo.order.OrderSaveParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/order/save")
    public ResponseEntity saveOrder(@Valid @RequestBody OrderSaveParam orderSaveParam) {
        return ResponseEntity.ok(orderService.saveOrder(orderSaveParam));
    }
}
