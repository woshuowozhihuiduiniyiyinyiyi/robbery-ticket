package com.hj.tj.gohome.service;

import java.util.List;

public interface WxTemplateMsgService {

    /**
     * 发送加速留言模板消息
     *
     * @param keywordList 关键字列表
     */
    void sendSpeedMsg(List<String> keywordList);
}
