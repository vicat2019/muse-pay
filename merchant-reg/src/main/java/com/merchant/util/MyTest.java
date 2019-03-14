package com.merchant.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.merchant.entity.BankCardInfo;
import com.merchant.entity.ResultData;
import com.merchant.entity.RuiShengUserInfo;
import com.merchant.service.BankCardInfoService;
import com.merchant.service.RsMerchantInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: muse-pay
 * @description:
 * @author: Vincent
 * @create: 2019-02-12 18:35
 **/
@Service("test")
public class MyTest {
    private Logger log = LoggerFactory.getLogger("MyTest");

    @Autowired
    private BankCardInfoService bankCardInfoService;

    @Autowired
    private RsMerchantInfoService rsMerchantInfoService;


    public void main() throws Exception {
        // 从滨农获取数据
        Map<String, Map<String, String>> bnMchMap = getBnMchList("F:\\member_aa.json");
        if (bnMchMap == null || bnMchMap.size() == 0) throw new Exception("获取滨农数据为空");

        // 获取数据库所有数据
        List<RuiShengUserInfo> dbMchList = rsMerchantInfoService.query(Maps.newHashMap());
        if (dbMchList == null || dbMchList.size() == 0) throw new Exception("获取数据库数据为空");


        List<String> bnSubMchIdList = Lists.newArrayList(bnMchMap.keySet());
        List<String> dbSubMchIdList = Lists.newArrayList();
        dbMchList.forEach(item -> dbSubMchIdList.add(item.getSubmchid()));

        // 以数据库为基准滨农多的记录
        List<String> bnMoreSubMchIdList = bnSubMchIdList.stream().filter(subMchId -> !dbSubMchIdList.contains(subMchId))
                .collect(Collectors.toList());
        log.info("滨农后台多出的记录=" + bnMoreSubMchIdList);

        // 以滨农为基准数据库多的记录
        List<String> dbMoreSubMchIdList = dbSubMchIdList.stream().filter(item -> !bnSubMchIdList.contains(item))
                .collect(Collectors.toList());
        log.info("数据库多出的记录=" + dbMoreSubMchIdList);


        // 滨农多的数据
        List<Map<String, String>> bnMoreMchMap = Lists.newArrayList();
        for (String subMchId : bnMoreSubMchIdList) {
            Map<String, String> mchMap = bnMchMap.get(subMchId);
            if (mchMap != null) bnMoreMchMap.add(mchMap);
        }
        if (bnMoreMchMap.size() > 0) {
            // 可进件二类卡信息
            Map<String, BankCardInfo> bankCardMap = Maps.newHashMap();
            List<BankCardInfo> bankCardList = bankCardInfoService.getAvailableBankCard();
            bankCardList.forEach(item -> bankCardMap.put(item.getPayName(), item));

            List<RuiShengUserInfo> rsMerchantList = genRsMerchant(bnMoreMchMap, bankCardMap);
            if (rsMerchantList == null || rsMerchantList.size() == 0) {
                throw new Exception("生成进件商户信息为空");
            }
            ResultData insertResult = rsMerchantInfoService.insertBatch(rsMerchantList);
            log.info("添加滨农后台多的数据到数据库=" + insertResult);
        }


        // 同步后台状态
        List<String> activeList = Lists.newArrayList();
        List<String> unactiveList = Lists.newArrayList();
        bnMchMap.keySet().forEach(item -> {
            Map<String, String> mchMap = bnMchMap.get(item);
            String subMchId = mchMap.get("submchid");

            if ("ACTIVE".equals(matchStatus(mchMap.get("_bnstatus")))) {
                activeList.add(subMchId);
            } else {
                unactiveList.add(subMchId);
            }
        });
        if (activeList.size() > 0) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("list", activeList);
            params.put("status", "ACTIVE");
            int activeCount = rsMerchantInfoService.updateBatch(params);
            log.info("同步滨农后台状态Active=" + activeCount);
        }
        if (unactiveList.size() > 0) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("list", unactiveList);
            params.put("status", "UNACTIVE");
            int unactiveCount = rsMerchantInfoService.updateBatch(params);
            log.info("同步滨农后台状态unactive=" + unactiveCount);
        }


    }

    private Map<String, Map<String, String>> getBnMchList(String path) {
        Map<String, Map<String, String>> bnMchMap = Maps.newHashMap();

        try {
            InputStreamReader in = new InputStreamReader(new FileInputStream(path));
            BufferedReader bin = new BufferedReader(in);

            StringBuilder sb = new StringBuilder();
            String line = bin.readLine();
            while (line != null) {
                sb.append(line);
                line = bin.readLine();
            }
            in.close();
            bin.close();

            JSONObject json = JSONObject.parseObject(sb.toString());
            JSONArray jsonArray = json.getJSONArray("data");

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                Map<String, String> mchMap = Maps.newHashMap();
                mchMap.put("name", temp.getString("name"));
                mchMap.put("payname", temp.getString("payname"));
                mchMap.put("mchid", temp.getString("mchid"));
                mchMap.put("submchid", temp.getString("submchid"));
                mchMap.put("_bnstatus", temp.getString("_bnstatus"));
                mchMap.put("createtime", temp.getString("createtime"));

                bnMchMap.put(temp.getString("submchid"), mchMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bnMchMap;
    }

    private List<RuiShengUserInfo> genRsMerchant(List<Map<String, String>> bnMoreMchMap, Map<String, BankCardInfo> bankCardMap) {
        List<RuiShengUserInfo> mchList = Lists.newArrayList();

        for (Map<String, String> item : bnMoreMchMap) {
            RuiShengUserInfo user = new RuiShengUserInfo();

            user.setPayname(item.get("payname"));
            user.setLegelname(item.get("payname"));
            user.setName(item.get("name"));
            user.setMchid(item.get("mchid"));
            user.setSubmchid(item.get("submchid"));

            if (!bankCardMap.containsKey(user.getPayname())) {
                continue;
            }
            BankCardInfo bankCardInfo = bankCardMap.get(user.getPayname());
            if (bankCardInfo == null) {
                continue;
            }
            user.setCardno(bankCardInfo.getCardNo());
            user.setEcardno(bankCardInfo.getEcardNo());

            user.setStatus(matchStatus(item.get("_bnstatus")));
            user.setCreateTime(parseDate(item.get("createtime")));
            mchList.add(user);
        }
        return mchList;
    }

    private String matchStatus(String statusStr) {
        if (StringUtils.isEmpty(statusStr)) {
            return "UNACTIVE";
        }

        if ("已通过".equals(statusStr.trim())) {
            return "ACTIVE";
        } else {
            return "UNACTIVE";
        }
    }

    private Date parseDate(String createTimeStr) {
        if (!StringUtils.isEmpty(createTimeStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(createTimeStr);
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date();
            }
        } else {
            return new Date();
        }
    }


}
