package com.hackathon.codechefapp.model.leaderboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SANDIP JANA on 29-09-2018.
 */

public class LeaderBoardResponse {

    @SerializedName("current_user")
    @Expose
    private String currentUser;
    @SerializedName("score")
    @Expose
    private Integer score;

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

}