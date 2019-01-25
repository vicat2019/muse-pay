package com.muse.pay.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.muse.common.util.EmailUtils;
import com.muse.pay.dao.CodeInfoMapper;
import com.muse.pay.entity.CodeInfo;
import com.muse.pay.service.CodeInfoService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;


/**
 * Created by code generator  on 2018/07/24.
 */
@Service
@Transactional
public class CodeInfoServiceImpl extends BaseService<CodeInfoMapper, CodeInfo> implements CodeInfoService {
    private static Logger log = LoggerFactory.getLogger("VerifyCodeServie");

    public static long EXPIRE_TIME_CODE = 30;

    @Autowired
    private EmailUtils emailUtils;

    @Override
    public ResultData add(CodeInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(CodeInfo obj) throws Exception {
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

        CodeInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData verifyCode(String email, String code, String codeType) {
        // 检查参数是否为空
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code)) {
            return ResultData.getErrResult("参数不能为空");
        }

        // 查询验证码信息
        CodeInfo codeInfo = mapper.selectByColumn(new CodeInfo(code, email, codeType));
        if (codeInfo == null) {
            return ResultData.getErrResult("验证码错误");
        }

        // 检查验证码是否过期
        if (codeInfo.whetherExpire()) {
            return ResultData.getErrResult("验证码已过期");
        }

        // 检查是否已经使用过
        if (codeInfo.getStatus().equals("2")) {
            return ResultData.getErrResult("该验证码已经使用过");
        }

        // 修改验证码状态
        mapper.updateByPrimaryKeySelective(new CodeInfo(codeInfo.getId(), 2));

        return ResultData.getSuccessResult();
    }

    @Override
    public ResultData makeVerifyCode(String email, String codeType) throws Exception {

        // 查询是否已经存在该邮箱和验证码类型的记录
        CodeInfo codeInfo = mapper.selectByColumn(new CodeInfo(email, codeType));
        if (codeInfo != null) {
            // 检查请求次数是否超过最大次数
            if (codeInfo.whetherOverCount()) {
                return ResultData.getErrResult("请求验证码超过最大次数");
            }
            // 更新验证码
            codeInfo = setNewCode(codeInfo);

        } else {
            // 生成新的验证码
            codeInfo = makeNewCode(email, codeType);
        }
        log.info("验证码对象信息=" + codeInfo.toString());

        // 获取邮件内容
        Map<String, String> emailContentMap = CodeInfo.getCodeContentByType(codeType, codeInfo.getCode());

        // 发送邮件给用户
        ResultData sendCodeData = emailUtils.sendEmail(email, emailContentMap.get("title"), emailContentMap.get("content"));
        if (!sendCodeData.isOk()) {
            return sendCodeData;
        }
        log.info("发送验证码结果=" + sendCodeData.toString());

        return ResultData.getSuccessResult();
    }

    /**
     * 设置新的验证码
     *
     * @param codeInfo 旧的验证码对象
     * @return CodeInfo
     */
    private CodeInfo setNewCode(CodeInfo codeInfo) {
        // 更新验证码参数
        LocalDateTime now = LocalDateTime.now();
        codeInfo.setExpireTime(now.plus(EXPIRE_TIME_CODE, ChronoUnit.MINUTES));

        codeInfo.setStatus(CodeInfo.CODE_STATUS_UNUSED);
        codeInfo.setCount(codeInfo.getCount() + 1);
        codeInfo.setCode(RandomStringUtils.randomAlphanumeric(6));

        // 更新，返回结果
        mapper.updateByPrimaryKey(codeInfo);
        return codeInfo;
    }

    /**
     * 生成验证码
     *
     * @param email    邮箱
     * @param codeType 验证码类型
     * @return CodeInfo
     */
    private CodeInfo makeNewCode(String email, String codeType) {
        // 生成验证码
        CodeInfo codeInfo = new CodeInfo();
        String code = RandomStringUtils.randomAlphanumeric(6);
        codeInfo.setCode(code);
        codeInfo.setEmail(email);
        codeInfo.setCodeType(codeType);
        codeInfo.setCount(0);

        LocalDateTime now = LocalDateTime.now();
        codeInfo.setExpireTime(now.plus(EXPIRE_TIME_CODE, ChronoUnit.MINUTES));

        // 保存
        mapper.insert(codeInfo);
        return codeInfo;
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
