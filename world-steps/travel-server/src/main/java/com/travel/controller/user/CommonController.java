package com.travel.controller.user;

import com.travel.constant.MessageConstant;
import com.travel.result.Result;
import com.travel.utils.AliOssUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Common interface
 */
@RestController
@RequestMapping("/user/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("File upload：{}", file);

        try {
            // Original file name
            String originalFilename = file.getOriginalFilename();
            // Get last extension of the file name
            String extension = originalFilename.substring(originalFilename.indexOf("."));
            // Construct new file name
            String objectName = UUID.randomUUID().toString() + extension;

            // File request path
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("File upload failed: {}", e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
