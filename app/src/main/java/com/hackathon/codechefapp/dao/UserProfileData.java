package com.hackathon.codechefapp.dao;

/**
 * Created by SANDIP JANA on 24-09-2018.
 */
public class UserProfileData {

    private String label;
    private int data;

    public UserProfileData(String label, int data) {
        this.label = label;
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

}
