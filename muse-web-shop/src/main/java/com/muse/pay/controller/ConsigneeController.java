package com.muse.pay.controller;

import com.muse.common.controller.BaseController;
import com.muse.common.entity.ResultData;
import com.muse.pay.entity.ConsigneeInfo;
import com.muse.pay.entity.UserInfo;
import com.muse.pay.service.ConsigneeInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/consignee")
public class ConsigneeController extends BaseController {
    private Logger log = LoggerFactory.getLogger("ConsigneeController");

    @Autowired
    private ConsigneeInfoService consigneeInfoService;

    @RequestMapping("/list")
    public String query(int userId, ModelMap modelMap) {

        try {
            ResultData resultData = consigneeInfoService.query(new ConsigneeInfo(userId));
            if (!resultData.isOk() || resultData.resultIsEmpty()) {
                modelMap.addAttribute("errMsg", resultData.getMessage());
            }

            List<ConsigneeInfo> consigneeList = (List<ConsigneeInfo>) resultData.getData();
            modelMap.addAttribute("data", consigneeList);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询常用联系人列表异常=" + e.getMessage());
        }

        return "consignee/index";
    }

    @RequestMapping("/addUI")
    public String addUI() {
        return "consignee/add";
    }

    @RequestMapping("/add")
    public String add(ConsigneeInfo consigneeInfo, ModelMap modelMap) {

        try {
            ResultData resultData = consigneeInfoService.add(consigneeInfo);
            if (!resultData.isOk()) {
                modelMap.addAttribute("errMsg", resultData.getMessage());
                return "consignee/edit";
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加常用联系人异常=" + e.getMessage());
        }

        return "redirect:/consignee/list?userId=" + consigneeInfo.getUserId();
    }

    @RequestMapping("/editUI/{id}")
    public String editUI(@PathVariable("id") int id, ModelMap modelMap) {
        try {
            ResultData resultData = consigneeInfoService.get(id);
            if (!resultData.isOk() || resultData.resultIsEmpty()) {
                UserInfo user = (UserInfo) this.getUserFromSession();
                if (user != null) {
                    modelMap.addAttribute("errMsg", resultData.getMessage());
                    return "redirect:/consignee/list?userId=" + user.getId();
                }
            }

            ConsigneeInfo consigneeInfo = (ConsigneeInfo) resultData.getData();
            modelMap.addAttribute("data", consigneeInfo);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取常用联系人异常=" + e.getMessage());
        }

        return "consignee/edit";
    }

    @RequestMapping("/edit")
    public String edit(ConsigneeInfo consigneeInfo, ModelMap modelMap) {

        try {
            ResultData resultData = consigneeInfoService.update(consigneeInfo);
            if (! resultData.isOk()) {
                modelMap.addAttribute("errMsg", resultData.getMessage());
                return "consignee/edit";
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改常用联系人异常=" + e.getMessage());
        }

        return "redirect:/consignee/list?userId=" + consigneeInfo.getUserId();
    }

    @RequestMapping("/del/{id}")
    @ResponseBody
    public ResultData del(@PathVariable("id") int id) {

        try {
            return consigneeInfoService.del(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除常用联系人异常=" + e.getMessage());
        }

        return ResultData.getErrResult("删除异常");
    }


}
