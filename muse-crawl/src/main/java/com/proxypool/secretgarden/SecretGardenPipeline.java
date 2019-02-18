package com.proxypool.secretgarden;

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

    @Autowired
    private Executor executor;

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

        SecretGardenInfo sgInfo = result.get(0);
        if (sgInfo != null) {
            try {
                queue.put(sgInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 多线程处理结果
        executor.execute(new SecretGardenPicTask(queue));

    }
}
