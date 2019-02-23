package com.mq.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.proxypool.service.MeBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: muse-pay
 * @description: 项目初始化
 * @author: Vincent
 * @create: 2018-10-15 18:20
 **/
@Component
public class InitRunner implements CommandLineRunner {
    private Logger log = LoggerFactory.getLogger("InitRunner");

    @Autowired
    private MeBookService meBookService;

    public static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 30000);

    @Override
    public void run(String... args) throws Exception {
        List<Integer> codeList = meBookService.getAllCode();
        if (codeList != null && codeList.size() > 0) {
            codeList.forEach(bloomFilter::put);
        }
        log.info("初始化布隆过滤器，值个数=" + codeList.size());

        log.debug("--------------------完成初始化--------------------");
    }


}
