package cn.geekview.controller;

import com.sun.deploy.net.HttpUtils;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController {

    @Value("${client_ID}")
    private String client_ID;
    @Value("${client_SERCRET}")
    private String client_SERCRET;
    @Value("${redirect_URI}")
    private String redirect_URI;
    @Value("${baseURL}")
    private String baseURL;
    @Value("${authorizeURL}")
    private String authorizeURL;
    @Value("${accessTokenURL}")
    private String accessTokenURL;
    @Value("${rmURL}")
    private String rmURL;

    @GetMapping("/sinaLogin")
   public void sinaLogin(HttpServletResponse response){
        //https://api.weibo.com/oauth2/authorize?client_id=YOUR_CLIENT_ID&response_type=code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI

        //https://api.weibo.com/oauth2/authorize?client_id=3967331671&redirect_uri=http://1670a21b58.imwork.net/login&response_type=code
        String loginUrl = authorizeURL+"?client_id="+client_ID+"&response_type=code"+"&redirect_uri="+redirect_URI;
        try {
            response.sendRedirect(loginUrl);
        } catch (IOException e) {
            e.printStackTrace();
            //登录失败
        }
   }
   @GetMapping("/loginCallBack")
   public String loginCallBack(@RequestParam("code") String code){
       System.out.println("code="+code);
       RestTemplate restTemplate = new RestTemplate();
       //https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=authorization_code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&code=CODE
       String tokenUrl = accessTokenURL+"?client_id="+client_ID+"&client_secret="+client_SERCRET+"&grant_type=authorization_code&redirect_uri="+redirect_URI+"&code="+code;
       ResponseEntity responseEntity = restTemplate.exchange(tokenUrl,HttpMethod.POST,HttpEntity.EMPTY,String.class);
       JSONObject jsonObject = (JSONObject) JSONUtil.parse(responseEntity.getBody());
       String access_token = (String) jsonObject.get("access_token");
       System.out.println("access_token="+access_token);
       System.out.println("uid="+jsonObject.get("uid"));
       return "login";
   }






}
