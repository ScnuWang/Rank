package cn.geekview.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
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
    public  String  fileUpload(@RequestParam("file") MultipartFile multipartFile){
//        File file = new File(staticUrl+UUID.randomUUID()+multipartFile.getOriginalFilename());
        //如果这里不设置文件路径的话，可以在属性文件中配置存放路径
        //文件异常信息处理，比如上传文件过大，格式不正确，当然可以直接在前端对文件大小进行验证，但是服务器最好还是要做处理
        File file = new File(UUID.randomUUID()+multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("文件上传异常："+e.getMessage());
        }
        return "index";
    }

    /**
     * 同时上传多个文件
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping(value = "/files/batch",method = RequestMethod.POST)
    public  String  filesUpload(HttpServletRequest request) {
        List<MultipartFile> list =((MultipartHttpServletRequest)request).getFiles("file");
        for (MultipartFile multipartFile : list) {
            File file = new File(staticUrl+UUID.randomUUID()+multipartFile.getOriginalFilename());
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "index";
    }
}
