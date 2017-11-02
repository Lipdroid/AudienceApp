package com.apom.audienceapp.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apom.audienceapp.R;
import com.apom.audienceapp.customViews.CustomProgressDialog;
import com.apom.audienceapp.objects.UserObject;

import java.security.MessageDigest;

/**
 * Created by mdmunirhossain on 11/2/17.
 */

public class GlobalUtils {
    private static UserObject userObject = null;
    private static CustomProgressDialog sPdLoading = null;

    public static void computePakageHash(Context mContext) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    "com.apom.audienceapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
    }

    public static UserObject getCurrentUserObj() {

        return userObject;
    }

    public static void setCurrentUserObj(UserObject userObj) {
        userObject = null;
        userObject = userObj;
    }

    public static void showLoadingProgress(Context context) {
        if (CustomProgressDialog.sPdCount <= 0) {
            CustomProgressDialog.sPdCount = 0;
            sPdLoading = null;
            sPdLoading = new CustomProgressDialog(context, R.style.CustomDialogTheme);
            sPdLoading.show();
            if (Build.VERSION.SDK_INT > 10) {
                View loadingV = LayoutInflater.from(context).inflate(R.layout.layout_pd_loading, null);
                new MultipleScreen(context);
                MultipleScreen.resizeAllView((ViewGroup) loadingV);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(MultipleScreen.getValueAfterResize(340),
                        MultipleScreen.getValueAfterResize(340));
                sPdLoading.addContentView(loadingV, lp);
            } else {
                String message = context.getResources().getString(R.string.common_loading);
            }
            CustomProgressDialog.sPdCount++;
        } else {
            CustomProgressDialog.sPdCount++;
        }
    }

    public static void dismissLoadingProgress() {
        if (CustomProgressDialog.sPdCount <= 1) {
            if (sPdLoading != null && sPdLoading.isShowing())
                sPdLoading.dismiss();
            CustomProgressDialog.sPdCount--;
        } else {
            CustomProgressDialog.sPdCount--;
        }
    }
}
