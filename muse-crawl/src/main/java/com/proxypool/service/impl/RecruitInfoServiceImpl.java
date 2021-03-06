package com.proxypool.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.proxypool.dao.RecruitInfoMapper;
import com.proxypool.entry.RecruitInfo;
import com.proxypool.service.RecruitInfoService;
import com.proxypool.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;


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
            } else if (!recruitInfo.getReleaseDates().contains(recruitInfo.getReleaseDate().trim())) {
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

    /**
     * 分页查询JL记录
     *
     * @param page        页码
     * @param size        每页记录数
     * @param companyName GS名称
     * @param postName    ZW名称
     * @param minSalary   最低GZ
     * @param maxSalary   最高GZ
     * @param releaseTime 发布日期
     * @param createTime  创建日期
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public Map<String, Object> queryRecruit(int page, int size, String companyName, String postName, String minSalary,
                                            String maxSalary, String releaseTime, String createTime) throws Exception {
        PageHelper.startPage(page, size);

        List<RecruitInfo> recruitInfoList = mapper.queryRecruit(companyName, postName, minSalary,
                maxSalary, releaseTime, createTime);
        PageInfo pageInfo = new PageInfo(recruitInfoList);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0");

        if (recruitInfoList.size() > 0) {
            resultMap.put("message", "查询成功");
            resultMap.put("data", pageInfo);
        } else {
            resultMap.put("message", "没有匹配的数据");
            resultMap.put("data", null);
        }

        resultMap.put("p", page);
        resultMap.put("s", size);
        return resultMap;
    }

    @Override
    public ResultData rinseRecruit(int page, int pageSize) throws Exception {

        // 分页查询数据
        PageHelper.startPage(page, pageSize);
        List<RecruitInfo> dataList = mapper.selectRecruit();
        if (dataList == null || dataList.size() == 0) {
            return ResultData.getSuccessResult("没有未处理的数据");
        }
        PageInfo<RecruitInfo> pageInfo = new PageInfo<>(dataList);

        // 清洗
        for (RecruitInfo item : dataList) {
            // XZ
            BigDecimal[] salaryAr = TextUtils.splitSalary(item);
            if (salaryAr != null) {
                item.setMinSalary(salaryAr[0]);
                item.setMaxSalary(salaryAr[1]);
            } else {
                item.setMinSalary(new BigDecimal("0"));
                item.setMaxSalary(new BigDecimal("0"));
            }

            // 发布日期
            if (item.getNumber().contains("发布")) {
                item.setReleaseDate(item.getNumber());
                item.setNumber("");
            }
            // 去掉"发布"
            if (!StringUtils.isEmpty(item.getReleaseDate()) && item.getReleaseDate().contains("发布")) {
                item.setReleaseDate(item.getReleaseDate().replaceAll("发布", ""));
            }
            if (!StringUtils.isEmpty(item.getReleaseDates()) && item.getReleaseDates().contains("发布")) {
                item.setReleaseDates(item.getReleaseDates().replaceAll("发布", ""));
            }

            // 经验
            int[] experiences = TextUtils.splitExperience(item);
            if (experiences != null) {
                item.setMinExp(experiences[0]);
                item.setMaxExp(experiences[1]);
            }
        }

        int rinseCount = 0;
        // 更新
        if (dataList.size() > 1000) {
            int count = (dataList.size() % 1000 == 0) ? (dataList.size() / 1000) : (dataList.size() / 1000 + 1);
            for (int i = 0; i < count; i++) {
                List<RecruitInfo> temp;
                if (i < count - 1) {
                    temp = dataList.subList((i * 1000), (i + 1) * 1000);
                } else {
                    temp = dataList.subList((i * 1000), dataList.size());
                }
                mapper.updateBatch(temp);
                rinseCount += temp.size();
            }
        } else {
            mapper.updateBatch(dataList);
            rinseCount += dataList.size();
        }
        log.info("rinseRecruit() 清洗数据个数=" + rinseCount);

        // 如果还有下一页
        int maxPage = pageInfo.getPages();
        if (page < maxPage) {
            rinseRecruit(page + 1, pageSize);
        }

        return ResultData.getSuccessResult("完成数据处理");
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
