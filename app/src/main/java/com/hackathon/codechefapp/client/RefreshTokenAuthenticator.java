package com.hackathon.codechefapp.client;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.activities.Login;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.dao.RefreshTokenBody;
import com.hackathon.codechefapp.model.login.AccessToken;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.retrofitmapping.IChef;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by SANDIP JANA on 25-09-2018.
 */
public class RefreshTokenAuthenticator implements Authenticator {

    private Context context;
    SharedPreferenceUtils prefs;

    public RefreshTokenAuthenticator(Context context) {
        this.context = context;
        prefs = SharedPreferenceUtils.getInstance(context);
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        final boolean[] successfull = {false};
        if (response.code() == 401) {

            String refreshToken = SharedPreferenceUtils.getInstance(context).getStringValue(PreferenceConstants.REFRESH_TOKEN, "");
            Log.d("my_token", refreshToken);

            Retrofit retrofit = new RetrofitClient().get(context);
            IChef ichef = retrofit.create(IChef.class);

            RefreshTokenBody body = new RefreshTokenBody();
            body.setGrant_type(context.getString(R.string.refresh_token));
            body.setRefresh_token(refreshToken);
            body.setClient_id(context.getString(R.string.client_id));
            body.setClient_secret(context.getString(R.string.client_secret));

            Call<AccessToken> refreshTokenCall = ichef.getRefreshToken(body);
            refreshTokenCall.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, retrofit2.Response<AccessToken> response) {
                    if (response.isSuccessful() && response != null && response.body() != null && response.body().getStatus().equalsIgnoreCase("OK")) {
                        Log.d("refreshToken", "yes received refresh Token successfull");
                        AccessToken tokens = response.body();
                        prefs.setValue(PreferenceConstants.ACCESS_TOKEN, tokens.getResult().getData().getAccessToken());
                        prefs.setValue(PreferenceConstants.REFRESH_TOKEN, tokens.getResult().getData().getRefreshToken());

                        successfull[0] = true;
                    } else {
                        // re login please
                        Log.d("refreshToken" , "authFails1");
                        startLoginActivity();
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    //make user login again
                    Log.d("refreshToken" , "authFails2");
                    startLoginActivity();
                }
            });

        }

        if (successfull[0] || !(response.code() == 401)) {
            return response.request().newBuilder()
                    .header("Authorization", prefs.getStringValue(PreferenceConstants.ACCESS_TOKEN, ""))
                    .build();
        }

        return null;
    }

    private void startLoginActivity() {
        Log.d("refreshToken", "Re--logging");
        prefs.clear();
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
    }

}
