package com.proxypool.dao;


import com.proxypool.entry.RpSequenceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RpSequenceInfoMapper {
    int deleteBySequence(String sequence);

    int insert(String sequence);

    RpSequenceInfo selectBySequence(String Sequence);

    int updateBySequence(@Param("status") String status, @Param("sequenceNo") String sequenceNo);

    List<Object> selectSequenceBatch(@Param("size") int size);

    int insertSequenceBatch(@Param("list") List<String> sequenceInfoList);

    int test(@Param("list") List<String> testlist);

    String getCurrentSequenceNum(String name);

    int updateCurrentSequenceNum(@Param("value") String value, @Param("name") String name);

}