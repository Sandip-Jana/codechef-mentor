package com.hackathon.codechefapp.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SANDIP JANA on 24-09-2018.
 */
public class ChatAuthBody {

    @SerializedName("current_user")
    @Expose
    private String currentUser;

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

}