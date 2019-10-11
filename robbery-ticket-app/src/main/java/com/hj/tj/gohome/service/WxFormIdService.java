package com.hj.tj.gohome.service;

import com.hj.tj.gohome.vo.form.WxFormIdParam;

public interface WxFormIdService {

    /**
     * 添加一条formId 用于微信推送
     *
     * @param wxFormIdParam fromId 参数
     */
    void wxFormIdSave(WxFormIdParam wxFormIdParam);
}
