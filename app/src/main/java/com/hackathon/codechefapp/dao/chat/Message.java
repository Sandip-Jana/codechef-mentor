package com.hackathon.codechefapp.dao.chat;

/**
 * Created by SANDIP JANA on 23-09-2018.
 */
public class Message {

    private String message;
    private String senderName;
    private String time;
    private boolean currentUser ;

    public boolean isCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(boolean currentUser) {
        this.currentUser = currentUser;
    }

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
