package com.hj.tj.gohome.controller;

import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.service.OrderService;
import com.hj.tj.gohome.utils.PortalUserContextHelper;
import com.hj.tj.gohome.vo.requestVo.OrderInsertReqObj;
import com.hj.tj.gohome.vo.requestVo.OrderReqObj;
import com.hj.tj.gohome.vo.responseVO.OrderResObj;
import com.hj.tj.gohome.vo.responseVO.OrderStatisticDataResObj;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/order/list")
    public ResponseEntity listOrder(@Validated @RequestBody OrderReqObj orderReqObj) {
        PageInfo<OrderResObj> orderResObjs = orderService.listOrder(orderReqObj);

        return ResponseEntity.ok(orderResObjs);
    }

    @PostMapping("/order/save")
    public ResponseEntity saveOrder(@Validated @RequestBody OrderInsertReqObj orderInsertReqObj) throws Exception {
        Integer userId = PortalUserContextHelper.getUserId();
        orderInsertReqObj.setPortalUserId(userId);

        Integer result = orderService.saveOrder(orderInsertReqObj);

        if (result.compareTo(0) > 0) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/order/detail/{id}")
    public ResponseEntity getOrderDetail(@PathVariable("id") Integer id) {
        OrderResObj orderDetail = orderService.getOrderDetail(id);

        return ResponseEntity.ok(orderDetail);
    }

    @PostMapping("/order/delete")
    public ResponseEntity deleteOrder(@Validated @NotEmpty(message = "订单id列表不能为空") @RequestBody List<Integer> orderIdList) {
        orderService.deleteOrder(orderIdList);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/order/statistic/data")
    public ResponseEntity statisticData() {
        OrderStatisticDataResObj orderStatisticDataResObj = orderService.statisticData();

        return ResponseEntity.ok(orderStatisticDataResObj);
    }

    @GetMapping("/order/refresh/query/date")
    public ResponseEntity refreshQueryDate() {
        orderService.refreshQueryDate();

        return ResponseEntity.ok().build();
    }

}
