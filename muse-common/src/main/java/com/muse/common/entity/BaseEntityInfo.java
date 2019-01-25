package com.muse.common.entity;

import org.thymeleaf.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/22 10 31
 **/
public class BaseEntityInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 记录标识
    protected int id;
    // 状态
    protected String status;
    // 版本号
    protected int version;
    // 修改时间
    protected Date modifyTime;
    // 创建时间
    protected Date createTime;

    /**
     * 该对象是否合法，即关键属性是否有值
     *
     * @return boolean
     */
    public boolean legitimate() {
        if (identificationCode() == null || StringUtils.isEmpty(identificationCode().toString())) {
            return false;
        }
        return true;
    }

    public Object identificationCode() {
        return id;
    }

    @Override
    public String toString() {
        return "BaseEntityInfo{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", version=" + version +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
