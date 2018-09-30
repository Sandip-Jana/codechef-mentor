
package com.hackathon.codechefapp.model.contests.ShowProblems;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("isParent")
    @Expose
    private Boolean isParent;
    @SerializedName("children")
    @Expose
    private List<Object> children = null;
    @SerializedName("bannerFile")
    @Expose
    private String bannerFile;
    @SerializedName("freezingTime")
    @Expose
    private Integer freezingTime;
    @SerializedName("announcements")
    @Expose
    private String announcements;
    @SerializedName("problemsList")
    @Expose
    private List<ProblemsList> problemsList = null;
    @SerializedName("currentTime")
    @Expose
    private Integer currentTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public List<Object> getChildren() {
        return children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }

    public String getBannerFile() {
        return bannerFile;
    }

    public void setBannerFile(String bannerFile) {
        this.bannerFile = bannerFile;
    }

    public Integer getFreezingTime() {
        return freezingTime;
    }

    public void setFreezingTime(Integer freezingTime) {
        this.freezingTime = freezingTime;
    }

    public String getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(String announcements) {
        this.announcements = announcements;
    }

    public List<ProblemsList> getProblemsList() {
        return problemsList;
    }

    public void setProblemsList(List<ProblemsList> problemsList) {
        this.problemsList = problemsList;
    }

    public Integer getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Integer currentTime) {
        this.currentTime = currentTime;
    }

}
