package com.proxypool.secretgarden;

import com.muse.common.entity.BaseEntityInfo;
import org.thymeleaf.util.StringUtils;

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
     *
     * @param title
     * @param url
     * @return
     */
    public static SgDataInfo getInstance(String title, String url) {
        SgDataInfo sgDataInfo = new SgDataInfo();
        sgDataInfo.title = title;
        sgDataInfo.url = url;

        return sgDataInfo;
    }

    @Override
    public int hashCode() {
        return genHashCode(this.title, this.url);
    }

    public static int genHashCode(String title, String url) {
        int result = StringUtils.isEmpty(title) ? 0 : title.hashCode();
        result = 31 * result + (StringUtils.isEmpty(url) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SgDataInfo) {
            SgDataInfo proxy = (SgDataInfo) obj;
            return title.equalsIgnoreCase(proxy.getTitle().trim())
                    && url.equals(proxy.getUrl());
        }

        return false;
    }

    public int getCode() {
        return hashCode();
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
