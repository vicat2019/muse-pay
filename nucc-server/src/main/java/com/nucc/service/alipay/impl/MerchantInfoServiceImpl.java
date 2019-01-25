package com.nucc.service.alipay.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.nucc.dao.alipay.MerchantInfoMapper;
import com.nucc.entity.alipay.AddressInfo;
import com.nucc.entity.alipay.BankCardInfo;
import com.nucc.entity.alipay.ContactInfo;
import com.nucc.entity.alipay.MerchantInfo;
import com.nucc.entity.alipay.base.BaseResponseEntity;
import com.nucc.entity.alipay.requestentity.MerchantInfoVO;
import com.nucc.entity.alipay.responseentity.MerchantCreateResponse;
import com.nucc.service.alipay.AddressInfoService;
import com.nucc.service.alipay.BankCardInfoService;
import com.nucc.service.alipay.ContactInfoService;
import com.nucc.service.alipay.MerchantInfoService;
import com.nucc.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by code generator  on 2018/09/26.
 */
@Service("merchantInfoService")
@Transactional
public class MerchantInfoServiceImpl extends BaseService<MerchantInfoMapper, MerchantInfo> implements MerchantInfoService {
    private Logger log = LoggerFactory.getLogger("MerchantInfoServiceImpl");

    @Autowired
    private ContactInfoService contactInfoService;

    @Autowired
    private AddressInfoService addressInfoService;

    @Autowired
    private BankCardInfoService bankCardInfoService;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public BaseResponseEntity add(MerchantInfoVO obj) throws Exception {
        // 检查是否缺少参数
        BaseResponseEntity validateResult = obj.paramLegal();
        if (!validateResult.isOk()) {
            return validateResult;
        }

        // 检查商户是否已经存在
        String external_id = obj.getExternal_id();
        int count = mapper.getCountByExternalId(external_id);
        if (count > 0) {
            return BaseResponseEntity.getErrResult("该商户已经进件[" + external_id + "]");
        }

        // 生成NUCC的商户编号，2088101156429885
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = sdf.format(new Date());
        String merchantId = timestamp + TextUtils.getRandomNum(3);

        MerchantInfo merchantInfo = MerchantInfo.getInstance(obj);
        merchantInfo.setSub_merchant_id(merchantId);

        mapper.insert(merchantInfo);

        // 联系方式
        if (merchantInfo.getContact_info() != null && merchantInfo.getContact_info().length > 0) {
            for (ContactInfo item : merchantInfo.getContact_info()) {
                item.setSub_merchant_id(merchantId);
                contactInfoService.add(item);
            }
        }

        // 地址信息
        if (merchantInfo.getAddress_info() != null && merchantInfo.getAddress_info().length > 0) {
            for (AddressInfo item : merchantInfo.getAddress_info()) {
                item.setSub_merchant_id(merchantId);
                addressInfoService.add(item);
            }
        }

        // 银行卡
        if (merchantInfo.getBankcard_info() != null && merchantInfo.getBankcard_info().length > 0) {
            for (BankCardInfo item : merchantInfo.getBankcard_info()) {
                item.setSub_merchant_id(merchantId);
                bankCardInfoService.add(item);
            }
        }


        MerchantCreateResponse response = new MerchantCreateResponse();
        response.setOkValue();
        response.setSub_mserchant_id(merchantInfo.getSub_merchant_id());
        return response;
    }

    @Override
    public BaseResponseEntity queryMerchantByKey(MerchantInfoVO params) throws Exception {
        // 检查参数
        if (params == null) {
            return BaseResponseEntity.getErrResult("参数不能为空");
        }
       /* ResultData validateResult = params.legalParam();
        if (!validateResult.isOk()) {
            return validateResult;
        }*/

        // 查询商户
        MerchantInfo merchantInfo;
        if (!StringUtils.isEmpty(params.getSub_merchant_id())) {
            merchantInfo = mapper.getByMerchantId(params.getSub_merchant_id());
        } else {
            merchantInfo = mapper.getByExternalId(params.getExternal_id());
        }
        if (merchantInfo == null) {
            return BaseResponseEntity.getErrResult("该商户不存在");
        }

        MerchantCreateResponse response = MerchantCreateResponse.getInstanceFrom(merchantInfo);
        response.setOkValue();
        return response;
    }

    @Override
    public BaseResponseEntity update(MerchantInfoVO params) throws Exception {
        // 检查参数
        if (params == null) {
            return BaseResponseEntity.getErrResult("参数不能为空");
        }

        MerchantInfo merchantInfo = mapper.getByExternalId(params.getExternal_id());
        if (merchantInfo == null) {
            log.error("商户不存在[" + params.getExternal_id() + "]");
            return BaseResponseEntity.getErrResult("商户不存在");
        }

        // 更新商户信息
        mapper.updateByExternalId(params.getMerchant());

        // 联系方式
        // 先删除
        ResultData delResult = contactInfoService.delByMerchantId(merchantInfo.getSub_merchant_id());
        if (!delResult.isOk()) {
            return BaseResponseEntity.getErrResult("修改联系信息失败");
        } else {
            log.info("修改商户，删除联系人信息记录数=" + delResult.getData());
        }

        log.info("修改后的联系人信息=" + params.getContact_infos());

        if (params.getContact_infos() != null && params.getContact_infos().length > 0) {
            for (ContactInfo item : params.getContact_infos()) {
                item.setSub_merchant_id(merchantInfo.getSub_merchant_id());
                log.info("====>" + item.toString());
                contactInfoService.add(item);
            }
        }

        // 地址信息
        if (params.getAddress_info() != null && params.getAddress_info().length > 0) {
            for (AddressInfo item : params.getAddress_info()) {
                addressInfoService.add(item);
            }
        }

        // 银行卡
        if (params.getBankcard_info() != null && params.getBankcard_info().length > 0) {
            for (BankCardInfo item : params.getBankcard_info()) {
                bankCardInfoService.add(item);
            }
        }

        MerchantCreateResponse response = new MerchantCreateResponse();
        response.setOkValue();
        response.setSub_mserchant_id(params.getSub_merchant_id());
        return response;
    }


    @Override
    public BaseResponseEntity del(Integer id) throws Exception {
        return null;
    }


    @Override
    public BaseResponseEntity get(Integer id) throws Exception {
        return null;
    }

    @Override
    public BaseResponseEntity setStatus(Integer id, String status) throws Exception {
        return null;
    }

    @Override
    public BaseResponseEntity getRandomActiveMerchant() throws Exception {
        return null;
    }

    @Override
    public BaseResponseEntity queryMerchant(String name, String status, String payKey, String paySecret, int pageNum, int pageSize) throws Exception {
        return null;
    }


}
