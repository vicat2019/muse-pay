package com.proxypool.secretgarden;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.proxypool.component.ProcessorTemplate;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: muse-pay
 * @description: SG
 * @author: Vincent
 * @create: 2019-02-11 14:56
 **/
@Service("secretGardenProcessor")
public class SecretGardenProcessor extends ProcessorTemplate {

    //private String url = "http://cc.gxia.icu/thread0806.php?fid=8";
    private String url = "http://cc.gxia.icu/htm_data/8/1902/3437076.html";

    @Override
    public List parseContent(Page page) {
        // 列表页
        Selectable rootNode = page.getHtml().xpath("//table[@id='ajaxtable']/tbody[2]/tr[@class='tr3']/td[@class='tal']/h3");
        if (rootNode != null) {
            List<String> detailUrl = Lists.newArrayList();

            List<Selectable> nodes = rootNode.nodes();
            nodes.forEach(item -> {
                String href = item.xpath("//a/@href").toString();
                href = "http://cc.gxia.icu" + href;

                detailUrl.add(href);
            });
            // 添加详情页
            page.addTargetRequests(detailUrl);
        }

        // 详情页
        String title = page.getHtml().xpath("//title/text()").toString();
        List<String> imgUrlList = page.getHtml().xpath("//input[@type='image']/@data-src").all();

        List<SecretGardenInfo> dataList = null;
        if (imgUrlList != null && imgUrlList.size() > 0) {
            SecretGardenInfo dataEntity = new SecretGardenInfo(title, imgUrlList, "1024", page.getUrl().toString());
            dataList = Lists.newArrayList(dataEntity);
        }

        return dataList;
    }

    @Override
    public List<String> parseUrl(Page page) {

        // 分页地址
        List<String> pageUrlList = page.getHtml().xpath("//div[@class='pages']/a/@href").all();
        pageUrlList = pageUrlList.stream().filter(s -> s.contains("page")).collect(Collectors.toList());

        Set<String> pageUrlSet = Sets.newHashSet(pageUrlList);
        pageUrlList.clear();
        for (String item : pageUrlSet) {
            pageUrlList.add("http://cc.gxia.icu/" + item);
        }

        // return pageUrlList;
        return null;
    }

    @Override
    public ProcessorTemplate getInstance() {
        return super.getBeanFromContainer(SecretGardenProcessor.class);
    }

    @Override
    public String getUrl() {
        return url;
    }
}
