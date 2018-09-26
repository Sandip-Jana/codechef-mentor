package com.hackathon.codechefapp.activities.SearchUser;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
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
import com.google.gson.Gson;
import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.dao.UserProfileData;
import com.hackathon.codechefapp.model.profile.Content;
import com.hackathon.codechefapp.model.profile.Profile;
import com.hackathon.codechefapp.model.profile.SubmissionStats;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity implements OnChartValueSelectedListener {

    private static final String TAG = UserProfile.class.getSimpleName();

    private Profile profile;

    private SharedPreferenceUtils prefs;

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
        setContentView(R.layout.activity_userprofile);

        prefs = SharedPreferenceUtils.getInstance(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        if (!prefs.contains(PreferenceConstants.USER_PROFILE)) {
            Log.d(TAG, "User not logged in");
        } else {
            String userProfle = prefs.getStringValue(PreferenceConstants.USER_PROFILE, "");
            Profile profile = new Gson().fromJson(userProfle, Profile.class);
            renderProfileDetailsToUI(profile);
        }
    }

    private void renderProfileDetailsToUI(Profile profile) {
        if (!profile.getStatus().toLowerCase().trim().equalsIgnoreCase(Constants.RESPONSE_OK)) {
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

        PieDataSet dataSet = new PieDataSet(entries, "User submissions");

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

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
