package com.muse.pay.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.muse.common.util.RedisUtil;
import com.muse.pay.dao.MuseMerchantInfoMapper;
import com.muse.pay.entity.MuseMerchantInfo;
import com.muse.pay.service.MuseMerchantInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;


/**
 * MUSE支付商户服务实现类
 * Created by code generator  on 2018/08/08.
 */
@Service("payMerchantInfoService")
@Transactional
public class MuseMerchantInfoServiceImpl extends BaseService<MuseMerchantInfoMapper, MuseMerchantInfo> implements MuseMerchantInfoService {
    private Logger log = LoggerFactory.getLogger("MuseMerchantInfoServiceImpl");

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ResultData add(MuseMerchantInfo obj) throws Exception {
        if(obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(MuseMerchantInfo obj) throws Exception {
        if(obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        if(obj.getId() == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.updateByPrimaryKey(obj);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData get(Integer id) throws Exception {
        if(id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        MuseMerchantInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData getByUserNo(String userNo) throws Exception {
        if (StringUtils.isEmpty(userNo)) {
            return ResultData.getErrResult("参数不能为空");
        }

        MuseMerchantInfo merchant = mapper.selectByUserNo(userNo);
        if (merchant == null) {
            return ResultData.getErrResult("该商户不存在");
        }

        return ResultData.getSuccessResult(merchant);
    }

    @Override
    public ResultData getSecretByUserNo(String userNo) throws Exception {
        if (StringUtils.isEmpty(userNo)) {
            return ResultData.getErrResult("参数不能为空");
        }

        // 先从缓存中获取数据
        String secret = "";
        String redisSecretKey = "SECRET_" + userNo;
        Object obj = redisUtil.get(redisSecretKey);
        if (obj != null) {
            secret = (String) obj;
        }
        if (StringUtils.isEmpty(secret)) {
            secret = mapper.getSecretByUserNo(userNo);
        }
        if (StringUtils.isEmpty(secret)) {
            return ResultData.getErrResult("该编号对应的商户不存在");
        } else {
            redisUtil.set(redisSecretKey, secret, 5*60);
        }

        return ResultData.getSuccessResult((Object) secret);
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        if(id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.deleteByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData list(MuseMerchantInfo merchantInfo, int pageNum) throws Exception {
        PageHelper.startPage(pageNum, 10);

        List<MuseMerchantInfo> bookList = mapper.selectByColumn(merchantInfo);
        PageInfo<MuseMerchantInfo> pageInfo = new PageInfo<MuseMerchantInfo>(bookList);

        log.info("分页查询数据，返回记录数=" + bookList.size());
        return ResultData.getSuccessResult(pageInfo);
    }
}
