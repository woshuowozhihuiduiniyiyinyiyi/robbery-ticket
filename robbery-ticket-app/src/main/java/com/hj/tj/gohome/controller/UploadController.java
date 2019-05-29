package com.hj.tj.gohome.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.hj.tj.gohome.config.handler.ServiceException;
import com.hj.tj.gohome.config.handler.ServiceExceptionEnum;
import com.hj.tj.gohome.service.UploadService;
import com.hj.tj.gohome.utils.OwnerContextHelper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author tangj
 * @description
 * @since 2019/5/23 13:36
 */
@RestController
@RequestMapping("/api/file/upload")
@Slf4j
@Validated
public class UploadController {

    @Resource
    private UploadService uploadService;

    @Resource
    private WxMaService wxMaService;

    @PostMapping
    public ResponseEntity<String> upload(@NotNull(message = "文件内容不能为空") MultipartFile file) throws IOException {
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        // 1M 以下进行敏感信息审核
        if (file.getSize() > 1024 * 1024) {
            throw new ServiceException(ServiceExceptionEnum.IMG_SITE_LIMIT);
        }

        String prefix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        File excelFile = File.createTempFile(UUID.randomUUID().toString().replaceAll("-", ""), prefix);
        file.transferTo(excelFile);

        try {
            wxMaService.imgSecCheck(excelFile);
        } catch (WxErrorException e) {
            WxError error = e.getError();
            if (87014 == error.getErrorCode()) {
                throw new ServiceException(ServiceExceptionEnum.IMG_SEC);
            } else {
                throw new ServiceException(ServiceExceptionEnum.SYS_ERROR);
            }
        }

        deleteFile(excelFile);

        log.info("User:{},type:{},Upload File:{},Size:{},ContentType:{}",
                OwnerContextHelper.getOwnerId(), type,
                file.getOriginalFilename(), file.getSize(), file.getContentType());

        String uploadUrl = uploadService.upload(type, file.getOriginalFilename(), file.getInputStream());
        return ResponseEntity.ok(uploadUrl);
    }

    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
