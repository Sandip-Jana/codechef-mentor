package com.hackathon.codechefapp.activities.nav.contest.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.hackathon.codechefapp.utils.DisplayDialog;
import com.hackathon.codechefapp.utils.DisplayToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProblemDescriptionFragment extends Fragment {


    private static final String TAG = ProblemDescriptionFragment.class.getSimpleName();
    private Activity activity;

    private WebView problemBody;
    private TextView noBodyTxt;

    private ProgressBar progressBar;

    private SharedPreferenceUtils prefs;

    String contestCode;
    String problemCode;

    private static final int PERMISSIONS = 102;
    private String permissionsNeeded[] = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

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

        fecthProblemDescription();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if (checkPermissions()) {
                createWebPrintJob2(problemBody);
            } else {
                requestPermissions();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void fecthProblemDescription() {
        progressBar.setVisibility(View.VISIBLE);

        String authToken = "Bearer " + prefs.getStringValue(PreferenceConstants.ACCESS_TOKEN, "");
        contestCode = prefs.getStringValue(PreferenceConstants.CONTESTCODE, "");
        problemCode = prefs.getStringValue(PreferenceConstants.PROBLEM_CODE, "");

        Log.d("work", contestCode + " " + problemCode);

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
            //htmlBody = htmlBody + "<br><br><p><b>Author Name : " + response.getResult().getData().getContent().getAuthor() + "</b></p>";
            problemBody.loadData(htmlBody, "text/html", "UTF-8");
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
        return ret;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    //permissions
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionsNeeded, PERMISSIONS);
        } else {
            DisplayToast.makeSnackbar(getView().getRootView(), "Sorry this feature is not available for You");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                createWebPrintJob2(problemBody);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    DisplayDialog.showMessageOKCancel("These permissions are mandatory for saving the question as PDF. Please allow access.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions();
                                    }
                                }
                            }, activity);
                    return;
                } else {
                    DisplayToast.makeSnackbar(getView().getRootView(), "Feature not available");
                }
            }
        }
    }

    private void createWebPrintJob2(WebView webView) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            @SuppressLint("WrongConstant") PrintManager printManager = (PrintManager) activity.getSystemService(Context.PRINT_SERVICE);

            PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

            String jobName = ((problemCode==null)?"Question":problemCode) + " Print";

            printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
        }
    }

}
