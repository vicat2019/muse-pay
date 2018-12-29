package com.muse.pay.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/26 11 58
 **/
public class OrderInfoVO implements Serializable {
    private int userId;

    private List<Integer> bookIds;
    private List<Integer> bookCounts;

    private BigDecimal amount;

    @Override
    public String toString() {
        return "OrderInfoVO{" +
                "userId=" + userId +
                ", bookIds=" + bookIds +
                ", bookCounts=" + bookCounts +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Integer> bookIds) {
        this.bookIds = bookIds;
    }

    public List<Integer> getBookCounts() {
        return bookCounts;
    }

    public void setBookCounts(List<Integer> bookCounts) {
        this.bookCounts = bookCounts;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
