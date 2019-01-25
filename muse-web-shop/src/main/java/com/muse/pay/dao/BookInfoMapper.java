package com.muse.pay.dao;


import com.muse.pay.entity.BookInfo;

import java.util.List;

public interface BookInfoMapper {

    int deleteByPrimaryKey(int id);

    int insert(BookInfo record);

    int insertSelective(BookInfo record);

    BookInfo selectByPrimaryKey(int id);

    BookInfo selectSimpleByPK(int id);

    int updateByPrimaryKeySelective(BookInfo record);

    int updateByPrimaryKey(BookInfo record);

    List<BookInfo> selectByColumn(BookInfo bookInfo);

    List<BookInfo> queryBookIn(List<Integer> ids);

    List<Integer> getAllBookIds();

}