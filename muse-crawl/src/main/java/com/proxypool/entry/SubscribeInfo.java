package com.proxypool.entry;

import com.muse.common.entity.BaseEntityInfo;

public class SubscribeInfo extends BaseEntityInfo {

    private String unitId;

    private String state;

    private String doctorId;

    private String doctorName;

    private String toDate;

    private String yuyueMax;

    private String yuyueNum;

    private String youzhiMax;

    private String youzhiNum;

    private String leftNum;

    private String timeType;

    private String timeTypeDesc;

    private String yState;

    private String yStateDesc;


    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId == null ? null : unitId.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId == null ? null : doctorId.trim();
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName == null ? null : doctorName.trim();
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate == null ? null : toDate.trim();
    }

    public String getYuyueMax() {
        return yuyueMax;
    }

    public void setYuyueMax(String yuyueMax) {
        this.yuyueMax = yuyueMax == null ? null : yuyueMax.trim();
    }

    public String getYuyueNum() {
        return yuyueNum;
    }

    public void setYuyueNum(String yuyueNum) {
        this.yuyueNum = yuyueNum == null ? null : yuyueNum.trim();
    }

    public String getYouzhiMax() {
        return youzhiMax;
    }

    public void setYouzhiMax(String youzhiMax) {
        this.youzhiMax = youzhiMax == null ? null : youzhiMax.trim();
    }

    public String getYouzhiNum() {
        return youzhiNum;
    }

    public void setYouzhiNum(String youzhiNum) {
        this.youzhiNum = youzhiNum == null ? null : youzhiNum.trim();
    }

    public String getLeftNum() {
        return leftNum;
    }

    public void setLeftNum(String leftNum) {
        this.leftNum = leftNum == null ? null : leftNum.trim();
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType == null ? null : timeType.trim();
    }

    public String getTimeTypeDesc() {
        return timeTypeDesc;
    }

    public void setTimeTypeDesc(String timeTypeDesc) {
        this.timeTypeDesc = timeTypeDesc == null ? null : timeTypeDesc.trim();
    }

    public String getyState() {
        return yState;
    }

    public void setyState(String yState) {
        this.yState = yState == null ? null : yState.trim();
    }

    public String getyStateDesc() {
        return yStateDesc;
    }

    public void setyStateDesc(String yStateDesc) {
        this.yStateDesc = yStateDesc == null ? null : yStateDesc.trim();
    }
}