package com.hackathon.codechefapp.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SANDIP JANA on 28-09-2018.
 */

public class RetrieveMessagesResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("time")
    @Expose
    private String time;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

