package com.muse.pay.service.impl;

import com.google.common.base.Splitter;
import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.muse.pay.dao.ShoppingCartMapper;
import com.muse.pay.entity.ShoppingCartInfo;
import com.muse.pay.entity.vo.OrderInfoVO;
import com.muse.pay.service.BookInfoService;
import com.muse.pay.service.OrderInfoService;
import com.muse.pay.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by code generator  on 2018/07/25.
 */
@Service("shoppingCartService")
public class ShoppingCartServiceImpl extends BaseService<ShoppingCartMapper, ShoppingCartInfo> implements ShoppingCartService {
    private Logger log = LoggerFactory.getLogger("ShoppingCartServiceImpl");

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private BookInfoService bookInfoService;


    @Override
    public ResultData add(ShoppingCartInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }

        // 检查是否已经添加到购物车中
        ShoppingCartInfo shopCarInfo = mapper.getShoppingCartBy(obj.getUserId(), obj.getBookId());
        if (shopCarInfo != null) {
            ShoppingCartInfo newCar = ShoppingCartInfo.getInstance();
            newCar.setId(shopCarInfo.getId());
            newCar.setUserId(obj.getUserId());
            newCar.setBookId(obj.getBookId());
            newCar.setCount(shopCarInfo.getCount() + obj.getCount());

            mapper.updateByPrimaryKey(newCar);

        } else {
            int result = mapper.insert(obj);
        }

        return ResultData.getSuccessResult();
    }


    @Override
    public ResultData update(ShoppingCartInfo obj) throws Exception {
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

        ShoppingCartInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData getUserShoppingCart(int userId) throws Exception {
        if (userId == 0) {
            return ResultData.getErrResult("用户ID不能为空");
        }

        // 查询用户记录
        List<ShoppingCartInfo> shopCarInfoList = mapper.selectByUserId(userId);

        return ResultData.getSuccessResult(shopCarInfoList);
    }

    @Override
    public int getCountByUserId(int userId) throws Exception {
        Integer count = mapper.getCountByUserId(userId);
        if (count == null) {
            return 0;
        } else {
            return count;
        }
    }

    @Override
    public ResultData getUserDetail(int userId) throws Exception {
        if (userId == 0) {
            return ResultData.getErrResult("参数不能为空");
        }

        List<ShoppingCartInfo> shopCarItemList = mapper.selectByUserId(userId);
        return ResultData.getSuccessResult(shopCarItemList);
    }

    @Override
    public ResultData emptyCart(int userId) throws Exception {
        if (userId == 0) {
            return ResultData.getErrResult("用户ID不能为空");
        }

        mapper.deleteByUserId(userId);

        return ResultData.getSuccessResult("已清空购物车");
    }

    @Override
    public ResultData removeShoppingCartItem(int userId, List<Integer> bookIds) throws Exception {
        // 检查参数
        if (userId == 0) {
            return ResultData.getErrResult("用户ID不能为空");
        }
        if (bookIds == null || bookIds.size() == 0) {
            return ResultData.getErrResult("图书ID列表不能为空");
        }

        // 调用方法，执行业务
        int count = mapper.delByUserIdAndBookIds(userId, bookIds);

        // 返回结果
        return ResultData.getSuccessResult(count);
    }

    @Override
    public ResultData buy(String userId, String bookId) throws Exception {
        // 检查参数
        if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
            return ResultData.getErrResult("用户ID不能为空");
        }
        if (StringUtils.isEmpty(bookId) || "0".equals(bookId)) {
            return ResultData.getErrResult("商品ID不能为空");
        }

        // 多本书
        List<String> bookIdList;
        if (bookId.contains("a")) {
            bookIdList = (List<String>) Splitter.on("a").split(bookId);
            bookIdList = bookIdList.stream().filter(s -> !StringUtils.isEmpty(s)).collect(Collectors.toList());
        } else {
            bookIdList = new ArrayList<>();
            bookIdList.add(bookId);
        }
        log.info("商城生成订单，收到的图书ID为=" + bookId + "，" + userId + "一共购买" + bookIdList.size() + "本书");

        // 查询图书
        for (String id : bookIdList) {
            ResultData bookResult = bookInfoService.get(Integer.valueOf(id));
            if (!bookResult.whetherOk() || bookResult.resultIsEmpty()) {
                return ResultData.getErrResult("该商品不存在[" + bookId + "]");
            }
        }

        // 生成订单
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        orderInfoVO.setUserId(Integer.valueOf(userId));
        // 图书ID
        List<Integer> bookIds = new ArrayList<>();
        // 图书数量
        List<Integer> bookCounts = new ArrayList<>();
        for (String id : bookIdList) {
            bookIds.add(Integer.valueOf(id));
            bookCounts.add(1);
        }
        orderInfoVO.setBookIds(bookIds);
        orderInfoVO.setBookCounts(bookCounts);
        // 保存订单
        ResultData orderResult = orderInfoService.addOrder(orderInfoVO);
        if (!orderResult.whetherOk()) {
            return orderResult;
        }

        // 返回结果
        String orderNo = (String) orderResult.getData();
        if (StringUtils.isEmpty(orderNo)) {
            log.error("创建订单，返回的订单号为空");
            return ResultData.getErrResult("下单异常");
        }
        return ResultData.getSuccessResult((Object) orderNo);
    }

    @Override
    public ResultData recharge(String userId, BigDecimal amount) throws Exception {
        // 检查参数
        if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
            return ResultData.getErrResult("用户ID不能为空");
        }
        if (amount.compareTo(BigDecimal.valueOf(1.00d)) < 0) {
            return ResultData.getErrResult("价格不能小于1.00元");
        }

        // 生成订单
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        orderInfoVO.setUserId(Integer.valueOf(userId));

        // 订单中产品的数量
        List<Integer> bookCounts = new ArrayList<>();
        bookCounts.add(1);
        orderInfoVO.setBookCounts(bookCounts);

        // 订单的产品ID
        List<Integer> bookIds = new ArrayList<>();
        bookIds.add(0);
        orderInfoVO.setBookIds(bookIds);

        // 价格
        orderInfoVO.setAmount(amount);

        // 保存订单
        ResultData orderResult = orderInfoService.addRechargeOrder(orderInfoVO);
        if (!orderResult.isOk()) {
            return orderResult;
        }

        // 返回结果
        String orderNo = (String) orderResult.getData();
        if (StringUtils.isEmpty(orderNo)) {
            log.error("创建订单，返回的订单号为空");
            return ResultData.getErrResult("下单异常");
        }
        return ResultData.getSuccessResult((Object) orderNo);
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
