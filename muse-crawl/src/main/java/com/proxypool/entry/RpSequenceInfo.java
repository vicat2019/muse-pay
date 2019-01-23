package com.proxypool.entry;

import com.muse.common.entity.BaseEntityInfo;

/**
 * @Description: 序列信息类
 * @Author: Vincent
 * @Date: 2018/12/18
 */
public class RpSequenceInfo extends BaseEntityInfo {

    // 缓存数据KEY
    public static final String REDIS_CACHE_KEY_1 = "REDIS_CACHE_KEY";

    // 序列号
    private String sequenceNo;


    /**
     * 随机获取Redis Key
     *
     * @return String
     */
    public static String obtainRedisKey() {
        return REDIS_CACHE_KEY_1;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo == null ? null : sequenceNo.trim();
    }
}