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
import android.widget.TextView;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.adapter.SearchAdapter;
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

public class ApprovedStudents extends Fragment implements OnItemClickListener {

    private Activity activity;

    ArrayList<Content> approvedStudents;

    private RecyclerView approvedStudentsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SearchAdapter adapter;

    private TextView noApprovedStudentsTxt;

    private SharedPreferenceUtils prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_approved_students, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        layoutManager = new LinearLayoutManager(activity);

        approvedStudentsRecyclerView = view.findViewById(R.id.approvedStudentsRecyclerView);
        approvedStudentsRecyclerView.setHasFixedSize(false);
        approvedStudentsRecyclerView.setLayoutManager(layoutManager);

        noApprovedStudentsTxt = (TextView) view.findViewById(R.id.noApprovedStudentsTxt);

        activity = getActivity();

        prefs = SharedPreferenceUtils.getInstance(activity.getApplicationContext());

        if (prefs.contains(PreferenceConstants.LOGGED_IN_USER_NAME))
            getListOfapprovedStudents();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getListOfapprovedStudents() {
        Retrofit retrofit = new RetrofitClient().getAlibabaCookiesApi(activity);
        IChef ichef = retrofit.create(IChef.class);
        Call<MentorOrStudent> approvedStudentApi = ichef.studentApi();

        approvedStudentApi.enqueue(new Callback<MentorOrStudent>() {
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

            approvedStudents = new ArrayList<>();

            for (int i = 0; i < studentResponselist.size(); i++) {
                if (studentResponselist.get(i).getStatus().toLowerCase().trim().equalsIgnoreCase(Constants.APPROVED_STATUS)) {
                    Content rowItem = new Content();
                    rowItem.setUsername(studentResponselist.get(i).getUsername());
                    rowItem.setFullname("Codechef User");
                    rowItem.setRoom_id(studentResponselist.get(i).getRoomId());

                    approvedStudents.add(rowItem);
                }
            }

            if (approvedStudents.size() > 0) {
                adapter = new SearchAdapter(approvedStudents);
                adapter.setItemClickListener(this);
                approvedStudentsRecyclerView.setAdapter(adapter);
            } else {
                noApprovedStudentsTxt.setVisibility(View.VISIBLE);
            }
        } else {
            DisplayToast.makeSnackbar(getView().getRootView(), getString(R.string.error_message));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (approvedStudents != null && approvedStudents.size() > position) {
            startActivityCodechefUser(approvedStudents.get(position).getUsername(), Constants.APPROVED_STATUS + Constants.DELIMETER + Constants.STUDENT, approvedStudents.get(position).getRoom_id());
        }
    }

    private void startActivityCodechefUser(String userName, String relationStatus, String roomId) {
        ((MyStudents) activity).startActivityCodechefUser(userName, relationStatus, roomId);
    }

    public void updateFragmentAdapter() {
        if (prefs.contains(PreferenceConstants.LOGGED_IN_USER_NAME))
            getListOfapprovedStudents();
    }

}
