package com.efuav;

import com.efuav.common.config.AppConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
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
	public static AppConfig appConfig;

	@Autowired
	public CloudApiApplication(AppConfig appConfig) {
		CloudApiApplication.appConfig = appConfig;
	}
}
