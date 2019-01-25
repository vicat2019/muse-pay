package com.proxypool.picture;

import com.google.common.collect.Lists;
import com.proxypool.component.ProcessorTemplate;
import com.proxypool.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: muse-pay
 * @description: 解析wallhaven
 * @author: Vincent
 * @create: 2019-01-23 17:20
 **/
@Service("wallhavenProcessor")
public class WallhavenProcessor extends ProcessorTemplate {
    private static final String URL = "https://alpha.wallhaven.cc/toplist?page=1";

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public List parseContent(Page page) {
        Selectable rootSelectable = page.getHtml().xpath("//div[@id='thumbs']/section/ul/li/figure");
        List<Selectable> nodeList = rootSelectable.nodes();

        List<PictureInfo> pictureInfoList = new ArrayList<>();

        for (Selectable item : nodeList) {
            String imgUrl = item.xpath("//a/@href").toString();
            String widthHeightStr = item.xpath("//div[@class='thumb-info']/span/text()").toString();

            // 转换成Picture对象
            PictureInfo picture = new PictureInfo(PictureInfo.toBigUrl(imgUrl), PictureInfo.toThumbUrl(imgUrl));
            picture.setWidthAndHeight(widthHeightStr);
            pictureInfoList.add(picture);
        }

        log.info("解析Picture列表，地址[" + page.getUrl().toString() + "]，从中解析出地址=" + pictureInfoList.size());
        return pictureInfoList;
    }

    @Override
    public List<String> parseUrl(Page page) {
        // 已经处理过得URL
        redisUtil.sSet("PICTURE_CACHE_PAGE_URL", page.getUrl().toString().trim());

        // 分页URL
        List<String> pageUrlList = page.getHtml().links().regex("https://alpha\\.wallhaven\\.cc/toplist\\?page=\\d+").all();
        if (pageUrlList != null) {
            pageUrlList.forEach(System.out::println);
        }
        Set<String> pageUrlSet = new HashSet<>(pageUrlList);

        // 根据缓存的已处理过的URL，去掉已经处理过得URL
        Set intersectSet = redisUtil.sIntersect("PICTURE_CACHE_PAGE_URL", pageUrlSet);
        if (intersectSet != null && intersectSet.size() > 0) {
            // 去掉交集
            pageUrlSet.removeAll(intersectSet);
        }
        log.info("解析Picture分页，地址[" + page.getUrl().toString() + "]，从中解析出地址=" + pageUrlList.size()
                + ", 去重后地址=" + pageUrlSet.size());

        return Lists.newArrayList(pageUrlSet);
    }

    @Override
    public ProcessorTemplate getInstance() {
        return super.getBeanFromContainer(this.getClass());
    }

    @Override
    public String getUrl() {
        return URL;
    }
}
