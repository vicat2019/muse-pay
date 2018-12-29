package com.proxypool.controller;

import com.muse.common.entity.ResultData;
import com.proxypool.entry.ProxyIpInfo;
import com.proxypool.recruit.Proxy51jobProcessor;
import com.proxypool.recruit.RecruitInfoPipeline;
import com.proxypool.service.ProxyIpInfoService;
import com.proxypool.service.RecruitInfoService;
import com.proxypool.spider.*;
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
    @Scheduled(cron = "0 0 22 * * ?")
    public ResultData recruit() {
        try {
            // 处理页面的时间间隔
            proxy51jobProcessor.setInterval(2000);
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
    @Scheduled(cron = "0 0 01 * * ?")
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
    @Scheduled(cron = "0 0 02 * * ?")
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
    @Scheduled(cron = "0 0 03 * * ?")
    public void checkScheduled() {
        try {
            proxyIpInfoService.checkProxyIp();
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


}
