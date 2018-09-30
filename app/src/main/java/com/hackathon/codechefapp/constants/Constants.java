package com.hackathon.codechefapp.constants;

/**
 * Created by SANDIP JANA on 09-09-2018.
 */
public final class Constants {

    // added for retrofit library read and write timeouts
    public static final int READ_TIMEOUT = 50;
    public static final int WRITE_TIMEOUT = 50;
    public static final int CONNECT_TIMEOUT = 50;

    //*constants for response*//
    public static final String RESPONSE_OK = "ok";
    public static final String RESPONSE_SUCCESS = "success";

    // added for adding to url paths in retrofit client
    public static final String fields = "fields";
    public static final String username = "username";

    // added to limit the number of fileds in search result
    public static final String limit = "limit";
    public static final String LIMIT_VALUE = "20";
    public static final String offset = "offset";
    public static final String OFFSET_VALUE = "1";
    public static final String search = "search";

    // saving in prefs delimeter separated
    public static final String DELIMETER = "--";

    // is it a relation
    public static final String RELATION = "relation";
    public static final String ROOM_ID = "room_id";

    // added for types of request status
    public static final String PENDING_STATUS = "pending";
    public static final String APPROVED_STATUS = "approved";
    public static final String REJECTED_STATUS = "rejected";

    // student or mentor
    public static final String STUDENT = "student";
    public static final String MENTOR = "mentor";

    //leaderboard
    public static final String USERS_COUNT = "10";

    //general chat rooms
    public static final String GENERAL_CHAT_ROOM = "-1";
    public static final String IOI_CHAT_ROOM = "-2";
    public static final String ACM_CHAT_ROOM = "-3";
    public static final String PLACEMENT_CHAT_ROOM = "-4";

}
