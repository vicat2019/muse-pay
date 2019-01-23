package com.proxypool.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @program: muse-pay
 * @description: 初始化
 * @author: Vincent
 * @create: 2018-10-15 18:20
 **/
@Component
public class InitRunner implements CommandLineRunner {
    private Logger log = LoggerFactory.getLogger("InitRunner");

    @Override
    public void run(String... args) throws Exception {
        log.debug("--------------------完成初始化--------------------");
    }


}
