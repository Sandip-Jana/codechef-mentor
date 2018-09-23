package com.hackathon.codechefapp.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by SANDIP JANA on 09-09-2018.
 */
public class DisplayToast {

    public DisplayToast() {

    }

    public static void make(final Context context, String text) {
        Toast.makeText(context, text, Snackbar.LENGTH_LONG).show();
    }

    public static void makeSnackbar(final View  coordinatorLayout, String snackTitle ) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, snackTitle, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView txtView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        txtView.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();
    }

}
