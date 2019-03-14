package com.merchant.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.merchant.entity.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: merchant-register
 * @description: 银行相关
 * @author: Vincent
 * @create: 2019-03-01 16:27
 **/
@Component("bankHelper")
public class BankHelper {

    @Autowired
    private HttpUtils httpUtils;

    // 缓存查询到的支行列表
    private static Map<String, Map<String, String>> cacheBranchMap = Maps.newHashMap();


    /**
     * 获取所有银行信息
     *
     * @return Map
     */
    public static Map<String, String> getAllBanks() {
        String content = "{\"code\":1,\"msg\":\"获取银行列表成功\",\"data\":[{\"value\":\"1009999\",\"text\":\"邮政储蓄银行\",\"code\":\"PSBC\",\"typecode\":\"9006\"},{\"value\":\"1020000\",\"text\":\"工商银行\",\"code\":\"ICBC\",\"typecode\":\"9040\"},{\"value\":\"1030000\",\"text\":\"农业银行\",\"code\":\"ABC\",\"typecode\":\"9009\"},{\"value\":\"1040000\",\"text\":\"中国银行\",\"code\":\"BOC\",\"typecode\":\"9021\"},{\"value\":\"1050000\",\"text\":\"建设银行\",\"code\":\"CCB\",\"typecode\":\"9008\"},{\"value\":\"3010000\",\"text\":\"交通银行\",\"code\":\"BCM\",\"typecode\":\"9005\"},{\"value\":\"3020000\",\"text\":\"中信银行\",\"code\":\"ECITIC\",\"typecode\":\"9001\"},{\"value\":\"3030000\",\"text\":\"光大银行\",\"code\":\"CEB\",\"typecode\":\"9002\"},{\"value\":\"3040000\",\"text\":\"华夏银行\",\"code\":\"HXB\",\"typecode\":\"9003\"},{\"value\":\"3050000\",\"text\":\"民生银行\",\"code\":\"CMBC\",\"typecode\":\"9010\"},{\"value\":\"3060000\",\"text\":\"广发银行股份有限公司\",\"code\":\"GDB\",\"typecode\":\"9012\"},{\"value\":\"3070010\",\"text\":\"平安银行\",\"code\":\"PAB\",\"typecode\":\"9013\"},{\"value\":\"3080000\",\"text\":\"招商银行\",\"code\":\"CMB\",\"typecode\":\"9004\"},{\"value\":\"3090000\",\"text\":\"兴业银行\",\"code\":\"CIB\",\"typecode\":\"9015\"},{\"value\":\"3100000\",\"text\":\"浦东发展银行\",\"code\":\"SPDB\",\"typecode\":\"9020\"}]}";
        JSONObject json = JSONObject.parseObject(content);
        JSONArray dataJsonArray = json.getJSONArray("data");

        Map<String, String> bankMap = Maps.newHashMap();

        for (int i = 0; i < dataJsonArray.size(); i++) {
            JSONObject item = dataJsonArray.getJSONObject(i);
            String text = item.getString("text");
            String value = item.getString("value");

            bankMap.put(text, value);
        }
        return bankMap;
    }

    /**
     * 根据银行编码获取支行数据
     *
     * @param parent 银行编码
     * @return Map
     */
    public Map<String, String> getBranchBy(String parent) {
        if (cacheBranchMap.get(parent) != null) {
            return cacheBranchMap.get(parent);
        }
        String url = "http://www.ruishengglass.cn/api-v1-bank/getBranchList?parent=" + parent;
        ResultData resultData = httpUtils.get(url, Maps.newHashMap());
        Map<String, String> branchMap = CommonUtils.parseBinNongResult(resultData);

        if (branchMap != null) {
            cacheBranchMap.put(parent, branchMap);
        }

        return branchMap;
    }

    /**
     * 根据银行获取银行编码
     *
     * @param bankName 银行名称
     * @return String
     */
    public String getBankCode(String bankName) {
        Map<String, String> bankCodeMap = getAllBanks();

        // 获取银行名称
        Pattern pattern = Pattern.compile("(.*银行)");
        Matcher matcher = pattern.matcher(bankName);
        if (matcher.find()) {
            String key = matcher.group(1);
            if (bankCodeMap.containsKey(key)) {
                return bankCodeMap.get(key);
            }
            key = key.replaceAll("中国", "");
            if (bankCodeMap.containsKey(key)) {
                return bankCodeMap.get(key);
            }
        }
        return "";
    }

    /**
     * 获取支行编码
     *
     * @param bankCode   银行编码
     * @param branchName 支行名称
     * @return String
     */
    public String getBranchCode(String bankCode, String branchName) {
        Map<String, String> branchMap = getBranchBy(bankCode);
        if (branchMap != null) {
            int index = branchName.indexOf("银行");
            String branchNameStr = branchName.substring(index + 2);

            for (String key : branchMap.keySet()) {
                if (key.contains(branchNameStr)) {
                    return branchMap.get(key);
                }
            }
        }

        return "";
    }


}
