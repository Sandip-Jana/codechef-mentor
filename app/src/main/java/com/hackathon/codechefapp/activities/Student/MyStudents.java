package com.hackathon.codechefapp.activities.Student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.activities.SearchUser.CodechefUser;
import com.hackathon.codechefapp.constants.Constants;

public class MyStudents extends AppCompatActivity {

    private static final int NUM_TABS = 2;
    private ViewPager studentPager;

    private TabLayout studentTabLayout;

    private ApprovedStudents approvedStudentsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_students);

        initToolbar();

        studentTabLayout = findViewById(R.id.studentTabs);
        studentPager = findViewById(R.id.studentPager);

        StudentTabPagerAdapter adapter = new StudentTabPagerAdapter(this, getSupportFragmentManager());
        studentPager.setAdapter(adapter);

        studentTabLayout.setupWithViewPager(studentPager);

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

    public void startActivityCodechefUser(String userName, String relationStatus) {
        Intent intent = new Intent(MyStudents.this, CodechefUser.class);
        intent.putExtra(Constants.username, userName);
        intent.putExtra(Constants.RELATION, relationStatus);

        startActivity(intent);
    }

    public void startActivityCodechefUser(String userName, String relationStatus , String roomId) {
        Intent intent = new Intent(MyStudents.this, CodechefUser.class);
        intent.putExtra(Constants.username, userName);
        intent.putExtra(Constants.RELATION, relationStatus);
        intent.putExtra(Constants.ROOM_ID , roomId);

        startActivity(intent);
    }

    public void refreshBothFragments() {
        if (approvedStudentsFragment != null) {
            approvedStudentsFragment.updateFragmentAdapter();
        }
    }

    public class StudentTabPagerAdapter extends FragmentPagerAdapter {
        private Context context;

        public StudentTabPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return approvedStudentsFragment = new ApprovedStudents();
                case 1:
                    return new PendingStudentRequests();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return context.getString(R.string.my_approved_students);
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
