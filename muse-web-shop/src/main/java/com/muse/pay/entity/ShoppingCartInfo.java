package com.muse.pay.entity;

import com.muse.common.entity.BaseEntityInfo;

/**
 * 购物车类
 */
public class ShoppingCartInfo extends BaseEntityInfo {

    private Integer userId;

    private Integer bookId;

    private Integer count;

    private BookInfo bookInfo;

    public static ShoppingCartInfo getInstance() {
        return new ShoppingCartInfo();
    }

    @Override
    public String toString() {
        return "ShoppingCartInfo{" +
                "userId=" + userId +
                ", bookId=" + bookId +
                ", count=" + count +
                ", id=" + id +
                ", version=" + version +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }
}