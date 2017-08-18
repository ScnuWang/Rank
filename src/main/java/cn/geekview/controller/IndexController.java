package cn.geekview.controller;

import com.sun.deploy.net.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController {

    @RequestMapping
    public String index(){
        return "index";
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
}