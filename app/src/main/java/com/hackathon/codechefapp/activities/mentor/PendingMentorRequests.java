package com.hackathon.codechefapp.activities.mentor;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.adapter.MentorRequestAdapter;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.listener.OnItemClickListener;
import com.hackathon.codechefapp.model.alibaba.mentor_student.MentorOrStudent;
import com.hackathon.codechefapp.model.alibaba.mentor_student.MentorStudentResponse;
import com.hackathon.codechefapp.model.search.Content;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.retrofitmapping.IChef;
import com.hackathon.codechefapp.utils.DisplayToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PendingMentorRequests extends Fragment implements OnItemClickListener{

    private Activity activity;

    private SharedPreferenceUtils prefs;

    ArrayList<Content> pendingMentorRequests;

    private RecyclerView pendingMentortRequestsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private MentorRequestAdapter pendingRequestsAdapter;

    ArrayList<Content> pendingMentors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pending_mentor_requests, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        layoutManager = new LinearLayoutManager(activity);

        pendingMentortRequestsRecyclerView = view.findViewById(R.id.pendingMentorsRequestsRecyclerView);
        pendingMentortRequestsRecyclerView.setHasFixedSize(false);
        pendingMentortRequestsRecyclerView.setLayoutManager(layoutManager);

        activity = getActivity();
        prefs = SharedPreferenceUtils.getInstance(activity.getApplicationContext());

        if (prefs.contains(PreferenceConstants.LOGGED_IN_USER_NAME))
            getPendingMentorRequests(prefs.getStringValue(PreferenceConstants.LOGGED_IN_USER_NAME, ""));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getPendingMentorRequests(String userName) {
        Retrofit retrofit = new RetrofitClient().getAlibaba(activity);
        IChef ichef = retrofit.create(IChef.class);
        Call<MentorOrStudent> approvedMentorApi = ichef.mentorApi(userName);

        approvedMentorApi.enqueue(new Callback<MentorOrStudent>() {
            @Override
            public void onResponse(Call<MentorOrStudent> call, Response<MentorOrStudent> response) {
                if (response.isSuccessful() && response != null && response.body() != null) {
                    parseMentorApiResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<MentorOrStudent> call, Throwable t) {
                DisplayToast.makeSnackbar(getView().getRootView(), String.valueOf(R.string.error_message));
            }
        });
    }

    private void parseMentorApiResponse(MentorOrStudent mentorResponse) {
        if (mentorResponse.getStatus() != null && mentorResponse.getStatus().toLowerCase().trim().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)) {
            List<MentorStudentResponse> mentorResponselist = mentorResponse.getMentorStudentResponse();

            pendingMentors = new ArrayList<>();

            for (int i = 0; i < mentorResponselist.size(); i++) {
                if (mentorResponselist.get(i).getStatus().toLowerCase().trim().equalsIgnoreCase(Constants.PENDING_STATUS)) {
                    Content rowItem = new Content();
                    rowItem.setUsername(mentorResponselist.get(i).getUsername());
                    rowItem.setFullname("Codechef User");

                    pendingMentors.add(rowItem);
                }
            }

            if (pendingMentors.size() > 0) {
                pendingRequestsAdapter = new MentorRequestAdapter(pendingMentors);
                pendingRequestsAdapter.setItemClickListener(this);
                pendingMentortRequestsRecyclerView.setAdapter(pendingRequestsAdapter);
            }

        } else {
            DisplayToast.makeSnackbar(getView().getRootView(), getString(R.string.error_message));
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        if (pendingMentors != null && pendingMentors.size() > position) {
            startActivityCodechefUser(pendingMentors.get(position).getUsername(), Constants.PENDING_STATUS+Constants.DELIMETER+Constants.MENTOR);
        }
    }

    private void startActivityCodechefUser(String userName, String relationStatus) {
        ((MyMentors) activity).startActivityCodechefUser(userName, relationStatus);
    }


}