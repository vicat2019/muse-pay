package com.muse.pay.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.muse.common.util.RandomUtils;
import com.muse.common.util.RedisUtil;
import com.muse.pay.dao.MerchantInfoMapper;
import com.muse.pay.entity.MerchantInfo;
import com.muse.pay.service.MerchantInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by code generator  on 2018/09/26.
 */
@Service("merchantInfoService")
@Transactional
public class MerchantInfoServiceImpl extends BaseService<MerchantInfoMapper, MerchantInfo> implements MerchantInfoService {
    private Logger log = LoggerFactory.getLogger("MerchantInfoServiceImpl");

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public ResultData queryMerchant(String name, String status, String payKey, String paySecret, int pageNum, int pageSize) throws Exception {
        if (pageNum == 0) pageNum = 1;
        if (pageSize == 0) pageSize = 20;
        PageHelper.startPage(pageNum, pageSize);

        List<MerchantInfo> orderList = mapper.queryBy(name, status, payKey, paySecret);
        PageInfo<MerchantInfo> pageInfo = new PageInfo<>(orderList);

        return ResultData.getSuccessResult(pageInfo);
    }


    @Override
    public ResultData add(MerchantInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(MerchantInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        if (obj.getId() == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.updateByPrimaryKey(obj);
        if (result > 0) {
            // 如果缓存中缓存的是该商户信息，则更新
            Object cacheObj = redisUtil.get(MerchantInfo.CACHE_KEY);
            MerchantInfo cacheMerchant = (MerchantInfo) cacheObj;
            if (cacheMerchant != null && cacheMerchant.getId() == obj.getId()) {
                redisUtil.set(MerchantInfo.CACHE_KEY, obj, RandomUtils.getRandomNum(5, 10) * 60);
            }
        }

        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData get(Integer id) throws Exception {
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        // 先从缓存中获取
        Object cacheObj = redisUtil.get(MerchantInfo.CACHE_KEY);
        MerchantInfo cacheMerchant;
        if (cacheObj instanceof MerchantInfo) {
            cacheMerchant = (MerchantInfo) cacheObj;
            if (cacheMerchant.getId() == id) {
                return ResultData.getSuccessResult(cacheMerchant);
            }

        } else {
            // 从数据库中获取
            cacheMerchant = mapper.selectByPrimaryKey(id);
        }

        return ResultData.getSuccessResult(cacheMerchant);
    }

    @Override
    public ResultData setStatus(Integer id, String status) throws Exception {
        if (id == 0 || StringUtils.isEmpty(status)) {
            return ResultData.getErrResult("参数不能为空");
        }

        // 同一时间只能有一个商户处于激活状态
        MerchantInfo merchantInfo = mapper.selectByPrimaryKey(id);
        if (merchantInfo == null) {
            return ResultData.getErrResult("要修改的商户不存在[" + id + "]");
        }

        String cacheKey = "ACTIVE_MERCHANT_LIST";
        int count;

        if ("ACTIVE".equals(status)) {
            count = mapper.setStatus(status, id);   // 激活
        } else {
            int activeCount = mapper.getActiveMerchantCount();
            if (activeCount == 1) {
                return ResultData.getErrResult("必须要有一个商户是激活状态");
            }
            count = mapper.setStatus(status, id);   // 冻结
        }

        // 激活/冻结成功
        if (count > 0) {
            List<MerchantInfo> activeMerchant = mapper.getActiveMerchant();
            redisUtil.lSet(cacheKey, activeMerchant);
        }

        return ResultData.getSuccessResult("修改成功");
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        // 不能删除处于激活状态的商户
        MerchantInfo merchant = mapper.selectByPrimaryKey(id);
        if (merchant == null) {
            return ResultData.getErrResult("该商户不存在");
        }
        if ("ACTIVE".equals(merchant.getStatus())) {
            return ResultData.getErrResult("不能删除激活状态的商户");
        }

        // 删除数据库记录
        int result = mapper.deleteByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    /**
     * 随机获取当前激活的商户
     *
     * @return ResultData
     */
    @Override
    public ResultData getRandomActiveMerchant() throws Exception {

        String cacheKey = "ACTIVE_MERCHANT_LIST";
        Random random = new Random();
        MerchantInfo currentMerchant = null;

        // 获取缓存中当前激活的商户账号
        long size = redisUtil.lGetListSize(cacheKey);
        if (size != 0) {
            int randomInt = random.nextInt(100);
            int targetIndex = (int)(randomInt % size);
            if (size == 1) targetIndex = 0;
            Object obj = redisUtil.lGetIndex(cacheKey, targetIndex);
            if (obj instanceof ArrayList) {
                List<MerchantInfo> list = (List<MerchantInfo>) obj;
                currentMerchant = list.get(0);
            } else if (obj instanceof MerchantInfo) {
                currentMerchant = (MerchantInfo) obj;
            }

        } else {
            // 从数据库中查询
            List<MerchantInfo> merchantList = mapper.getActiveMerchant();
            if (merchantList.isEmpty()) {
                return ResultData.getErrResult("没有激活的商户");
            }
            // 设置到缓存中
            redisUtil.lSet(cacheKey, merchantList);
            int index = random.nextInt(10) % merchantList.size();
            currentMerchant = index < merchantList.size() ? merchantList.get(index) : merchantList.get(0);
        }

        log.info("随机获取激活商户对象=" + currentMerchant);
        if (currentMerchant == null) {
            return ResultData.getErrResult("没有激活的商户");
        } else {
            return ResultData.getSuccessResult(currentMerchant);
        }
    }
}
