
package com.hackathon.codechefapp.model.contests.ShowContests;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("currentTime")
    @Expose
    private Integer currentTime;
    @SerializedName("contestList")
    @Expose
    private List<ContestList> contestList = null;

    public Integer getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Integer currentTime) {
        this.currentTime = currentTime;
    }

    public List<ContestList> getContestList() {
        return contestList;
    }

    public void setContestList(List<ContestList> contestList) {
        this.contestList = contestList;
    }

}
