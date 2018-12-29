package com.muse.pay.entity;

import com.muse.common.entity.BaseEntityInfo;

import java.math.BigDecimal;
import java.util.List;

public class OrderInfo extends BaseEntityInfo {

    // 订单类型
    public static String ORDER_TYPE_NORMAL = "NORMAL";
    public static String ORDER_TYPE_RECHARGE = "RECHARGE";

    private String orderNo;

    private Integer userId;

    private BigDecimal amount;

    private int itemCount;

    private String payNo;

    private String remark;

    private String orderType;

    private List<OrderItemInfo> itemList;

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderNo='" + orderNo + '\'' +
                ", userId=" + userId +
                ", amount=" + amount +
                ", itemCount=" + itemCount +
                ", payNo='" + payNo + '\'' +
                ", remark='" + remark + '\'' +
                ", orderType='" + orderType + '\'' +
                ", itemList=" + itemList +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", version=" + version +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                '}';
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public List<OrderItemInfo> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItemInfo> itemList) {
        this.itemList = itemList;
    }

    public int getItemCount() {

        if (itemCount == 0 && itemList!=null && itemList.size()>0) {
            for (OrderItemInfo item : itemList) {
                this.itemCount += item.getCount();
            }
        }

        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}