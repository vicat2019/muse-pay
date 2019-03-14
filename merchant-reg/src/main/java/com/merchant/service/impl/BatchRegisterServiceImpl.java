package com.merchant.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.merchant.entity.*;
import com.merchant.service.*;
import com.merchant.util.CommonUtils;
import com.merchant.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: merchant-register
 * @description: 批量录入
 * @author: Vincent
 * @create: 2019-02-14 09:00
 **/
@Component("batchRegister")
public class BatchRegisterServiceImpl {
    private static Logger log = LoggerFactory.getLogger("BatchRegisterServiceImpl");

    @Autowired
    private RsMerchantInfoService rsMerchantInfoService;

    @Autowired
    private MerchantBaseService merchantBaseService;

    @Autowired
    private BankCardInfoService bankCardInfoService;

    @Autowired
    private ChannelInfoService channelInfoService;

    @Autowired
    private RedisUtil redisUtil;

    // 线程池
    private static ExecutorService executor = Executors.newCachedThreadPool();


    /**
     * 执行批量进件
     *
     * @param bankCardExcel   二类卡Excel文件
     * @param bankCardCertPic 二类卡对应的身份证照片目录
     * @param mchFilePath     商户信息文件路径
     * @param pathType        商户信息文件类型：Excel、目录
     * @param doorPicFolder   门头照目录
     * @return
     * @throws Exception
     */
    public ResultData execute(String bankCardExcel, String bankCardCertPic, String mchFilePath,
                              FilePathTypeEnum pathType, String doorPicFolder) throws Exception {

        // 获取已经进件的商铺
        List<String> alreadyRegMchNameList;
        try {
            alreadyRegMchNameList = rsMerchantInfoService.getAllName();
        } catch (Exception e) {
            log.error("execute() 查询已进件的商户名称异常=" + e.getMessage());
            alreadyRegMchNameList = Lists.newArrayList();
        }


        // 获取店铺信息(名称)
        List<MerchantBaseInfo> merchantBaseList = merchantBaseService.getMerchantBase(mchFilePath, pathType, doorPicFolder);
        if (CommonUtils.collectionIsEmpty(merchantBaseList)) {
            log.error("execute() 要进件的商户信息列表为空。");
            return ResultData.getErrResult("商户信息列表为空");
        }


        // 获取二类卡信息
        ResultData initBankCardResult = bankCardInfoService.initBankCard(bankCardExcel, bankCardCertPic);
        List<BankCardInfo> bankCardList;
        if (initBankCardResult.whetherOk() && initBankCardResult.resultIsNotEmpty()) {
            bankCardList = (List<BankCardInfo>) initBankCardResult.getData();
        } else {
            return initBankCardResult;
        }
        if (CommonUtils.collectionIsEmpty(bankCardList)) {
            log.error("execute() 获取要进件的二类卡信息为空");
            return ResultData.getErrResult("二类卡信息列表为空, 无法进件");
        }


        // 按照通道号分组
        Map<String, List<BankCardInfo>> bankCardGroup = Maps.newHashMap();
        for (BankCardInfo bankCard : bankCardList) {
            List<BankCardInfo> children = bankCardGroup.computeIfAbsent(bankCard.getMchId(), k -> Lists.newArrayList());
            children.add(bankCard);
        }

        // 将商户信息列表分组
        int groupSize = merchantBaseList.size() / bankCardGroup.size();
        List<List<MerchantBaseInfo>> mchBaseGroup = Lists.partition(merchantBaseList, groupSize);
        if (mchBaseGroup.size() < bankCardGroup.size()) {
            log.error("execute() 商户基础信息分组小于二类卡通道分组, 商户信息分组=" + mchBaseGroup.size()
                    + ", 二类卡通道分组=" + bankCardGroup.size());
            return ResultData.getErrResult("商户基础信息分组小于二类卡通道分组");
        }


        // 获取所有通道信息
        List<ChannelInfo> activeChannelList = channelInfoService.getActiveChannel();
        if (CommonUtils.collectionIsEmpty(activeChannelList)) {
            throw new Exception("获取激活的通道为空");
        }


        // 根据分组数启动相应的线程
        executeRegisterTask(alreadyRegMchNameList, bankCardGroup, mchBaseGroup, activeChannelList);
        return ResultData.getSuccessResult("execute() 启动批量进件任务，任务数=" + bankCardGroup.size());
    }


