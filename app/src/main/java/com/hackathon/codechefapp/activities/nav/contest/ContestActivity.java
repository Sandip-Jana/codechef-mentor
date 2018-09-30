package com.hackathon.codechefapp.activities.nav.contest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.activities.nav.contest.fragments.ProblemDescriptionFragment;
import com.hackathon.codechefapp.activities.nav.contest.fragments.ShowContests;
import com.hackathon.codechefapp.activities.nav.contest.fragments.ShowProblemsFragment;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.custom.ZoomOutPageTransformer;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;

public class ContestActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 3;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private String contestCode;

    private SharedPreferenceUtils prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        prefs = SharedPreferenceUtils.getInstance(getApplicationContext());

        pager = findViewById(R.id.contestPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(pagerAdapter);
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ShowContests();
                case 1:
                    return new ShowProblemsFragment();
                case 2:
                    return new ProblemDescriptionFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void showProblemsFragment(String contestCode) {
        prefs.setValue(PreferenceConstants.CONTESTCODE, contestCode);
        pager.setCurrentItem(1);
    }

    public void showProblemBody(String problemCode, String contestCode) {
        prefs.setValue(PreferenceConstants.PROBLEM_CODE, problemCode);
        prefs.setValue(PreferenceConstants.CONTESTCODE, contestCode);
        pager.setCurrentItem(2);
    }

}
