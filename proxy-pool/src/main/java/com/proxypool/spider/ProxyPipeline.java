package com.proxypool.spider;

import com.proxypool.entry.ProxyIpInfo;
import com.proxypool.service.ProxyIpInfoService;
import com.proxypool.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: muse-pay
 * @description: 解析结果处理类
 * @author: Vincent
 * @create: 2018-10-15 16:16
 **/
@Service("proxyPipeline")
public class ProxyPipeline implements Pipeline {
    private Logger log = LoggerFactory.getLogger("ProxyPipeline");

    @Autowired
    private ProxyIpInfoService proxyIpInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void process(ResultItems resultItems, Task task) {
        if (resultItems == null) return;

        // 获取结果
        List<ProxyIpInfo> result = resultItems.get("result");
        if (result == null) return ;

        // 遍历结果集
        result.forEach(item -> {
            // 是否是HTTP类型的代理
            if (!StringUtils.isEmpty(item.getType())
                    && (!item.getType().contains("http") && !item.getType().contains("HTTP"))) {
                return;
            }

            // 去掉时间中的"秒"
            if (item.getResponseSpeed().contains("秒")) {
                item.setResponseSpeed(item.getResponseSpeed().replace("秒", ""));
            }

            try {
                Set<String> itemCodeSet = (Set<String>) redisUtil.get("url_code");
                if (itemCodeSet == null) {
                    // 保存到数据库
                    proxyIpInfoService.add(item);
                    // 设置到缓存中
                    itemCodeSet = new HashSet<>();
                    itemCodeSet.add(item.getCode());
                    redisUtil.set("url_code", itemCodeSet, 20*60);
                } else {
                    // 没有存在库中
                    if (!itemCodeSet.contains(item.getCode())) {
                        proxyIpInfoService.add(item);
                        itemCodeSet.add(item.getCode());
                        redisUtil.set("url_code", itemCodeSet, 20*60);
                    } else {
                        log.debug(item.getIp() + ":" + item.getPort() + " 已存在");
                    }
                }
            } catch (Exception e) {
                log.error("保存数据到数据库异常=" + e.getMessage());
            }
        });

    }
}
