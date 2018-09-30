
package com.hackathon.codechefapp.model.contests.ShowProblems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProblemsList {

    @SerializedName("viewStart")
    @Expose
    private String viewStart;
    @SerializedName("submitStart")
    @Expose
    private String submitStart;
    @SerializedName("visibleStart")
    @Expose
    private String visibleStart;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("problemCode")
    @Expose
    private String problemCode;
    @SerializedName("contestCode")
    @Expose
    private String contestCode;
    @SerializedName("successfulSubmissions")
    @Expose
    private Integer successfulSubmissions;
    @SerializedName("accuracy")
    @Expose
    private Double accuracy;

    public String getViewStart() {
        return viewStart;
    }

    public void setViewStart(String viewStart) {
        this.viewStart = viewStart;
    }

    public String getSubmitStart() {
        return submitStart;
    }

    public void setSubmitStart(String submitStart) {
        this.submitStart = submitStart;
    }

    public String getVisibleStart() {
        return visibleStart;
    }

    public void setVisibleStart(String visibleStart) {
        this.visibleStart = visibleStart;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getProblemCode() {
        return problemCode;
    }

    public void setProblemCode(String problemCode) {
        this.problemCode = problemCode;
    }

    public String getContestCode() {
        return contestCode;
    }

    public void setContestCode(String contestCode) {
        this.contestCode = contestCode;
    }

    public Integer getSuccessfulSubmissions() {
        return successfulSubmissions;
    }

    public void setSuccessfulSubmissions(Integer successfulSubmissions) {
        this.successfulSubmissions = successfulSubmissions;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

}
