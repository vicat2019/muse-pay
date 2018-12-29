package com.proxypool.dao;

import com.proxypool.entry.RecruitInfo;

import java.util.List;

public interface RecruitInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(RecruitInfo record);

    RecruitInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(RecruitInfo record);

    int getCountByCode(String code);

    List<Integer> getRepeatRecruitIds();

    int delRepeatByIds(List<Integer> idList);

    RecruitInfo getByIdentificationCode(String code);

    int updateByCode(RecruitInfo recruitInfo);

}