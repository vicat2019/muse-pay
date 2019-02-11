package com.proxypool.recruit;

import com.muse.common.entity.ResultData;
import com.proxypool.entry.RecruitInfo;
import com.proxypool.service.RecruitInfoService;
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
@Service("recruitInfoPipeline")
public class RecruitInfoPipeline implements Pipeline {
    private Logger log = LoggerFactory.getLogger("RecruitInfoPipeline");

    @Autowired
    private RecruitInfoService recruitInfoService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        if (resultItems == null) return;

        List<RecruitInfo> result = resultItems.get("result");
        if (result == null) return;

        result.forEach(item -> {
            try {
                ResultData addResult = recruitInfoService.add(item);
                log.info("保存JL数据结果=" + addResult);

            } catch (Exception e) {
                e.printStackTrace();
                log.error("保存JL数据结果异常=" + e.getMessage());
            }
        });

    }
}
