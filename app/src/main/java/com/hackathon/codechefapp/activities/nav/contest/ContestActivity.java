package com.hackathon.codechefapp.activities.nav.contest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.activities.nav.contest.fragments.ProblemDescriptionFragment;
import com.hackathon.codechefapp.activities.nav.contest.fragments.ShowContests;
import com.hackathon.codechefapp.activities.nav.contest.fragments.ShowProblemsFragment;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.custom.ZoomOutPageTransformer;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;

public class ContestActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 6;
    private static final String TAG = ContestActivity.class.getSimpleName();
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private String contestCode;

    private SharedPreferenceUtils prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        initToolbar();

        prefs = SharedPreferenceUtils.getInstance(getApplicationContext());

        pager = findViewById(R.id.contestPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(pagerAdapter);
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() != 0) {
            if (pager.getCurrentItem() == 2)
                pager.setCurrentItem(0, true);
            else {
                pager.setCurrentItem(2, true);
                getSupportActionBar().setTitle(prefs.getStringValue(PreferenceConstants.CONTESTCODE , ""));
            }
        } else {
            super.onBackPressed();
        }
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
                    return new ShowContests();
                case 2:
                    return new ShowProblemsFragment();
                case 3:
                    return new ShowProblemsFragment();
                case 4:
                    return new ProblemDescriptionFragment();
                case 5:
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
        pager.setCurrentItem(2);
    }

    public void showProblemBody(String problemCode, String contestCode) {
        prefs.setValue(PreferenceConstants.PROBLEM_CODE, problemCode);
        prefs.setValue(PreferenceConstants.CONTESTCODE, contestCode);
        pager.setCurrentItem(5);
    }

}
