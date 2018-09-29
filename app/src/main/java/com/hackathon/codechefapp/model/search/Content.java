
package com.hackathon.codechefapp.model.search;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.StringTokenizer;

public class Content {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("fullname")
    @Expose
    private String fullname;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsernameCamelCase() {
        String trimmedName = getFullname().trim();
        Log.d("name", " " + trimmedName);
        StringTokenizer stk = new StringTokenizer(trimmedName, " ");
        int countTokens = stk.countTokens();
        String name = "";
        for (int i = 0; i < countTokens; i++) {
            String token = stk.nextToken();
            if (token.length() > 0) {
                String firstChar = token.substring(0, 1).toUpperCase();
                String remainingChars = token.substring(1);
                name = name + firstChar + remainingChars;
                if (i != countTokens - 1) {
                    name += " ";
                }
            }
        }
        Log.d("name", name);
        return name;
    }
}
