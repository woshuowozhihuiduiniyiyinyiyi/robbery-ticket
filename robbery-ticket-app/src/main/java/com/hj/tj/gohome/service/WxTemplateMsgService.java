package com.hj.tj.gohome.service;

import com.hj.tj.gohome.entity.Order;

import java.util.List;

public interface WxTemplateMsgService {

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

    /**
     * 添加一条新的推送信息
     *
     * @param ownerId
     * @param pushOwnerId
     */
    void addNewMsg(Integer ownerId, Integer pushOwnerId);
}
