package com.proxypool.dao;


import com.proxypool.kindlebook.MeBookInfo;

public interface MeBookInfoMapper {

    int insertOrUpdate(MeBookInfo bookInfo);

    int updateByPrimary(MeBookInfo bookInfo);
}