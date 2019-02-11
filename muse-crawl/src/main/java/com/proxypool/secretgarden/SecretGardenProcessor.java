package com.proxypool.secretgarden;

import com.proxypool.component.ProcessorTemplate;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * @program: muse-pay
 * @description: SG
 * @author: Vincent
 * @create: 2019-02-11 14:56
 **/
@Service("secretGardenProcessor")
public class SecretGardenProcessor extends ProcessorTemplate {

    private String url = "http://cc.gxia.icu/thread0806.php?fid=8";

    @Override
    public List parseContent(Page page) {

        // 解析列表页内容
        Selectable rootNode = page.getHtml().xpath("//table[@id='ajaxtable']/tbody/tr/td[@class='tal']/h3/a");
        if (rootNode != null) {
            List<Selectable> nodes = rootNode.nodes();

            nodes.forEach(item -> {
                String detailUrl = item.xpath("//@href").toString();
                String title = item.xpath("//text()").toString();

                System.out.println(title + " - " + detailUrl);
            });
        }

        System.out.println(page.getHtml().toString());


        return null;
    }

    @Override
    public List<String> parseUrl(Page page) {
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
