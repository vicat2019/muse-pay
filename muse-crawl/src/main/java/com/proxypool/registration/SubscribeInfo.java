package com.proxypool.registration;

/**
 * @program: muse-pay
 * @description: 预约信息
 * @author: Vincent
 * @create: 2019-02-23 16:14
 **/
public class SubscribeInfo {

    private String title;

    private String total;

    private String residue;

    private String price;

    private String type;

    public SubscribeInfo() {

    }

    /**
     * 构造方法
     *
     * @param title
     */
    public static SubscribeInfo getInstance(String title) {
        SubscribeInfo temp = new SubscribeInfo();
        temp.setTitle(title);
        return temp;
    }

    /**
     * 构造方法
     *
     * @param total
     * @param residue
     * @param price
     * @param type
     */
    public SubscribeInfo(String total, String residue, String price, String type) {
        this.total = total;
        this.residue = residue;
        this.price = price;
        this.type = type;
    }


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getResidue() {
        return residue;
    }

    public void setResidue(String residue) {
        this.residue = residue;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
