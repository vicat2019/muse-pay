package com.muse.pay.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.entity.ResultStatusEnum;
import com.muse.common.service.BaseService;
import com.muse.common.util.*;
import com.muse.pay.dao.UserInfoDao;
import com.muse.pay.entity.CodeInfo;
import com.muse.pay.entity.UserInfo;
import com.muse.pay.entity.common.VerifyCodeTypeEnum;
import com.muse.pay.entity.vo.UserInfoVO;
import com.muse.pay.service.CodeInfoService;
import com.muse.pay.service.OrderPayService;
import com.muse.pay.service.ShoppingCartService;
import com.muse.pay.service.UserService;
import com.muse.pay.util.GenRandomPersonInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/22 11 21
 **/
@Service("userService")
public class UserServiceImpl extends BaseService<UserInfoDao, CodeInfo> implements UserService {
    private Logger log = LoggerFactory.getLogger("UserServiceImpl");

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CodeInfoService codeService;

    @Autowired
    private EmailUtils emailUtils;

    @Value("{upload.path}")
    private String path;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderPayService orderPayService;


    public ResultData<UserInfo> getUser(int id) throws Exception {
        log.info("查询用户，参数Id=" + id);

        // 先从Redis中取
        Object obj = redisUtil.get(UserInfo.REDIS_USER_KEY_PREFIX + id);
        if (obj != null) {
            return ResultData.getSuccessResult((UserInfo) obj);
        }

        UserInfo userInfo = mapper.getUserById(id);
        if (userInfo == null) {
            return ResultData.getErrResult("要查询的用户不存在");
        }

        redisUtil.set(UserInfo.REDIS_USER_KEY_PREFIX + id, userInfo, RandomUtils.getRandomNum(5, 10)*60);
        return ResultData.getSuccessResult(userInfo);
    }

    /**
     * 添加用户
     *
     * @param userInfo
     * @return
     * @throws Exception
     */
    public ResultData addUser(UserInfo userInfo) throws Exception {
        if (userInfo == null) {
            return ResultData.getErrResult("用户参数不能为空");
        }

        mapper.insertUser(userInfo);

        return ResultData.getDefaultResult(ResultStatusEnum.SUCCESS.getValue(), "添加成功");
    }

    /**
     * 修改用户
     *
     * @param userInfo
     * @return
     * @throws Exception
     */
    public ResultData updateUser(UserInfo userInfo, HttpSession session) throws Exception {
        if (userInfo == null) {
            return ResultData.getErrResult("参数不能为空");
        }

        // 检查主要参数
        if (userInfo.getId() == 0) {
            return ResultData.getErrResult("参数ID不能为空");
        }

        // 检查用户是否存在
        UserInfo targetUser = mapper.getUserById(userInfo.getId());
        if (targetUser == null) {
            return ResultData.getErrResult("用户不存在");
        }

        // 修改对应属性
        // 名称
        if (StringUtils.isEmpty(userInfo.getUserName())) {
            userInfo.setUserName(targetUser.getUserName());
        }
        // 性别
        if (StringUtils.isEmpty(userInfo.getSex())) {
            userInfo.setSex(targetUser.getSex());
        }
        // 地址
        if (StringUtils.isEmpty(userInfo.getAddress())) {
            userInfo.setAddress(targetUser.getAddress());
        }
        // 密码
        if (StringUtils.isEmpty(userInfo.getPassword())) {
            userInfo.setPassword(targetUser.getPassword());
        }
        // 年龄
        if (userInfo.getAge() == 0) {
            userInfo.setAge(targetUser.getAge());
        }
        mapper.updateUser(userInfo);

        // 清空Redis中该用户的缓存
        redisUtil.del(UserInfo.REDIS_USER_KEY_PREFIX + userInfo.getId());
        // session中的对象
        session.setAttribute("user", userInfo);

        return ResultData.getDefaultResult(ResultStatusEnum.SUCCESS.getValue(), "修改成功");
    }

    @Override
    public ResultData register(UserInfoVO userVO) throws Exception {
        // 比较密码是否正确
        if (!userVO.getUserInfo().getPassword().equals(userVO.getConfirmPassword())) {
            return ResultData.getErrResult("两次输入密码不正确，请重新输入");
        }

        // 检查验证码
        ResultData verifyCodeResult = codeService.verifyCode(userVO.getUserInfo().getEmail(), userVO.getCode(), userVO.getCodeType());
        if (!verifyCodeResult.isOk()) {
            return verifyCodeResult;
        }

        // 注册
        mapper.insertUser(userVO.getUserInfo());

        // 修改注册码状态
        return ResultData.getSuccessResult("注册成功");
    }

