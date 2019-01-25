package com.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @program: muse-pay
 * @description: 秒杀主类
 * @author: Vincent
 * @create: 2018-12-24 14:35
 **/
@SpringBootApplication
@ComponentScan(basePackages = {"com.seckill", "com.muse"})
@MapperScan("com.seckill.dao")
public class SecKillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecKillApplication.class, args);
    }

}
