package com.proxypool.picture;

import com.proxypool.service.PictureInfoService;
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
@Service("pictureInfoPipeline")
public class PictureInfoPipeline implements Pipeline {
    private static Logger log = LoggerFactory.getLogger("PictureInfoPipeline");


    @Autowired
    private PictureInfoService pictureInfoService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        if (resultItems == null) {
            log.error("结果项集合[resultItems]为空");
            return;
        }

        // 获取结果
        List<PictureInfo> result = resultItems.get("result");
        if (result == null) {
            log.error("解析结果项集合[result]为空");
            return;
        }

        // 遍历结果集
        result.forEach(item -> {
            try {
                pictureInfoService.insert(item);

            } catch (Exception e) {
                e.printStackTrace();
                log.error("保存Picture=" + item.toString() + ", 异常=" + e.getMessage());
            }
        });
    }
}
