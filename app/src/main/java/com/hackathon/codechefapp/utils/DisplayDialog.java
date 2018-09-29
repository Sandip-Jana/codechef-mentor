package com.hackathon.codechefapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by SANDIP JANA on 27-09-2018.
 */
public class DisplayDialog {

    public DisplayDialog() {

    }

    public static void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, Context context) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
