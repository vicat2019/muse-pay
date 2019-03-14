package com.merchant.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.merchant.dao.RsMerchantInfoMapper;
import com.merchant.entity.ChannelInfo;
import com.merchant.entity.ResultData;
import com.merchant.entity.RuiShengUserInfo;
import com.merchant.service.BaseService;
import com.merchant.service.RsMerchantInfoService;
import com.merchant.util.HttpUtils;
import com.merchant.util.Rsaencrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * @Description: 商户数据服务类
 * @Author: Vincent
 * @Date: 2019/2/16
 */
@Service("rsMerchantInfoService")
public class RsMerchantInfoServiceImpl extends BaseService<RsMerchantInfoMapper, RuiShengUserInfo>
        implements RsMerchantInfoService {
    private Logger log = LoggerFactory.getLogger("rsMerchantInfoService");

    // 商户进件地址
    private static final String REGISTER_URL = "http://bn.ruiyihuyu.com/api-v1-user/register";

    // 修改费率地址
    private static final String MODIFY_RATE_URL = "http://bn.ruiyihuyu.com/api-v1-user/channel";

    @Autowired
    private HttpUtils httpUtils;


    /**
     * 添加商户信息
     *
     * @param obj 商户信息
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData add(RuiShengUserInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }

    /**
     * 更新商户
     *
     * @param obj 商户信息
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData update(RuiShengUserInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        if (obj.getId() == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.updateByPrimaryKey(obj);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public int updateBatch(Map<String, Object> params) throws Exception {
        if (params == null || params.size()==0) {
            throw new Exception("批量更新，参数不能为空");
        }

        return mapper.updateBatch(params);
    }

    /**
     * 根据ID查询商户
     *
     * @param id ID值
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData get(Integer id) throws Exception {
        if (id == null || id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        RuiShengUserInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    /**
     * 查询商户
     *
     * @param shengUserInfo 查询条件
     * @param pageNum       页码
     * @param size          每页记录数
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData queryRuiShengUser(RuiShengUserInfo shengUserInfo, int pageNum, int size) throws Exception {
        PageHelper.startPage(pageNum, size);

        List<RuiShengUserInfo> userList = mapper.queryRuiShengUser(null);
        PageInfo page = new PageInfo(userList);
        return ResultData.getSuccessResult(page);
    }

    /**
     * 查询所有商户名称
     *
     * @return List
     * @throws Exception 异常
     */
    @Override
    public List<String> getAllName() throws Exception {
        return mapper.getAllName();
    }

    @Override
    public List<RuiShengUserInfo> query(Map<String, Object> params) {
        return mapper.query(params);
    }

    /**
     * 删除商户
     *
     * @param id ID
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData del(Integer id) throws Exception {
        if (id == null || id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.deleteByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    /**
     * 商户进件
     *
     * @param userInfo    商户信息
     * @param channelInfo 通道信息
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData insert(RuiShengUserInfo userInfo, ChannelInfo channelInfo) throws Exception {
        // 检查参数
        if (userInfo == null) {
            return ResultData.getErrResult("商户信息不能为空");
        }
        userInfo.checkPropertiesEmpay();
        if (channelInfo == null) {
            return ResultData.getErrResult("通道信息不能为空");
        }

        // 参数
        Map<String, Object> params = userInfo.toMap(channelInfo.getDesKey());
        // 签名
        Rsaencrypt rsaencrypt = new Rsaencrypt();
        params.put("sign", rsaencrypt.signByMap(new TreeMap<>(params), channelInfo.getPrivateKey()));

        // 请求
        log.info("商户进件，请求参数=" + JSONObject.toJSONString(params));
        ResultData data = httpUtils.post(REGISTER_URL, params);
        log.info("商户进件，返回内容=" + data);


        // 进件成功则保存
        handleResultForDB(userInfo, data);
        return data;
    }

    @Override
    public ResultData insertBatch(List<RuiShengUserInfo> list) throws Exception {
        if (list == null || list.size() == 0) {
            throw new Exception("批量添加，参数为空");
        }
        return ResultData.getSuccessResult(mapper.insertBatch(list));
    }

    /**
     * 检查进件是否成功，成功则保存
     *
     * @param userInfo 商户信息
     * @param data     进件返回结果
     * @throws Exception 异常
     */
    private void handleResultForDB(RuiShengUserInfo userInfo, ResultData data) throws Exception {
        if (data.whetherOk()) {
            String jsonStr = (String) data.getData();
            JSONObject json = JSONObject.parseObject(jsonStr);

            if (json.get("code").equals(1)) {
                JSONObject jsonData = json.getJSONObject("data");
                if (jsonData != null) {
                    userInfo.setSubmchid((String) jsonData.get("submchid"));
                    add(userInfo);
                } else {
                    log.error("商户进件失败，解析结果，返回内容为空=" + data);
                }
            } else {
                log.error("商户进件失败，返回结果=" + data);
            }
        }
    }

    /**
     * 修改费率
     *
     * @param rate                    费率值
     * @param mchId                   代理商编号
     * @param submchId                商户编号
     * @param channelRatePropertyName 渠道费率属性名称
     * @param privateKey              秘钥
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData modifyRate(double rate, String mchId, String submchId, String channelRatePropertyName, String privateKey) throws Exception {
        // 检查参数
        Preconditions.checkArgument(rate != 0, "费率不能为0");
        Preconditions.checkArgument(!StringUtils.isEmpty(mchId), "代理商编号不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(submchId), "商户编号不能为空");

        if (StringUtils.isEmpty(channelRatePropertyName)) {
            channelRatePropertyName = "bnwxFix";
        }

        // 费率参数
        Map<String, Object> subParamMap = Maps.newHashMap();
        subParamMap.put("rate", rate);
        subParamMap.put("fee", 0);
        Map<String, Object> rateParamMap = Maps.newHashMap();
        rateParamMap.put(channelRatePropertyName, subParamMap);

        // 请求参数
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("mchid", mchId);
        paramMap.put("submchid", submchId);
        paramMap.put("channelinfo", JSONObject.toJSONString(rateParamMap));

        // 生成签名
        Rsaencrypt rsaencrypt = new Rsaencrypt();
        String sign = rsaencrypt.signByMap(new TreeMap<>(paramMap), privateKey);
        paramMap.put("sign", sign);

        // 发送请求
        log.info("修改费率，请求参数=" + JSONObject.toJSONString(paramMap));
        ResultData resultData = httpUtils.post(MODIFY_RATE_URL, paramMap);
        log.info("修改费率，返回内容=" + resultData);

        return resultData;
    }
}
