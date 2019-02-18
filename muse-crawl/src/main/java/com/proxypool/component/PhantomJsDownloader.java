package com.proxypool.component;

import com.proxypool.util.PhantomJSDriverHelper;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.downloader.HttpUriRequestConverter;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.CharsetUtils;
import us.codecraft.webmagic.utils.HttpClientUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: muse-pay
 * @description: 默认Downloader
 * @author: Vincent
 * @create: 2019-02-11 17:23
 **/
public class PhantomJsDownloader extends AbstractDownloader {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();

    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

    private HttpUriRequestConverter httpUriRequestConverter = new HttpUriRequestConverter();

    private ProxyProvider proxyProvider;

    private boolean responseHeader = true;

    public void setHttpUriRequestConverter(HttpUriRequestConverter httpUriRequestConverter) {
        this.httpUriRequestConverter = httpUriRequestConverter;
    }

    public void setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
    }

    @Override
    public Page download(Request request, Task task) {
        if (task == null || task.getSite() == null) {
            throw new NullPointerException("Task任务不能为空");
        }

        PhantomJSDriver driver = PhantomJSDriverHelper.getDriver();

        Page page = Page.fail();
        try {
            String url = request.getUrl();
            logger.info("下载地址=" + url);
            driver.get(url);
            String htmlContent = driver.getPageSource();
            page = handleResponse(request, "utf-8", htmlContent);

            onSuccess(request);
            logger.info("downloading page success {}", request.getUrl());
            return page;
        } catch (IOException e) {
            logger.warn("download page {} error", request.getUrl(), e);
            onError(request);
            return page;
        }
    }

    @Override
    public void setThread(int thread) {
        httpClientGenerator.setPoolSize(thread);
    }

    protected Page handleResponse(Request request, String charset, String htmlContent) throws IOException {
        InputStream in = new ByteArrayInputStream(htmlContent.getBytes());
        byte[] bytes = IOUtils.toByteArray(in);

        Page page = new Page();
        page.setBytes(bytes);
        if (!request.isBinaryContent()) {
            page.setCharset(charset);
            page.setRawText(new String(bytes, charset));
        }
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(200);
        page.setDownloadSuccess(true);
        if (responseHeader) {
            page.setHeaders(HttpClientUtils.convertHeaders(new Header[0]));
        }
        return page;
    }

    private String getHtmlCharset(String contentType, byte[] contentBytes) throws IOException {
        String charset = CharsetUtils.detectCharset(contentType, contentBytes);
        if (charset == null) {
            charset = Charset.defaultCharset().name();
            logger.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
        }
        return charset;
    }
}
