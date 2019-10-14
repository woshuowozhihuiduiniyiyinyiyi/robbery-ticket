package com.hj.tj.gohome.service;

import com.hj.tj.gohome.entity.Order;

import java.util.List;

public interface WxTemplateMsgService {

    /**
     * 发送模板消息
     *
     * @param keywordList
     * @param formId
     * @param toUserOpenId
     * @param appId
     * @param templateId
     * @return
     */
    boolean sendMsg(List<String> keywordList, String formId, String toUserOpenId, String appId, String templateId, String page);


    /**
     * 发送有新订单通知
     *
     * @param order
     * @return
     */
    boolean sendNewOrderMsg(Order order);

    /**
     * 推送消息表里面待推送的消息
     *
     * @return
     */
    Integer pushMsg();
}
