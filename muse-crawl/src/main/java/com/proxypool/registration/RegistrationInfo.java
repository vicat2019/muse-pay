package com.proxypool.registration;

import java.util.List;

/**
 * @program: muse-pay
 * @description: 挂号
 * @author: Vincent
 * @create: 2019-02-23 16:11
 **/
public class RegistrationInfo {

    private String name;

    private String position;

    private String speciality;

    private String subscribeCount;

    private String goodReputation;

    private List<SubscribeInfo> amSubscribeList;
    private List<SubscribeInfo> pmSubscribeList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(String subscribeCount) {
        this.subscribeCount = subscribeCount;
    }

    public String getGoodReputation() {
        return goodReputation;
    }

    public void setGoodReputation(String goodReputation) {
        this.goodReputation = goodReputation;
    }

    public List<SubscribeInfo> getAmSubscribeList() {
        return amSubscribeList;
    }

    public void setAmSubscribeList(List<SubscribeInfo> amSubscribeList) {
        this.amSubscribeList = amSubscribeList;
    }

    public List<SubscribeInfo> getPmSubscribeList() {
        return pmSubscribeList;
    }

    public void setPmSubscribeList(List<SubscribeInfo> pmSubscribeList) {
        this.pmSubscribeList = pmSubscribeList;
    }
}
