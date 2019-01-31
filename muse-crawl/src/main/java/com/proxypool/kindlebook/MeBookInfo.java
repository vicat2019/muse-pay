package com.proxypool.kindlebook;

import com.muse.common.entity.BaseEntityInfo;
import com.proxypool.util.TextUtils;
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
    // 编码
    private int code;
    // 名称
    private String name;
    // 作者
    private String author;

    // 评分
    private double score;
    // 数据来源
    private String source;


    /**
     * 默认构造方法
     */
    public MeBookInfo() {

    }

    /**
     * 构造方法
     *
     * @param name        名称
     * @param category    分类
     * @param author      作者
     * @param postUrl     海报
     * @param title       标题
     * @param releaseTime 发布时间
     * @param intro       简介
     * @param detailUrl   详情URL
     */
    private MeBookInfo(String name, String category, String author, String postUrl, String title, String releaseTime,
                       String intro, String detailUrl) {
        this.name = TextUtils.trim(name);
        this.author = TextUtils.trim(author);
        this.category = TextUtils.trim(category);
        this.postUrl = TextUtils.trim(postUrl);
        this.title = TextUtils.trim(title);
        this.releaseTime = TextUtils.trim(releaseTime);
        this.intro = TextUtils.trim(intro);
        this.detailUrl = TextUtils.trim(detailUrl);
    }

    /**
     * 获取实例
     *
     * @param name        名称
     * @param category    分类
     * @param author      作者
     * @param postUrl     海报
     * @param title       标题
     * @param releaseTime 发布时间
     * @param intro       简介
     * @param detailUrl   详情URL
     * @return MeBookInfo
     */
    public static MeBookInfo from(String name, String category, String author, String postUrl, String title, String releaseTime,
                                  String intro, String detailUrl) {
        return new MeBookInfo(name, category, author, postUrl, title, releaseTime, intro, detailUrl);
    }

    /**
     * 补充详情页的信息
     *
     * @param detailDesc  详情
     * @param downloadUrl 下载地址
     */
    public void supplement(String detailDesc, String downloadUrl) {
        this.detailDesc = detailDesc;
        this.downloadUrl = downloadUrl;
    }

    /**
     * 是否可以保存
     *
     * @return boolean
     */
    public boolean canSave() {
        return !StringUtils.isEmpty(title) && !StringUtils.isEmpty(downloadUrl);
    }

    /**
     * 是否可以进一步处理
     *
     * @return boolean
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
                ", code=" + code +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", score=" + score +
                ", source='" + source + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        String[] hashContents = new String[]{this.title, this.category, this.intro, this.releaseTime};
        for (String item : hashContents) {
            if (!StringUtils.isEmpty(item)) {
                hashCode += 31 * hashCode + item.hashCode();
            }
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
