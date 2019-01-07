package com.proxypool.dao;


import com.proxypool.kindlebook.MeBookInfo;

import java.util.List;

public interface MeBookInfoMapper {

    int insertOrUpdate(MeBookInfo bookInfo);

    int updateByPrimary(MeBookInfo bookInfo);

    List<MeBookInfo> getAllMeBook();
}