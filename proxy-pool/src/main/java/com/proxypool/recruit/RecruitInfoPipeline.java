package com.proxypool.recruit;

import com.proxypool.entry.RecruitInfo;
import com.proxypool.service.RecruitInfoService;
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
@Service("recruitInfoPipeline")
public class RecruitInfoPipeline implements Pipeline {

    @Autowired
    private RecruitInfoService recruitInfoService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        if (resultItems == null) return;

        List<RecruitInfo> result = resultItems.get("result");
        if (result == null) return;

        result.forEach(item -> {
            try {
                recruitInfoService.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
