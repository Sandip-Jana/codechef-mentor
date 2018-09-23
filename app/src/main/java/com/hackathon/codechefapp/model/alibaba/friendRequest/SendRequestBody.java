package com.hackathon.codechefapp.model.alibaba.friendRequest;

/**
 * Created by SANDIP JANA on 19-09-2018.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "mentor_name",
        "current_user"
})
public class SendRequestBody {

    @JsonProperty("mentor_name")
    private String mentor_name;
    @JsonProperty("current_user")
    private String current_user;

    @JsonProperty("mentor_name")
    public String getMentor_name() {
        return mentor_name;
    }

    @JsonProperty("mentor_name")
    public void setMentor_name(String mentor_name) {
        this.mentor_name = mentor_name;
    }

    @JsonProperty("current_user")
    public String getCurrent_user() {
        return current_user;
    }

    @JsonProperty("current_user")
    public void setCurrent_user(String current_user) {
        this.current_user = current_user;
    }
}