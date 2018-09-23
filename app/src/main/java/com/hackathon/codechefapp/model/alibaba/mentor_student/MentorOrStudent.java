package com.hackathon.codechefapp.model.alibaba.mentor_student;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SANDIP JANA on 22-09-2018.
 */
public class MentorOrStudent {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("response")
    @Expose
    private List<MentorStudentResponse> mentorStudentResponse = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MentorStudentResponse> getMentorStudentResponse() {
        return mentorStudentResponse;
    }

    public void setMentorStudentResponse(List<MentorStudentResponse> mentorStudentResponse) {
        this.mentorStudentResponse = mentorStudentResponse;
    }

}