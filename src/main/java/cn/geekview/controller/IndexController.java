package cn.geekview.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsFileUploadSupport;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.List;
import java.util.UUID;

@Controller
public class IndexController {

    @Value("${static.url}")
    private String staticUrl;

    protected Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/errortest")
    public String errortest(){
        return "error";
    }

    /**
     * 重定向到指定地址
     * @param response
     */
    @RequestMapping("/redirect")
    public void redirect(String url, HttpServletResponse response){
        if (url!=null){
            try {
                response.sendRedirect(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传单个文件
     * @param multipartFile
     * @return
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping(value = "/file",method = RequestMethod.POST)
    public  String  fileUpload(@RequestParam("singleFile") MultipartFile multipartFile) throws IOException, ServletException {
        File file = new File(staticUrl+UUID.randomUUID()+multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return "index";
    }
}
