package com.nucc.service.weixin.impl;

import com.muse.common.entity.ResultData;
import com.nucc.dao.weixin.WXMerchantInfoMapper;
import com.nucc.entity.weixin.WXMerchantInfo;
import com.nucc.entity.weixin.WXBaseResponse;
import com.nucc.entity.weixin.requestentity.WXMerchantInfoVO;
import com.nucc.entity.weixin.responseentity.WXMerchantResponse;
import com.nucc.service.alipay.BaseService;
import com.nucc.service.weixin.WXMerchantInfoService;
import com.nucc.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;


/**
 * Created by code generator  on 2018/11/29.
 */
@Service("wxMerchantInfoService")
@Transactional
public class WXMerchantInfoServiceImpl extends BaseService<WXMerchantInfoMapper, WXMerchantInfo> implements WXMerchantInfoService {
    private Logger log = LoggerFactory.getLogger("WXMerchantInfoServiceImpl");

    @Override
    public WXBaseResponse add(WXMerchantInfoVO obj) throws Exception {
        // 检查是否已经存在
        WXMerchantInfo merchantInfo = obj.getMerchantInfo();

        // 检查商户是否已经存在
        Integer count = mapper.getCountBy(merchantInfo);
        if (count!=null && count > 0) {
            return WXBaseResponse.getErrResult("商户[" + merchantInfo.getMerchant_name() + "]已经存在");
        }

        // 生成子商户号
        String subMerchantId = System.currentTimeMillis() + TextUtils.getRandomNum(1);
        merchantInfo.setSub_merchant_id(subMerchantId);

        // 保存
        int result = mapper.insert(merchantInfo);
        log.info("微信子商户进件, 结果=" + result);

        // 返回结果
        WXMerchantResponse response = WXMerchantResponse.getSuccessResult();
        response.setReturnData(merchantInfo);
        return response;
    }


    @Override
    public WXBaseResponse update(WXMerchantInfoVO obj) throws Exception {
        // 获取提交的参数
        WXMerchantInfo merchantInfo = obj.getMerchantInfo();

        // 该商户是否存在
        Integer exitCount = mapper.getCountBySubMerchantId(merchantInfo.getSub_merchant_id());
        if (exitCount == null || exitCount==0) {
            return WXBaseResponse.getErrResult("商户[" + merchantInfo.getSub_merchant_id() + "]不存在");
        }

        // 修改
        Integer result = mapper.updateBySubMerchantId(merchantInfo);
        log.info("微信子商户修改信息, 结果=" + result);

        // 返回结果
        WXMerchantResponse response = WXMerchantResponse.getSuccessResult();
        return response;
    }

    @Override
    public WXBaseResponse query(WXMerchantInfoVO obj) throws Exception {
        // 检查参数
        if (StringUtils.isEmpty(obj.getSub_mch_id()) && StringUtils.isEmpty(obj.getMerchant_remark())) {
            return WXBaseResponse.getErrResult("商户备注和商户识别码不能都为空");
        }

        // 查询
        WXMerchantInfo merchantInfo = mapper.query(obj.getMerchantInfo());
        if (merchantInfo == null) {
            return WXBaseResponse.getErrResult("商户[" + (!StringUtils.isEmpty(obj.getSub_mch_id()) ? obj.getSub_mch_id()
                    : obj.getMerchant_remark()) + "]不存在");
        }

        // 返回结果
        WXMerchantResponse response = WXMerchantResponse.getSuccessResult();
        response.setReturnAllData(merchantInfo);
        return response;
    }

    @Override
    public ResultData get(Integer id) throws Exception {
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        WXMerchantInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.deleteByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }
}
