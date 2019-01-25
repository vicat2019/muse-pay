package com.muse.pay.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.muse.common.util.RedisUtil;
import com.muse.common.util.TextUtils;
import com.muse.pay.dao.OrderInfoMapper;
import com.muse.pay.entity.BookInfo;
import com.muse.pay.entity.OrderInfo;
import com.muse.pay.entity.OrderItemInfo;
import com.muse.pay.entity.vo.OrderInfoVO;
import com.muse.pay.service.BookInfoService;
import com.muse.pay.service.OrderInfoService;
import com.muse.pay.service.OrderItemInfoService;
import com.muse.pay.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by code generator  on 2018/07/26.
 */
@Service("orderInfoService")
public class OrderInfoServiceImpl extends BaseService<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    private Logger log = LoggerFactory.getLogger("OrderInfoServiceImpl");

    // 订单在Redis缓存中的有效期
    private static int ORDER_EXPIRE_TIME = 60;

    @Autowired
    private BookInfoService bookInfoService;

    @Autowired
    private OrderItemInfoService orderItemInfoService;

    @Autowired
    private ShoppingCartService shopCarInfoService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 生成订单
     *
     * @param orderInfoVO 订单信息
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    @Transactional
    public ResultData add(OrderInfoVO orderInfoVO) throws Exception {
        if (orderInfoVO == null) {
            log.error("生成订单，参数为空");
            return ResultData.getErrResult("参数不能为空");
        }
        log.info("生成订单，收到的参数=" + orderInfoVO.toString());

        // 检查商品ID和商品数量
        if (orderInfoVO.getBookIds() == null || orderInfoVO.getBookCounts().size() == 0) {
            return ResultData.getErrResult("选择的商品不能为空");
        }
        if (orderInfoVO.getBookCounts() == null || orderInfoVO.getBookCounts().size() == 0) {
            return ResultData.getErrResult("选择的商品数量不能为空");
        }
        if (orderInfoVO.getBookIds().size() != orderInfoVO.getBookCounts().size()) {
            return ResultData.getErrResult("商品参数异常");
        }

        // 生成并保存订单
        ResultData orderResult = addOrder(orderInfoVO);
        if (!orderResult.isOk()) {
            return orderResult;
        }

        // 删除购物车中已下单的商品
        ResultData delItemResultData = shopCarInfoService.removeShoppingCartItem(orderInfoVO.getUserId(), orderInfoVO.getBookIds());
        if (!delItemResultData.isOk()) {
            return delItemResultData;
        }

        return ResultData.getSuccessResult();
    }

    /**
     * 创建并保存订单
     *
     * @param orderInfoVO 订单参数
     * @return
     * @throws Exception
     */
    @Transactional
    public ResultData addOrder(OrderInfoVO orderInfoVO) throws Exception {

        // 查询商品信息
        ResultData resultData = bookInfoService.queryBookIn(orderInfoVO.getBookIds());
        if (!resultData.isOk() || resultData.getData() == null) {
            return resultData;
        }
        List<BookInfo> bookList = (List<BookInfo>) resultData.getData();

        // 生成订单对象
        OrderInfo orderInfo = new OrderInfo();
        // 用户ID
        orderInfo.setUserId(orderInfoVO.getUserId());
        // 订单号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String orderNo = sdf.format(new Date()) + TextUtils.getNumRandomStr(10);
        orderInfo.setOrderNo(orderNo);
        // 计算总价格
        BigDecimal total = BigDecimal.ZERO;
        int totalCount = 0;
        for (int i = 0; i < bookList.size(); i++) {
            BookInfo item = bookList.get(i);

            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(item.getPrice().trim()));
            BigDecimal itemCount = BigDecimal.valueOf(orderInfoVO.getBookCounts().get(i));
            total = total.add(price.multiply(itemCount));
            totalCount += itemCount.intValue();
        }
        orderInfo.setAmount(total.setScale(2,BigDecimal.ROUND_HALF_UP));
        orderInfo.setItemCount(totalCount);
        log.info("生成订单信息=" + orderInfo.toString());

        // 订单类型-充值
        orderInfo.setOrderType(OrderInfo.ORDER_TYPE_NORMAL);

        // 保存订单主信息
        mapper.insert(orderInfo);
        // 保存订单项
        List<OrderItemInfo> orderItemList = new ArrayList<>();
        for (int i = 0; i < bookList.size(); i++) {
            BookInfo item = bookList.get(i);
            OrderItemInfo orderItem = new OrderItemInfo();

            orderItem.setOrderNo(orderNo);
            orderItem.setCount(orderInfoVO.getBookCounts().get(i));
            orderItem.setValueFrom(item);
            orderItemList.add(orderItem);
        }
        ResultData addBatchResult = orderItemInfoService.insertByBatch(orderItemList);
        if (!addBatchResult.isOk()) {
            return addBatchResult;
        }
        orderInfo.setItemList(orderItemList);

        // 将订单放到缓存中 60秒过期
        redisUtil.set("ORDER_" + orderInfo.getOrderNo(), orderInfo, ORDER_EXPIRE_TIME);

        // 返回结果
        return ResultData.getSuccessResult((Object) orderInfo.getOrderNo());
    }


    /**
     * 创建并保存订单
     *
     * @param orderInfoVO 订单参数
     * @return
     * @throws Exception
     */
    @Transactional
    public ResultData addRechargeOrder(OrderInfoVO orderInfoVO) throws Exception {

        // 生成订单对象
        OrderInfo orderInfo = new OrderInfo();

        // 用户ID
        orderInfo.setUserId(orderInfoVO.getUserId());

        // 订单号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String orderNo = sdf.format(new Date()) + TextUtils.getNumRandomStr(10);
        orderInfo.setOrderNo(orderNo);

        // 计算总价格
        orderInfo.setAmount(orderInfoVO.getAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
        orderInfo.setItemCount(1);

        // 订单类型-充值
        orderInfo.setOrderType(OrderInfo.ORDER_TYPE_RECHARGE);

        // 保存订单主信息
        mapper.insert(orderInfo);

        // 保存订单项
        OrderItemInfo orderItemInfo = new OrderItemInfo();
        orderItemInfo.valueFrom(orderInfo);
        List<OrderItemInfo> orderItemList = new ArrayList<>();
        orderItemList.add(orderItemInfo);
        ResultData addBatchResult = orderItemInfoService.insertByBatch(orderItemList);
        if (!addBatchResult.isOk()) {
            return addBatchResult;
        }
        orderInfo.setItemList(orderItemList);

        // 将订单放到缓存中 60秒过期
        redisUtil.set("ORDER_" + orderInfo.getOrderNo(), orderInfo, ORDER_EXPIRE_TIME);

        // 返回结果
        return ResultData.getSuccessResult((Object) orderInfo.getOrderNo());
    }

    @Override
    public ResultData<PageInfo<OrderInfo>> getOrderByCondition(List<Integer> ids, String startTime, String endTime, int status,
                                                               int pageNum, int pageSize) throws Exception {
        if (pageNum == 0) pageNum = 1;
        if (pageSize == 0) pageSize = 20;
        PageHelper.startPage(pageNum, pageSize);

        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("status", status);

        List<OrderInfo> orderList = mapper.getOrderByCondition(params);
        PageInfo<OrderInfo> pageInfo = new PageInfo<>(orderList);

        return ResultData.getSuccessResult(pageInfo);
    }

    @Override
    public ResultData updateOrderByNo(OrderInfo orderInfo) throws Exception {
        if (orderInfo == null || StringUtils.isEmpty(orderInfo.getOrderNo())) {
            return ResultData.getErrResult("修改订单信息，参数不能为空");
        }

        mapper.updateByOrderNo(orderInfo);

        return ResultData.getSuccessResult();
    }

    @Override
    public ResultData update(OrderInfo obj) throws Exception {
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

        OrderInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData getUserOrders(int userId, String startTime, String endTime, int status, int pageNum, int pageSize) throws Exception {
        if (userId == 0) {
            return ResultData.getErrResult("用户ID不能为空");
        }

        if (pageNum == 0) pageNum = 1;
        if (pageSize == 0) pageSize = 20;

        PageHelper.startPage(pageNum, pageSize);

        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("status", status);
        params.put("id", userId);
        List<OrderInfo> orderList = mapper.getOrderByCondition(params);

        PageInfo<OrderInfo> pageInfo = new PageInfo<>(orderList);

        return ResultData.getSuccessResult(pageInfo);
    }

    @Override
    public ResultData getUserOrderCount(int userId) throws Exception {
        if (userId == 0) {
            return ResultData.getErrResult("用户ID不能为空");
        }

        Integer count = mapper.getCountByUserId(userId);
        if (count == null) {
            count = 0;
        }
        return ResultData.getSuccessResult(count);
    }

    public ResultData<OrderInfo> getOrderByNo(String orderNo) throws Exception {
        if (StringUtils.isEmpty(orderNo)) {
            return ResultData.getErrResult("参数异常");
        }

        OrderInfo orderInfo = mapper.selectByNo(orderNo);
        if (orderInfo == null) {
            return ResultData.getErrResult("订单不存在");
        }

        return ResultData.getSuccessResult(orderInfo);
    }

    @Override
    public ResultData setOrderStatus(String orderNo, int status) throws Exception {
        if (StringUtils.isEmpty(orderNo)) {
            return ResultData.getErrResult("订单编号为空");
        }

        mapper.updateOrderStatus(status, orderNo);

        return ResultData.getSuccessResult();
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
