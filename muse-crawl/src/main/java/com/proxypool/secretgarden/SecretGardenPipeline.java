package com.proxypool.secretgarden;

import com.google.common.collect.Lists;
import com.proxypool.service.SgDataInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;

/**
 * @program: muse-pay
 * @description: SecretGarden数据处理
 * @author: Vincent
 * @create: 2019-02-18 15:20
 **/
@Component("secretGardenPipeline")
public class SecretGardenPipeline implements Pipeline {
    private Logger log = LoggerFactory.getLogger("SecretGardenPipeline");

    @Autowired
    private Executor executor;

    @Autowired
    private SgDataInfoService sgDataInfoService;

    // 存放数据的队列
    private static ArrayBlockingQueue<SecretGardenInfo> queue;

    static {
        queue = new ArrayBlockingQueue(1024);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        if (resultItems == null) return;

        List<SecretGardenInfo> result = resultItems.get("result");
        if (result == null || result.size() == 0) {
            return;
        }

        log.info("处理解析获取的内容=" + result);

        SecretGardenInfo sgInfo = result.get(0);
        if (sgInfo != null) {
            // 保存数据
            List<SgDataInfo> sgDataList = Lists.newArrayList();
            for (String item : sgInfo.getTargetUrlList()) {
                sgDataList.add(SgDataInfo.getInstance(sgInfo.getTitle(), item));
            }
            try {
                // 保存到库中
                int addResult = sgDataInfoService.insertBatch(sgDataList);
                // 添加到队列中处理
                if (addResult > 0) {
                    queue.put(sgInfo);
                } else {
                    log.error("保存抓取数据失败.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("处理抓取数据异常=" + e.getMessage());
            }
        }

        // 多线程处理结果
        executor.execute(new SecretGardenPicTask(queue, sgDataInfoService));

    }
}
