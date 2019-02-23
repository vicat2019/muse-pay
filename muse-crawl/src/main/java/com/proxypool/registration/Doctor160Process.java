package com.proxypool.registration;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.proxypool.component.ProcessorTemplate;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: muse-pay
 * @description: 160
 * @author: Vincent
 * @create: 2019-02-23 09:35
 **/
@Service("doctor160Process")
public class Doctor160Process extends ProcessorTemplate {

    //private String url = "https://www.91160.com/dep/show/depid-3826.html";
    private String url = "https://www.91160.com/dep/show/depid-2348.html";


    @Override
    public List parseContent(Page page) {

        List<RegistrationInfo> registrationList = Lists.newArrayList();

        Selectable root = page.getHtml().xpath("//div[@id='gre_doc_list']/div");
        if (root != null) {
            List<Selectable> children = root.nodes();
            for (Selectable item : children) {
                RegistrationInfo registration = new RegistrationInfo();
                // 姓名
                String name = item.xpath("//div/div/div[2]/h2/@title").toString();
                registration.setName(name);

                // 职位
                String position = item.xpath("//div/div/div[2]/span/text()").toString();
                registration.setPosition(position);

                // 专长
                String speciality = item.xpath("//div/a[@class='doc_exp']/@title").toString();
                registration.setSpeciality(speciality);

                // 预约量和好评
                List<String> subInfo2 = item.xpath("//div/div[@class='hosyynum']/p/em/text()").all();
                if (subInfo2 != null && subInfo2.size() == 2) {
                    registration.setSubscribeCount(subInfo2.get(0));
                    registration.setGoodReputation(subInfo2.get(1));
                }

                registrationList.add(registration);
            }
        }
        System.out.println("registrationList.size = " + registrationList.size());

        // 获取预约信息
        // 上午
        List<List<SubscribeInfo>> amSubscribeList = getSubscribe(page, "1");
        System.out.println("amSubscribeList.size = " + amSubscribeList.size());
        // 下午
        List<List<SubscribeInfo>> pmSubscribeList = getSubscribe(page, "2");
        System.out.println("pmSubscribeList.size = " + pmSubscribeList.size());

        // 整合结果
        if (registrationList.size() == amSubscribeList.size()) {
            for (int i = 0; i < registrationList.size(); i++) {
                registrationList.get(i).setAmSubscribeList(amSubscribeList.get(i));
            }
        }
        if (registrationList.size() == pmSubscribeList.size()) {
            for (int i = 0; i < registrationList.size(); i++) {
                registrationList.get(i).setPmSubscribeList(pmSubscribeList.get(i));
            }
        }

        // 打印
        registrationList.forEach(item -> System.out.println(JSONObject.toJSONString(item)));

        return null;
    }

    /**
     * 获取预约详情信息
     *
     * @param page     PAGE对象
     * @param timeType 时间类型：1上午，2下午
     * @return List
     */
    private List<List<SubscribeInfo>> getSubscribe(Page page, String timeType) {
        List<List<SubscribeInfo>> subscribeList = Lists.newArrayList();

        Selectable appointment = page.getHtml().xpath("//div[@id='gre_doc_times']/ul");
        if (appointment != null) {
            // UL List
            List<Selectable> ulList = appointment.nodes();
            for (Selectable ul : ulList) {
                Selectable amLi = ul.xpath("//li[" + timeType + "]");
                String liContent = amLi.toString().toLowerCase();

                //
                liContent = liContent.replaceAll("<a.+?/a>", "@A@");
                liContent = liContent.replaceAll("\n", "");
                liContent = liContent.replaceAll("<div class=\"yuyuebor\">.*?</div>\\s*</div>", "@DIV@");
                liContent = liContent.replaceAll("\\s+", "");

                List<String> tagList = Lists.newArrayList(Splitter.on("@").split(liContent));
                tagList = tagList.stream().filter((Predicate<String>) s -> s.equals("A") || s.equals("DIV"))
                        .collect(Collectors.toList());

                List<SubscribeInfo> subSubscribeList = Lists.newArrayList();

                // 遍历
                int linkCount = 0;
                int divCount = 0;
                for (String tag : tagList) {
                    // A标签
                    if (tag.toLowerCase().equals("a")) {
                        linkCount += 1;
                        String title = amLi.xpath("//a[" + linkCount + "]/text()").toString();
                        subSubscribeList.add(SubscribeInfo.getInstance(title));

                        // DIV标签
                    } else if (tag.toLowerCase().equals("div")) {
                        divCount += 1;
                        // 标题
                        String title = amLi.xpath("//div[" + divCount + "]/a/text()").toString();

                        // 预约具体信息
                        String subContent = ul.xpath("//li[" + timeType + "]/div[" + divCount + "]/div").toString();
                        String content = subContent.replaceAll("\n", "");
                        content = content.replaceAll("<div.*?>", "");
                        content = content.replaceAll("</div>", "");
                        content = content.replaceAll("</*i>", "");
                        content = content.replaceAll("\\s", "");
                        // 拆分值
                        List<String> subInfoList = Lists.newArrayList(Splitter.on("<br>").split(content));

                        // 生成预约信息对象
                        SubscribeInfo subscribeInfo = new SubscribeInfo(subInfoList.get(0), subInfoList.get(1),
                                subInfoList.get(2), subInfoList.get(3));
                        subscribeInfo.setTitle(title);
                        // 结果
                        subSubscribeList.add(subscribeInfo);
                    }
                }
                subscribeList.add(subSubscribeList);
            }
        }
        return subscribeList;
    }


    @Override
    public List<String> parseUrl(Page page) {


        return null;
    }

    @Override
    public ProcessorTemplate getInstance() {
        return super.getBeanFromContainer(Doctor160Process.class);
    }

    @Override
    public String getUrl() {
        return url;
    }


}
