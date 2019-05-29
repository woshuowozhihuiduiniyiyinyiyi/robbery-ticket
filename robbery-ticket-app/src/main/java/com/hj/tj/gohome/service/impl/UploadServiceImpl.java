package com.hj.tj.gohome.service.impl;

import com.aliyun.oss.OSSClient;
import com.hj.tj.gohome.config.oss.OssProperties;
import com.hj.tj.gohome.service.UploadService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Date;

/**
 * @author tangj
 * @description
 * @since 2019/5/23 13:37
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Resource
    private OSSClient ossClient;

    @Resource
    private OssProperties ossProperties;

    private static final String ROBBERY = "ROBBERY";

    @Resource
    private ApplicationContext applicationContext;

    private String generate(String type, String originalFilename) {
        String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String base64Name = Base64.encodeBase64URLSafeString(fileName.getBytes());
        String profile = applicationContext.getEnvironment().getActiveProfiles()[0];
        Date date = new Date();
        return ROBBERY +
                "/" +
                profile +
                "/" +
                type +
                "/" +
                date.getTime() +
                "-" +
                RandomStringUtils.randomNumeric(5) +
                "-" +
                base64Name +
                "." +
                fileType;
    }

    @Override
    public String upload(String type, String originalFilename, InputStream inputStream) {
        String fileName = generate(type, originalFilename);
        ossClient.putObject(ossProperties.getBucketName(), fileName, inputStream);
        return "https://" + ossProperties.getBucketName() + "." + ossProperties.getEndpoint() + "/" + fileName;
    }
}
