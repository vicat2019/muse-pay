package com.merchant.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.merchant.entity.*;
import com.merchant.util.CommonUtils;
import com.merchant.util.FileHelper;
import com.merchant.util.PhoneNumHelper;
import com.merchant.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: muse-pay
 * @description: 进件任务
 * @author: Vincent
 * @create: 2019-03-07 09:06
 **/
public class RegisterTask implements Runnable {
    private Logger log = LoggerFactory.getLogger("RegisterTask");

    // Redis缓存KEY值
    public static final String REPEAT_MCH_NAME_CACHE_KEY = "MERCHANT_REGISTER_REPEAT";

    private String taskName;

    private RsMerchantInfoService rsMerchantInfoService;
    private BankCardInfoService bankCardInfoService;
    private MerchantBaseService merchantBaseService;
    private RedisUtil redisUtil;

    private List<BankCardInfo> bankCardList;
    private List<MerchantBaseInfo> merchantBaseList;
    private List<String> alreadyRegMchNameList;
    private List<ChannelInfo> activeChannelList;

    // 是否需要长休眠
    private boolean longSleep = true;
    // 连续短时间休眠次数
    private int shotSleepCount = 0;
    // 连续出现异常次数
    private int exceptionCount = 0;


    /**
     * 构造方法
     *
     * @param taskName
     * @param alreadyRegMchNameList
     * @param bankCardList
     * @param merchantBaseList
     * @param activeChannelList
     */
    public RegisterTask(String taskName, List<String> alreadyRegMchNameList, List<BankCardInfo> bankCardList,
                        List<MerchantBaseInfo> merchantBaseList, List<ChannelInfo> activeChannelList) {
        this.taskName = taskName;
        this.alreadyRegMchNameList = alreadyRegMchNameList;
        this.bankCardList = bankCardList;
        this.merchantBaseList = merchantBaseList;
        this.activeChannelList = activeChannelList;
    }

    /**
     * 设置服务属性值
     *
     * @param merchantService
     * @param bankCardInfoService
     * @param merchantBaseService
     * @param redisUtil
     */
    public void setService(RsMerchantInfoService merchantService,
                           BankCardInfoService bankCardInfoService, MerchantBaseService merchantBaseService, RedisUtil redisUtil) {
        this.rsMerchantInfoService = merchantService;
        this.bankCardInfoService = bankCardInfoService;
        this.merchantBaseService = merchantBaseService;
        this.redisUtil = redisUtil;
    }


