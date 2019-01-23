package com.proxypool.proxyip;

import com.alibaba.druid.util.StringUtils;
import com.muse.common.util.SpringBeanUtils;
import com.proxypool.component.ProcessorTemplate;
import com.proxypool.entry.ProxyIpInfo;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @program: muse-pay
 * @description: ip.ihuan.me
 * @author: Vincent
 * @create: 2018-10-28 11:11
 **/
@Service("proxyJiangxianliProcessor")
public class ProxyJiangxianliProcessor extends ProcessorTemplate {
    private String url = "http://ip.jiangxianli.com";

    @Override
    public List<ProxyIpInfo> parseContent(Page page) {
        List<ProxyIpInfo> proxyList = new ArrayList<>();

        Selectable selectable = page.getHtml().xpath("//table[@class='table-striped']/tbody/tr");
        if (selectable == null) return proxyList;

        Pattern pattern = Pattern.compile("(\\d+\\.?){4}");

        List<Selectable> nodes = selectable.nodes();
        nodes.forEach(item -> {
            String ip = item.xpath("//td[2]/text()").toString();
            if (!StringUtils.isEmpty(ip)) {
                Matcher matcher = pattern.matcher(ip);
                if (matcher.find()) {

                    String port = item.xpath("//td[3]/text()").toString();
                    String anonymous = item.xpath("//td[4]/text()").toString();
                    String type = item.xpath("//td[5]/text()").toString();
                    String location = item.xpath("//td[6]/text()").toString();

                    String responseSpeed = item.xpath("//td[8]/text()").toString();
                    if (!StringUtils.isEmpty(responseSpeed)) {
                        if (responseSpeed.contains("毫秒")) {
                            responseSpeed = responseSpeed.replace("毫秒", "");

                            if (StringUtils.isNumber(responseSpeed.trim())) {
                                responseSpeed = String.valueOf(Double.parseDouble(responseSpeed) / 1000D);
                            }
                        } else if (responseSpeed.contains("秒")) {
                            responseSpeed = responseSpeed.replace("秒", "");
                        }
                    }

                    proxyList.add(ProxyIpInfo.getInstance(ip, port, "HTTP", responseSpeed, location, "",
                            anonymous, "1", location, page.getUrl().toString()));
                }
            }
        });

        log.info("解析页面内容，获取代理个数=" + proxyList.size());
        return proxyList;
    }

    @Override
    public List<String> parseUrl(Page page) {
        // 获取页码连接
        List<String> urlList = page.getHtml().links().regex("http://ip\\.jiangxianli\\.com\\?page=\\d+").all();
        // 过滤，只取20页
        urlList = urlList.stream().filter(s -> {
            int index = s.lastIndexOf("=");
            String pageStr = s.substring(index + 1);
            if (!StringUtils.isEmpty(pageStr) && StringUtil.isNumeric(pageStr)) {
                int pageInt = Integer.valueOf(pageStr.trim());
                return pageInt < 20;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

        log.info("获取页码连接个数=" + urlList.size());
        return urlList;
    }

    @Override
    public ProcessorTemplate getInstance() {
        return (ProcessorTemplate) SpringBeanUtils.getBean("proxyJiangxianliProcessor");
    }

    @Override
    public String getUrl() {
        return url;
    }
}
