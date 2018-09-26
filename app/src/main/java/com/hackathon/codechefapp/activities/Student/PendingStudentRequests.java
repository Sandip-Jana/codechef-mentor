package com.hackathon.codechefapp.activities.Student;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.adapter.StudentRequestAdapter;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.listener.OnItemClickListener;
import com.hackathon.codechefapp.model.alibaba.friendRequest.SendRequestResponse;
import com.hackathon.codechefapp.model.alibaba.mentor_student.MentorOrStudent;
import com.hackathon.codechefapp.model.alibaba.mentor_student.MentorStudentResponse;
import com.hackathon.codechefapp.model.alibaba.mentor_student.StudentAcceptRejectBody;
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

public class PendingStudentRequests extends Fragment implements OnItemClickListener {

    private Activity activity;

    private SharedPreferenceUtils prefs;

    ArrayList<Content> pendingStudentRequests;

    private RecyclerView pendingStudentRequestsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private StudentRequestAdapter pendingRequestsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pending_student_requests, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        layoutManager = new LinearLayoutManager(activity);

        pendingStudentRequestsRecyclerView = view.findViewById(R.id.pendingStudentRequestsRecyclerView);
        pendingStudentRequestsRecyclerView.setHasFixedSize(false);
        pendingStudentRequestsRecyclerView.setLayoutManager(layoutManager);

        activity = getActivity();
        prefs = SharedPreferenceUtils.getInstance(activity.getApplicationContext());

        if (prefs.contains(PreferenceConstants.LOGGED_IN_USER_NAME))
            getPendingStudentRequests();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getPendingStudentRequests() {
        Retrofit retrofit = new RetrofitClient().getAlibabaCookiesApi(activity);
        IChef ichef = retrofit.create(IChef.class);
        Call<MentorOrStudent> pendingStudentApi = ichef.studentApi();

        pendingStudentApi.enqueue(new Callback<MentorOrStudent>() {
            @Override
            public void onResponse(Call<MentorOrStudent> call, Response<MentorOrStudent> response) {
                if (response.isSuccessful() && response != null && response.body() != null) {
                    parseStudentApiResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<MentorOrStudent> call, Throwable t) {
                DisplayToast.makeSnackbar(getView().getRootView(), String.valueOf(R.string.error_message));
            }
        });
    }

    private void parseStudentApiResponse(MentorOrStudent studentResponse) {
        if (studentResponse.getStatus() != null && studentResponse.getStatus().toLowerCase().trim().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)) {
            List<MentorStudentResponse> studentResponselist = studentResponse.getMentorStudentResponse();

            pendingStudentRequests = new ArrayList<>();

            for (int i = 0; i < studentResponselist.size(); i++) {
                if (studentResponselist.get(i).getStatus().toLowerCase().trim().equalsIgnoreCase(Constants.PENDING_STATUS)) {
                    Content rowItem = new Content();
                    rowItem.setUsername(studentResponselist.get(i).getUsername());
                    rowItem.setFullname("Codechef User");

                    pendingStudentRequests.add(rowItem);
                }
            }

            if (pendingStudentRequests.size() > 0) {
                pendingRequestsAdapter = new StudentRequestAdapter(pendingStudentRequests);
                pendingRequestsAdapter.setItemClickListener(this);
                pendingStudentRequestsRecyclerView.setAdapter(pendingRequestsAdapter);
            }

        } else {
            DisplayToast.makeSnackbar(getView().getRootView(), getString(R.string.error_message));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.acceptBtn) {
            callPutApiAcceptReject(pendingStudentRequests.get(position).getUsername(), Constants.APPROVED_STATUS);
        } else if (view.getId() == R.id.rejectBtn) {
            callPutApiAcceptReject(pendingStudentRequests.get(position).getUsername(), Constants.REJECTED_STATUS);
        }
    }

    private void callPutApiAcceptReject(final String username, final String status) {
        Retrofit retrofit = new RetrofitClient().getAlibabaCookiesApi(activity);
        IChef ichef = retrofit.create(IChef.class);

        StudentAcceptRejectBody body = new StudentAcceptRejectBody();
        body.setStudentName(username);
        body.setStatus(status);

        Call<SendRequestResponse> approvedStudentApi = ichef.putRequestAcceptedOrRejected(prefs.getStringValue(PreferenceConstants.LOGGED_IN_USER_NAME, ""), body);

        approvedStudentApi.enqueue(new Callback<SendRequestResponse>() {
            @Override
            public void onResponse(Call<SendRequestResponse> call, Response<SendRequestResponse> response) {
                if (response.isSuccessful() && response != null && response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        if (status.length() > 0)
                            DisplayToast.makeSnackbar(getView().getRootView(), "Request " + status.substring(0, 1).toUpperCase() + status.substring(1));

                        startActivityCodechefUser();
                        updateFragmentAdapter(username);

                    } else {
                        DisplayToast.makeSnackbar(getView().getRootView(), String.valueOf(R.string.error_message));
                    }
                } else {
                    DisplayToast.makeSnackbar(getView().getRootView(), String.valueOf(R.string.error_message));
                }
            }

            @Override
            public void onFailure(Call<SendRequestResponse> call, Throwable t) {
                DisplayToast.makeSnackbar(getView().getRootView(), String.valueOf(R.string.error_message));
            }
        });
    }

    private void startActivityCodechefUser() {
        ((MyStudents) activity).refreshBothFragments();
    }

    private void updateFragmentAdapter(String username) {
        for (int i = 0; i < pendingStudentRequests.size(); i++) {
            if (pendingStudentRequests.get(i).getUsername().toLowerCase().trim().equalsIgnoreCase(username)) {
                pendingStudentRequests.remove(pendingStudentRequests.get(i));
                break;
            }
        }
        pendingRequestsAdapter = new StudentRequestAdapter(pendingStudentRequests);
        pendingStudentRequestsRecyclerView.swapAdapter(pendingRequestsAdapter, true);
    }

}
