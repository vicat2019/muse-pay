package com.seckill.dao;

import com.seckill.entity.SuccessInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SuccessInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SuccessInfo record);

    SuccessInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SuccessInfo record);

    int updateByPrimaryKey(SuccessInfo record);

    int getCountByCode(@Param("code") String code);
}