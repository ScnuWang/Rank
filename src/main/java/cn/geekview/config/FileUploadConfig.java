package cn.geekview.config;

import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
public class FileUploadConfig {
//    @Bean
//    public MultipartConfigElement multipartConfigElement(){
//        // 设置文件大小限制 ,超出设置页面会抛出异常信息，
//        // 这样在文件上传的地方就需要进行异常信息的处理了;
//        MultipartConfigFactory configFactory = new MultipartConfigFactory();
//        configFactory.setMaxFileSize("1MB");//KB或者MB
//        configFactory.setMaxRequestSize("5MB");
//        configFactory.setLocation("C:/Projects/");
//        return configFactory.createMultipartConfig();
//    }
}
