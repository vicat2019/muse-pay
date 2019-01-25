package com.seckill.dao;

import com.seckill.entity.AlipayTransOrder;
import com.seckill.entity.AlipayTransOrderExample;

public interface AlipayTransOrderMapper {
    long countByExample(AlipayTransOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AlipayTransOrder record);

    int insertSelective(AlipayTransOrder record);

    AlipayTransOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AlipayTransOrder record);

    int updateByPrimaryKey(AlipayTransOrder record);
}