package com.muse.common.controller;

import com.muse.common.entity.BaseEntityInfo;
import com.muse.common.entity.ResultData;
import com.muse.common.entity.ResultStatusEnum;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/22 16 06
 **/
public class BaseController {
    protected static Logger log = LoggerFactory.getLogger("BaseController");

    /**
     * 处理数据绑定的异常信息
     *
     * @param errors 异常对象列表
     * @return ResultData
     */
    protected ResultData handleErrors(Errors errors) {
        ResultData resultData = null;
        StringBuilder errMsg = new StringBuilder();

        List<ObjectError> errList = errors.getAllErrors();
        if (errList.size() > 0) {
            for (ObjectError err : errList) {
                errMsg.append(err.getDefaultMessage());
                errMsg.append(";");
            }
            if (errMsg.length() > 0) {
                errMsg.delete(errMsg.length() - 1, errMsg.length());
            }
            resultData = ResultData.getDefaultResult(ResultStatusEnum.ERR.getValue(), errMsg.toString());
        }

        return resultData;
    }

    /**
     * 账号异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    public String authHandler(AuthorizationException exception) {
        log.info("进去鉴权异常处理方法 = " + exception.getLocalizedMessage());
        return "redirect:/login";
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return UserInfo
     */
    public BaseEntityInfo getUserFromSession() {
        Session session = SecurityUtils.getSubject().getSession();
        if (session != null) {
            Object obj = session.getAttribute("user");
            if (obj != null) {
                return (BaseEntityInfo) obj;
            }
        }

        return null;
    }


}
