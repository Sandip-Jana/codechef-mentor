package com.hackathon.codechefapp.activities.nav.contest.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.activities.nav.contest.ContestActivity;
import com.hackathon.codechefapp.adapter.ProblemsFragmentAdapter;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.listener.OnItemClickListener;
import com.hackathon.codechefapp.model.contests.ShowProblems.ProblemsList;
import com.hackathon.codechefapp.model.contests.ShowProblems.ProblemsResponse;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.retrofitmapping.IChef;
import com.hackathon.codechefapp.utils.DisplayToast;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowProblemsFragment extends Fragment implements OnItemClickListener {

    private static final String TAG = ShowProblemsFragment.class.getSimpleName();
    private TextView contestNameTxt;
    private TextView noDataTxt;
    private ImageView bannerFile;

    private ProgressBar progressBar;

    private RecyclerView problemsRecyclerView;
    private ProblemsFragmentAdapter adapter;

    private Activity activity;
    private String contestCode;

    private List<ProblemsList> problemsList;

    private SharedPreferenceUtils prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_problems, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = getActivity();

        prefs = SharedPreferenceUtils.getInstance(activity.getApplicationContext());

        contestNameTxt = view.findViewById(R.id.contestName);
        noDataTxt = view.findViewById(R.id.noDataTxt);
        bannerFile = view.findViewById(R.id.bannerFile);

        progressBar = view.findViewById(R.id.progressBarProblems);

        problemsRecyclerView = view.findViewById(R.id.problemsRecyclerView);
        problemsRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        problemsRecyclerView.setHasFixedSize(false);

        contestCode = prefs.getStringValue(PreferenceConstants.CONTESTCODE, "");

        fetchContestDetailsApi();
    }

    private void fetchContestDetailsApi() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new RetrofitClient().get(activity);
        IChef ichef = retrofit.create(IChef.class);
        String authToken = "Bearer " + prefs.getStringValue(PreferenceConstants.ACCESS_TOKEN, "");
        Call<ProblemsResponse> getProblemsApi = ichef.getProblems(authToken, contestCode);
        getProblemsApi.enqueue(new Callback<ProblemsResponse>() {
            @Override
            public void onResponse(Call<ProblemsResponse> call, Response<ProblemsResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful() && response.body() != null) {
                    renderResponseToUI(response.body());
                } else {
                    noDataTxt.setVisibility(View.VISIBLE);
                    DisplayToast.makeSnackbar(getView().getRootView(), getString(R.string.error_message));
                }
            }

            @Override
            public void onFailure(Call<ProblemsResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                noDataTxt.setVisibility(View.VISIBLE);
                DisplayToast.makeSnackbar(getView().getRootView(), getString(R.string.error_message));
            }
        });
    }

    private void renderResponseToUI(ProblemsResponse response) {
        if (response != null && response.getStatus() != null && response.getStatus().equalsIgnoreCase(Constants.RESPONSE_OK)) {
            Picasso.get().load(response.getResult().getData().getContent().getBannerFile())
                    .placeholder(R.drawable.piccasso_banner)
                    .error(R.drawable.codechef_face)
                    .into(bannerFile);

            contestNameTxt.setText(response.getResult().getData().getContent().getName());
            problemsList = response.getResult().getData().getContent().getProblemsList();

            if (problemsList != null && problemsList.size() > 0) {
                renderProblemsListToUI();
            } else {
                noDataTxt.setVisibility(View.VISIBLE);
            }

        } else {
            noDataTxt.setVisibility(View.VISIBLE);
            DisplayToast.makeSnackbar(getView().getRootView(), getString(R.string.error_message));
        }
    }

    private void renderProblemsListToUI() {
        adapter = new ProblemsFragmentAdapter(problemsList);
        adapter.setItemClickListener(this);
        problemsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, position + "  showProblemsFragment " + problemsList.get(position).getProblemCode());
        if (problemsList != null && problemsList.size() > position) {
            ((ContestActivity) activity).showProblemBody(problemsList.get(position).getProblemCode(), problemsList.get(position).getContestCode());
            ((ContestActivity) getActivity()).getSupportActionBar().setTitle(problemsList.get(position).getProblemCode());
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
