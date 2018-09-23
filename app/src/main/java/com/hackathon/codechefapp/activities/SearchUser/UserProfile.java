package com.hackathon.codechefapp.activities.SearchUser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.model.profile.Content;
import com.hackathon.codechefapp.model.profile.Profile;
import com.hackathon.codechefapp.model.profile.SubmissionStats;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;

public class UserProfile extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        prefs = SharedPreferenceUtils.getInstance(getApplicationContext());

        username = findViewById(R.id.username);

        allContestRating = findViewById(R.id.allContestRating);

        myGlobalRank = findViewById(R.id.myGlobalRank);
        myCountryRank = findViewById(R.id.myCountryRank);

        longChallengeRating =  findViewById(R.id.longChallengeRating);
        cookOffRating = findViewById(R.id.cookOffRating);
        lunchTimeRating = findViewById(R.id.lunchTimeRating);

        longChallengeGlobalRank = findViewById(R.id.longChallengeGlobalRank);
        cookOffGlobalRank = findViewById(R.id.cookOffGlobalRank);
        lunchTimeGlobalRank = findViewById(R.id.lunchTimeGlobalRank);

        longChallengeCountryRank =   findViewById(R.id.longChallengeCountryRank);
        cookOffCountryRank =  findViewById(R.id.cookOffCountryRank);
        lunchTimeCountryRank =  findViewById(R.id.lunchTimeCountryRank);

        if( !prefs.contains(PreferenceConstants.USER_PROFILE) ) {
            Log.d(TAG , "User not logged in");
        } else {
            String userProfle = prefs.getStringValue(PreferenceConstants.USER_PROFILE , "");
            Profile profile = new Gson().fromJson(userProfle , Profile.class);
            renderProfileDetailsToUI( profile );
        }
    }

    private void renderProfileDetailsToUI( Profile profile ) {
        if( !profile.getStatus().toLowerCase().trim().equalsIgnoreCase(Constants.RESPONSE_OK) ) {
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
        int runTimeError =  stats.getRunTimeError();
        int timeLimitExceed = stats.getTimeLimitExceed();
        int compilationError = stats.getCompilationError();
        int partiallySolvedSubmissions = stats.getPartiallySolvedSubmissions();
        int acceptedSubmissions = stats.getAcceptedSubmissions();
        int partiallySolvedProblems = stats.getPartiallySolvedProblems();
    }

}
