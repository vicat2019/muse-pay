package com.muse.pay.dao;


import com.muse.pay.entity.ConsigneeInfo;

import java.util.List;

public interface ConsigneeInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsigneeInfo record);

    int insertSelective(ConsigneeInfo record);

    ConsigneeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsigneeInfo record);

    int updateByPrimaryKey(ConsigneeInfo record);

    List<ConsigneeInfo> selectByColumn(ConsigneeInfo consigneeInfo);
}