package com.hackathon.codechefapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.dao.AccessTokenBody;
import com.hackathon.codechefapp.model.login.AccessToken;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.retrofitmapping.IChef;
import com.hackathon.codechefapp.utils.DisplayToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by SANDIP JANA on 09-09-2018.
 */

public class Login extends AppCompatActivity {

    private static final String TAG = Login.class.getSimpleName().toString();

    private ImageView imageView;
    private Button loginBtn;
    private ProgressBar progressBarLogin;

    private SharedPreferenceUtils prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageView = findViewById(R.id.codechefBanner);
        loginBtn = findViewById(R.id.loginBtn);
        progressBarLogin = findViewById(R.id.progressBarLogin);

        prefs = SharedPreferenceUtils.getInstance(getApplicationContext());

        if (prefs != null && prefs.contains(PreferenceConstants.ACCESS_TOKEN_UPDATED) &&
                prefs.getBoolanValue(PreferenceConstants.ACCESS_TOKEN_UPDATED, true)) {
            gotoMainActivity();
        }
        addListeners();
    }

    private void addListeners() {

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start login
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("api.codechef.com")
                        .appendPath("oauth")
                        .appendPath("authorize")
                        .appendQueryParameter("response_type", getString(R.string.response_type))
                        .appendQueryParameter("client_id", getString(R.string.client_id))
                        .appendQueryParameter("state", "xyz")
                        .appendQueryParameter("redirect_uri", getString(R.string.redirect_to_backend));

                startLoginAuthentication(builder);

            }
        });

    }

    private void startLoginAuthentication(Uri.Builder builder) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(builder.build().toString()));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthToken();
    }

    private void checkAuthToken() {
        if (getIntent() == null || getIntent().getData() == null) {
            return;
        }
        Uri uri = getIntent().getData();

        if (uri.toString().startsWith(getString(R.string.redirect_uri))) {
            String authCode = uri.getQueryParameter("code");
            if (authCode != null) {
                //use authCode to getToken
                Log.d(TAG, "Code = " + authCode);
                callApiForAccessToken(authCode);
            } else {
                Toast.makeText(getApplicationContext(), "Some Error Occured. Plese Login Again.", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void callApiForAccessToken(final String authCode) {

        progressBarLogin.setVisibility(View.VISIBLE);

        Retrofit retrofitClient = new RetrofitClient().get(this);

        final IChef ichef = retrofitClient.create(IChef.class);

        Call<AccessToken> requestToken = ichef.getAccessToken(getAccessTokenBody(authCode));

        requestToken.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                progressBarLogin.setVisibility(View.INVISIBLE);
                int statusCode = response.code();
                if (response.isSuccessful()) {

                    parseAccessTokenAndRefreshToken(response, authCode);

                } else {
                    DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.error_message));
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {

                progressBarLogin.setVisibility(View.INVISIBLE);

                DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.no_internet));

            }
        });

    }

    private void parseAccessTokenAndRefreshToken(Response<AccessToken> response, String authCode) {
        if (response == null || response.body() == null) {
            DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.unsuccessfull_login));
            return;
        }

        AccessToken tokens = response.body();
        if (tokens.getStatus().equalsIgnoreCase("OK")) {
            String access_token = tokens.getResult().getData().getAccessToken();
            int expires_in = tokens.getResult().getData().getExpiresIn();
            String token_type = tokens.getResult().getData().getTokenType();
            String scope = tokens.getResult().getData().getScope();
            String refresh_token = tokens.getResult().getData().getRefreshToken();
            Log.d(TAG, "AccessToken = " + access_token + "\n" + token_type + "\n" + scope + "\n refreshToken" + refresh_token + " \n" + expires_in);
            gotoMainPage(access_token, refresh_token, authCode);
        } else {
            DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), getString(R.string.unsuccessfull_login));
            return;
        }
    }

    public AccessTokenBody getAccessTokenBody(String authCode) {
        AccessTokenBody accessTokenBody = new AccessTokenBody();
        accessTokenBody.setGrant_type(getString(R.string.authorization_code));
        accessTokenBody.setCode(authCode);
        accessTokenBody.setClient_id(getString(R.string.client_id));
        accessTokenBody.setClient_secret(getString(R.string.client_secret));
        accessTokenBody.setRedirect_uri(getString(R.string.redirect_to_backend));
        return accessTokenBody;
    }

    private void gotoMainPage(String accessToken, String refreshToken, String authCode) {
        updatePreferences(accessToken, refreshToken, authCode);

        gotoMainActivity();
    }

    private void updatePreferences(String accessToken, String refreshToken, String authCode) {
        if (prefs != null) {
            Log.d(TAG, "Updated Preferences");

            prefs.clear();
            ;
            prefs.setValue(PreferenceConstants.AUTH_CODE, authCode);
            prefs.setValue(PreferenceConstants.ACCESS_TOKEN_UPDATED, true);
            prefs.setValue(PreferenceConstants.ACCESS_TOKEN, accessToken);
            prefs.setValue(PreferenceConstants.REFRESH_TOKEN, refreshToken);
        }
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }

}
