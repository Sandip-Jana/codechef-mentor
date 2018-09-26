package com.hackathon.codechefapp.retrofitmapping;

import com.hackathon.codechefapp.dao.AccessTokenBody;
import com.hackathon.codechefapp.dao.RefreshTokenBody;
import com.hackathon.codechefapp.dao.chat.ChatAuthResponse;
import com.hackathon.codechefapp.model.alibaba.friendRequest.SendRequestBody;
import com.hackathon.codechefapp.model.alibaba.friendRequest.SendRequestResponse;
import com.hackathon.codechefapp.model.alibaba.mentor_student.MentorOrStudent;
import com.hackathon.codechefapp.model.alibaba.mentor_student.StudentAcceptRejectBody;
import com.hackathon.codechefapp.model.alibaba.myRelationshipApi.UserRelations;
import com.hackathon.codechefapp.model.chat.ChatAuthBody;
import com.hackathon.codechefapp.model.login.AccessToken;
import com.hackathon.codechefapp.model.profile.Profile;
import com.hackathon.codechefapp.model.search.Search;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import static com.hackathon.codechefapp.constants.URLConstants.CHAT_AUTHENTICATE;
import static com.hackathon.codechefapp.constants.URLConstants.GET_CODECHEF_PROFILE;
import static com.hackathon.codechefapp.constants.URLConstants.GET_MENTORS;
import static com.hackathon.codechefapp.constants.URLConstants.GET_STUDENTS;
import static com.hackathon.codechefapp.constants.URLConstants.GET_USER_RELATIONS;
import static com.hackathon.codechefapp.constants.URLConstants.OATH;
import static com.hackathon.codechefapp.constants.URLConstants.PROFILE;
import static com.hackathon.codechefapp.constants.URLConstants.REQUEST_PUT_API;
import static com.hackathon.codechefapp.constants.URLConstants.SEARCH_BY_USERNAME;
import static com.hackathon.codechefapp.constants.URLConstants.SEND_MENTOR_REQUEST;

/**
 * Created by SANDIP JANA on 09-09-2018.
 */
public interface IChef {

    @Headers("content-Type: application/json")
    @POST(OATH)
    Call<AccessToken> getAccessToken(@Body AccessTokenBody body);

    @Headers("content-Type: application/json")
    @POST(OATH)
    Call<AccessToken> getRefreshToken(@Body RefreshTokenBody body);


    @Headers("Accept: application/json")
    @GET(PROFILE)
    Call<Profile> getUserProfile(@Header("Authorization") String authToken);

    @Headers("Accept: application/json")
    @GET(SEARCH_BY_USERNAME)
    Call<Search> searchUsersByName(@Header("Authorization") String accessToken, @QueryMap Map<String, String> data);

    @Headers("Content-Type: application/json")
    @POST(SEND_MENTOR_REQUEST)
    Call<SendRequestResponse> sendMentorRequest(@Body SendRequestBody body);

    @Headers("Content-Type: application/json")
    @GET(GET_USER_RELATIONS)
    Call<UserRelations> getUserRelations();

    @Headers("Content-Type: application/json")
    @GET(GET_CODECHEF_PROFILE)
    Call<Profile> getCodeChefProfileDetails(@Header("Authorization") String authToken, @Path("username") String username);

    @Headers("Content-Type: application/json")
    @GET(GET_STUDENTS)
    Call<MentorOrStudent> studentApi();

    @Headers("Content-Type: application/json")
    @GET(GET_MENTORS)
    Call<MentorOrStudent> mentorApi();

    @Headers("Content-Type: application/json")
    @PUT(REQUEST_PUT_API)
    Call<SendRequestResponse> putRequestAcceptedOrRejected(@Path("username") String username, @Body StudentAcceptRejectBody body);

    @Headers("Content-Type: application/json")
    @POST(CHAT_AUTHENTICATE)
    Call<ChatAuthResponse> chatAuthentication(@Body ChatAuthBody Body);


}
