package com.muse.pay.controller;

import com.muse.common.controller.BaseController;
import com.muse.common.entity.ResultData;
import com.muse.pay.entity.ShoppingCartInfo;
import com.muse.pay.service.ShoppingCartService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by code generator  on 2018/07/25.
 */
@Controller
@RequestMapping("/shoppingCart")
@RequiresAuthentication
public class ShoppingCartController extends BaseController {
    private Logger logger = LoggerFactory.getLogger("ShoppingCartInfoController");

    @Resource
    private ShoppingCartService shoppingCartService;

    @RequestMapping("/add")
    @ResponseBody
    public ResultData add(@Valid ShoppingCartInfo shopCarInfo, Errors errors) {
        // 检查参数
        ResultData resultData = handleErrors(errors);
        if (resultData != null) {
            return resultData;
        }

        try {
            return shoppingCartService.add(shopCarInfo);
        } catch (Exception e) {
            logger.error("添加数据异常=" + e.getMessage());
            return ResultData.getErrResult("添加失败");
        }
    }

    @RequestMapping("/delete")
    public ResultData delete(@RequestParam Integer id) {
        try {
            return shoppingCartService.del(id);
        } catch (Exception e) {
            logger.error("删除数据异常=" + e.getMessage());
            return ResultData.getErrResult("删除失败");
        }
    }

    @RequestMapping("/update")
    public ResultData update(@Valid ShoppingCartInfo shopCarInfo, Errors errors) {
        // 检查参数
        ResultData resultData = handleErrors(errors);
        if (resultData != null) {
            return resultData;
        }

        try {
            return shoppingCartService.update(shopCarInfo);
        } catch (Exception e) {
            logger.error("更新数据异常=" + e.getMessage());
            return ResultData.getErrResult("更新失败");
        }
    }

    @RequestMapping("/list/{userId}")
    @ResponseBody
    public ResultData getUserShopCar(@PathVariable("userId") int userId) {
        try {
            return shoppingCartService.getUserShoppingCart(userId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户购物车异常=" + e.getMessage());
        }

        return ResultData.getErrResult("查询购物车异常");
    }

    @RequestMapping("/count/{userId}")
    @ResponseBody
    public ResultData getCountByUserId(@PathVariable("userId") int userId) {
        int count = 0;
        try {
            Integer countObj = shoppingCartService.getCountByUserId(userId);
            if (countObj != null) {
                count = countObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户购物车中商品数量异常=" + e.getMessage());
        }

        return ResultData.getSuccessResult(count);
    }

    @RequestMapping("/detail/{userId}")
    @RequiresAuthentication
    public String userDetail(@PathVariable("userId") int userId, ModelMap modelMap) {

        try {
            ResultData resultData = shoppingCartService.getUserShoppingCart(userId);

            List<ShoppingCartInfo> showCarItemList = (List<ShoppingCartInfo>) resultData.getData();

            if (showCarItemList==null || showCarItemList.size()==0) {
                modelMap.addAttribute("data", new ArrayList<>());
            } else {
                modelMap.addAttribute("data", showCarItemList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询购物车详情异常=" + e.getMessage());
        }

        return "shoppingCart/index";
    }

    @RequestMapping("/buy")
    @RequiresAuthentication
    public ModelAndView buy(String bookId, String userId, HttpServletRequest request) {

        String orderNo = "";
        try {
            ResultData buyResult = shoppingCartService.buy(userId, bookId);
            if (!buyResult.whetherOk() || buyResult.resultIsEmpty()) {
                return new ModelAndView("redirect:/order/list" + userId);
            }

            orderNo = (String) buyResult.getData();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("购买图书异常=" + e.getMessage());
        }

        request.setAttribute("orderNo", orderNo);
        return new ModelAndView("forward:/orderPay/settlement?orderNo=" + orderNo);
    }


}
