package com.hackathon.codechefapp.dao.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SANDIP JANA on 24-09-2018.
 */
public class ChatAuthResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("current_user")
    @Expose
    private String currentUser;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

}