package com.muse.pay.dao;


import com.muse.pay.entity.CodeInfo;

public interface CodeInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CodeInfo record);

    int insertSelective(CodeInfo record);

    CodeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CodeInfo record);

    int updateByPrimaryKey(CodeInfo record);

    CodeInfo selectByColumn(CodeInfo verifyCodeInfo);

}