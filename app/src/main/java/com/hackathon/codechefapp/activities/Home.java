package com.hackathon.codechefapp.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.activities.SearchUser.SearchActivity;
import com.hackathon.codechefapp.activities.SearchUser.UserProfile;
import com.hackathon.codechefapp.activities.Student.MyStudents;
import com.hackathon.codechefapp.activities.chat.ChatActivity;
import com.hackathon.codechefapp.activities.devs.DeveloperActivity;
import com.hackathon.codechefapp.activities.mentor.MyMentors;
import com.hackathon.codechefapp.activities.nav.contest.ContestActivity;
import com.hackathon.codechefapp.activities.nav.leaderboard.LeaderBoard;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.dao.chat.ChatAuthResponse;
import com.hackathon.codechefapp.model.alibaba.logout.Logout;
import com.hackathon.codechefapp.model.chat.ChatAuthBody;
import com.hackathon.codechefapp.model.profile.Profile;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.retrofitmapping.IChef;
import com.hackathon.codechefapp.utils.DisplayDialog;
import com.hackathon.codechefapp.utils.DisplayToast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.CAMERA;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = Home.class.getSimpleName().toString();
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView userName;

    private View contentMain;

    private FabSpeedDial fab;

    private CardView mentorCard;
    private CardView studentCard;
    private CardView searchCard;

    private SharedPreferenceUtils prefs;

    //ImageView for profile Pic
    private ImageView navigationDrawerProfilePic;
    private Bitmap profilePicBitmap;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private static final int ALL_PERMISSIONS_RESULT = 100;
    private static final int IMAGE_PICK_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.login_in_successfull));

        fab = (FabSpeedDial) findViewById(R.id.fabSpeedDial);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        contentMain = (View) findViewById(R.id.content_main);

        mentorCard = contentMain.findViewById(R.id.mentorCard);
        studentCard = contentMain.findViewById(R.id.studentCard);
        searchCard = contentMain.findViewById(R.id.searchCard);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        userName = navigationView.getHeaderView(0).findViewById(R.id.userName);
        navigationDrawerProfilePic = navigationView.getHeaderView(0).findViewById(R.id.nav_profile);

        prefs = SharedPreferenceUtils.getInstance(getApplicationContext());

        addListeners();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.add(CAMERA);
            permissionsToRequest = findUnAskedPermissions(permissions);
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        if (!prefs.contains(PreferenceConstants.USER_PROFILE)) {
            getUserProfile();
        }

    }

    private void addListeners() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationDrawerProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(startImagePickerIntent(), IMAGE_PICK_REQUEST_CODE);
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

        fab.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_general:
                        startChatActivity(Constants.GENERAL_CHAT_ROOM);
                        break;
                    case R.id.action_acm:
                        startChatActivity(Constants.ACM_CHAT_ROOM);
                        break;
                    case R.id.action_ioi:
                        startChatActivity(Constants.IOI_CHAT_ROOM);
                        break;
                    case R.id.action_placements:
                        startChatActivity(Constants.PLACEMENT_CHAT_ROOM);
                        break;
                    default:
                        break;
                }
                return true;
            }

            @Override
            public void onMenuClosed() {

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startUserProfileActivity();
        } else if (id == R.id.nav_contests) {
            startContestActivity();
        } else if (id == R.id.nav_leaderboard) {
            startLeaderBardActivity();
        } else if (id == R.id.nav_developers) {
            startDevActivity();
        } else if (id == R.id.nav_logout) {
            startLoginActivity();
        } else if (id == R.id.nav_share) {
            startWhatsAppIntent();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("WrongConstant")
    private void startLoginActivity() {
        logoutFromBackend();
    }

    private void startMyMentorActivity() {
        Intent intent = new Intent(getApplicationContext(), MyMentors.class);
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

    private void startContestActivity() {
        Intent intent = new Intent(getApplicationContext(), ContestActivity.class);
        startActivity(intent);
    }

    private void startLeaderBardActivity() {
        Intent intent = new Intent(getApplicationContext(), LeaderBoard.class);
        startActivity(intent);
    }

    private void startDevActivity() {
        Intent intent = new Intent(getApplicationContext(), DeveloperActivity.class);
        startActivity(intent);
    }

    private void startChatActivity(String roomId) {
        Intent intent = new Intent(Home.this, ChatActivity.class);
        intent.putExtra(Constants.ROOM_ID, roomId);
        startActivity(intent);
    }

    private void startWhatsAppIntent() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please download the app Codechef Mentors from playstore.");
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), "Whatsapp have not been installed.");
        } catch (Exception e) {
            DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.error_message));
        }
    }

    private void startUserProfileActivity() {
        if (!prefs.contains(PreferenceConstants.USER_PROFILE)) {
            DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), "Unable to fetch User Details.");
        } else {
            Intent intent = new Intent(Home.this, UserProfile.class);
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

        addToPreferences(profile);

        userName.setText(profile.getResult().getData().getContent().getUsername());

        prefs.setValue(PreferenceConstants.LOGGED_IN_USER_NAME, userName.getText().toString());

        authenticateUser();
    }

    private void addToPreferences(Profile profile) {
        String profilePrefs = new Gson().toJson(profile);
        prefs.setValue(PreferenceConstants.USER_PROFILE, profilePrefs);
    }

    private void authenticateUser() {


        Retrofit retrofit = new RetrofitClient().getAlibabaCookiesApi(this);
        IChef chef = retrofit.create(IChef.class);

        ChatAuthBody body = new ChatAuthBody();
        body.setCurrentUser(prefs.getStringValue(PreferenceConstants.LOGGED_IN_USER_NAME, ""));

        Call<ChatAuthResponse> chatAuth = chef.chatAuthentication(body);

        chatAuth.enqueue(new Callback<ChatAuthResponse>() {
            @Override
            public void onResponse(Call<ChatAuthResponse> call, Response<ChatAuthResponse> response) {
                if (response.isSuccessful() && response != null && response.body() != null) {
                    Headers header = response.headers();
                    Log.d(TAG, "User authenticated for chat");
                }
            }

            @Override
            public void onFailure(Call<ChatAuthResponse> call, Throwable t) {
                Log.d(TAG, "User not authenticated for chat");
                DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.error_message));
            }
        });

    }

    //navigation drawer proile picture permissions

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();
        for (String permissionsWanted : wanted) {
            if (!hasPermission(permissionsWanted)) {
                result.add(permissionsWanted);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (hasPermission(perms)) {
                    } else {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            DisplayDialog.showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }, this);
                            return;
                        }
                    }
                }
                break;
        }
    }

    public Intent startImagePickerIntent() {
        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }
        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);
        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        return chooserIntent;
    }

    private File imageFile;

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        imageFile = getPublicAlbumStorageDir("profilePic.png");
        if (imageFile != null) {
            // Log.d("Image location" , imageFile.getAbsolutePath()+" "+imageFile.getPath());
            outputFileUri = Uri.fromFile(imageFile);
        }
        return outputFileUri;
    }

    public File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            file.mkdirs();
        }
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICK_REQUEST_CODE) {
            Bitmap bitmap;
            if (resultCode == Activity.RESULT_OK) {
                if (getPickImageResultUri(data) != null) {
                    Uri profilePicUri = getPickImageResultUri(data);
                    try {
                        profilePicBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), profilePicUri);
                        navigationDrawerProfilePic.setImageBitmap(profilePicBitmap);

                        //uploadImageToServer();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    profilePicBitmap = bitmap;
                    navigationDrawerProfilePic.setImageBitmap(profilePicBitmap);

                    //uploadImageToServer();
                }
            }
        }
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private void logoutFromBackend() {
        Retrofit retrofit = new RetrofitClient().getAlibabaCookiesApi(this);
        final IChef ichef = retrofit.create(IChef.class);
        Call<Logout> logoutCall = ichef.logout();
        logoutCall.enqueue(new Callback<Logout>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<Logout> call, Response<Logout> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)) {
                    prefs.clear();
                    Intent intent = new Intent(Home.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), String.valueOf(R.string.error_message));
                }
            }

            @Override
            public void onFailure(Call<Logout> call, Throwable t) {
                DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), String.valueOf(R.string.error_message));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefs != null && prefs.contains(PreferenceConstants.LOGGED_IN_USER_NAME) && !prefs.getStringValue(PreferenceConstants.LOGGED_IN_USER_NAME, "").isEmpty())
            userName.setText(prefs.getStringValue(PreferenceConstants.LOGGED_IN_USER_NAME, ""));
    }

}
