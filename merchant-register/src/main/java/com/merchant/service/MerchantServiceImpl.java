package com.merchant.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.merchant.entity.ResultData;
import com.merchant.entity.RuiShengUserInfo;
import com.merchant.service.impl.MerchantService;
import com.merchant.util.HttpUtils;
import com.merchant.util.Rsaencrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

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
    private static final String REGISTER_URL = "http://www.ruishengglass.cn/api-v1-user/register";

    // 修改费率地址
    private static final String MODIFY_RATE_URL = "http://www.ruishengglass.cn/api-v1-user/channel";

    @Autowired
    private HttpUtils httpUtils;


    @Override
    public ResultData add(RuiShengUserInfo userInfo) throws Exception {
        userInfo = new RuiShengUserInfo();
        userInfo.setMchid("00020019");
        userInfo.setName("首战（珠海）科技");
        userInfo.setProvince("440000");
        userInfo.setCity("440400");
        userInfo.setArea("440402");
        userInfo.setAddress("珠海市香洲区园林路1号32栋202房");
        userInfo.setLegelname("陈裕明");
        userInfo.setEmail("bm555111122@sohu.com");
        userInfo.setPhone("17841629387");

        userInfo.setBankno("1040000");
        userInfo.setBranchno("104584002488");

        userInfo.setEcardno("Du91QHgJLpVDmVJNNjIpHWy+JE+joE2l");

        userInfo.setCardno("+2ySH0gNfCntROMFXwSYrOFC0lXUJt3I");
        //userInfo.setCardno(Rsaencrypt.java_openssl_encrypt("6225887832365325", "7ba87a2ecc6bd4d8fe133524", "1234567890123456"));
        userInfo.setLegelcertno("vYm5mkOEpOy+LIZ457oV5fAVCDjZT0Iq");
        // userInfo.setLegelcertno(Rsaencrypt.java_openssl_encrypt("45682419900102542685122", "7ba87a2ecc6bd4d8fe133524", "1234567890123456"));
        userInfo.setPayname("qmOAxAXXWIjBVvLChuuF7g==");
        //userInfo.setPayname(Rsaencrypt.java_openssl_encrypt("测试2", "7ba87a2ecc6bd4d8fe133524", "1234567890123456"));
        userInfo.setCertno("vYm5mkOEpOy+LIZ457oV5fAVCDjZT0Iq");
        //userInfo.setCertno(Rsaencrypt.java_openssl_encrypt("9144030008386556X5", "7ba87a2ecc6bd4d8fe133524", "1234567890123456"));

        userInfo.setPayphone("13160721607");
        userInfo.setCardprovince("440000");
        userInfo.setCardcity("440300");
        userInfo.setCardarea("440307");
        userInfo.setType("1");
        userInfo.setCerttype("1");

        userInfo.setBuslicpic("/uploads/50/8b2790293d519840c6070b842821df.jpg");
        userInfo.setLegfrontpic("/uploads/97/cccab652e8720cd0999eb417360bf8.jpg");
        userInfo.setLegbackpic("/uploads/97/cccab652e8720cd0999eb417360bf8.jpg");
        userInfo.setHandpic("/uploads/e4/3192cc27528a96222a9915f509ecf2.jpg");
        userInfo.setDoorpic("/uploads/50/8b2790293d519840c6070b842821df.jpg");

        userInfo.setChannelinfo("60");


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
