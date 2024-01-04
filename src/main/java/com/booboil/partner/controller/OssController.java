package com.booboil.partner.controller;


import com.booboil.partner.common.BaseResponse;
import com.booboil.partner.common.ResultUtils;
import com.booboil.partner.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Api(description = "阿里云文件管理")
@CrossOrigin(origins = {"http://175.178.172.77/"}, allowCredentials = "true")
@RestController
@RequestMapping("/fileOss")
public class OssController {

    @Resource
    private OssService ossService;

    //上传头像
    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public BaseResponse<String> uploadOssFile(@RequestParam(required = false) MultipartFile file) {
        //获取上传的文件
        if (file.isEmpty()) {
            return null;
        }
        //返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file);
        //返回r对象
        return ResultUtils.success(url);
    }

}
