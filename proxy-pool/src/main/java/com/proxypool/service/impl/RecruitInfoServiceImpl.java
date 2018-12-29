package com.proxypool.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.proxypool.dao.RecruitInfoMapper;
import com.proxypool.entry.RecruitInfo;
import com.proxypool.service.RecruitInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by code generator  on 2018/11/04.
 */
@Service("recruitInfoService")
@Transactional
public class RecruitInfoServiceImpl extends BaseService<RecruitInfoMapper, RecruitInfo> implements RecruitInfoService {
    private Logger log = LoggerFactory.getLogger("RecruitInfoServiceImpl");

    @Override
    public ResultData add(RecruitInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }

        // 检查是否已经存在
        RecruitInfo recruitInfo = mapper.getByIdentificationCode(obj.getIdentificationCode());
        if (recruitInfo != null) {
            // 记录当前发布日期
            if (StringUtils.isEmpty(recruitInfo.getReleaseDates())) {
                recruitInfo.setReleaseDates(obj.getReleaseDate());
            } else if (!recruitInfo.getReleaseDates().contains(recruitInfo.getReleaseDate().trim())){
                recruitInfo.setReleaseDates(recruitInfo.getReleaseDates() + "," + obj.getReleaseDate());
            }
            // 更新
            mapper.updateByCode(recruitInfo);
            return ResultData.getErrResult("该职位信息已经存在=" + obj.getPostName() + ", " + obj.getCompanyName());
        }

        // 添加
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData update(RecruitInfo obj) throws Exception {
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

        RecruitInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData delRepeatRecruit() throws Exception {
        // 检查是否有重复的记录
        List<Integer> repeatIdList = mapper.getRepeatRecruitIds();
        if (repeatIdList == null || repeatIdList.size() == 0) {
            return ResultData.getSuccessResult("没有重复记录");
        }
        // 获取重复记录中的第一个
        List<Integer> delIdList = new ArrayList<>();
        for (int i = 0; i < repeatIdList.size(); i++) {
            if (i % 2 == 0) {
                delIdList.add(repeatIdList.get(i));
            }
        }
        // 批量删除记录
        int count = 0;
        if (delIdList.size() > 0) {
            count = mapper.delRepeatByIds(delIdList);
        }

        log.info("删除重复的职位记录数=" + count);
        return ResultData.getSuccessResult("成功删除重复记录[" + count + "]");
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
