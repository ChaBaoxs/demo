package com.jc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DemoApplication {

	//启动类
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		log.info("启动成功");
	}

}
