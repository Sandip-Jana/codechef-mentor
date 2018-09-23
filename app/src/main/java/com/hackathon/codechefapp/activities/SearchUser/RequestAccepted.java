package com.hackathon.codechefapp.activities.SearchUser;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hackathon.codechefapp.R;

public class RequestAccepted extends Fragment {

    private Activity activity;

    private ProgressBar progressBar;

    private Button acceptedBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request_accepted, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = getActivity();
        acceptedBtn = view.findViewById(R.id.requestAccepted);
        addListeners();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void addListeners() {
        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CodechefUser) activity).startChatActicity();
            }
        });
    }
}
