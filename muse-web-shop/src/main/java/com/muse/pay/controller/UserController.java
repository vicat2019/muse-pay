package com.muse.pay.controller;

import com.muse.common.controller.BaseController;
import com.muse.common.entity.ResultData;
import com.muse.pay.entity.UserInfo;
import com.muse.pay.entity.vo.UserInfoVO;
import com.muse.pay.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/24 11 42
 **/
@RequestMapping("/user")
@Controller
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("/to_register")
    public String toRegister() {
        return "user/index";
    }


    @RequestMapping("/register")
    public String register(UserInfoVO user, ModelMap model, Errors errors) {

        // 检查绑定的参数
        ResultData resultData = handleErrors(errors);
        if (resultData != null) {
            model.addAttribute("msg", resultData.getMessage());
            return "user/index";
        }

        // 注册用户
        try {
            userService.register(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户注册异常=" + e.getMessage());
            model.addAttribute("msg", "用户注册异常");
        }

        return "user/index";
    }

    @RequestMapping("/index/{userId}")
    @RequiresAuthentication
    public String userCenter(@PathVariable("userId") int userId, ModelMap modelMap) {

        try {
            ResultData resultData = userService.getUser(userId);
            if (resultData.isOk() && resultData.getData() != null) {
                UserInfo user = (UserInfo) resultData.getData();
                modelMap.addAttribute("user", user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询用户信息异常=" + e.getMessage());
        }

        return "user/center";
    }

    @RequestMapping("/modify/{userId}")
    @RequiresAuthentication
    public String toModify(@PathVariable("userId") int userId, ModelMap modelMap) {

        try {
            ResultData resultData = userService.getUser(userId);
            if (resultData.isOk() && resultData.getData() != null) {
                UserInfo user = (UserInfo) resultData.getData();
                modelMap.addAttribute("muser", user);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询用户信息异常");
        }

        return "user/modify";
    }

    @RequestMapping("doModify")
    @RequiresAuthentication
    public String doModify(UserInfo userInfo, HttpSession session, ModelMap modelMap) {

        try {
            ResultData resultData = userService.updateUser(userInfo, session);
            if (!resultData.isOk()) {
                modelMap.addAttribute("msg", resultData.getMessage());
                return "/user/modify";
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户修改信息异常=" + e.getMessage());
        }

        return "redirect:/user/index/" + userInfo.getId();
    }

    @RequestMapping("/modPwd/{userId}")
    @RequiresAuthentication
    public String toModPwd(@PathVariable("userId") int userId, ModelMap modelMap) {
        modelMap.addAttribute("userId", userId);
        return "user/modPwd";
    }

    @RequestMapping("/doModPwd")
    @RequiresAuthentication
    @ResponseBody
    public ResultData doModPwd(int userId, String newPwd, String oldPwd, String code, String codeType, ModelMap modelMap) {

        try {
            ResultData modPwdResult = userService.modifyPassword(userId, newPwd, oldPwd, code, codeType);
            if (!modPwdResult.isOk()) {
                return modPwdResult;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改密码异常=" + e.getMessage());
        }

        return ResultData.getSuccessResult();
    }

    @RequestMapping("/forgetPwd")
    public String forgetPwd() {
        return "user/forgetPwd";
    }

    @RequestMapping("/resetPwd")
    public String resetPwd(String account, String code, ModelMap modelMap) {

        try {
            ResultData resultData = userService.resetPwd(account, code);
            if (!resultData.isOk()) {
                modelMap.addAttribute("account", account);

                String msg = resultData.getMessage();
                modelMap.addAttribute("msg", msg);
                return "user/forgetPwd";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/";
    }

    @RequestMapping("/to_upload")
    public String iconUpload() {
        return "user/uploadWin";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public ResultData upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        try {
            ResultData resultData = userService.modUserIcon(file, request);
            return resultData;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultData.getErrResult();
    }

    @RequestMapping("/toRecharge")
    public String toRecharge(int userId, ModelMap modelMap) {
        try {
            ResultData resultData = userService.getUser(userId);
            if (resultData.isOk() && resultData.getData() != null) {
                UserInfo user = (UserInfo) resultData.getData();
                modelMap.addAttribute("muser", user);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询用户信息异常");
        }
        return "user/recharge";
    }

    @RequestMapping("/recharge")
    public String recharge(int id, double amount, ModelMap modelMap) {

        try {
            ResultData result = userService.recharge(id, amount);
            if (result.isOk() && result.resultIsNotEmpty()) {
                Map<String, String> data = (Map<String, String>) result.getData();

                String url = data.get("url");
                /*if (!StringUtils.isEmpty(url)) {
                    return "redirect:" + url.toString();
                }*/

                modelMap.addAttribute("url", data.get("url"));
                modelMap.addAttribute("amount", data.get("amount"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "user/middlePay";
    }
































}
