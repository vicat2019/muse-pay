package com.proxypool.spider;

import com.muse.common.util.SpringBeanUtils;
import com.proxypool.entry.ProxyIpInfo;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: muse-pay
 * @description: 66IP处理类
 * @author: Vincent
 * @create: 2018-10-16 17:10
 **/
@Service("ip66Processor")
public class IP66Processor extends ProcessorTemplate {
    private String rootUrl = "http://www.66ip.cn/";

    @Override
    public List<ProxyIpInfo> parseContent(Page page) {
        List<ProxyIpInfo> proxyIpInfoList = new ArrayList<>();

        Selectable selectable = page.getHtml().xpath("//div[@class='container']/div/div/table/tbody/tr");
        if (selectable != null) {
            List<Selectable> nodes = selectable.nodes();

            nodes.forEach(node -> {
                String ip = node.xpath("//td[1]/text()").toString();
                String port = node.xpath("//td[2]/text()").toString();
                String location = node.xpath("//td[3]/text()").toString();
                String anonymous = node.xpath("//td[4]/text()").toString();
                String checkTime = node.xpath("//td[5]/text()").toString();

                if (!StringUtils.isEmpty(checkTime)) {
                    checkTime = checkTime.replaceAll("验证|时", "").trim();
                    checkTime = checkTime.replaceAll("年|月", "-");
                    checkTime = checkTime.replaceAll("日", " ");
                }

                proxyIpInfoList.add(ProxyIpInfo.getInstance(ip, port, "HTTP", "1", location, checkTime,
                        anonymous, "1", location, page.getUrl().toString()));
            });
        }

        log.info("解析页面内容，获取代理个数=" + proxyIpInfoList.size());
        return proxyIpInfoList;
    }

    @Override
    public List<String> parsePageUrl(Page page) {
        // 获取页码连接
        List<String> urlList = page.getHtml().links().regex("http://www\\.66ip\\.cn/\\d+\\.html").all();

        // 过滤，只取20页
        urlList = urlList.stream().filter(s -> {
            int index = s.lastIndexOf("/");
            int end = s.lastIndexOf(".");
            String pageStr = s.substring(index + 1, end);
            if (!StringUtils.isEmpty(pageStr) && StringUtil.isNumeric(pageStr)) {
                int pageInt = Integer.valueOf(pageStr.trim());
                return pageInt < 10;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

        log.info("获取页码连接个数=" + urlList.size());
        return urlList;
    }

    @Override
    public ProcessorTemplate getInstance() {
        return (ProcessorTemplate) SpringBeanUtils.getBean("ip66Processor");
    }

    @Override
    public String getUrl() {
        return rootUrl;
    }
}
