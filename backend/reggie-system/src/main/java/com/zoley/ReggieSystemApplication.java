package com.zoley;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@MapperScan("com.zoley.mapper")
public class ReggieSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReggieSystemApplication.class, args);
		log.info("❤❈❉✿❀❃❁===========启动成功===============☂☃☄♨☣☢☠♝");
	}

}
