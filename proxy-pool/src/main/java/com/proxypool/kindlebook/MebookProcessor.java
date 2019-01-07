package com.proxypool.kindlebook;

import com.google.common.base.Joiner;
import com.proxypool.component.ProcessorTemplate;
import com.proxypool.util.RedisUtil;
import com.proxypool.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: muse-pay
 * @description: KINDLE BOOK
 * @author: Vincent
 * @create: 2019-01-04 10:25
 **/
@Component("mebookProcessor")
public class MebookProcessor extends ProcessorTemplate {

    // 分页地址正则表达式
    private static final String PAGE_URL_REGEX = "http://mebook\\.cc/page/\\d+";
    // 详情页地址正则表达式
    private static final String DETAIL_URL_REGEX = "http://mebook\\.cc/\\d+\\.html";

    // URL
    private static final String ROOT_URL = "http://mebook.cc";

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public List parseContent(Page page) {
        String currentUrl = page.getUrl().toString();
        List<MeBookInfo> currentList = new ArrayList<>();

        // 获取页面内容
        if (TextUtils.isMatch(PAGE_URL_REGEX, currentUrl) || ROOT_URL.equals(currentUrl)) {
            Selectable selectable = page.getHtml().xpath("//ul[@class='list']/li");

            if (selectable != null && selectable.nodes().size() > 0) {
                List<Selectable> children = selectable.nodes();
                children.forEach(item -> {
                    // 分类信息
                    String category = item.xpath("//div[@class='thumbnail']/div[@class='cat']/a/text()").all().toString();
                    // 海报地址
                    String postImg = item.xpath("//div[@class='thumbnail']/div[@class='img']/a/img/@src").toString();
                    // 图书标题
                    String title = item.xpath("//div[@class='content']/h2/a/@title").toString();
                    // 名称
                    String name = TextUtils.getNameFromTitle(title);
                    // 作者
                    String author = TextUtils.getAuthorFromTitle(title);
                    // 更新时间
                    String releaseTime = item.xpath("//div[@class='content']/div[@class='info']/text()").toString();
                    // 内容简介
                    String intro = item.xpath("//div[@class='content']/p[1]/text()").toString();
                    // 详情地址
                    String detailUrl = item.xpath("//div[@class='content']/p[2]/a/@href").toString();
                    // 处理结果
                    MeBookInfo bookInfo = MeBookInfo.from(name, category, author, postImg, title, releaseTime, intro, detailUrl);
                    if (bookInfo.canMoreHandle()) {
                        redisUtil.hset(MeBookInfo.REDIS_KEY_BOOK_LIST, bookInfo.getDetailUrl(), bookInfo);
                    }
                });
            }

        } else if (TextUtils.isMatch(DETAIL_URL_REGEX, currentUrl)) {
            MeBookInfo bookInfo;
            Object cacheObj = redisUtil.hget(MeBookInfo.REDIS_KEY_BOOK_LIST, currentUrl);
            if (cacheObj == null) {
                // 标题
                String title = page.getHtml().xpath("//div[@id='primary']/h1[@class='sub']/text()").toString();
                // 名称
                String name = TextUtils.getNameFromTitle(title);
                // 作者
                String author = TextUtils.getAuthorFromTitle(title);
                // 分类
                String category = "";
                List<String> categoryList = page.getHtml().xpath("//div[@class='postinfo']/div[@class='left']/a/text()").all();
                if (categoryList != null) {
                    category = Joiner.on(", ").join(categoryList);
                }
                // 发布时间
                String releaseTime = "";
                List<String> releaseTimes = page.getHtml().xpath("//div[@class='postinfo']/div[@class='left']/text()").all();
                if (releaseTimes != null) {
                    releaseTime = Joiner.on(", ").join(releaseTimes);
                    if (!StringUtils.isEmpty(releaseTime)) {
                        releaseTime = TextUtils.getMatch("(\\d{4}年\\d{1,2}月\\d{1,2}日)", releaseTime);
                    }
                }
                // 海报
                String postUrl = page.getHtml().xpath("//div[@id='content']/p[1]/img/@src").toString();
                bookInfo = MeBookInfo.from(name, category, author, postUrl, title, releaseTime, "", currentUrl);
            } else {
                bookInfo = (MeBookInfo) cacheObj;
            }

            // 详细内容
            String detailDesc = "";
            List<String> detailDescList = page.getHtml().xpath("//div[@id='content']/p/span/text()").all();
            if (detailDescList != null) {
                detailDesc = Joiner.on("\n").join(detailDescList);
            }
            if (StringUtils.isEmpty(detailDesc)) {
                detailDesc = page.getHtml().xpath("//div[@class='intro']/p/span/text()").toString();
            }
            // 下载地址
            String downloadUrl = page.getHtml().xpath("//p[@class='downlink']/strong/a/@href").toString();
            // 补充信息
            bookInfo.supplement(detailDesc, downloadUrl);
            currentList.add(bookInfo);
        }

        return currentList;
    }

    @Override
    public List<String> parseUrl(Page page) {
        List<String> urlList = new ArrayList<>();

        // 分页地址
        int pageUrlCount = 0;
        List<String> pageUrlList = page.getHtml().links().regex(PAGE_URL_REGEX).all();
        if (pageUrlList != null) {
            Set<String> pageUrlSet = new HashSet<>(pageUrlList);
            urlList.addAll(pageUrlSet);
            pageUrlCount = pageUrlSet.size();
        }

        // 详情地址
        int detailUrlCount = 0;
        List<String> detailUrlList = page.getHtml().links().regex(DETAIL_URL_REGEX).all();
        if (detailUrlList != null) {
            Set<String> detailUrlSet = new HashSet<>(detailUrlList);
            urlList.addAll(detailUrlSet);
            detailUrlCount = detailUrlSet.size();
        }

        log.info("总地址数=" + urlList.size() + ", 详情页地址数=" + detailUrlCount + ", 分页地址数=" + pageUrlCount);
        return urlList;
    }

    @Override
    public ProcessorTemplate getInstance() {
        return super.getBeanFromContainer(this.getClass());
    }

    @Override
    public String getUrl() {
        //return "http://mebook.cc/26403.html";
        return ROOT_URL;
    }


}
