package com.proxypool.proxyip;

import com.proxypool.entry.ProxyIpInfo;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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
 * @description: 代理信息处理类
 * @author: Vincent
 * @create: 2018-10-15 15:21
 **/
@Service("proxyProcessor")
public class ProxyProcessor implements PageProcessor {
    private Logger log = LoggerFactory.getLogger("ProxyProcessor");

    @Autowired
    private ProxyPipeline proxyPipeline;

    @Value("${threadCount}")
    public int threadCount;

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);


    /**
     * process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
     *
     * @param page
     */
    @Override
    public void process(Page page) {
        String url = page.getUrl().toString();

        List<ProxyIpInfo> proxyIpInfoList = new ArrayList<>();

        Selectable selectable = page.getHtml().xpath("//table[@id='ip_list']/tbody/tr");
        if (selectable != null) {
            List<Selectable> nodes = selectable.nodes();
            for (Selectable item : nodes) {
                // IP地址
                String ip = item.xpath("//td[2]/text()").toString();
                if (!StringUtils.isEmpty(ip)) {
                    // 国家
                    String country = item.xpath("//td[1]/img/@alt").toString();
                    // 端口
                    String port = item.xpath("//td[3]/text()").toString();
                    // 位置
                    String location = item.xpath("//td[4]/a/text()").toString();
                    if (StringUtils.isEmpty(location)) location = item.xpath("//td[4]/text()").toString();
                    // 隐匿类型
                    String anonymous = item.xpath("//td[5]/text()").toString();
                    // 类型
                    String type = item.xpath("//td[6]/text()").toString();
                    // 速度
                    String speed = item.xpath("//td[7]/div/@title").toString();
                    // 连接时间
                    String connectTime = item.xpath("//td[8]/div/@title").toString();
                    // 存活时间
                    String survivalTime = item.xpath("//td[9]/text()").toString();
                    // 验证时间
                    String lastCheckTime = item.xpath("//td[10]/text()").toString();

                    // 生成实例
                    proxyIpInfoList.add(ProxyIpInfo.getInstance(ip, port, type, speed, location, lastCheckTime,
                            anonymous, survivalTime, country, url));
                }
            }

            log.info("解析页面内容，获取代理个数=" + proxyIpInfoList.size());
            page.putField("result", proxyIpInfoList);
        }

        // 部分三：从页面发现后续的url地址来抓取
        List<String> urls = page.getHtml().links().regex("http://www.xicidaili.com/nn/\\d+").all();
        urls.addAll(page.getHtml().links().regex("http://www.xicidaili.com/wt/\\d+").all());
        urls = urls.stream().filter(s -> {
            int index = s.lastIndexOf("/");
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
    @Scheduled(cron = "0 0 18 * * ?")
    public void execute() throws Exception {
        Spider.create(new ProxyProcessor())
                .addUrl("http://www.xicidaili.com/nn")
                .addUrl("http://www.xicidaili.com/wt/")
                .thread(threadCount)
                .addPipeline(proxyPipeline)
                .run();
    }


    public static void main(String[] args) {
        ProxyProcessor processor = new ProxyProcessor();
        try {
            processor.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
