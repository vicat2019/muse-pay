package com.proxypool.dao;

import com.proxypool.entry.DoctorInfo;

public interface DoctorInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DoctorInfo record);

    DoctorInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(DoctorInfo record);
}