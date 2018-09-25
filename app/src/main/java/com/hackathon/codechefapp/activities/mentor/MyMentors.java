package com.hackathon.codechefapp.activities.mentor;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.activities.SearchUser.CodechefUser;
import com.hackathon.codechefapp.constants.Constants;

public class MyMentors extends AppCompatActivity {

    private static final int NUM_TABS = 2;

    private ViewPager mentorPager;

    private TabLayout mentorTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mentors);

        mentorTabLayout = findViewById(R.id.mentorsTabLayout);
        mentorPager = findViewById(R.id.mentorPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MentorTabPagerAdapter adapter = new MentorTabPagerAdapter(this , getSupportFragmentManager());
        mentorPager.setAdapter(adapter);

        mentorTabLayout.setupWithViewPager(mentorPager);
    }

    public void startActivityCodechefUser(String userName, String relationStatus) {
        Intent intent = new Intent(MyMentors.this, CodechefUser.class);
        intent.putExtra(Constants.username, userName);
        intent.putExtra(Constants.RELATION, relationStatus);
        startActivity(intent);
    }

    public class MentorTabPagerAdapter extends FragmentPagerAdapter {
        private Context context;

        public MentorTabPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ApprovedMentors();
                case 1:
                    return new PendingMentorRequests();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return context.getString(R.string.my_approved_mentors);
                case 1:
                    return context.getString(R.string.pending_requests);
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_TABS;
        }
    }

}
