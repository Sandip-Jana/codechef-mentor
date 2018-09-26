package com.hackathon.codechefapp.activities.SearchUser;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.activities.chat.ChatActivity;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.custom.ZoomOutPageTransformer;
import com.hackathon.codechefapp.dao.UserProfileData;
import com.hackathon.codechefapp.model.profile.Content;
import com.hackathon.codechefapp.model.profile.Profile;
import com.hackathon.codechefapp.model.profile.SubmissionStats;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.retrofitmapping.IChef;
import com.hackathon.codechefapp.utils.DisplayToast;

import java.util.ArrayList;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CodechefUser extends AppCompatActivity implements OnChartValueSelectedListener {

    private static final int NUM_PAGES = 4;

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private ProgressBar progressBarSearch;

    private String codechefProfileUsername;

    private SharedPreferenceUtils prefs;

    private View user;

    private TextView username;

    private TextView allContestRating;

    private TextView myGlobalRank;
    private TextView myCountryRank;

    private TextView longChallengeRating;
    private TextView cookOffRating;
    private TextView lunchTimeRating;

    private TextView longChallengeGlobalRank;
    private TextView cookOffGlobalRank;
    private TextView lunchTimeGlobalRank;

    private TextView longChallengeCountryRank;
    private TextView cookOffCountryRank;
    private TextView lunchTimeCountryRank;

    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codechef_user);

        pager = findViewById(R.id.btnPager);
        progressBarSearch = findViewById(R.id.progressBarUser);

        prefs = SharedPreferenceUtils.getInstance(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = findViewById(R.id.userLayout);

        username = findViewById(R.id.username);

        allContestRating = findViewById(R.id.allContestRating);

        myGlobalRank = findViewById(R.id.myGlobalRank);
        myCountryRank = findViewById(R.id.myCountryRank);

        longChallengeRating = findViewById(R.id.longChallengeRating);
        cookOffRating = findViewById(R.id.cookOffRating);
        lunchTimeRating = findViewById(R.id.lunchTimeRating);

        longChallengeGlobalRank = findViewById(R.id.longChallengeGlobalRank);
        cookOffGlobalRank = findViewById(R.id.cookOffGlobalRank);
        lunchTimeGlobalRank = findViewById(R.id.lunchTimeGlobalRank);

        longChallengeCountryRank = findViewById(R.id.longChallengeCountryRank);
        cookOffCountryRank = findViewById(R.id.cookOffCountryRank);
        lunchTimeCountryRank = findViewById(R.id.lunchTimeCountryRank);

        pieChart = findViewById(R.id.pieChart);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || !bundle.containsKey(Constants.username)) {
            DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.error_fetching_user_profile));
            finish();
            return;
        }

        String username = (String) bundle.get(Constants.username);

        setCodechefProfileUsername(username);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(pagerAdapter);

        if (bundle != null && bundle.containsKey(Constants.RELATION)) {
            StringTokenizer tokenizer = new StringTokenizer((String) bundle.get(Constants.RELATION), Constants.DELIMETER);

            switch (tokenizer.nextToken()) {
                case Constants.PENDING_STATUS:
                    String relation = tokenizer.nextToken();
                    if (relation.toLowerCase().trim().equalsIgnoreCase(Constants.MENTOR)) {
                        setMentorRequestSentStatus();
                    } else if (relation.toLowerCase().trim().equalsIgnoreCase(Constants.STUDENT)) {
                        setPendingStudentRequest();
                    }
                    break;
                case Constants.APPROVED_STATUS:
                    setApprovedStatus();
                    break;
                case Constants.REJECTED_STATUS:
                    //no status normal
                    break;
                default:
                    break;
            }

        }

        fetchUserDetails(username);
    }

    private void fetchUserDetails(String userName) {
        progressBarSearch.setVisibility(View.VISIBLE);

        Retrofit retrofit = new RetrofitClient().get(this);
        final IChef ichef = retrofit.create(IChef.class);

        String authToken = "Bearer " + prefs.getStringValue(PreferenceConstants.ACCESS_TOKEN, "");

        Log.d("CodechefUser", authToken);

        Call<Profile> userDetails = ichef.getCodeChefProfileDetails(authToken, userName);
        userDetails.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                progressBarSearch.setVisibility(View.INVISIBLE);
                if (response.isSuccessful() && response != null && response.body() != null) {
                    renderProfileDetailsToUI(response.body());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                progressBarSearch.setVisibility(View.INVISIBLE);
                DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.error_message));
            }
        });
    }

    public String getCodechefProfileUsername() {
        return codechefProfileUsername;
    }

    public void setCodechefProfileUsername(String codechefProfileUsername) {
        this.codechefProfileUsername = codechefProfileUsername;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SendMentorRequest();
                case 1:
                    return new MentorRequestSent();
                case 2:
                    return new PendingStudentRequest();
                case 3:
                    return new RequestAccepted();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void setMentorRequestSentStatus() {
        pager.setCurrentItem(1);
    }

    public void setPendingStudentRequest() {
        pager.setCurrentItem(2);
    }

    public void setApprovedStatus() {
        pager.setCurrentItem(3);
    }

    private void renderProfileDetailsToUI(Profile profile) {
        if (profile != null && !profile.getStatus().toLowerCase().trim().equalsIgnoreCase(Constants.RESPONSE_OK)) {
            return;
        }
        Content content = profile.getResult().getData().getContent();

        username.setText(content.getUsername());

        allContestRating.setText(content.getRatings().getAllContest().toString());

        myGlobalRank.setText(content.getRankings().getAllContestRanking().getGlobal().toString());
        myCountryRank.setText(content.getRankings().getAllContestRanking().getCountry().toString());

        longChallengeRating.setText(content.getRatings().getLong().toString());
        cookOffRating.setText(content.getRatings().getShort().toString());
        lunchTimeRating.setText(content.getRatings().getLTime().toString());

        longChallengeGlobalRank.setText(content.getRankings().getLongRanking().getGlobal().toString());
        cookOffGlobalRank.setText(content.getRankings().getShortRanking().getGlobal().toString());
        lunchTimeGlobalRank.setText(content.getRankings().getLtimeRanking().getGlobal().toString());

        longChallengeCountryRank.setText(content.getRankings().getLongRanking().getCountry().toString());
        cookOffCountryRank.setText(content.getRankings().getShortRanking().getCountry().toString());
        lunchTimeCountryRank.setText(content.getRankings().getLtimeRanking().getCountry().toString());

        //get Submission Stats and fill pie chart
        SubmissionStats stats = content.getSubmissionStats();
        int solvedProblems = stats.getSolvedProblems();
        int attemptedProblems = stats.getAttemptedProblems();
        int submittedSolutions = stats.getSubmittedSolutions();
        int wrongSubmissions = stats.getWrongSubmissions();
        int runTimeError = stats.getRunTimeError();
        int timeLimitExceed = stats.getTimeLimitExceed();
        int compilationError = stats.getCompilationError();
        int partiallySolvedSubmissions = stats.getPartiallySolvedSubmissions();
        int acceptedSubmissions = stats.getAcceptedSubmissions();
        int partiallySolvedProblems = stats.getPartiallySolvedProblems();

        if (submittedSolutions != 0)
            pieChart.setVisibility(View.VISIBLE);


        ArrayList<UserProfileData> submissionsList = new ArrayList<>();
        submissionsList.add(new UserProfileData("Solved", solvedProblems));
        submissionsList.add(new UserProfileData("Attempted", attemptedProblems));
        submissionsList.add(new UserProfileData("Submitted", submittedSolutions));
        submissionsList.add(new UserProfileData("WA", wrongSubmissions));
        submissionsList.add(new UserProfileData("RTE", runTimeError));
        submissionsList.add(new UserProfileData("TLE", timeLimitExceed));
        submissionsList.add(new UserProfileData("CE", compilationError));
        submissionsList.add(new UserProfileData("partial", partiallySolvedSubmissions + partiallySolvedProblems));
        submissionsList.add(new UserProfileData("AC", acceptedSubmissions));

        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setCenterText(generateCenterSpannableText());

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(53f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);

        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setOnChartValueSelectedListener(this);


        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < submissionsList.size(); i++) {
            entries.add(new PieEntry((float) submissionsList.get(i).getData(), submissionsList.get(i).getLabel(), getResources().getDrawable(R.drawable.star)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();

        // ENDS ADDING DATA

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setEntryLabelTextSize(12f);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setEntryLabelTextSize(12f);
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Submissions");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 11, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 11, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 11, s.length(), 0);
        return s;

    }

    public void startChatActicity() {
        Intent intent = new Intent(CodechefUser.this, ChatActivity.class);
        startActivity(intent);
    }

}
