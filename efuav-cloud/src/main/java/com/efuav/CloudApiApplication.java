package com.efuav;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.efuav.*.dao")
@SpringBootApplication
@EnableScheduling
@ConfigurationProperties("com.efuav")
public class CloudApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudApiApplication.class, args);
	}

}
