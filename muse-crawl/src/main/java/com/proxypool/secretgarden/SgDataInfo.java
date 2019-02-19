package com.proxypool.secretgarden;

import com.muse.common.entity.BaseEntityInfo;

/**
 * @program: muse-pay
 * @description: DB数据类
 * @author: Vincent
 * @create: 2019-02-19 17:38
 **/
public class SgDataInfo extends BaseEntityInfo {

    private int code;

    private String title;

    private String url;

    /**
     * 生成实例
     * @param code
     * @param title
     * @param url
     * @return
     */
    public SgDataInfo getInstance(int code, String title, String url) {
        SgDataInfo sgDataInfo = new SgDataInfo();
        sgDataInfo.code = code;
        sgDataInfo.title = title;
        sgDataInfo.url = url;

        return sgDataInfo;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
