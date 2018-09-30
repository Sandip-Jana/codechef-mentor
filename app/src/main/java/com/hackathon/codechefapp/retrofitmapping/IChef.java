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
import com.hackathon.codechefapp.model.chat.RetrieveMessagesResponse;
import com.hackathon.codechefapp.model.contests.ShowContests.ContestResponse;
import com.hackathon.codechefapp.model.contests.ShowProblems.ProblemsResponse;
import com.hackathon.codechefapp.model.contests.problemDesc.ProblemDescription;
import com.hackathon.codechefapp.model.leaderboard.LeaderBoardResponse;
import com.hackathon.codechefapp.model.login.AccessToken;
import com.hackathon.codechefapp.model.profile.Profile;
import com.hackathon.codechefapp.model.search.Search;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import static com.hackathon.codechefapp.constants.URLConstants.CHAT_AUTHENTICATE;
import static com.hackathon.codechefapp.constants.URLConstants.CONTESTS;
import static com.hackathon.codechefapp.constants.URLConstants.GET_CODECHEF_PROFILE;
import static com.hackathon.codechefapp.constants.URLConstants.GET_MENTORS;
import static com.hackathon.codechefapp.constants.URLConstants.GET_PREVIOUS_MESSAGES;
import static com.hackathon.codechefapp.constants.URLConstants.GET_STUDENTS;
import static com.hackathon.codechefapp.constants.URLConstants.GET_USER_RELATIONS;
import static com.hackathon.codechefapp.constants.URLConstants.LEADERBOARD;
import static com.hackathon.codechefapp.constants.URLConstants.OATH;
import static com.hackathon.codechefapp.constants.URLConstants.PROBLEMS;
import static com.hackathon.codechefapp.constants.URLConstants.PROBLEM_DESC;
import static com.hackathon.codechefapp.constants.URLConstants.PROFILE;
import static com.hackathon.codechefapp.constants.URLConstants.REQUEST_PUT_API;
import static com.hackathon.codechefapp.constants.URLConstants.SEARCH_BY_USERNAME;
import static com.hackathon.codechefapp.constants.URLConstants.SEND_MENTOR_REQUEST;
import static com.hackathon.codechefapp.constants.URLConstants.UPLOAD_PROFILE_PIC;

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

    @Multipart
    @POST(UPLOAD_PROFILE_PIC)
    Call<Object> uploadProfilePic(@Part MultipartBody.Part image, @Part("name") RequestBody name);

    @Headers("Content-Type: application/json")
    @GET(GET_PREVIOUS_MESSAGES)
    Call<List<RetrieveMessagesResponse>> getPreviousMessages(@Path("roomid") String roomId);

    @Headers("Content-Type: application/json")
    @GET(LEADERBOARD)
    Call<List<LeaderBoardResponse>> getLeaderBoard(@Query("num") String usersCount);

    @Headers("Content-Type: application/json")
    @GET(CONTESTS)
    Call<ContestResponse> getContests(@Header("Authorization") String authToken);

    @Headers("Content-Type: application/json")
    @GET(PROBLEMS)
    Call<ProblemsResponse> getProblems(@Header("Authorization") String authToken , @Path("contestCode") String contestCode);

    @Headers("Content-Type: application/json")
    @GET(PROBLEM_DESC)
    Call<ProblemDescription> getProblemDesc(@Header("Authorization") String authToken , @Path("contestCode") String contestCode , @Path("problemCode") String problemCode);

}
