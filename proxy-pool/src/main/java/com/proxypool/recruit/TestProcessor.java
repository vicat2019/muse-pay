package com.proxypool.recruit;

import com.muse.common.util.SpringBeanUtils;
import com.proxypool.spider.ProcessorTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;

import java.util.List;

/**
 * @program: muse-pay
 * @description: 测试
 * @author: Vincent
 * @create: 2018-12-22 15:56
 **/
@Service("testProcessor")
public class TestProcessor extends ProcessorTemplate {
    private Logger log = LoggerFactory.getLogger("TestProcessor");

    private String url = "https://www.ip.cn/";

    @Autowired
    private SpringBeanUtils springBeanUtils;

    @Override
    public List parseContent(Page page) {
        // System.out.println(page.getHtml().toString());

        String ip = page.getHtml().xpath("//div[@id='result']/div/p[1]/code/text()").toString();
        String location = page.getHtml().xpath("//div[@id='result']/div/p[2]/code/text()").toString();

        log.info("解析，获取结果ip=" + ip + "\nlocation=" + location);

        return null;
    }

    @Override
    public List<String> parsePageUrl(Page page) {
        return null;
    }

    @Override
    public ProcessorTemplate getInstance() {
        return (ProcessorTemplate) springBeanUtils.getBean("testProcessor");
    }

    @Override
    public String getUrl() {
        return url;
    }
}
