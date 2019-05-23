package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.service.UploadService;
import com.hj.tj.gohome.utils.OwnerContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author tangj
 * @description
 * @since 2019/5/23 13:36
 */
@RestController
@RequestMapping("/api/auth/file/upload")
@Slf4j
@Validated
public class UploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping
    public ResponseEntity<String> upload(@NotNull(message = "文件内容不能为空") MultipartFile file) throws IOException {
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        log.info("User:{},type:{},Upload File:{},Size:{},ContentType:{}",
                OwnerContextHelper.getOwnerId(), type,
                file.getOriginalFilename(), file.getSize(), file.getContentType());

        String uploadUrl = uploadService.upload(type, file.getOriginalFilename(), file.getInputStream());
        return ResponseEntity.ok(uploadUrl);
    }
}
