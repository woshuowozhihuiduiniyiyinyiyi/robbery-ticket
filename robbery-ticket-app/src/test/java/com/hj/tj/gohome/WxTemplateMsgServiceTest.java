package com.hj.tj.gohome;

import com.hj.tj.gohome.entity.Order;
import com.hj.tj.gohome.service.WxTemplateMsgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WxTemplateMsgServiceTest {

    @Resource
    private WxTemplateMsgService wxTemplateMsgService;

    @Test
    public void testSendMsg() {
        Order order = new Order();
        order.setDestination("北京");
        order.setOrigin("上海");
        order.setOwnerId(224);

        wxTemplateMsgService.sendNewOrderMsg(order);
    }

    @Test
    public void testPushMsg() {
        wxTemplateMsgService.pushMsg();
    }
}