    @Override
    public void run() {

        // 获取进件后返回重复的商户名
        List<String> repeatNameList = Lists.newArrayList();
        Object cacheObj = redisUtil.lGet(REPEAT_MCH_NAME_CACHE_KEY, 0, redisUtil.lGetListSize(
                REPEAT_MCH_NAME_CACHE_KEY));
        if (cacheObj != null) {
            repeatNameList = (List<String>) cacheObj;
        }


        // 计算要进件的数量
        int maxRegCount = 0;
        for (BankCardInfo item : bankCardList) {
            maxRegCount += item.needToRegisterNum();
        }
        log.info("run() " + taskName + ", 通道[" + bankCardList.get(0).getMchId() + "]" + "要进件数=" + maxRegCount);


        // 遍历前N条
        int handledCount = 0;
        int index = 0;
        while (handledCount < maxRegCount) {

            // 当前时间是否在进件时间之外
            if (CommonUtils.outOfTimeRange()) {
                log.info("run() " + taskName + " 当前时间不在进件时间内[06:30-23:10]");
                break;
            }
            // 二类卡列表为空
            if (bankCardList.size() == 0) {
                log.info("run() " + taskName + " 二类卡信息列表为空");
                break;
            }
            // 商户已经进件完毕
            if (index >= merchantBaseList.size()) {
                log.info("run() " + taskName + " 已经轮询商户列表到结尾=" + index);
                break;
            }

            // 商户名称
            MerchantBaseInfo merchantBase = merchantBaseList.get(index);
            // 初始商户名称
            String merchantName = merchantBase.getName();
            String originalName = merchantName;
            // 索引递增
            index = index + 1;


            // 是否已经重复
            if (repeatNameList.contains(originalName)) {
                if (merchantName.length() > 6) {
                    merchantName = merchantName.substring(0, 6);
                }
                merchantName = merchantName + CommonUtils.genRandomStr(3);
            }

            // 格式化商户名称
            try {
                merchantName = merchantBaseService.formatMerchantName(merchantName, originalName);
            } catch (Exception e) {
                log.error("格式化商户名称[" + merchantName + "]异常=" + e.getMessage());
                continue;
            }

            // 最终检查商铺名称是否超过10个字符
            if (merchantName.length() >= 10) {
                log.error("run() " + taskName + " 该商铺名称超过或等于10个字符=" + originalName);
                continue;
            }
            // 是否已经进件
            if (alreadyRegMchNameList.contains(merchantName)) {
                log.error("run() " + taskName + " 该商铺名称已经进过件=" + originalName);
                continue;
            }


            // 生成客服号码
            String phone = PhoneNumHelper.genPhoneNum();
            // 生成邮箱
            String email = CommonUtils.getRandomEmail(phone);


            // 获取省市区编码
            String provinceCode = merchantBase.getProvinceCode();
            String provinceName = merchantBase.getProvinceName();
            String cityCode = merchantBase.getCityCode();
            String cityName = merchantBase.getCityName();
            String areaCode = merchantBase.getAreaCode();
            String areaName = merchantBase.getAreaName();
            String address = merchantBase.getAddress();
            if (StringUtils.isEmpty(provinceCode) || StringUtils.isEmpty(cityCode) || StringUtils.isEmpty(areaCode)) {
                log.error("run() " + taskName + " 商户[" + originalName + "]没有获取到对应的省市区编号=" + address);
                continue;
            }

            // 获取门头照图片，检查是否存在
            File doorPicFile = FileHelper.getDoorPicFile(merchantBase.getDoorPicPath());
            if (doorPicFile == null || !doorPicFile.exists()) {
                log.error("run() " + taskName + " 根据名称=" + originalName + "找不到对应的图片文件["
                        + merchantBase.getDoorPicPath() + "]");
                continue;
            }
            // 上传门头照
            String doorPicUrl;
            try {
                doorPicUrl = FileHelper.uploadFile(doorPicFile);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("run() 上传商户[" + originalName + "]门头照[" + doorPicFile.getAbsolutePath() + "]异常="
                        + e.getMessage());
                continue;
            }
            if (StringUtils.isEmpty(doorPicUrl)) {
                log.error("run() 上传商户[" + originalName + "]门头照[" + doorPicFile.getAbsolutePath() + "]返回地址为空");
                continue;
            }


            // 生成完整商户信息
            int userIndex = handledCount % bankCardList.size();
            BankCardInfo bankCardInfo = bankCardList.get(userIndex);
            RuiShengUserInfo merchantInfo = RuiShengUserInfo.supplementParams(bankCardInfo, merchantName, provinceCode,
                    cityCode, areaCode, address, doorPicUrl, provinceName, cityName, areaName, phone, email, originalName);

            // 查询通道信息
            ChannelInfo channelInfo = null;
            for (ChannelInfo item : activeChannelList) {
                if (item.getMchId().equals(bankCardInfo.getMchId())) {
                    channelInfo = item;
                    break;
                }
            }
            if (channelInfo == null) {
                log.error("run() " + taskName + " 查询通道返回为空，通道编号=" + bankCardInfo.getMchId());
                return;
            }


            // 进件
            try {
                // 调用进件接口
                ResultData addResult = rsMerchantInfoService.insert(merchantInfo, channelInfo);

                // 解析进件返回结果
                String resultDataStr = (String) addResult.getData();
                JSONObject returnJson = JSONObject.parseObject(resultDataStr);
                int registerRet = returnJson.getInteger("code");
                // 添加成功
                if (registerRet == 1) {
                    // 进件计数
                    handledCount += 1;

                    // 二类卡信息中已进件数+1
                    bankCardInfo.increaseRegisterCount();
                    // 该二类卡是否还需要再继续进件
                    if (!bankCardInfo.isNeedToRegister()) {
                        bankCardList.remove(bankCardInfo);
                        log.info("run() " + taskName + " 二类卡[" + bankCardInfo.getPayName() + "]完成进件数量["
                                + bankCardInfo.getRegisterCount() + "]，从集合中移除");
                    }
                    // 更新二类卡进件状态信息
                    bankCardInfoService.updateRegInfo(bankCardInfo);

                    // 软删除进件成功的商户信息记录
                    int softDelCount = merchantBaseService.softDeleteById(merchantBase.getId());
                    log.info("run() " + taskName + " 删除进件成功的商户信息记录=" + softDelCount);


                    // 移除门头照图片
                    try {
                        FileHelper.removeDoneFile(doorPicFile, merchantBase.getDoorPicPath());
                    } catch (Exception e) {
                        log.error("run() " + taskName + " 移动进件成功的门头照图片=" + e.getMessage());
                    }

                    // 添加到已进件商户名称列表中
                    alreadyRegMchNameList.add(merchantInfo.getName());

                    // 进件成功，需要长休眠
                    longSleep = true;
                    shotSleepCount = 0;

                } else {
                    longSleep = false;
                    shotSleepCount += 1;

                    try {
                        if (returnJson.getString("msg").contains("已有商户使用该商户名称")) {
                            repeatNameList.add(merchantBase.getName());
                            redisUtil.lRightPush(REPEAT_MCH_NAME_CACHE_KEY, merchantBase.getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("进件操作Redis缓存异常=" + e.getMessage());
                    }
                }

                exceptionCount += 0;
                log.info("run() " + taskName + " 批量商户进件结果[" + handledCount + "]=" + registerRet + ", " + addResult.getData());

            } catch (Exception e) {
                exceptionCount += 1;
                longSleep = true;

                e.printStackTrace();
                log.error("run() " + taskName + " 商户进件异常=" + e.getMessage());
            }

            // 如果连续出现异常超过5次，则停止进件
            if (exceptionCount >= 5) {
                log.error("run() " + taskName + " 商户进件连续出现异常" + exceptionCount + "次，停止进件");
                break;
            }
            // 如果连续出现进件返回失败，则停止进件
            if (shotSleepCount >= 10) {
                log.error("run() " + taskName + " 商户进件连续返回失败结果" + shotSleepCount + "次，停止进件");
                break;
            }


            // 睡眠时间
            if (handledCount < maxRegCount) {
                try {
                    int interval;
                    // 正常进件情况下
                    if (longSleep || shotSleepCount >= 5) {
                        interval = CommonUtils.getRandomNum(150, 180);
                    } else {
                        interval = CommonUtils.getRandomNum(10, 15);
                    }
                    // 进件出现异常情况下
                    if (exceptionCount >= 3) {
                        interval = CommonUtils.getRandomNum(180, 300);
                    }
                    log.info("run() " + taskName + " 休眠" + interval + "秒");
                    TimeUnit.SECONDS.sleep(interval);
                } catch (InterruptedException e) {
                    log.error("run() " + taskName + " 休眠异常=" + e.getMessage());
                }
            }
        }

        log.info(taskName + " 成功进件" + handledCount + "个。");
    }

}
