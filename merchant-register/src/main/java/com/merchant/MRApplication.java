package com.merchant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.merchant"})
@MapperScan("com.merchant.dao")
public class MRApplication {

	public static void main(String[] args) {
		SpringApplication.run(MRApplication.class, args);
	}
}
