package com.proxypool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.proxypool", "com.muse"})
@MapperScan("com.proxypool.dao")
@EnableScheduling
public class ProxyPoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyPoolApplication.class, args);
	}
}
