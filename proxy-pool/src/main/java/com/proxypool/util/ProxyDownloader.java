package com.proxypool.util;

import com.proxypool.entry.ProxyIpInfo;
import com.proxypool.service.ProxyIpInfoService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.CharsetUtils;
import us.codecraft.webmagic.utils.HttpClientUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: muse-pay
 * @description: 使用代理池的下载类
 * @author: Vincent
 * @create: 2018-12-22 15:06
 **/
@Component
public class ProxyDownloader extends AbstractDownloader implements Downloader {
    private Logger log = LoggerFactory.getLogger("ProxyDownloader");

    @Autowired
    private ProxyIpInfoService proxyIpInfoService;

    private final Map<String, CloseableHttpClient> httpClients = new HashMap<>();

    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

    private boolean responseHeader = true;

    // 当前index
    static int currIndex = 0;

    // 可用的代理IP集合
    static List<ProxyIpInfo> availableProxyIpList = new ArrayList<>();

    // 互斥锁 参数默认false，不公平锁
    private static Lock lock = new ReentrantLock(true);

    // 请求头中的用户代理
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/62.0.3202.94 Safari/537.36";

    // 重试次数
    private int retryCount = 3;

    /**
     * 初始化，查询可用的代理IP列表
     */
    private void init() {
        // 查询可用的IP
        try {
            availableProxyIpList = proxyIpInfoService.getUsableProxyIp(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Page download(Request request, Task task) {
        if (task == null || task.getSite() == null) {
            throw new NullPointerException("task or site can not be null");
        }
        CloseableHttpResponse httpResponse;
        CloseableHttpClient httpClient = getHttpClient(task.getSite());

        // 创建httpGet实例
        HttpGet httpGet = new HttpGet(request.getUrl());
        // 设置请求头消息
        httpGet.setHeader("User-Agent", USER_AGENT);

        httpResponse = getCloseableHttpResponse(httpClient, httpGet);
        int statusCode = getResponseStatus(httpResponse);

        // 请求的状态
        int tryCount = 0;
        while (statusCode != HttpStatus.OK.value() && tryCount < retryCount) {
            updateProxyIpStatus(httpGet, "FAIL");
            httpResponse = getCloseableHttpResponse(httpClient, httpGet);
            statusCode = getResponseStatus(httpResponse);

            tryCount++;
        }
        // 如果成功，则更改成功信息
        if (statusCode == HttpStatus.OK.value()) updateProxyIpStatus(httpGet, "SUCCESS");

        Page page = Page.fail();
        try {
            page = handleResponse(request, request.getCharset() != null ? request.getCharset() : task.getSite().getCharset(), httpResponse);
            onSuccess(request);
            log.info("下载页面成功，地址=" + request.getUrl());
            return page;

        } catch (IOException e) {
            log.error("下载页面失败，地址=" + request.getUrl() + "，异常信息=" + e.getMessage());
            onError(request);
            return page;

        } finally {
            if (httpResponse != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
        }
    }

    /**
     * 设置使用代理信息结果
     *
     * @param httpGet
     * @param status
     */
    private void updateProxyIpStatus(HttpGet httpGet, String status) {
        try {
            String ip = httpGet.getConfig().getProxy().getHostName();
            proxyIpInfoService.updateProxyIpStatusInfo(new ProxyIpInfo(ip, status));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用代理发送请求
     *
     * @param httpClient
     * @param httpGet
     * @return
     */
    private CloseableHttpResponse getCloseableHttpResponse(CloseableHttpClient httpClient, HttpGet httpGet) {
        CloseableHttpResponse httpResponse = null;

        // 获取代理IP
        Map<String, Object> proxyIpInfo = getProxyInfo();
        String proxyIp = (String) proxyIpInfo.get("proxyIp");
        int proxyPort = (int) proxyIpInfo.get("proxyPort");

        // 是否有代理
        if (!StringUtils.isEmpty(proxyIp) && proxyPort != 0) {
            // 设置代理IP，设置连接超时时间 、 设置 请求读取数据的超时时间 、 设置从connect Manager获取Connection超时时间、
            HttpHost proxy = new HttpHost(proxyIp, proxyPort);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setProxy(proxy)
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .setConnectionRequestTimeout(3000)
                    .build();
            httpGet.setConfig(requestConfig);
        }
        try {
            // 发送请求
            httpResponse = httpClient.execute(httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResponse;
    }

    /**
     * 获取请求返回的状态
     *
     * @param httpResponse
     * @return
     */
    private int getResponseStatus(CloseableHttpResponse httpResponse) {
        if (httpResponse == null) {
            return 9999;
        }
        return httpResponse.getStatusLine().getStatusCode();
    }

    /**
     * 获取代理IP信息
     *
     * @return
     */
    private Map<String, Object> getProxyInfo() {
        String proxyIp = "";
        int proxyPort = 0;
        lock.lock();
        try {
            if (availableProxyIpList == null || availableProxyIpList.size() == 0) {
                init();
            }
            if (availableProxyIpList != null && availableProxyIpList.size() > 0) {
                ProxyIpInfo tempProxy = availableProxyIpList.get(currIndex % availableProxyIpList.size());
                proxyIp = tempProxy.getIp();
                String portStr = tempProxy.getPort();
                if (!StringUtils.isEmpty(portStr)) {
                    proxyPort = Integer.parseInt(portStr);
                }
                currIndex++;
            }
            log.info("当前可用代理IP集合元素个数=" + availableProxyIpList.size() + "，当前使用IP=" + proxyIp + ", 端口=" + proxyPort
                    + ", 对应索引=" + (currIndex % availableProxyIpList.size()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("proxyIp", proxyIp);
        result.put("proxyPort", proxyPort);
        return result;
    }

    private CloseableHttpClient getHttpClient(Site site) {
        if (site == null) {
            return httpClientGenerator.getClient(null);
        }
        String domain = site.getDomain();
        CloseableHttpClient httpClient = httpClients.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(domain);
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient(site);
                    httpClients.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }

    @Override
    public void setThread(int threadNum) {
        httpClientGenerator.setPoolSize(threadNum);
    }

    /**
     * 处理返回的结果
     *
     * @param request
     * @param charset
     * @param httpResponse
     * @return
     * @throws IOException
     */
    protected Page handleResponse(Request request, String charset, HttpResponse httpResponse) throws IOException {
        byte[] bytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
        String contentType = httpResponse.getEntity().getContentType() == null ? "" : httpResponse.getEntity().getContentType().getValue();
        Page page = new Page();
        page.setBytes(bytes);
        if (!request.isBinaryContent()) {
            if (charset == null) {
                charset = getHtmlCharset(contentType, bytes);
            }
            page.setCharset(charset);
            page.setRawText(new String(bytes, charset));
        }
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        page.setDownloadSuccess(true);
        if (responseHeader) {
            page.setHeaders(HttpClientUtils.convertHeaders(httpResponse.getAllHeaders()));
        }
        return page;
    }

    /**
     * 获取HTML编码
     *
     * @param contentType
     * @param contentBytes
     * @return
     * @throws IOException
     */
    private String getHtmlCharset(String contentType, byte[] contentBytes) throws IOException {
        String charset = CharsetUtils.detectCharset(contentType, contentBytes);
        if (charset == null) {
            charset = Charset.defaultCharset().name();
            log.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
        }
        return charset;
    }

    public ProxyDownloader setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }
}
