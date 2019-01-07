package com.proxypool.kindlebook;

import com.muse.common.entity.BaseEntityInfo;
import org.thymeleaf.util.StringUtils;

/**
 * @program: muse-pay
 * @description: mebook信息类
 * @author: Vincent
 * @create: 2019-01-04 12:26
 **/
public class MeBookInfo extends BaseEntityInfo {

    public static final String REDIS_KEY_BOOK_LIST = "KINDLE_BOOK_LIST";

    // 分类
    private String category;
    // 海报地址
    private String postUrl;
    // 标题
    private String title;
    // 发布时间
    private String releaseTime;
    // 简介
    private String intro;
    // 详情地址
    private String detailUrl;
    // 详情
    private String detailDesc;
    // 下载地址
    private String downloadUrl;

    private int code;

    /**
     * 默认构造方法
     */
    public MeBookInfo() {

    }

    /**
     * 构造方法
     *
     * @param category
     * @param postUrl
     * @param title
     * @param releaseTime
     * @param intro
     * @param detailUrl
     */
    private MeBookInfo(String category, String postUrl, String title, String releaseTime,
                       String intro, String detailUrl) {
        this.category = category;
        this.postUrl = postUrl;
        this.title = title;
        this.releaseTime = releaseTime;
        this.intro = intro;
        this.detailUrl = detailUrl;
    }

    /**
     * 获取实例
     *
     * @param category
     * @param postUrl
     * @param title
     * @param releaseTime
     * @param intro
     * @param detailUrl
     * @return
     */
    public static MeBookInfo from(String category, String postUrl, String title, String releaseTime,
                                  String intro, String detailUrl) {
        return new MeBookInfo(category, postUrl, title, releaseTime, intro, detailUrl);
    }

    /**
     * 补充详情页的信息
     *
     * @param detailDesc
     * @param downloadUrl
     */
    public void supplement(String detailDesc, String downloadUrl) {
        this.detailDesc = detailDesc;
        this.downloadUrl = downloadUrl;
    }

    /**
     * 是否可以保存
     *
     * @return
     */
    public boolean canSave() {
        return !StringUtils.isEmpty(title) && !StringUtils.isEmpty(downloadUrl);
    }

    /**
     * 是否可以进一步处理
     * @return
     */
    public boolean canMoreHandle() {
        return !StringUtils.isEmpty(title) && !StringUtils.isEmpty(detailUrl);
    }


    @Override
    public String toString() {
        return "MeBookInfo{" +
                "category='" + category + '\'' +
                ", postUrl='" + postUrl + '\'' +
                ", title='" + title + '\'' +
                ", releaseTime='" + releaseTime + '\'' +
                ", intro='" + intro + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                ", detailDesc='" + detailDesc + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", version=" + version +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        String[] hashContents = new String[]{this.title, this.category, this.intro, this.releaseTime};
        for (String item : hashContents) {
            hashCode += 31 * hashCode + item.hashCode();
        }
        return hashCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getCode() {
        return hashCode();
    }

    public void setCode(int code) {
        this.code = code;
    }
}