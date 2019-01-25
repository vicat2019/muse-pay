package com.proxypool.kindlebook;

import com.proxypool.service.MeBookService;
import com.proxypool.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * @program: muse-pay
 * @description: 解析结果处理类
 * @author: Vincent
 * @create: 2018-10-15 16:16
 **/
@Service("meBookPipeline")
public class MeBookPipeline implements Pipeline {
    private static Logger log = LoggerFactory.getLogger("MeBookPipeline");

    @Autowired
    private MeBookService meBookService;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public void process(ResultItems resultItems, Task task) {
        if (resultItems == null) {
            log.error("结果项集合[resultItems]为空");
            return;
        }

        // 获取结果
        List<MeBookInfo> result = resultItems.get("result");
        if (result == null) {
            log.info("解析结果项集合[result]为空");
            return;
        }

        // 遍历结果集
        result.forEach(item -> {
            try {
                // 检查是否已经存在
                if (meBookService.getCountByCode(item.getCode()) > 0) {
                    log.info("图书《" + item.getName() + "》已经存在");
                    return;
                }
                // 保存数据
                meBookService.insert(item);
                redisUtil.hdel(MeBookInfo.REDIS_KEY_BOOK_LIST, item.getDetailUrl());

            } catch (Exception e) {
                e.printStackTrace();
                log.error("保存图书异常， 图书信息=" + item.toString() + ", 异常=" + e.getMessage());
            }
        });
    }
}
