package com.proxypool.recruit;

import com.muse.common.util.SpringBeanUtils;
import com.proxypool.entry.RecruitInfo;
import com.proxypool.component.ProcessorTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @program: muse-pay
 * @description: 51job抓取
 * @author: Vincent
 * @create: 2018-11-04 11:56
 **/
@Service("proxy51jobProcessor")
public class Job51Processor extends ProcessorTemplate {

    // 第一个列表页地址
    private String url = "https://search.51job.com/list/040000,000000,0000,00,9,99,java,2,1.html?lang=c&stype=&" +
            "postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0" +
            "&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

    @Override
    public List<RecruitInfo> parseContent(Page page) {
        List<RecruitInfo> resultList = new ArrayList<>();

        String currentUrl = page.getUrl().toString();

        // 是详情页，解析内容
        if (currentUrl.contains("https://jobs.51job.com")) {
            RecruitInfo recruitInfo = new RecruitInfo();

            // 头部信息
            Selectable selectable = page.getHtml().xpath("//div[@class='tHjob']/div/div/");
            if (selectable != null) {
                // 职位名称
                String postName = selectable.xpath("//h1/@title").toString();
                String salary = selectable.xpath("//strong/text()").toString();
                String companyName = selectable.xpath("//p[@class='cname']/a/text()").toString();
                recruitInfo.setPostName(postName);
                recruitInfo.setSalary(salary);
                recruitInfo.setCompanyName(companyName);

                String detailMsg = selectable.xpath("//p[@class='msg']").toString();
                String area = "";
                String experience = "";
                String education = "";
                String number = "";
                String releaseDate = "";
                if (!StringUtils.isEmpty(detailMsg)) {
                    List<String> infoList = splitDetailInfo(detailMsg);
                    if (infoList.size() > 0) {
                        int index = 0;
                        infoList.size();
                        area = infoList.get(index);
                        experience = infoList.size() > ++index ? infoList.get(index) : "";
                        education = infoList.size() > ++index ? infoList.get(index) : "";
                        number = infoList.size() > ++index ? infoList.get(index) : "";
                        releaseDate = infoList.size() > ++index ? infoList.get(index) : "";
                    }
                }

                recruitInfo.setArea(area);
                recruitInfo.setExperience(experience);
                recruitInfo.setEducation(education);
                recruitInfo.setNumber(number);
                recruitInfo.setReleaseDate(releaseDate);

                List<String> welfareList = selectable.xpath("//div[@class='jtag']/div/span/text()").all();
                String welfare = "";
                if (welfareList.size() > 0) {
                    welfare = StringUtils.join(welfareList, ",");
                }

                recruitInfo.setWelfare(welfare);
            }

            Selectable detailSelectable = page.getHtml().xpath("//div[@class='tCompany_main']");
            if (detailSelectable != null) {
                // 职位信息
                List<String> jobMsgList = detailSelectable.xpath("//div[@class='job_msg']/p").all();
                String jobMsg = StringUtils.join(jobMsgList, ",");
                // contact
                String contact = detailSelectable.xpath("//div[2]/div/p/text()").toString();
                // 公司描述
                String companyDesc = detailSelectable.xpath("//div[3]/div/text()").toString();

                recruitInfo.setJobInformation(jobMsg);
                recruitInfo.setContact(contact);
                recruitInfo.setCompanyDesc(companyDesc);
            }

            List<String> companyInfoList = page.getHtml().xpath("//div[@class='com_tag']/p/@title").all();
            int tag = 0;
            String companyType = companyInfoList.size() > tag ? companyInfoList.get(tag) : "";
            String companySize = companyInfoList.size() > ++tag ? companyInfoList.get(tag) : "";
            String companyIndustry = companyInfoList.size() > ++tag ? companyInfoList.get(tag) : "";

            recruitInfo.setCompanyType(companyType);
            recruitInfo.setCompanySize(companySize);
            recruitInfo.setCompanyIndustry(companyIndustry);
            recruitInfo.setSource("https://www.51job.com/");
            recruitInfo.setDetailUrl(currentUrl);

            // 更正
            if (StringUtils.isEmpty(recruitInfo.getReleaseDate()) && (StringUtils.isEmpty(recruitInfo.getNumber())
                    && recruitInfo.getNumber().contains("发布"))) {
                recruitInfo.setReleaseDate(recruitInfo.getNumber());
                recruitInfo.setNumber("");
            }

            resultList.add(recruitInfo);
            log.info("解析详情页，当前连接[" + currentUrl + "], 获取职位个数=" + resultList.size());
        }

        return resultList;
    }

    @Override
    public List<String> parseUrl(Page page) {
        List<String> urlList = new ArrayList<>();

        // 如果是列表页，解析一下地址
        String currentUrl = page.getUrl().toString();
        // 包含一下部分，则为详情页，不需要获取分页地址和列表项地址
        if (currentUrl.contains("https://jobs.51job.com")) {
            return urlList;
        }

        // 解析分页
        List<String> pageUrlList = page.getHtml().xpath("//div[@class='dw_page']/div/div/div/ul/li/a/@href").all();
        // 解析列表页中每一项的地址
        List<String> detailList = page.getHtml().xpath("//div[@class='dw_table']/div[@class='el']/p/span/a/@href").all();
        urlList.addAll(pageUrlList);
        urlList.addAll(detailList);
        // 简单去重
        urlList = new ArrayList<>(new HashSet<>(urlList));

        log.info("解析分页链接，分页连接数=" + pageUrlList.size() + "，详情连接数=" + detailList.size() + ", 一共连接数=" + urlList.size());
        return urlList;
    }

    @Override
    public ProcessorTemplate getInstance() {
        return (ProcessorTemplate) SpringBeanUtils.getBean("proxy51jobProcessor");
    }

    @Override
    public String getUrl() {
        return url;
    }

    /**
     * 解析返回的详细信息内容
     *
     * @param content 字符串内容
     * @return List
     */
    private List<String> splitDetailInfo(String content) {
        String target = content.trim().replaceAll("&nbsp;", "");
        target = target.trim().replaceAll("<p.*?>", "");
        target = target.trim().replaceAll("</p>", "");
        target = target.trim().replaceAll("</?span>", "");

        List<String> resultList = Arrays.asList(target.split("\\|"));
        return resultList;
    }

}