    public ResultData execute() throws Exception {
        // 获取已经进件的商铺
        List<String> alreadyRegMchNameList;
        try {
            alreadyRegMchNameList = rsMerchantInfoService.getAllName();
        } catch (Exception e) {
            log.error("execute() 查询已进件的商户名称异常=" + e.getMessage());
            alreadyRegMchNameList = Lists.newArrayList();
        }


        // 获取店铺信息(名称)
        List<MerchantBaseInfo> merchantBaseList = merchantBaseService.getUnusedMerchantBase();
        if (CommonUtils.collectionIsEmpty(merchantBaseList)) {
            log.error("execute() 要进件的商户信息列表为空。");
            return ResultData.getErrResult("商户信息列表为空");
        }


        // 获取二类卡信息
        List<BankCardInfo> bankCardList = bankCardInfoService.getAvailableBankCard();
        if (CommonUtils.collectionIsEmpty(bankCardList)) {
            log.error("execute() 获取要进件的二类卡信息为空");
            return ResultData.getErrResult("二类卡信息列表为空, 无法进件");
        }


        // 按照通道号分组
        Map<String, List<BankCardInfo>> bankCardGroup = Maps.newHashMap();
        for (BankCardInfo bankCard : bankCardList) {
            List<BankCardInfo> children = bankCardGroup.computeIfAbsent(bankCard.getMchId(), k -> Lists.newArrayList());
            children.add(bankCard);
        }


        // 将商户信息列表分组
        int groupSize = merchantBaseList.size() / bankCardGroup.size();
        List<List<MerchantBaseInfo>> mchBaseGroup = Lists.partition(merchantBaseList, groupSize);
        if (mchBaseGroup.size() < bankCardGroup.size()) {
            log.error("execute() 商户基础信息分组小于二类卡通道分组, 商户信息分组=" + mchBaseGroup.size()
                    + ", 二类卡通道分组=" + bankCardGroup.size());
            return ResultData.getErrResult("商户基础信息分组小于二类卡通道分组");
        }

        // 获取所有通道信息
        List<ChannelInfo> activeChannelList = channelInfoService.getActiveChannel();
        if (CommonUtils.collectionIsEmpty(activeChannelList)) {
            throw new Exception("获取激活的通道为空");
        }

        // 启动任务
        executeRegisterTask(alreadyRegMchNameList, bankCardGroup, mchBaseGroup, activeChannelList);
        // 返回结果
        return ResultData.getSuccessResult("execute() 启动批量进件任务，任务数=" + bankCardGroup.size());
    }

    /**
     * 启动批量进件任务
     *
     * @param alreadyRegMchNameList 已经进件的商户名称列表
     * @param bankCardGroup         二类卡分组
     * @param mchBaseGroup          商户分组
     * @param channelList           激活的通道列表
     */
    private void executeRegisterTask(List<String> alreadyRegMchNameList, Map<String, List<BankCardInfo>> bankCardGroup,
                                     List<List<MerchantBaseInfo>> mchBaseGroup, List<ChannelInfo> channelList) {
        // 根据分组数启动相应的线程
        int mchBaseGroupIndex = 0;
        for (String key : bankCardGroup.keySet()) {
            // 商户分组
            List<MerchantBaseInfo> subMchBaseList = mchBaseGroup.get(mchBaseGroupIndex);
            mchBaseGroupIndex += 1;

            // 二类卡分组
            List<BankCardInfo> subBankCardList = bankCardGroup.get(key);

            // 执行批量进件任务
            RegisterTask registerTask = new RegisterTask(key, alreadyRegMchNameList, subBankCardList, subMchBaseList, channelList);
            registerTask.setService(rsMerchantInfoService, bankCardInfoService, merchantBaseService, redisUtil);
            executor.execute(registerTask);
        }
    }


}
