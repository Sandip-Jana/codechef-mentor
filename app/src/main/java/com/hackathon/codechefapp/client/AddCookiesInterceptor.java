package com.hackathon.codechefapp.client;

import android.content.Context;
import android.util.Log;

import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SANDIP JANA on 24-09-2018.
 */
public class AddCookiesInterceptor implements Interceptor {

    private Context context;

    AddCookiesInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = SharedPreferenceUtils.getInstance(context).getCookies();
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            Log.v("OkHttp", "Adding Header: " + cookie);
        }
        return chain.proceed(builder.build());
    }
}