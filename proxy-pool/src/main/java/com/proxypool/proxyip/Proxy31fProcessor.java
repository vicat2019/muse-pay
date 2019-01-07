package com.proxypool.proxyip;

import com.muse.common.util.SpringBeanUtils;
import com.proxypool.component.ProcessorTemplate;
import com.proxypool.entry.ProxyIpInfo;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: muse-pay
 * @description: f31代理
 * @author: Vincent
 * @create: 2018-10-28 10:35
 **/
@Service("proxy31fProcessor")
public class Proxy31fProcessor extends ProcessorTemplate {

    private String url = "http://31f.cn/https-proxy/";


    @Override
    public List<ProxyIpInfo> parseContent(Page page) {
        List<ProxyIpInfo> proxyList = new ArrayList<>();

        // 解析内容
        Selectable selectable = page.getHtml().xpath("//table[@class='table-striped']/tbody/tr");
        Pattern pattern = Pattern.compile("(\\d+\\.?){4}");

        List<Selectable> nodes = selectable.nodes();
        if (nodes != null && nodes.size() > 0) {
            nodes.forEach(item -> {
                String ip = item.xpath("//td[2]/text()").toString();
                if (ip != null && !StringUtils.isEmpty(ip)) {
                    Matcher matcher = pattern.matcher(ip);
                    if (matcher.find()) {

                        String port = item.xpath("//td[3]/text()").toString();
                        String location = item.xpath("//td[4]/a/text()").toString() + item.xpath("//td[5]/a/text()").toString();
                        String type = item.xpath("//td[7]/a/text()").toString();
                        String responseSpeed = item.xpath("//td[8]/text()").toString();
                        if (responseSpeed != null && responseSpeed.contains("毫秒")) {
                            responseSpeed = responseSpeed.replace("毫秒", "");
                        }
                        if (responseSpeed != null && NumberUtils.isNumber(responseSpeed.trim())) {
                            responseSpeed = String.valueOf(Double.parseDouble(responseSpeed.trim()) / 1000D);
                        }

                        proxyList.add(ProxyIpInfo.getInstance(ip, port, type.toUpperCase(), responseSpeed, location, "",
                                "未知", "1", location, page.getUrl().toString()));
                    }
                }
            });
        }

        log.info("解析页面内容，获取代理个数=" + proxyList.size());
        return proxyList;
    }

    @Override
    public List<String> parseUrl(Page page) {
        return new ArrayList<>();
    }

    @Override
    public ProcessorTemplate getInstance() {
        return (ProcessorTemplate) SpringBeanUtils.getBean("proxy31fProcessor");
    }

    @Override
    public String getUrl() {
        return url;
    }
}
