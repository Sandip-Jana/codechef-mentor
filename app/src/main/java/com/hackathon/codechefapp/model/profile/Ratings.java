
package com.hackathon.codechefapp.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ratings {

    @SerializedName("allContest")
    @Expose
    private Integer allContest;
    @SerializedName("long")
    @Expose
    private Integer _long;
    @SerializedName("short")
    @Expose
    private Integer _short;
    @SerializedName("lTime")
    @Expose
    private Integer lTime;
    @SerializedName("allSchoolContest")
    @Expose
    private Integer allSchoolContest;
    @SerializedName("longSchool")
    @Expose
    private Integer longSchool;
    @SerializedName("shortSchool")
    @Expose
    private Integer shortSchool;
    @SerializedName("lTimeSchool")
    @Expose
    private Integer lTimeSchool;

    public Integer getAllContest() {
        return allContest;
    }

    public void setAllContest(Integer allContest) {
        this.allContest = allContest;
    }

    public Integer getLong() {
        return _long;
    }

    public void setLong(Integer _long) {
        this._long = _long;
    }

    public Integer getShort() {
        return _short;
    }

    public void setShort(Integer _short) {
        this._short = _short;
    }

    public Integer getLTime() {
        return lTime;
    }

    public void setLTime(Integer lTime) {
        this.lTime = lTime;
    }

    public Integer getAllSchoolContest() {
        return allSchoolContest;
    }

    public void setAllSchoolContest(Integer allSchoolContest) {
        this.allSchoolContest = allSchoolContest;
    }

    public Integer getLongSchool() {
        return longSchool;
    }

    public void setLongSchool(Integer longSchool) {
        this.longSchool = longSchool;
    }

    public Integer getShortSchool() {
        return shortSchool;
    }

    public void setShortSchool(Integer shortSchool) {
        this.shortSchool = shortSchool;
    }

    public Integer getLTimeSchool() {
        return lTimeSchool;
    }

    public void setLTimeSchool(Integer lTimeSchool) {
        this.lTimeSchool = lTimeSchool;
    }

}
