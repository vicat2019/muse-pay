package com.proxypool.secretgarden;

import com.muse.common.entity.BaseEntityInfo;

import java.util.List;

/**
 * @program: muse-pay
 * @description: 数据实例
 * @author: Vincent
 * @create: 2019-02-18 15:12
 **/
public class SecretGardenInfo extends BaseEntityInfo {

    private String title;

    private List<String> targetUrlList;

    private String source;

    private String detailUrl;

    /**
     * 构造方法
     *
     * @param title
     * @param url
     * @param detailUrl
     * @param source
     */
    public SecretGardenInfo(String title, List<String> url, String detailUrl, String source) {
        this.title = title;
        this.targetUrlList = url;
        this.source = source;
        this.detailUrl = detailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTargetUrlList() {
        return targetUrlList;
    }

    public void setTargetUrlList(List<String> targetUrlList) {
        this.targetUrlList = targetUrlList;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}
