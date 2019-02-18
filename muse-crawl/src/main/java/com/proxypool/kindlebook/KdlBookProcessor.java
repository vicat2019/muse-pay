package com.proxypool.kindlebook;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.proxypool.component.ProcessorTemplate;
import com.proxypool.util.RedisUtil;
import com.proxypool.util.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: muse-pay
 * @description: kdlbook
 * @author: Vincent
 * @create: 2019-01-30 14:57
 **/
@Service("kdlBookProcessor")
public class KdlBookProcessor extends ProcessorTemplate {
    private Logger log = LoggerFactory.getLogger("KdlBookProcessor");

    // 根地址
    private String URL = "https://www.kdlbook.cn/";

    // 列表页地址正则表达式
    private static String LIST_PAGE_URL_REGEX = "https://www\\.kdlbook\\.cn/\\?page=\\d+";
    // 详情页地址正则表达式
    private static String DETAIL_PAGE_URL_REGEX = "https://www\\.kdlbook\\.cn/books/\\d+";

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public List parseContent(Page page) {
        List<MeBookInfo> bookList = new ArrayList<>();
        String url = page.getUrl().toString();

        if (TextUtils.isMatch(LIST_PAGE_URL_REGEX, url)) {
            Selectable rootNode = page.getHtml().xpath("//div[@class='TabDetail']/div/div[@class='TabDet_List_Box']");
            List<Selectable> children = rootNode.nodes();

            int count = 0;
            for (Selectable item : children) {
                // 评分
                String scoreStr = TextUtils.getNumFrom(item.xpath("//div[@class='sore']/text()").toString());
                // 详情页地址
                String detailUrl = item.xpath("//a/@href").toString();
                if (!StringUtils.isEmpty(detailUrl) && !detailUrl.startsWith("http://")) {
                    detailUrl = URL + (detailUrl.startsWith("/") ? detailUrl.substring(1) : detailUrl);
                }
                // 海报地址
                String postUrl = item.xpath("//a/img/@src").toString();
                // 标题
                String title = item.xpath("//div[@class='TabDet_List_Txt01']/text()").toString();
                // 作者
                String author = item.xpath("//div[@class='author']/text()").toString();

                // 生成对象
                MeBookInfo book = new MeBookInfo();
                book.setTitle(TextUtils.trim(title));
                book.setScore(Double.parseDouble(scoreStr));
                book.setPostUrl(postUrl);
                book.setAuthor(TextUtils.trim(author));
                book.setDetailUrl(detailUrl);

                // 暂时保存到Redis中
                if (!StringUtils.isEmpty(detailUrl)) {
                    redisUtil.hset(MeBookInfo.REDIS_KEY_BOOK_LIST, url, book, 60 * 60 * 3);
                    count += 1;
                }
            }
            log.info("往Redis中设置了" + count + "个数据, url=" + url);

        } else if (TextUtils.isMatch(DETAIL_PAGE_URL_REGEX, url)) {
            // 标题
            String title = page.getHtml().xpath("//div[@class='BookName']/text()").toString();
            // ISBN
            String isbn = page.getHtml().xpath("//div[@class='BookData_Info']/div[6]/text()").toString();
            // 简介
            List<String> intros = page.getHtml().xpath("//div[@class='Book_detail01']/p/text()").all();
            intros = intros.stream().filter(s -> !StringUtils.isEmpty(s)).collect(Collectors.toList());
            String intro = Joiner.on("").join(intros);
            // 下载地址
            String downloadUrl = page.getHtml().xpath("//a[@class='baiduWP']/@static-surl").toString();
            // 更新日期
            String releaseDate = page.getHtml().xpath("//div[@class='BookData']/div[@class='BookData_Info']/div[4]/text()").toString();
            if (!StringUtils.isEmpty(releaseDate)) {
                releaseDate = releaseDate.replaceAll("时间：", "");
            }

            MeBookInfo book;

            // 从Redis中取在列表页时存放的信息
            Object obj = redisUtil.hget(MeBookInfo.REDIS_KEY_BOOK_LIST, url);
            if (!(obj instanceof MeBookInfo)) {
                // 海报
                String postUrl = page.getHtml().xpath("//div[@class='BookData']/img/@src").toString();
                // 作者
                String author = page.getHtml().xpath("//div[@class='BookData']/div[@class='BookData_Info']/div[1]/text()").toString();
                if (author.contains("作者：")) {
                    author = author.replaceAll("作者：", "");
                }
                // 评分
                String score = TextUtils.getNumFrom(page.getHtml().xpath("//div[@class='BookData']/div[@class='BookData_Info']/div[5]/text()").toString());

                // 生成图书对象
                book = new MeBookInfo();
                book.setTitle(title);
                book.setPostUrl(postUrl);
                book.setScore(Double.parseDouble(score));
                book.setAuthor(author);
                book.setDetailUrl(url);
            } else {
                book = (MeBookInfo) obj;
                redisUtil.hdel(MeBookInfo.REDIS_KEY_BOOK_LIST, url);
            }

            // 补充图书对象属性值
            book.setIntro(TextUtils.trim(intro));
            book.setDownloadUrl(downloadUrl);
            book.setReleaseTime(releaseDate);
            book.setSource("四颗萌芽");
            book.setName(book.getTitle());
            book.setDetailDesc(intro);
            if (!StringUtils.isEmpty(intro) && intro.length() > 120) {
                book.setIntro(intro.substring(0, 120) + "...");
            }
            bookList.add(book);
        }

        return bookList;
    }

    @Override
    public List<String> parseUrl(Page page) {
        List<String> urlList = Lists.newArrayList();

        // 详情页集合
        List<String> detailUrlList = page.getHtml().links().regex(DETAIL_PAGE_URL_REGEX).all();
        // 分页集合
        List<String> pageUrlList = page.getHtml().links().regex(LIST_PAGE_URL_REGEX).all();
        Set<String> pageUrlSet = Sets.newHashSet(pageUrlList);

        // 初始化后，只查询前3页
        pageUrlSet = pageUrlSet.stream().filter((s) -> {
            int index = s.lastIndexOf("=");
            if (index > 0) {
                String pageNum = s.substring(index+1);
                if (!StringUtils.isEmpty(pageNum) && TextUtils.isNumber(pageNum)) {
                    int p = Integer.parseInt(pageNum);
                    return p <= 3;
                }
            }
            return false;
        }).collect(Collectors.toSet());

        urlList.addAll(detailUrlList);
        urlList.addAll(pageUrlSet);

        return urlList;
    }


    @Override
    public ProcessorTemplate getInstance() {
        return super.getBeanFromContainer(KdlBookProcessor.class);
    }

    @Override
    public String getUrl() {
        return URL;
    }

}
