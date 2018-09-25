package com.hackathon.codechefapp.client;

import android.content.Context;

import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;


import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SANDIP JANA on 25-09-2018.
 */
public class ReceivedCookiesInterceptor implements Interceptor {

    private Context context;

    ReceivedCookiesInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }
            SharedPreferenceUtils.getInstance(context).setCookies(cookies);
        }
        return originalResponse;
    }
}
