package com.proxypool.weibo;

import com.proxypool.component.ProcessorTemplate;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;

import java.util.List;

/**
 * @program: muse-pay
 * @description: Weibo
 * @author: Vincent
 * @create: 2019-01-28 16:34
 **/
@Service("weiboProcessor")
public class WeiboProcessor extends ProcessorTemplate {

    private String URL = "https://weibo.com/?category=10007";

    @Override
    public List parseContent(Page page) {

        String content = page.getHtml().toString();
        System.out.println(content);

        return null;
    }

    @Override
    public List<String> parseUrl(Page page) {
        return null;
    }

    @Override
    public ProcessorTemplate getInstance() {
        return super.getBeanFromContainer(WeiboProcessor.class);
    }

    @Override
    public String getUrl() {
        return URL;
    }
}
