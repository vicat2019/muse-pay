package com.proxypool.registration;

import com.proxypool.component.ProcessorTemplate;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;

import java.util.List;

/**
 * @program: muse-pay
 * @description: 160
 * @author: Vincent
 * @create: 2019-02-23 09:35
 **/
@Service("doctor160JSONProcess")
public class Doctor160JSONProcess extends ProcessorTemplate {

    private String url = "https://www.91160.com/dep/getschmast/uid-118/depid-2354/date-2019-2-23/p-0.html";


    @Override
    public List parseContent(Page page) {

        System.out.println(page.getHtml().toString());

        return null;
    }


    @Override
    public List<String> parseUrl(Page page) {


        return null;
    }

    @Override
    public ProcessorTemplate getInstance() {
        return super.getBeanFromContainer(Doctor160JSONProcess.class);
    }

    @Override
    public String getUrl() {
        return url;
    }


}
