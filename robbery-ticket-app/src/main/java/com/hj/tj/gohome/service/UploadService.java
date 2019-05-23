package com.hj.tj.gohome.service;

import java.io.InputStream;

/**
 * @author tangj
 * @description
 * @since 2019/5/23 13:36
 */
public interface UploadService {

    /**
     * 上传文件
     *
     * @param type             类型
     * @param inputStream      文件流
     * @param originalFilename 文件名
     * @return
     */
    String upload(String type, String originalFilename, InputStream inputStream);
}
