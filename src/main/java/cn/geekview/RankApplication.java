package cn.geekview;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("cn.geekview.mapper")
public class RankApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(RankApplication.class, args);
	}
}
