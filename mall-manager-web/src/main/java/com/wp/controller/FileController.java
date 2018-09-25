package com.wp.controller;

import com.wp.common.pojo.E3Result;
import com.wp.common.util.FastDFSClient;
import com.wp.common.util.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: WpMall
 * @description: manager file upload
 * @author: Pan wu
 * @create: 2018-09-25 00:38
 **/
@Controller
public class FileController {



    /*需要返回的json格式如下   成功时{
        "error" : 0,
        "url" : "http://www.example.com/path/to/file.ext"}
          失败时{
        "error" : 1,
        "message" : "错误信息"}*/
    @RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
    @ResponseBody
    //consider platform-cross to return string
    public String pictureUpload(MultipartFile uploadFile) {
        Map map = new HashMap();
        try {
            //get file extension name
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //Create fastDfs client
            String conf = "classpath:/FastDFS.properties";
            FastDFSClient fastDFSClient = new FastDFSClient(conf);
            String path = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            String url = "http://192.168.25.133/" + path;
            map.put("error", 0);
            map.put("url", url);
            return JsonUtils.objectToJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", 1);
            map.put("message", "picture upload failed");
            return JsonUtils.objectToJson(map);
        }
    }
}
