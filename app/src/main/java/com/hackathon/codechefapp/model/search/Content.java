
package com.hackathon.codechefapp.model.search;

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
        StringTokenizer stk = new StringTokenizer(getFullname() ," ");
        int countTokens = stk.countTokens();
        String fullName = "";
        for(int i=0 ; i<countTokens ; i++) {
            String token = stk.nextToken();
            if(token.length()>0) {
                String firstChar = token.substring(0, 1).toUpperCase();
                String remainingChars = token.substring(1);
                fullName = fullName + firstChar + remainingChars;
                if (i != countTokens - 1) {
                    fullName += " ";
                }
            }
        }
        return fullName;
    }
}
