package com.hackathon.codechefapp.activities.SearchUser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.model.alibaba.friendRequest.SendRequestBody;
import com.hackathon.codechefapp.model.alibaba.friendRequest.SendRequestResponse;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.retrofitmapping.IChef;
import com.hackathon.codechefapp.utils.DisplayToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SendMentorRequest extends Fragment {

    private Button sendRequestBtn;
    private Activity activity;
    private String currentUser;
    private String mentorUser;

    private ProgressBar progressBar;

    private SharedPreferenceUtils prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_mentor_request, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = getActivity();
        sendRequestBtn = view.findViewById(R.id.sendRequestBtn);

        progressBar = activity.findViewById(R.id.progressBarUser);

        mentorUser = ((CodechefUser) activity).getCodechefProfileUsername();

        prefs = SharedPreferenceUtils.getInstance(getActivity().getApplicationContext());

        if (prefs.contains(PreferenceConstants.LOGGED_IN_USER_NAME)) {
            currentUser = prefs.getStringValue(PreferenceConstants.LOGGED_IN_USER_NAME, "");
        }

        sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null && currentUser.length() > 0)
                    sendMentorRequest();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void sendMentorRequest() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofitClient = new RetrofitClient().getAlibaba(activity);

        IChef request = retrofitClient.create(IChef.class);

        SendRequestBody body = new SendRequestBody();
        body.setCurrent_user(currentUser);
        body.setMentor_name(mentorUser);

        Call<SendRequestResponse> callSendRequestApi = request.sendMentorRequest(body);

        callSendRequestApi.enqueue(new Callback<SendRequestResponse>() {
            @Override
            public void onResponse(Call<SendRequestResponse> call, Response<SendRequestResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                int statusCode = response.code();
                if(response.isSuccessful() && response!=null && response.body()!=null) {
                    parseResponse( response.body() );
                }
            }

            @Override
            public void onFailure(Call<SendRequestResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void parseResponse( SendRequestResponse response ) {
        if( response.getStatus().toLowerCase().trim().equalsIgnoreCase("success") ) {
            DisplayToast.makeSnackbar(getView().getRootView() , "Mentor Request Sent");

            ((CodechefUser)activity).setMentorRequestSentStatus();

        } else if( response.getReason()!=null )  {
            DisplayToast.makeSnackbar(getView().getRootView() , response.getReason());
        }
        else {
            DisplayToast.makeSnackbar(getView().getRootView() , "Mentor Request Not Sent..Try Again");
        }
    }

}
