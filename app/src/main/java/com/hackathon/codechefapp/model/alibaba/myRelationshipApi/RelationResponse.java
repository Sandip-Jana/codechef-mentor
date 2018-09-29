package com.hackathon.codechefapp.model.alibaba.myRelationshipApi;

/**
 * Created by SANDIP JANA on 19-09-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RelationResponse {

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
    private String room_id;

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
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