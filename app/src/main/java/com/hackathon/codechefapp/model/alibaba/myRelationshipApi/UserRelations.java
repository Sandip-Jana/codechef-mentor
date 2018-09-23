package com.hackathon.codechefapp.model.alibaba.myRelationshipApi;

/**
 * Created by SANDIP JANA on 19-09-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRelations {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("response")
    @Expose
    private List<RelationResponse> response = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RelationResponse> getResponse() {
        return response;
    }

    public void setResponse(List<RelationResponse> response) {
        this.response = response;
    }

}