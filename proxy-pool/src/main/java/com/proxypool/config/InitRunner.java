package com.proxypool.config;

import com.proxypool.service.ProxyIpInfoService;
import com.proxypool.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ProxyIpInfoService proxyIpInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(String... args) throws Exception {
        // 查询所有的KEY
        /*ResultData result = proxyIpInfoService.getAllProxyCode();
        if (result.isOk() && result.resultIsNotEmpty()) {
            List<String> proxyCodeList = (List<String>) result.getData();

            Set<String> proxyCodeSet = proxyCodeList.stream().collect(Collectors.toSet());
            redisUtil.set("url_code", proxyCodeSet, 20 * 60);
        }*/

        log.debug("--------------------完成初始化--------------------");
    }


}
