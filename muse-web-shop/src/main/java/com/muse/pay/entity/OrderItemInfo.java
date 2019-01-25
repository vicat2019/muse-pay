package com.muse.pay.entity;

import com.muse.common.entity.BaseEntityInfo;

import java.util.Date;

public class OrderItemInfo extends BaseEntityInfo {
    private String orderNo;

    private Integer count;

    private int bookId;

    private String title;

    private String titleDesc;

    private String picUrl;

    private String author;

    private String bookConcern;

    private String publishingTime;

    private String score;

    private String price;

    private String fixedPrice;

    private String edition;

    private String pageCount;

    private String printingTime;

    private String bookSize;

    private String paper;

    private String suit;

    private String packing;

    private String category;

    private String detailUrl;

    private String dataSources;

    private String isbn;

    private String contentDesc;
    private String recommendation;
    private String authorDesc;

    /**
     * 根据图书内容生成订单项内容
     *
     * @param bookInfo 图书对象
     * @return OrderItemInfo
     */
    public void setValueFrom(BookInfo bookInfo) {
        this.id = bookInfo.getId();
        this.bookId = bookInfo.getId();
        this.title = bookInfo.getTitle();
        this.titleDesc = bookInfo.getTitleDesc();
        this.picUrl = bookInfo.getPicUrl();
        this.author = bookInfo.getAuthor();
        this.bookConcern = bookInfo.getBookConcern();
        this.publishingTime = bookInfo.getPublishingTime();
        this.score = bookInfo.getScore();
        this.price = bookInfo.getPrice();
        this.fixedPrice = bookInfo.getFixedPrice();
        this.edition = bookInfo.getEdition();
        this.pageCount = bookInfo.getPageCount();
        this.printingTime = bookInfo.getPrintingTime();
        this.bookSize = bookInfo.getBookSize();
        this.paper = bookInfo.getPaper();
        this.suit = bookInfo.getSuit();
        this.packing = bookInfo.getPacking();
        this.category = bookInfo.getCategory();
        this.detailUrl = bookInfo.getDetailUrl();
        this.dataSources = bookInfo.getDataSources();
        this.isbn = bookInfo.getIsbn();
        this.contentDesc = bookInfo.getContentDesc();
        this.recommendation = bookInfo.getRecommendation();
        this.authorDesc = bookInfo.getAuthor();
    }

    /**
     * 生成充值的订单内容
     * @param orderInfo 订单
     */
    public void valueFrom(OrderInfo orderInfo) {
        this.bookId = 0;
        this.orderNo = orderInfo.getOrderNo();
        this.count = 1;
        this.title = "充值";
        this.titleDesc = "充值";
        this.picUrl = "";
        this.author = "";
        this.bookConcern = "";
        this.publishingTime = "";
        this.score = "";
        this.price = orderInfo.getAmount().toString();
        this.fixedPrice = orderInfo.getAmount().toString();
        this.edition = "";
        this.pageCount = "";
        this.printingTime = "";
        this.bookSize = "";
        this.paper = "";
        this.suit = "";
        this.packing = "";
        this.category = "";
        this.detailUrl = "";
        this.dataSources = "";
        this.isbn = "";
        this.contentDesc = "";
        this.recommendation = "";
        this.authorDesc = "";
    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? null : isbn.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getTitleDesc() {
        return titleDesc;
    }

    public void setTitleDesc(String titleDesc) {
        this.titleDesc = titleDesc == null ? null : titleDesc.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getBookConcern() {
        return bookConcern;
    }

    public void setBookConcern(String bookConcern) {
        this.bookConcern = bookConcern == null ? null : bookConcern.trim();
    }

    public String getPublishingTime() {
        return publishingTime;
    }

    public void setPublishingTime(String publishingTime) {
        this.publishingTime = publishingTime == null ? null : publishingTime.trim();
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score == null ? null : score.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(String fixedPrice) {
        this.fixedPrice = fixedPrice == null ? null : fixedPrice.trim();
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition == null ? null : edition.trim();
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount == null ? null : pageCount.trim();
    }

    public String getPrintingTime() {
        return printingTime;
    }

    public void setPrintingTime(String printingTime) {
        this.printingTime = printingTime == null ? null : printingTime.trim();
    }

    public String getBookSize() {
        return bookSize;
    }

    public void setBookSize(String bookSize) {
        this.bookSize = bookSize == null ? null : bookSize.trim();
    }

    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper == null ? null : paper.trim();
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit == null ? null : suit.trim();
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing == null ? null : packing.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl == null ? null : detailUrl.trim();
    }

    public String getDataSources() {
        return dataSources;
    }

    public void setDataSources(String dataSources) {
        this.dataSources = dataSources == null ? null : dataSources.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContentDesc() {
        return contentDesc;
    }

    public void setContentDesc(String contentDesc) {
        this.contentDesc = contentDesc;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getAuthorDesc() {
        return authorDesc;
    }

    public void setAuthorDesc(String authorDesc) {
        this.authorDesc = authorDesc;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}