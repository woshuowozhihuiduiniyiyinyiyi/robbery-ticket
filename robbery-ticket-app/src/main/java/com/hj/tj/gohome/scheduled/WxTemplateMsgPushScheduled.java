package com.hj.tj.gohome.scheduled;

import com.hj.tj.gohome.service.WxTemplateMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
@Slf4j
@EnableScheduling
public class WxTemplateMsgPushScheduled {

    @Resource
    private WxTemplateMsgService wxTemplateMsgService;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void pushTemplateMsg() {
        log.info("[action = `WxTemplateMsgPushScheduled.pushTemplateMsg`. push msg start.time:{}]", new Date());
        Integer count = wxTemplateMsgService.pushMsg();
        log.info("[action = `WxTemplateMsgPushScheduled.pushTemplateMsg`. push msg end.time:{}, success count:{}]", new Date(), count);
    }
}
