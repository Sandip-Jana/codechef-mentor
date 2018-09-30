package com.hackathon.codechefapp.activities.nav.contest.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.activities.nav.contest.ContestActivity;
import com.hackathon.codechefapp.adapter.ContestAdapter;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.listener.OnItemClickListener;
import com.hackathon.codechefapp.model.contests.ShowContests.ContestList;
import com.hackathon.codechefapp.model.contests.ShowContests.ContestResponse;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.retrofitmapping.IChef;
import com.hackathon.codechefapp.utils.DisplayToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowContests extends Fragment implements OnItemClickListener {

    private ProgressBar progressBar;
    private Activity activity;
    private RecyclerView contestsRecyclerView;
    private ContestAdapter adapter;

    private List<ContestList> contestList;
    private SharedPreferenceUtils prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contests, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = getActivity();

        progressBar = view.findViewById(R.id.progressBar);

        contestsRecyclerView = view.findViewById(R.id.contestsRecyclerView);
        contestsRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        contestsRecyclerView.setHasFixedSize(false);

        prefs = SharedPreferenceUtils.getInstance(activity.getApplicationContext());

        fetchContestsApi();
    }

    private void fetchContestsApi() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new RetrofitClient().get(activity);
        IChef ichef = retrofit.create(IChef.class);

        String authToken = "Bearer " + prefs.getStringValue(PreferenceConstants.ACCESS_TOKEN, "");

        Call<ContestResponse> fetchContests = ichef.getContests(authToken);
        fetchContests.enqueue(new Callback<ContestResponse>() {
            @Override
            public void onResponse(Call<ContestResponse> call, Response<ContestResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equalsIgnoreCase(Constants.RESPONSE_OK)) {
                    renderResponseToUI(response.body());
                } else {
                    DisplayToast.makeSnackbar(getView().getRootView(), getString(R.string.error_message));
                }
            }

            @Override
            public void onFailure(Call<ContestResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                DisplayToast.makeSnackbar(getView().getRootView(), getString(R.string.error_message));
            }
        });
    }

    private void renderResponseToUI(ContestResponse response) {
        if (response.getResult() == null || response.getResult().getData() == null || response.getResult().getData().getContent() == null ||
                response.getResult().getData().getContent().getContestList() == null) {
            DisplayToast.makeSnackbar(getView().getRootView(), getString(R.string.error_message));
            return;
        }
        contestList = response.getResult().getData().getContent().getContestList();
        adapter = new ContestAdapter(contestList);
        adapter.setItemClickListener(this);
        contestsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (contestList != null && contestList.size() > position) {
            ((ContestActivity) activity).showProblemsFragment(contestList.get(position).getCode());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
