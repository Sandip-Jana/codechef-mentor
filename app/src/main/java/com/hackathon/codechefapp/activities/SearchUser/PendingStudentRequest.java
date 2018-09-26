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


public class PendingStudentRequest extends Fragment {

    private Activity activity;

    private ProgressBar progressBar;

    private Button sudentRequestPending;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pending_student_request, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = getActivity();

        sudentRequestPending = view.findViewById(R.id.studentRequestPending);

        progressBar = activity.findViewById(R.id.progressBarUser);

        sudentRequestPending.setEnabled(false);

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
