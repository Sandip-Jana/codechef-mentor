package com.hackathon.codechefapp.activities.nav.contest.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.model.contests.problemDesc.ProblemDescription;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.retrofitmapping.IChef;
import com.hackathon.codechefapp.utils.DisplayToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProblemDescriptionFragment extends Fragment {


    private Activity activity;

    private WebView problemBody;
    private TextView noBodyTxt;

    private ProgressBar progressBar;

    private SharedPreferenceUtils prefs;

    String contestCode;
    String problemCode;

    private boolean isVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_problem_description, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = getActivity();

        problemBody = view.findViewById(R.id.problemBody);
        progressBar = view.findViewById(R.id.progressBar);
        noBodyTxt = view.findViewById(R.id.noDataTxt);

        prefs = SharedPreferenceUtils.getInstance(activity.getApplicationContext());

        isVisible = true;
    }

    private void fecthProblemDescription() {
        progressBar.setVisibility(View.VISIBLE);

        String authToken = "Bearer " + prefs.getStringValue(PreferenceConstants.ACCESS_TOKEN, "");
        contestCode = prefs.getStringValue(PreferenceConstants.CONTESTCODE, "");
        problemCode = prefs.getStringValue(PreferenceConstants.PROBLEM_CODE, "");

        Log.d("results", contestCode + " " + problemCode);

        Retrofit retrofit = new RetrofitClient().get(activity);
        IChef iChef = retrofit.create(IChef.class);
        Call<ProblemDescription> fecthProblemDescriptionApi = iChef.getProblemDesc(authToken, contestCode, problemCode);
        fecthProblemDescriptionApi.enqueue(new Callback<ProblemDescription>() {
            @Override
            public void onResponse(Call<ProblemDescription> call, Response<ProblemDescription> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful() && response.body() != null) {
                    renderResponseToUI(response.body());
                } else {
                    noBodyTxt.setVisibility(View.VISIBLE);
                    DisplayToast.makeSnackbar(getView().getRootView(), getString(R.string.error_message));
                }
            }

            @Override
            public void onFailure(Call<ProblemDescription> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                noBodyTxt.setVisibility(View.VISIBLE);
                DisplayToast.makeSnackbar(getView().getRootView(), getString(R.string.error_message));
            }
        });
    }

    private void renderResponseToUI(ProblemDescription response) {
        if (response.getResult() == null || response.getResult().getData() == null ||
                response.getResult().getData().getContent() == null || response.getResult().getData().getContent().getBody() == null) {
            noBodyTxt.setVisibility(View.VISIBLE);
            DisplayToast.makeSnackbar(getView().getRootView(), response.getResult().getData().getMessage());
            return;
        }
        if (response.getStatus().equalsIgnoreCase(Constants.RESPONSE_OK)) {
            String htmlBody = parseHtml(response.getResult().getData().getContent().getBody());
            problemBody.loadData(htmlBody, "text/html", null);
        } else {
            noBodyTxt.setVisibility(View.VISIBLE);
            DisplayToast.makeSnackbar(getView().getRootView(), getString(R.string.error_message));
        }
    }

    private String parseHtml(String html) {
        boolean found = true;
        String ret = "";
        for (int i = 0; i < html.length(); i++) {
            if (html.charAt(i) == '$') {
                if (found) {
                    ret += "<b>";
                    found = !found;
                } else {
                    ret += "</b>";
                    found = !found;
                }
            } else {
                ret += html.charAt(i);
            }
        }

        ret = ret.replaceAll("\\\\leq", "<=");
        Log.d("results", ret);
        return ret;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisible) {
            fecthProblemDescription();
        }
        super.setUserVisibleHint(isVisibleToUser);
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
