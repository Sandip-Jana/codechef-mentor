package com.hackathon.codechefapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * Created by SANDIP JANA on 09-09-2018.
 */
public class NetworkCheck {

    public static boolean isConnected(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected;

        if (Build.VERSION.SDK_INT < 23) {
            isConnected = (connectivityManager != null && ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null &&
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) ||
                    (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null &&
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)));
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetworkInfo != null && ((activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) ||
                    (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)));
        }

        return isConnected;

    }

}
