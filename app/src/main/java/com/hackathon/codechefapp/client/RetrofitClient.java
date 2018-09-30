package com.hackathon.codechefapp.client;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.constants.URLConstants;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.utils.DisplayToast;
import com.hackathon.codechefapp.utils.NetworkCheck;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by SANDIP JANA on 09-09-2018.
 */

public class RetrofitClient {

    public Retrofit get(final Context context) {

        if (!NetworkCheck.isConnected(context)) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    DisplayToast.make(context.getApplicationContext(), context.getString(R.string.no_internet));
                }
            });
        }

        OkHttpClient.Builder okhttpbuilder = new OkHttpClient.Builder();

        OkHttpClient okHttpClient = okhttpbuilder.readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .authenticator(new RefreshTokenAuthenticator(context))
                .build();

        return new Retrofit.Builder()
                .baseUrl(URLConstants.BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

    }

    public Retrofit getAlibabaCookiesApi(final Context context) {

        if (!NetworkCheck.isConnected(context)) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    DisplayToast.make(context.getApplicationContext(), context.getString(R.string.no_internet));
                }
            });
        }

        OkHttpClient.Builder okhttpbuilder = new OkHttpClient.Builder();

        OkHttpClient.Builder okHttpClient = okhttpbuilder.readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.interceptors().add(new AddCookiesInterceptor(context));
        okHttpClient.interceptors().add(new ReceivedCookiesInterceptor(context));
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String authCode = SharedPreferenceUtils.getInstance(context).getStringValue(PreferenceConstants.AUTH_CODE, "");
                Request request = chain.request().newBuilder().addHeader("Auth-Code", authCode).build();
                return chain.proceed(request);
            }
        });

        return new Retrofit.Builder()
                .baseUrl(URLConstants.BASE_API2)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();

    }

}
