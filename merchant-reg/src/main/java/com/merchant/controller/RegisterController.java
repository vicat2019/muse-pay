package com.merchant.controller;

import com.merchant.entity.ChannelInfo;
import com.merchant.entity.ResultData;
import com.merchant.service.BankCardInfoService;
import com.merchant.service.ChannelInfoService;
import com.merchant.service.MerchantBaseService;
import com.merchant.service.RsMerchantInfoService;
import com.merchant.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: muse-pay
 * @description: 进件控制类
 * @author: Vincent
 * @create: 2019-02-12 11:22
 **/
@RequestMapping("/merchant")
@RestController
public class RegisterController {
    private Logger log = LoggerFactory.getLogger("RegisterController");

    @Autowired
    private MerchantBaseService merchantBaseService;

    @Autowired
    private RsMerchantInfoService rsMerchantInfoService;

    @Autowired
    private ChannelInfoService channelInfoService;

    @Autowired
    private BankCardInfoService bankCardInfoService;


    /**
     * 商户进件
     *
     * @return ResultData
     */
    @RequestMapping("/register")
    public ResultData register() {
        try {
            return merchantBaseService.add(null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("商户进件异常=" + e.getMessage());
            return ResultData.getErrResult("添加异常=" + e.getMessage());
        }
    }


    /**
     * 修改商户费率
     *
     * @param rate                    费率值
     * @param mchid                   代理商号
     * @param submchid                商户编号
     * @param channelRatePropertyName 费率渠道属性名称
     * @return ResultData
     */
    @RequestMapping("/modRate")
    public ResultData modifyRate(double rate, String mchid, String submchid, String channelRatePropertyName) {
        try {
            ChannelInfo channel = channelInfoService.getChannelByMchId(mchid);
            return rsMerchantInfoService.modifyRate(rate, mchid, submchid, channelRatePropertyName, channel.getPrivateKey());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改商户费率异常=" + e.getMessage());
            return ResultData.getErrResult("修改费率异常，" + e.getMessage());
        }
    }

    /**
     * 查询列表
     *
     * @param p 页码
     * @param s 每页记录数
     * @return ResultData
     */
    @RequestMapping("/list")
    public ResultData queryUserInfo(@RequestParam(required = false, defaultValue = "1") int p,
                                    @RequestParam(required = false, defaultValue = "30") int s) {
        try {
            return rsMerchantInfoService.queryRuiShengUser(null, p, s);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getErrResult("查询进件商户列表异常，" + e.getMessage());
        }
    }

    @RequestMapping("/upload")
    @ResponseBody
    public ResultData upload(MultipartFile file, String fileType, HttpServletRequest request) {
        try {
            // return bankCardInfoService.uploadDataFile(file, request, fileType);

            return FileHelper.doUploadFile(file, "f:\\test");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传数据文件异常=" + e.getMessage());
        }
        return ResultData.getErrResult();
    }


}
