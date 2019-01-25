package com.proxypool.picture;

import com.muse.common.entity.BaseEntityInfo;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

/**
 * @program: muse-pay
 * @description: Picture信息
 * @author: Vincent
 * @create: 2019-01-23 17:42
 **/
public class PictureInfo extends BaseEntityInfo {
    private Logger log = LoggerFactory.getLogger("PictureInfo");

    // 标题
    private String title;

    // 大图地址
    private String bigUrl;

    // 缩略图地址
    private String thumbUrl;

    // 备注
    private String remark;

    // 分类
    private String category;

    // 宽度
    private int width;

    // 高度
    private int height;

    /**
     * 构造方法
     */
    public PictureInfo() {

    }

    /**
     * 构造方法
     *
     * @param bigUrl   大图地址
     * @param thumbUrl 缩略图地址
     */
    public PictureInfo(String bigUrl, String thumbUrl) {
        this.bigUrl = bigUrl;
        this.thumbUrl = thumbUrl;
    }

    /**
     * 根据主地址获取缩略地址
     *
     * @param middleUrl 中间地址
     * @return String
     */
    public static String toThumbUrl(String middleUrl) {
        int index = middleUrl.lastIndexOf("/");
        String no = middleUrl.substring(index + 1);

        return "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-" + no + ".jpg";
    }

    /**
     * 返回大图地址
     *
     * @param middleUrl 中间地址
     * @return String
     */
    public static String toBigUrl(String middleUrl) {
        int index = middleUrl.lastIndexOf("/");
        String no = middleUrl.substring(index + 1);

        return "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-" + no + ".jpg";
    }

    /**
     * 设置图片宽度和高度消息
     *
     * @param content 宽高信息字符串
     */
    public void setWidthAndHeight(String content) {
        if (StringUtils.isEmpty(content)) return;

        String[] infoAr = Strings.split(content, 'x');
        if (infoAr.length != 2) {
            log.error("设置图片宽度和高度，输入内容=" + content);
            return;
        }
        String width = infoAr[0].trim();
        String height = infoAr[1].trim();
        this.width = Integer.parseInt(width);
        this.height = Integer.parseInt(height);
    }


    @Override
    public String toString() {
        return "PictureInfo{" +
                "title='" + title + '\'' +
                ", bigUrl='" + bigUrl + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", remark='" + remark + '\'' +
                ", category='" + category + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBigUrl() {
        return bigUrl;
    }

    public void setBigUrl(String bigUrl) {
        this.bigUrl = bigUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
