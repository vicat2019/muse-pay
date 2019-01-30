package com.proxypool.entry;

import com.muse.common.entity.BaseEntityInfo;
import com.proxypool.util.TextUtils;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;

/**
 * @Description: JL信息类
 * @Author: Vincent
 * @Date: 2018/11/4
 */
public class RecruitInfo extends BaseEntityInfo {

    private String postName;

    private String experience;

    private int minExp;

    private int maxExp;

    private String number;

    private String contact;

    private String salary;

    private BigDecimal minSalary;

    private BigDecimal maxSalary;

    private String area;

    private String education;

    private String welfare;

    private String jobInformation;

    private String companyName;

    private String companyType;

    private String companySize;

    private String companyIndustry;

    private String companyDesc;

    private String releaseDate;

    private String source;

    private String detailUrl;

    private String identificationCode;

    private String releaseDates;

    private int releaseCount;


    @Override
    public int hashCode() {
        int result = postName != null ? postName.hashCode() : 0;
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (jobInformation != null ? jobInformation.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProxyIpInfo) {
            RecruitInfo proxy = (RecruitInfo) obj;
            return postName.equalsIgnoreCase(proxy.getPostName().trim())
                    && companyName.equals(proxy.getCompanyName())
                    && jobInformation.equals(proxy.getJobInformation().trim());
        }

        return false;
    }

    /**
     * 设置XZ
     */
    public void setMultiSalary() {
        if (!StringUtils.isEmpty(this.salary)) {
            BigDecimal[] salarys = TextUtils.splitSalary(this);
            if (salarys != null) {
                this.minSalary = salarys[0];
                this.maxSalary = salarys[1];
            }
        }
    }

    /**
     * 设置经验属性
     */
    public void setMultiExperience() {
        if (!StringUtils.isEmpty(this.experience)) {
            int[] experiences = TextUtils.splitExperience(this);
            this.minExp = experiences[0];
            this.maxExp = experiences[1];
        }
    }


    @Override
    public String toString() {
        return "RecruitInfo{" +
                "postName='" + postName + '\'' +
                ", experience='" + experience + '\'' +
                ", number='" + number + '\'' +
                ", contact='" + contact + '\'' +
                ", salary='" + salary + '\'' +
                ", area='" + area + '\'' +
                ", education='" + education + '\'' +
                ", welfare='" + welfare + '\'' +
                ", jobInformation='" + jobInformation + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyType='" + companyType + '\'' +
                ", companySize='" + companySize + '\'' +
                ", companyIndustry='" + companyIndustry + '\'' +
                ", companyDesc='" + companyDesc + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", source='" + source + '\'' +
                ", identificationCode='" + identificationCode + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                '}';
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName == null ? null : postName.trim();
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience == null ? null : experience.trim();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType == null ? null : companyType.trim();
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize == null ? null : companySize.trim();
    }

    public String getCompanyIndustry() {
        return companyIndustry;
    }

    public void setCompanyIndustry(String companyIndustry) {
        this.companyIndustry = companyIndustry == null ? null : companyIndustry.trim();
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate == null ? null : releaseDate.trim();
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }

    public String getJobInformation() {
        return jobInformation;
    }

    public void setJobInformation(String jobInformation) {
        this.jobInformation = jobInformation;
    }

    public String getCompanyDesc() {
        return companyDesc;
    }

    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIdentificationCode() {
        return String.valueOf(hashCode());
    }

    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getReleaseDates() {
        return releaseDates;
    }

    public void setReleaseDates(String releaseDates) {
        this.releaseDates = releaseDates;
    }

    public int getReleaseCount() {
        return releaseCount;
    }

    public void setReleaseCount(int releaseCount) {
        this.releaseCount = releaseCount;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public int getMinExp() {
        return minExp;
    }

    public void setMinExp(int minExp) {
        this.minExp = minExp;
    }

    public int getMaxExp() {
        return maxExp;
    }

    public void setMaxExp(int maxExp) {
        this.maxExp = maxExp;
    }
}