package com.nucc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.nucc"})
@EnableScheduling
@MapperScan("com.nucc.dao")
public class NucApplication {

	public static void main(String[] args) {
		SpringApplication.run(NucApplication.class, args);
	}
}
