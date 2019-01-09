package com.proxypool.dao;


import com.proxypool.kindlebook.MeBookInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MeBookInfoMapper {

    int insertOrUpdate(MeBookInfo bookInfo);

    int updateByPrimary(MeBookInfo bookInfo);

    List<MeBookInfo> getAllMeBook();

    List<MeBookInfo> handleMeBook();

    List<Integer> getAllCode();

    int getCountByCode(@Param("code") int code);
}