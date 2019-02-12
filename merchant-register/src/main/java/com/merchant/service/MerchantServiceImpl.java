package com.merchant.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.merchant.entity.RuiShengUserInfo;
import com.merchant.service.impl.MerchantService;
import com.merchant.util.HttpUtils;
import com.merchant.util.Rsaencrypt;
import com.muse.common.entity.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @program: muse-pay
 * @description: 服务类
 * @author: Vincent
 * @create: 2019-02-12 11:28
 **/
@Service("merchantServiceImpl")
public class MerchantServiceImpl implements MerchantService {
    private Logger log = LoggerFactory.getLogger("MerchantServiceImpl");

    // 商户进件地址
    private static final String REGISTER_URL = "http://api.ruishengglass.cn/api-v1-user/register";

    // 修改费率地址
    private static final String MODIFY_RATE_URL = "http://api.ruishengglass.cn/api-v1-user/channel";

    @Autowired
    private HttpUtils httpUtils;


    @Override
    public ResultData add(RuiShengUserInfo userInfo) throws Exception {
        userInfo = new RuiShengUserInfo();
        userInfo.setMchid("00020019");
        userInfo.setName("测试商户");
        userInfo.setProvince("440000");
        userInfo.setCity("440300");
        userInfo.setArea("440306");
        userInfo.setAddress("西乡街道办宝源路1号");
        userInfo.setLegelname("李白");
        userInfo.setLegelcertno("45682419900102542685122");
        userInfo.setEmail("test@qq.com");
        userInfo.setPhone("18902132301");

        userInfo.setBankno("3080000");
        userInfo.setBranchno("308584001821");
        userInfo.setCardno("6225887832365325");
        userInfo.setPayname("测试2");
        userInfo.setPayphone("18902132301");
        userInfo.setCardprovince("440000");
        userInfo.setCardcity("440300");
        userInfo.setCardarea("440306");
        userInfo.setType("1");
        userInfo.setCerttype("1");
        userInfo.setCertno("9144030008386556X5");

        userInfo.setBuslicpic("/uploads/50/8b2790293d519840c6070b842821df.jpg");
        userInfo.setLegfrontpic("/uploads/97/cccab652e8720cd0999eb417360bf8.jpg");
        userInfo.setLegbackpic("/uploads/97/cccab652e8720cd0999eb417360bf8.jpg");
        userInfo.setHandpic("/uploads/e4/3192cc27528a96222a9915f509ecf2.jpg");
        userInfo.setDoorpic("/uploads/50/8b2790293d519840c6070b842821df.jpg");

        Map<String, Object> rate = new HashMap<>();
        Map<String, Integer> temp = Maps.newHashMap();
        temp.put("rate", 60);
        temp.put("fee", 0);
        rate.put("alipayQR", temp);
        temp = Maps.newHashMap();
        temp.put("rate", 0);
        temp.put("fee", 2);
        rate.put("otherFul", temp);
        userInfo.setChannelinfo(JSONObject.toJSONString(rate));


        Map<String, Object> params = userInfo.toMap();
        TreeMap<String, Object> signParam = new TreeMap<>(params);
        params.put("sign", Rsaencrypt.signByMap(signParam));


        log.info("商户进件，请求参数=" + JSONObject.toJSONString(params));
        ResultData data = httpUtils.post(REGISTER_URL, params);
        log.info("商户进件，返回内容=" + data);

        return data;
    }

    @Override
    public ResultData insert(RuiShengUserInfo userInfo) throws Exception {
        if (userInfo == null) {
            return ResultData.getErrResult("参数不能为空");
        }

        // 参数
        Map<String, Object> params = userInfo.toMap();

        // 签名
        TreeMap<String, Object> signParam = new TreeMap<>(params);
        params.put("sign", Rsaencrypt.signByMap(signParam));

        // 请求
        log.info("商户进件，请求参数=" + JSONObject.toJSONString(params));
        ResultData data = httpUtils.post(REGISTER_URL, params);
        log.info("商户进件，返回内容=" + data);

        return data;
    }

    @Override
    public ResultData modifyRate(int rate, String mchId, String submchId) throws Exception {
        // 检查参数
        Preconditions.checkArgument(rate != 0, "费率不能为0");
        Preconditions.checkArgument(!StringUtils.isEmpty(mchId), "代理商编号不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(submchId), "商户编号不能为空");

        // 费率参数
        Map<String, Integer> subParamMap = Maps.newHashMap();
        subParamMap.put("rate", rate);
        subParamMap.put("fee", 0);
        Map<String, Integer> otherParamMap = Maps.newHashMap();
        otherParamMap.put("rate", rate);
        otherParamMap.put("fee", 0);
        Map<String, Object> rateParamMap = Maps.newHashMap();
        rateParamMap.put("bnzfbQR", subParamMap);
        rateParamMap.put("otherFul", otherParamMap);


        // 请求参数
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("mchid", mchId);
        paramMap.put("submchid", submchId);
        paramMap.put("channelinfo", JSONObject.toJSONString(rateParamMap));


        // 发送请求
        log.info("修改费率，请求参数=" + JSONObject.toJSONString(paramMap));
        ResultData resultData = httpUtils.post(MODIFY_RATE_URL, paramMap);
        log.info("修改费率，返回内容=" + resultData);

        return resultData;
    }
}
