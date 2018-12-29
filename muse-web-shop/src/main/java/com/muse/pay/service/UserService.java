package com.muse.pay.service;

import com.muse.common.entity.ResultData;
import com.muse.pay.entity.UserInfo;
import com.muse.pay.entity.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/22 11 20
 **/
public interface UserService {

    /**
     * 查询用户
     *
     * @param id 用户ID
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData<UserInfo> getUser(int id) throws Exception;

    /**
     * 添加用户
     *
     * @param userInfo 用户信息
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData addUser(UserInfo userInfo) throws Exception;

    /**
     * 修改用户
     *
     * @param userInfo 用户信息
     * @param session  会话
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData updateUser(UserInfo userInfo, HttpSession session) throws Exception;


    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData register(UserInfoVO user) throws Exception;

    /**
     * 登录
     *
     * @param email    账号
     * @param password 密码
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData login(String email, String password, HttpSession session);

    /**
     * 根据条件查询用户
     *
     * @param userInfo 查询条件
     * @return ResultData
     * @throws Exception
     */
    ResultData getUserBy(UserInfo userInfo) throws Exception;

    /**
     * 修改用户密码
     *
     * @param userId   用户ID
     * @param newPwd   新密码
     * @param oldPwd   旧密码
     * @param code     验证码
     * @param codeType 验证码类型
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData modifyPassword(int userId, String newPwd, String oldPwd, String code, String codeType) throws Exception;

    /**
     * 返回所有用户信息
     *
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData getAllUser() throws Exception;

    /**
     * 用随机生成的信息去更新用户信息
     *
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData updateRandomUserInfo() throws Exception;

    /**
     * 根据名称获取用户ID
     *
     * @param name 名称
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData getUserIdByName(String name) throws Exception;

    /**
     * 忘记密码
     *
     * @param account 账号
     * @param code    验证码
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData resetPwd(String account, String code) throws Exception;

    /**
     * 修改用户头像
     *
     * @param file    头像文件
     * @param request 请求信息
     * @return ResutlData
     * @throws Exception 异常
     */
    ResultData modUserIcon(MultipartFile file, HttpServletRequest request) throws Exception;

    /**
     * 用户充值
     *
     * @param id     用户ID
     * @param amount 充值金额
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData recharge(int id, double amount) throws Exception;

    /**
     * 修改用户余额
     *
     * @param userId  用户Id
     * @param amount  金额
     * @param session 会话
     * @return ResultData
     * @throws Exception 异常
     */
    ResultData modifyUserAmount(int userId, BigDecimal amount, HttpSession session) throws Exception;

}
