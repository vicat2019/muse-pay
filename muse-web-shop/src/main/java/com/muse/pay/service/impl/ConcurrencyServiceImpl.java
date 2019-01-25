package com.muse.pay.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.util.HttpUtils;
import com.muse.pay.service.ConcurrencyService;
import com.muse.pay.service.OrderPayService;
import com.muse.pay.service.ShoppingCartService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 并发测试服务类
 */
@Service("concurrencyService")
public class ConcurrencyServiceImpl implements ConcurrencyService {
    private Logger log = LoggerFactory.getLogger("ConcurrencyServiceImpl");

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderPayService orderPayService;

    @Autowired
    private HttpUtils httpUtils;

    /**
     * 并发测试
     *
     * @param userId  用户ID
     * @param bookIds 图书ID
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData concurrency(String userId, String bookIds) throws Exception {
        // 购买
        // http://192.168.0.90:9091/shoppingCart/buy?userId=603&bookId=473&count=1
        long orderStart = System.currentTimeMillis();
        ResultData buyResult = shoppingCartService.buy(userId, bookIds);
        if (!buyResult.isOk() || buyResult.resultIsEmpty()) {
            log.info("concurrency() 商城生成订单，STS耗时=" + ((System.currentTimeMillis() - orderStart) / 1000d));
            return buyResult;
        }
        // 订单号
        String orderNo = (String) buyResult.getData();
        if (StringUtils.isEmpty(orderNo)) {
            log.info("concurrency() 商城生成订单耗时=" + ((System.currentTimeMillis() - orderStart) / 1000d));
            return ResultData.getErrResult("返回的订单编号为空，用户ID[" + userId + "]，图书ID[" + bookIds + "]");
        }
        log.info("concurrency() 1-商城生成订单，STS耗时=" + ((System.currentTimeMillis() - orderStart) / 1000d));

        // 预下单
        // http://192.168.0.90:9091/orderPay/pay?userId=603&orderNo=201808281900563520040172915
        ResultData resultData = orderPayService.doPay(Integer.valueOf(userId), orderNo);
        if (!resultData.isOk() || resultData.resultIsEmpty()) {
            return resultData;
        }
        Map<String, String> resultMap = (Map<String, String>) resultData.getData();
        String url = resultMap.get("payMessage");
        if (StringUtils.isEmpty(url)) {
            return ResultData.getErrResult("预下单，返回的二维码为空，订单号[" + orderNo + "]");
        }

        // 支付
        // url=http://192.168.0.90:9091/muse/pay/MUSE20180828190140292258725
        long start = System.currentTimeMillis();
        ResultData payResult = httpUtils.get(url, new HashMap<>());
        if (!payResult.isOk() || payResult.resultIsEmpty()) {
            log.info("concurrency() 3-预下单后，调用支付接口=" + url + "，返回=" + payResult.getMessage() + "，STM耗时="
                    + ((System.currentTimeMillis() - start) / 1000d));
            log.info("concurrency() 0-并发测试，一个订单，CONCURRENCY耗时=" + ((System.currentTimeMillis() - orderStart) / 1000d));
            return payResult;
        }

        String content = (String) payResult.getData();
        log.info("concurrency() 3-预下单后，调用支付接口=" + url + "，返回=" + content + "，STM耗时=" + ((System.currentTimeMillis() - start) / 1000d));
        log.info("concurrency() 0-并发测试，一个订单，CONCURRENCY耗时=" + ((System.currentTimeMillis() - orderStart) / 1000d));

        return ResultData.getSuccessResult((Object) content);
    }


}
