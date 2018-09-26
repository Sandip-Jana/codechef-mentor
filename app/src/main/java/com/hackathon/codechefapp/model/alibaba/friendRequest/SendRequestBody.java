package com.hackathon.codechefapp.model.alibaba.friendRequest;

/**
 * Created by SANDIP JANA on 19-09-2018.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "current_user",
        "mentor_name"
})
public class SendRequestBody {

    @JsonProperty("mentor_name")
    private String mentor_name;

    @JsonProperty("mentor_name")
    public String getMentor_name() {
        return mentor_name;
    }

    @JsonProperty("mentor_name")
    public void setMentor_name(String mentor_name) {
        this.mentor_name = mentor_name;
    }
    
}