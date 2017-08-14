package cn.geekview;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.geekview.mapper")
public class RankApplication {

	public static void main(String[] args) {
		SpringApplication.run(RankApplication.class, args);
	}
}
