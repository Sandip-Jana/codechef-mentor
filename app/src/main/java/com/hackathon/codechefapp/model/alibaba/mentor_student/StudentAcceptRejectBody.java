package com.hackathon.codechefapp.model.alibaba.mentor_student;

/**
 * Created by SANDIP JANA on 22-09-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentAcceptRejectBody {

    @SerializedName("student_name")
    @Expose
    private String studentName;
    @SerializedName("status")
    @Expose
    private String status;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}