    @Override
    public ResultData login(String email, String password, HttpSession session) {
        // 检查参数
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            return ResultData.getErrResult("登录参数不能为空");
        }

        UsernamePasswordToken token = new UsernamePasswordToken(email, password, false);
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(token);

        } catch (UnknownAccountException uae) {
            uae.printStackTrace();
            return ResultData.getErrResult("未知账户");
        } catch (IncorrectCredentialsException ice) {
            return ResultData.getErrResult("密码不正确");
        } catch (LockedAccountException lae) {
            return ResultData.getErrResult("账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            return ResultData.getErrResult("用户名或密码错误次数过多");
        } catch (AuthenticationException ae) {
            ae.printStackTrace();
            return ResultData.getErrResult("用户名或密码不正确");
        }

        // 登录
        UserInfo user = mapper.login(email, password);
        if (user == null) {
            return ResultData.getErrResult("账号或者密码不正确");
        }
        session.setAttribute("user", user);

        return ResultData.getSuccessResult();
    }

    @Override
    public ResultData getUserBy(UserInfo userInfo) throws Exception {
        if (userInfo == null) {
            return ResultData.getErrResult("参数不能为空");
        }

        UserInfo targetUser = mapper.selectUserByColumn(userInfo);
        if (targetUser == null) {
            return ResultData.getErrResult("没有查询到用户信息");
        }

        return ResultData.getSuccessResult(targetUser);
    }

    @Override
    public ResultData modifyPassword(int userId, String newPwd, String oldPwd, String code, String codeType) throws Exception {
        // 检查参数
        if (org.thymeleaf.util.StringUtils.isEmpty(newPwd)) {
            return ResultData.getErrResult("新密码不能为空");
        }
        if (org.thymeleaf.util.StringUtils.isEmpty(oldPwd)) {
            return ResultData.getErrResult("旧密码不能为空");
        }
        if (org.thymeleaf.util.StringUtils.isEmpty(code)) {
            return ResultData.getErrResult("验证码不能为空");
        }

        // 获取用户
        UserInfo userInfo = mapper.getUserById(userId);
        if (userInfo == null) {
            log.error("用户不存在[" + userId + "]");
            return ResultData.getErrResult("用户不存在");
        }

        // 检查验证码
        ResultData codeResult = codeService.verifyCode(userInfo.getEmail(), code, codeType);
        log.info("校验验证码结果=" + codeResult.toString());
        if (!codeResult.isOk()) {
            return codeResult;
        }

        // 检查密码是否正确
        String hashAlgorithmName = "MD5";
        int hashIterations = 1024;
        SimpleHash simpleHash = new SimpleHash(hashAlgorithmName, oldPwd, "", hashIterations);
        log.info("加密后的密码=" + simpleHash.toHex());

        // 检查密码是否正确
        if (!oldPwd.equals(userInfo.getPassword())) {
            log.error("输入密码不正确");
            return ResultData.getErrResult("输入的密码不正确");
        }

        // 修改密码
        mapper.updatePassword(userId, newPwd);
        log.info("成功修改用户密码");

        return ResultData.getSuccessResult();
    }

    @Override
    public ResultData getAllUser() throws Exception {
        List<UserInfo> userList = mapper.getAllUser();
        return ResultData.getSuccessResult(userList);
    }

    @Override
    public ResultData updateRandomUserInfo() throws Exception {
        // 获取用户列表
        try {
            List<UserInfo> userList = mapper.getAllUser();
            if (userList == null || userList.size() == 0) {
                return ResultData.getErrResult("用户列表为空");
            }

            for (UserInfo user : userList) {
                if (user.getId() == 603) {
                    continue;
                }
                Map<String, Object> personInfo = GenRandomPersonInfo.getPersonInfo();
                user.setSex((String) personInfo.get("sex"));
                user.setUserName((String) personInfo.get("name"));
                user.setAddress((String) personInfo.get("road"));
                user.setEmail((String) personInfo.get("email"));

                mapper.updateUser(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("批量更新所有用户信息，异常=" + e.getMessage());
        }

        return ResultData.getSuccessResult();
    }

    @Override
    public ResultData getUserIdByName(String name) throws Exception {
        if (StringUtils.isEmpty(name)) {
            return ResultData.getErrResult("用户名称为空");
        }

        List<Integer> ids = mapper.getUserIdByName(name);

        return ResultData.getSuccessResult(ids);
    }

    @Override
    public ResultData resetPwd(String account, String code) throws Exception {
        if (StringUtils.isEmpty(account)) {
            log.error("忘记密码，账号参数为空");
            return ResultData.getErrResult("账号不能为空");
        }
        if (StringUtils.isEmpty(code)) {
            log.error("忘记密码，验证码参数为空");
            return ResultData.getErrResult("验证码不能为空");
        }

        // 检查账号是否存在
        Integer count = mapper.getUserCountByEmail(account);
        if (count == null || count == 0) {
            log.error("忘记密码，该账号不存在：" + account);
            return ResultData.getErrResult("该账号不存在");
        }

        // 检查验证码
        ResultData codeResult = codeService.verifyCode(account, code, VerifyCodeTypeEnum.FORGET_PASSWORD.name());
        if (!codeResult.isOk()) {
            return codeResult;
        }

        // 生成随机密码
        String newPwd = TextUtils.getRandomStr(12);
        mapper.setPwd(account, newPwd);

        // 将新密码通过邮件发送给用户
        emailUtils.sendEmail(account, "忘记密码", "您的新密码为：" + newPwd + "，" + DateUtils.getTimestamp() + "。");

        return ResultData.getSuccessResult();
    }

    @Override
    public ResultData modUserIcon(MultipartFile file, HttpServletRequest request) throws Exception {
        // ID
        int userId = 0;
        String userIdStr = request.getParameter("userId");
        log.info("修改用户头像，用户ID=" + userIdStr);
        if (!StringUtils.isEmpty(userIdStr)) {
            userId = Integer.parseInt(userIdStr);
        }

        // 重新生成唯一文件名，用于存储数据库
        String newFileName = UUID.randomUUID().toString() + ".jpg";
        log.info("修改用户头像，休想文件名称=" + newFileName);

        //创建文件
        File dest = new File(path + File.separator + newFileName);

        try {
            file.transferTo(dest);
            String iconUrl = "http://localhost:8088/" + newFileName;
            mapper.modUserIcon(userId, iconUrl);
            redisUtil.del(UserInfo.REDIS_USER_KEY_PREFIX + userId);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("修改用户头像，异常=" + e.getMessage());
        }

        return ResultData.getSuccessResult();
    }

    @Override
    public ResultData recharge(int id, double amount) throws Exception {
        if (id == 0 || amount < 1) {
            return ResultData.getErrResult("参数异常ID=" + id + ", amount=" + amount);
        }

        // 检查用户是否存在
        UserInfo user = mapper.getUserById(id);
        if (user == null) {
            return ResultData.getErrResult("该用户不存在ID=" + id);
        }

        // 生成订单
        long orderStart = System.currentTimeMillis();
        ResultData buyResult = shoppingCartService.recharge(String.valueOf(user.getId()), BigDecimal.valueOf(amount));
        if (!buyResult.isOk() || buyResult.resultIsEmpty()) {
            log.info("concurrency() 商城生成订单，STS耗时=" + ((System.currentTimeMillis() - orderStart) / 1000d));
            return buyResult;
        }
        // 订单号
        String orderNo = (String) buyResult.getData();
        if (StringUtils.isEmpty(orderNo)) {
            log.info("concurrency() 商城生成订单耗时=" + ((System.currentTimeMillis() - orderStart) / 1000d));
            return ResultData.getErrResult("返回的订单编号为空，用户ID[" + id + "]，充值失败。");
        }
        log.info("concurrency() 1-商城生成订单，STS耗时=" + ((System.currentTimeMillis() - orderStart) / 1000d));

        // 预下单
        ResultData resultData = orderPayService.doRechargePay(id, orderNo);
        if (!resultData.isOk() || resultData.resultIsEmpty()) {
            return resultData;
        }
        Map<String, String> resultMap = (Map<String, String>) resultData.getData();
        String url = resultMap.get("payMessage");
        if (StringUtils.isEmpty(url)) {
            return ResultData.getErrResult("预下单，返回的二维码为空，订单号[" + orderNo + "]");
        }

        Map<String, String> result = new HashMap<>();
        result.put("url", "http://www.baidu.com");
        result.put("amount", String.valueOf(amount));

        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData modifyUserAmount(int userId, BigDecimal amount, HttpSession session) throws Exception {
        if (userId == 0 || amount.compareTo(BigDecimal.valueOf(1))<0) {
            return ResultData.getErrResult("参数异常ID=" + userId + ", amount=" + amount);
        }

        int result = mapper.modifyUserAmount(userId, amount);
        if (result > 0) {

            // 清空Redis中该用户的缓存
            redisUtil.del(UserInfo.REDIS_USER_KEY_PREFIX + userId);

            // session中的对象
            UserInfo userInfo = mapper.getUserById(userId);
            session.setAttribute("user", userInfo);

            return ResultData.getSuccessResult("修改成功");
        } else {
            return ResultData.getErrResult("修改失败");
        }
    }























}
