package com.hackathon.codechefapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.activities.SearchUser.SearchActivity;
import com.hackathon.codechefapp.activities.SearchUser.UserProfile;
import com.hackathon.codechefapp.activities.Student.MyStudents;
import com.hackathon.codechefapp.activities.mentor.MyMentors;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.model.profile.Profile;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.retrofitmapping.IChef;
import com.hackathon.codechefapp.utils.DisplayToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName().toString();
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView userName;

    private View contentMain;

    private CardView mentorCard;
    private CardView studentCard;
    private CardView searchCard;

    private SharedPreferenceUtils prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.login_in_successfull));

        fab = (FloatingActionButton) findViewById(R.id.fab);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        contentMain = (View) findViewById(R.id.content_main);

        mentorCard = contentMain.findViewById(R.id.mentorCard);
        studentCard = contentMain.findViewById(R.id.studentCard);
        searchCard = contentMain.findViewById(R.id.searchCard);

        prefs = SharedPreferenceUtils.getInstance(getApplicationContext());


        addListeners();

        if( !prefs.contains(PreferenceConstants.USER_PROFILE) )
            getUserProfile();

    }

    private void addListeners() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        userName = header.findViewById(R.id.userName);

        // TODO
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), "Please Write to Us");
            }
        });

        mentorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start mentor activity
                startMyMentorActivity();
            }
        });

        studentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMyStudentsActivity();
            }
        });

        searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start search Activity
                startSearchActivity();
            }
        });

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startUserProfileActivity();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startMyMentorActivity()  {
        Intent intent = new Intent(getApplicationContext() , MyMentors.class);
        startActivity(intent);
    }

    private void startMyStudentsActivity() {
        Intent intent = new Intent(getApplicationContext(), MyStudents.class);
        startActivity(intent);
    }

    private void startSearchActivity() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
    }

    private void startUserProfileActivity() {
        if (!prefs.contains(PreferenceConstants.USER_PROFILE)) {
            DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), "Unable to fetch User Details.");
        } else {
            Intent intent = new Intent(MainActivity.this , UserProfile.class);
            startActivity(intent);
        }
    }


    private void getUserProfile() {

        Retrofit retrofitClient = new RetrofitClient().get(this);

        final IChef ichef = retrofitClient.create(IChef.class);

        String authToken = "Bearer " + prefs.getStringValue(PreferenceConstants.ACCESS_TOKEN, "");

        Log.d(TAG, authToken);

        Call<Profile> requestToken = ichef.getUserProfile(authToken);

        requestToken.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {

                    populateUserProfileData(response);

                } else {
                    DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.error_message));
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

                DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.error_message));

            }
        });
    }

    private void populateUserProfileData(Response<Profile> response) {

        if (response == null || response.body() == null) {
            DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.error_fetching_user_profile));
            return;
        }

        Profile profile = response.body();

        addToPreferences( profile );

        userName.setText(profile.getResult().getData().getContent().getUsername());

        prefs.setValue(PreferenceConstants.LOGGED_IN_USER_NAME , userName.getText().toString());

    }

    private void addToPreferences( Profile profile ) {
        String profilePrefs = new Gson().toJson(profile);
        prefs.setValue(PreferenceConstants.USER_PROFILE , profilePrefs);
    }

}
