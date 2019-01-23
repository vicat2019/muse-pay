package com.proxypool.proxyip;

import com.proxypool.entry.ProxyIpInfo;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: muse-pay
 * @description: 89IP代理抓取类
 * @author: Vincent
 * @create: 2018-10-16 15:33
 **/
@Component("ip89Processor")
public class IP89Processor implements PageProcessor {
    private Logger log = LoggerFactory.getLogger("IP89Processor");

    @Autowired
    private ProxyPipeline proxyPipeline;

    @Value("${threadCount}")
    public int threadCount;

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        String url = page.getUrl().toString();

        // 解析内容
        Selectable selectable = page.getHtml().xpath("//table[@class='table-striped']/tbody/tr");

        if (selectable != null) {
            List<Selectable> nodes = selectable.nodes();

            List<ProxyIpInfo> proxyIpInfoList = new ArrayList<>();
            nodes.forEach(node -> {
                String ip = node.xpath("//td[1]/text()").toString();
                String port = node.xpath("//td[2]/text()").toString();
                String anonymous = node.xpath("//td[3]/text()").toString();
                String type = node.xpath("//td[4]/text()").toString();
                String location = node.xpath("//td[6]/text()").toString();
                String responseSpeed = node.xpath("//td[7]/text()").toString();
                String checkTime = node.xpath("//td[8]/text()").toString();

                // 生成实例
                proxyIpInfoList.add(ProxyIpInfo.getInstance(ip, port, type, responseSpeed, location, checkTime,
                        anonymous, "", location, url));
            });

            log.info("解析页面内容，获取代理个数=" + proxyIpInfoList.size());
            if (proxyIpInfoList.size() > 0) page.putField("result", proxyIpInfoList);
        }

        // 部分三：从页面发现后续的url地址来抓取
        List<String> urls = page.getHtml().links().regex("http://www\\.ip3366\\.net/\\?stype=1&page=\\d+").all();
        urls = urls.stream().filter(s -> {
            int index = s.lastIndexOf("=");
            String pageStr = s.substring(index + 1);

            if (!StringUtils.isEmpty(pageStr) && StringUtil.isNumeric(pageStr)) {
                int pageInt = Integer.valueOf(pageStr.trim());
                return pageInt < 10;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

        log.info("获取页码连接个数=" + urls.size());
        page.addTargetRequests(urls);
    }

    @Override
    public Site getSite() {
        return site;
    }

    /**
     * 开始执行任务
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 20 18 * * ?")
    public void execute() throws Exception {
        Spider.create(new IP89Processor())
                .addUrl("http://www.ip3366.net/")
                .thread(threadCount)
                .addPipeline(proxyPipeline)
                .run();
    }


    public static void main(String[] args) {
        IP89Processor processor = new IP89Processor();
        try {
            processor.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
