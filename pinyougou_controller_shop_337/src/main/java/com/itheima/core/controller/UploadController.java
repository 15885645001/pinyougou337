package com.itheima.core.controller;

import com.itheima.core.utils.FastDFSClient;
import entity.Result;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @描述: 上传图片管理
 * @Auther: yanlong
 * @Date: 2019/3/10 16:34:10
 * @Version: 1.0
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String fsu;//文件服务器地址

    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file){

        try {
            //1. 获取文件的完整名称(原始名)
            String filename = file.getOriginalFilename();
            System.out.println(filename);

            //2. 创建一个FastDFS---(分布式文件系统)的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");

            //获取扩展名  apache提供的工具类
            String extName = FilenameUtils.getExtension(filename);
            //3. 上传图片----得到部分路径
            String path = fastDFSClient.uploadFile(file.getBytes(), extName, null);

            return new Result(true,fsu+path);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }

    }
}
