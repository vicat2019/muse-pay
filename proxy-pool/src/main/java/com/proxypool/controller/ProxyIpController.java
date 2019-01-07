package com.proxypool.controller;

import com.muse.common.entity.ResultData;
import com.proxypool.component.ProxyDownloader;
import com.proxypool.entry.ProxyIpInfo;
import com.proxypool.kindlebook.MeBookPipeline;
import com.proxypool.kindlebook.MebookProcessor;
import com.proxypool.proxyip.*;
import com.proxypool.recruit.Proxy51jobProcessor;
import com.proxypool.recruit.RecruitInfoPipeline;
import com.proxypool.service.ProxyIpInfoService;
import com.proxypool.service.RecruitInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: muse-pay
 * @description: 代理信息控制类
 * @author: Vincent
 * @create: 2018-10-15 12:06
 **/
@RequestMapping("/proxy")
@RestController
public class ProxyIpController {

    @Autowired
    private ProxyPipeline proxyPipeline;

    @Autowired
    private RecruitInfoPipeline recruitInfoPipeline;

    @Autowired
    private ProxyIpInfoService proxyIpInfoService;

    @Autowired
    private ProxyProcessor proxyProcessor;

    @Autowired
    private IP89Processor ip89Processor;

    @Autowired
    private IP66Processor ip66Processor;

    @Autowired
    private Proxy31fProcessor proxy31fProcessor;

    @Autowired
    private ProxyJiangxianliProcessor proxyJiangxianliProcessor;

    @Autowired
    private ProxyKuaidailiProcessor proxyKuaidailiProcessor;

    @Autowired
    private Proxy51jobProcessor proxy51jobProcessor;

    @Autowired
    private RecruitInfoService recruitInfoService;

    @Autowired
    private ProxyDownloader proxyDownloader;

    @Autowired
    private MebookProcessor mebookProcessor;

    @Autowired
    private MeBookPipeline meBookPipeline;


    @RequestMapping("/delRepeat")
    public ResultData delRepeatProxy() {
        ResultData result;
        try {
            result = proxyIpInfoService.delRepeatProxy();
        } catch (Exception e) {
            e.printStackTrace();
            result = ResultData.getErrResult(e.getMessage());
        }

        return result;
    }

    /**
     * 抓取接口
     *
     * @return ResultData
     */
    @RequestMapping("/recruit")
    public ResultData test() {
        return recruit();
    }

    /**
     * 定时抓取数据
     *
     * @return ResultData
     */
    @Scheduled(cron = "${RECRUIT_CRON}")
    public ResultData recruit() {
        try {
            // 处理页面的时间间隔
            proxy51jobProcessor.setInterval(1000).setThreadCount(3);
            proxy51jobProcessor.execute(recruitInfoPipeline, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 定时清理重复的记录
     *
     * @return ResultData
     */
    @RequestMapping("/delRepeatRecruit")
    public ResultData delRepeat() {
        ResultData result;
        try {
            result = recruitInfoService.delRepeatRecruit();
        } catch (Exception e) {
            e.printStackTrace();
            result = ResultData.getErrResult(e.getMessage());
        }
        return result;
    }

    /**
     * 定时清理重复的记录
     *
     * @return ResultData
     */
    @Scheduled(cron = "${DEL_REPEAT_RECRUIT_CRON}")
    public ResultData delRepeatRecruit() {
        ResultData result;
        try {
            result = recruitInfoService.delRepeatRecruit();
        } catch (Exception e) {
            e.printStackTrace();
            result = ResultData.getErrResult(e.getMessage());
        }
        return result;
    }

    @RequestMapping("/query")
    public ResultData query(int id) {

        try {
            ResultData result = proxyIpInfoService.get(id);
            if (result.isOk() && result.resultIsNotEmpty()) {
                return result;
            } else {
                return ResultData.getErrResult("ID为" + id + "的代理信息不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultData.getErrResult("查询异常");
    }

    @RequestMapping("/start")
    public ResultData parse() {
        return execute();
    }

    /**
     * 获取代理IP
     *
     * @return ResultData
     */
    @Scheduled(cron = "${PROXY_IP_EXECUTE_CRON}")
    private ResultData execute() {
        try {
            proxyProcessor.execute();
            ip89Processor.execute();

            ip66Processor.execute(proxyPipeline, false);
            proxy31fProcessor.execute(proxyPipeline, false);
            proxyJiangxianliProcessor.execute(proxyPipeline, false);
            proxyKuaidailiProcessor.execute(proxyPipeline, false);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getErrResult("抓取数据异常");
        }
        return ResultData.getSuccessResult();
    }

    @RequestMapping("/check")
    public ResultData check() {

        try {
            proxyIpInfoService.checkProxyIp();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getErrResult("校验异常");
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 验证代理是否可用
     */
    @Scheduled(cron = "${CHECK_PROXY_IP_CRON}")
    public void checkScheduled() {
        try {
            // proxyIpInfoService.checkProxyIp();
            proxyIpInfoService.checkAvailability(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/update")
    public ResultData update(int id, String status) {

        ProxyIpInfo proxyIpInfo = new ProxyIpInfo();
        proxyIpInfo.setId(id);
        proxyIpInfo.setStatus(status);

        try {
            proxyIpInfoService.update(proxyIpInfo);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getErrResult("校验异常");
        }

        return ResultData.getSuccessResult();
    }

    @Scheduled(cron = "${CRAWL_BOOK_CRON}")
    @RequestMapping("/mebook")
    public ResultData mebookProcessor() {
        try {
            mebookProcessor.setInterval(200).setThreadCount(10).execute(meBookPipeline, proxyDownloader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultData.getSuccessResult();
    }



}
