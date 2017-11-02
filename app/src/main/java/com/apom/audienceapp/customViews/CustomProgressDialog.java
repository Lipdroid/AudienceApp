package com.apom.audienceapp.customViews;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by lipuhossain on 1/29/17.
 */

public class CustomProgressDialog extends Dialog {

    public static int sPdCount = 0;

    public CustomProgressDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.setCancelable(false);
    }

    public CustomProgressDialog(Context context, int style) {
        super(context, style);
        // TODO Auto-generated constructor stub
        this.setCancelable(false);
    }

    @Override
    public void onBackPressed() {
        sPdCount = 0;
        this.dismiss();
    }
}
