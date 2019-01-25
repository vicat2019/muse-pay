package com.seckill.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.seckill.dao.ProductInfoMapper;
import com.seckill.dao.SuccessInfoMapper;
import com.seckill.dao.UserInfoMapper;
import com.seckill.entity.ProductInfo;
import com.seckill.entity.SuccessInfo;
import com.seckill.entity.UserInfo;
import com.seckill.service.ProductInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by Vincent on 2018/12/24.
 */
@Service("productInfoService")
public class ProductInfoServiceImpl extends BaseService<ProductInfoMapper, ProductInfo> implements ProductInfoService {
    private Logger log = LoggerFactory.getLogger("ProductInfoServiceImpl");


    @Autowired
    private SuccessInfoMapper successInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    // 核心线程池中线程个数
    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    // 创建线程池
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1,
            10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5000));



    @Override
    public ResultData startByExecutor(String code) throws Exception {
        // N个购买者
        int killerNum = 1000;
        final CountDownLatch latch = new CountDownLatch(killerNum);

        final String killProductId = code;
        log.info("开始秒杀一(会出现超卖)");

        for (int i = 0; i < killerNum; i++) {
            final int userId = (i + 1);
            executor.execute(() -> {
                ResultData result;
                try {
                    result = startSecKill(killProductId, userId);
                    if (result != null) {
                        log.info("用户:{}, {}", userId, result.getMessage());
                    } else {
                        log.info("用户:{}, {}", userId, "人也太多了，请稍后！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });
        }

        try {
            // 等待所有人任务结束
            latch.await();
            int count = successInfoMapper.getCountByCode(code);
            log.info("一共秒杀出{}件商品", count);

            String msg = "产品总数=100，卖出=" + count + ", 超卖=" + (100-count);
            return ResultData.getSuccessResult(msg);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResultData.getErrResult();
    }


    @Override
    public ResultData startSecKill(String code, int userId) throws Exception {
        // 查询产品库存
        ProductInfo productInfo = mapper.getProductByCode(code);
        if (productInfo == null) {
            return ResultData.getErrResult("商品不存在");
        }
        log.info("查询商品信息=" + productInfo.getName() + ", " + productInfo.getCount());

        if (productInfo.getCount() > 0) {
            // 获取用户信息
            UserInfo user = userInfoMapper.selectByPrimaryKey(userId);
            if (user == null) {
                return ResultData.getErrResult("用户不存在");
            }

            // 减库存
            int result = mapper.inventoryDeduction(code);
            log.info("扣除库存, " + productInfo.getName() + ", " + productInfo.getCount() + ", 结果=" + result);

            // 生成记录
            SuccessInfo successInfo = new SuccessInfo();
            successInfo.setUserId(userId);
            successInfo.setUserName(user.getName());
            successInfo.setProductCode(code);
            successInfo.setProductName(productInfo.getName());
            int addResult = successInfoMapper.insert(successInfo);
            log.info("添加抢购成功记录=" + addResult);
        } else {
            log.info("商品库存不足，" + productInfo.getName() + ", " + productInfo.getCount());
            return ResultData.getErrResult("库存不足，code=" + code + ", userId=" + userId);
        }
        return ResultData.getSuccessResult();
    }


    @Override
    public ResultData add(ProductInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(ProductInfo obj) throws Exception {
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
    public ResultData get(Integer id) throws Exception {
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        ProductInfo result = mapper.selectByPrimaryKey(id);
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
