package com.proxypool.component;


import com.muse.common.entity.BaseEntityInfo;
import com.muse.common.entity.ResultData;
import com.muse.common.util.SpringBeanUtils;
import com.proxypool.entry.ProxyIpInfo;
import com.proxypool.service.ProxyIpInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: muse-pay
 * @description: 解析模版
 * @author: Vincent
 * @create: 2018-10-16 16:35
 **/
public abstract class ProcessorTemplate<T extends BaseEntityInfo> implements PageProcessor {
    protected static Logger log = LoggerFactory.getLogger("ProcessorTemplate");

    // 并发线程数
    public int threadCount = 1;

    // 爬取时间间隔
    private int interval = 1000;

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site;

    @Autowired
    private ProxyIpInfoService proxyIpInfoService;

    /**
     * 默认构造方法
     */
    public ProcessorTemplate() {
        initSite();
    }

    /**
     * 构造方法
     *
     * @param interval    处理两个页面之间的时间间隔
     * @param threadCount 线程数
     */
    public ProcessorTemplate(int interval, int threadCount) {
        this.threadCount = threadCount;
        this.interval = interval;

        initSite();
    }

    /**
     * 初始化抓取网站的相关配置
     */
    private void initSite() {
        site = Site.me().setRetryTimes(3).setSleepTime(interval);
    }

    /**
     * 处理下载的页面美容
     *
     * @param page 页面信息
     */
    @Override
    public void process(Page page) {
        // 解析内容
        List<T> resultItemList = parseContent(page);

        // 过滤为空的内容
        if (resultItemList != null && resultItemList.size() > 0) {
            resultItemList = resultItemList.stream().filter(BaseEntityInfo::legitimate).collect(Collectors.toList());
            if (resultItemList != null && resultItemList.size() > 0) {
                page.putField("result", resultItemList);
            }
        }

        // 解析分页信息
        List<String> urlList = parseUrl(page);
        if (urlList != null && urlList.size() > 0) {
            page.addTargetRequests(urlList);
        }
    }

    /**
     * 解析内容
     *
     * @param page 下载的页面信息
     */
    public abstract List<T> parseContent(Page page);

    /**
     * 解析网页内容
     *
     * @param page 下载的页面信息
     */
    public abstract List<String> parseUrl(Page page);

    /**
     * 获取实例
     *
     * @return ProcessorTemplate
     */
    public abstract ProcessorTemplate getInstance();

    /**
     * 从Spring中获取实例
     *
     * @param clazz
     * @return
     */
    protected ProcessorTemplate getBeanFromContainer(Class<?> clazz) {
        return (ProcessorTemplate) SpringBeanUtils.getBean(clazz);
    }

    /**
     * 从Spring中获取实例
     *
     * @param serverName
     * @return
     */
    protected ProcessorTemplate getBeanFromContainer(String serverName) {
        return (ProcessorTemplate) SpringBeanUtils.getBean(serverName);
    }


    /**
     * 获取URL
     *
     * @return String
     */
    public abstract String getUrl();

    /**
     * 开始执行任务
     *
     * @param pipeline 结果处理对象
     * @param useProxy 是否使用代理，默认不使用
     * @throws Exception 异常
     */
    public void execute(Pipeline pipeline, boolean useProxy) throws Exception {
        Spider spider = Spider.create(getInstance())
                .addUrl(getUrl())
                .thread(threadCount);

        // 设置结果数据处理对象
        if (pipeline != null) spider.addPipeline(pipeline);
        // 设置代理池
        if (useProxy) setHttpProxyPool(spider);
        // 开始抓取
        spider.run();
    }

    /**
     * 开始执行任务
     *
     * @param pipeline   结果处理对象
     * @param useProxy   是否使用代理，默认不使用
     * @param cookiesMap Cookies，默认不使用
     * @param downloader 下载器
     * @throws Exception 异常
     */
    public void execute(Pipeline pipeline, boolean useProxy, Map<String, String> cookiesMap, Downloader downloader) throws Exception {
        Spider spider = Spider.create(getInstance())
                .addUrl(getUrl())
                .thread(threadCount);

        // 设置结果数据处理对象
        if (pipeline != null) spider.addPipeline(pipeline);
        // 设置代理池
        if (useProxy) setHttpProxyPool(spider);
        // 设置Cookies
        if (cookiesMap != null && cookiesMap.size() > 0) {
            setCookies(cookiesMap);
        }
        if (downloader != null) {
            spider.setDownloader(downloader);
        }
        // 开始抓取
        spider.run();
    }

    /**
     * 设置Cookies值
     *
     * @param cookiesMap Cookies值MAP
     */
    private void setCookies(Map<String, String> cookiesMap) {
        if (this.site == null) {
            initSite();
        }
        cookiesMap.forEach((k, v) -> site.addCookie(k, v));
    }

    /**
     * 开始执行任务
     *
     * @param pipeline   结果处理对象
     * @param downloader 自定义下载组件
     * @throws Exception 异常
     */
    public void execute(Pipeline pipeline, Downloader downloader) throws Exception {
        Spider spider = Spider.create(getInstance())
                .addUrl(getUrl())
                .thread(threadCount);

        // 设置结果数据处理对象
        if (pipeline != null) spider.addPipeline(pipeline);
        // 设置代理池
        if (downloader != null) spider.setDownloader(downloader);
        // 开始抓取
        spider.run();
    }

    /**
     * 设置代理池
     *
     * @param spider 爬虫
     * @throws Exception 异常
     */
    private void setHttpProxyPool(Spider spider) throws Exception {
        ResultData proxyResultData = proxyIpInfoService.getFrontProxy();

        if (proxyResultData.isOk() && proxyResultData.resultIsNotEmpty()) {
            List<ProxyIpInfo> proxyIpList = (List<ProxyIpInfo>) proxyResultData.getData();

            // 代理信息为空
            if (proxyIpList == null || proxyIpList.size() == 0) return;

            // 生成代理对象
            List<Proxy> proxyList = new ArrayList<>();
            proxyIpList.forEach(item -> {
                if (item.legal()) {
                    int port = 0;
                    try {
                        port = Integer.valueOf(item.getPort().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    proxyList.add(new Proxy(item.getIp(), port));
                }
            });

            // 设置代理池
            if (proxyList.size() > 0) {
                HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
                httpClientDownloader.setProxyProvider(new SimpleProxyProvider(proxyList));

                spider.setDownloader(httpClientDownloader);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    /**
     * 设置时间间隔
     *
     * @param interval 时间间隔(毫秒)
     * @return ProcessorTemplate
     */
    public ProcessorTemplate setInterval(int interval) {
        this.interval = interval;

        if (site != null) site.setSleepTime(this.interval);

        return this;
    }

    /**
     * 设置线程数
     *
     * @param threadCount 线程数
     * @return ProcessorTemplate
     */
    public ProcessorTemplate setThreadCount(int threadCount) {
        this.threadCount = threadCount;
        return this;
    }
}
