
package com.hackathon.codechefapp.model.contests.problemDesc;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("problemCode")
    @Expose
    private String problemCode;
    @SerializedName("problemName")
    @Expose
    private String problemName;
    @SerializedName("dateAdded")
    @Expose
    private String dateAdded;
    @SerializedName("languagesSupported")
    @Expose
    private List<String> languagesSupported = null;
    @SerializedName("sourceSizeLimit")
    @Expose
    private Integer sourceSizeLimit;
    @SerializedName("maxTimeLimit")
    @Expose
    private Integer maxTimeLimit;
    @SerializedName("challengeType")
    @Expose
    private String challengeType;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("successfulSubmissions")
    @Expose
    private Integer successfulSubmissions;
    @SerializedName("totalSubmissions")
    @Expose
    private Integer totalSubmissions;
    @SerializedName("partialSubmissions")
    @Expose
    private Integer partialSubmissions;
    @SerializedName("tags")
    @Expose
    private List<Object> tags = null;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("user")
    @Expose
    private String user;

    public String getProblemCode() {
        return problemCode;
    }

    public void setProblemCode(String problemCode) {
        this.problemCode = problemCode;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public List<String> getLanguagesSupported() {
        return languagesSupported;
    }

    public void setLanguagesSupported(List<String> languagesSupported) {
        this.languagesSupported = languagesSupported;
    }

    public Integer getSourceSizeLimit() {
        return sourceSizeLimit;
    }

    public void setSourceSizeLimit(Integer sourceSizeLimit) {
        this.sourceSizeLimit = sourceSizeLimit;
    }

    public Integer getMaxTimeLimit() {
        return maxTimeLimit;
    }

    public void setMaxTimeLimit(Integer maxTimeLimit) {
        this.maxTimeLimit = maxTimeLimit;
    }

    public String getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(String challengeType) {
        this.challengeType = challengeType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getSuccessfulSubmissions() {
        return successfulSubmissions;
    }

    public void setSuccessfulSubmissions(Integer successfulSubmissions) {
        this.successfulSubmissions = successfulSubmissions;
    }

    public Integer getTotalSubmissions() {
        return totalSubmissions;
    }

    public void setTotalSubmissions(Integer totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }

    public Integer getPartialSubmissions() {
        return partialSubmissions;
    }

    public void setPartialSubmissions(Integer partialSubmissions) {
        this.partialSubmissions = partialSubmissions;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
