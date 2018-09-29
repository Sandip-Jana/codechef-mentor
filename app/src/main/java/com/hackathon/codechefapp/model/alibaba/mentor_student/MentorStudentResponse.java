package com.hackathon.codechefapp.model.alibaba.mentor_student;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SANDIP JANA on 22-09-2018.
 */

public class MentorStudentResponse {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("relationship")
    @Expose
    private String relationship;
    @SerializedName("room_id")
    @Expose
    private String roomId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

}