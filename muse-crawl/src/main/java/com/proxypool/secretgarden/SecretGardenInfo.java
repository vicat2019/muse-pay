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

    private String detaulUrl;

    /**
     * 构造方法
     *
     * @param title
     * @param url
     * @param detaulUrl
     * @param source
     */
    public SecretGardenInfo(String title, List<String> url, String detaulUrl, String source) {
        this.title = title;
        this.targetUrlList = url;
        this.source = source;
        this.detaulUrl = detaulUrl;
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

    public String getDetaulUrl() {
        return detaulUrl;
    }

    public void setDetaulUrl(String detaulUrl) {
        this.detaulUrl = detaulUrl;
    }
}
