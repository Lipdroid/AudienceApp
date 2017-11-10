package com.apom.audienceapp.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.apom.audienceapp.AudienceApplication;
import com.apom.audienceapp.R;
import com.apom.audienceapp.customViews.CustomDialog;
import com.apom.audienceapp.customViews.CustomProgressDialog;
import com.apom.audienceapp.interfaces.DialogCallback;
import com.apom.audienceapp.interfaces.DialogForValueCallback;
import com.apom.audienceapp.objects.JobObject;
import com.apom.audienceapp.objects.UserObject;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdmunirhossain on 11/2/17.
 */

public class GlobalUtils {
    private static UserObject userObject = null;
    private static CustomProgressDialog sPdLoading = null;
    public static Boolean addAditionalHeader = false;
    public static String additionalHeaderTag = null;
    public static String additionalHeaderValue = null;

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

    public static boolean isNetworkConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) AudienceApplication.AudienceApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
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


    public static void chooseUserPopup(final Context context, final DialogCallback dialogCallback) {
        final CustomDialog infoDialog = new CustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.dialog_user_type_picker, null);

        new MultipleScreen(context);
        MultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        LinearLayout btnExpert = (LinearLayout) infoDialog.findViewById(R.id.expert);
        LinearLayout btnClient = (LinearLayout) infoDialog.findViewById(R.id.client);


        btnExpert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction1();
                }


                infoDialog.dismiss();
            }
        });


        btnClient.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction2();
                }

                infoDialog.dismiss();

            }
        });


        infoDialog.show();
    }

    public static void showInfoDialog(Context context, String title, String body, String action, final DialogCallback dialogCallback) {
        final CustomDialog infoDialog = new CustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_show_info_dialog, null);

        new MultipleScreen(context);
        MultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        Button btnOK = (Button) infoDialog.findViewById(R.id.dialog_btn_positive);
        TextView tvTitle = (TextView) infoDialog.findViewById(R.id.dialog_tv_title);
        TextView tvBody = (TextView) infoDialog.findViewById(R.id.dialog_tv_body);

        if (title == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        if (body == null) {
            tvBody.setVisibility(View.GONE);
        } else {
            tvBody.setText(body);
        }

        if (action != null) {
            btnOK.setText(action);
        }
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction1();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }

    public static UserObject parseUser(JSONObject src) {
        UserObject userObj = new UserObject();
        try {
            if (((JSONObject) src).has(Constants.TAG_ID)) {
                if (!((JSONObject) src).getString(Constants.TAG_ID).equals("null")) {
                    userObj.setLinked_in_id(((JSONObject) src).getString(Constants.TAG_ID));
                } else {
                    userObj.setLinked_in_id("");
                }

            }

            if (((JSONObject) src).has(Constants.TAG_NAME)) {
                if (!((JSONObject) src).getString(Constants.TAG_NAME).equals("null")) {
                    userObj.setFirstName(((JSONObject) src).getString(Constants.TAG_NAME));
                } else {
                    userObj.setFirstName("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_EMAIL)) {
                if (!((JSONObject) src).getString(Constants.TAG_EMAIL).equals("null")) {
                    userObj.setEmail(((JSONObject) src).getString(Constants.TAG_EMAIL));
                } else {
                    userObj.setEmail("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_IMAGE_URL)) {
                if (!((JSONObject) src).getString(Constants.TAG_IMAGE_URL).equals("null")) {
                    userObj.setProfile_image_url(((JSONObject) src).getString(Constants.TAG_IMAGE_URL));
                } else {
                    userObj.setProfile_image_url("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_MOBILE)) {
                if (!((JSONObject) src).getString(Constants.TAG_MOBILE).equals("null")) {
                    userObj.setMobile(((JSONObject) src).getString(Constants.TAG_MOBILE));
                } else {
                    userObj.setMobile("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_INDUSTRY)) {
                if (!((JSONObject) src).getString(Constants.TAG_INDUSTRY).equals("null")) {
                    userObj.setIndustry(((JSONObject) src).getString(Constants.TAG_INDUSTRY));
                } else {
                    userObj.setIndustry("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_TYPE)) {
                if (!((JSONObject) src).getString(Constants.TAG_TYPE).equals("null")) {
                    userObj.setCategory(((JSONObject) src).getString(Constants.TAG_TYPE));
                } else {
                    userObj.setCategory("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_STATUS)) {
                if (!((JSONObject) src).getString(Constants.TAG_STATUS).equals("null")) {
                    userObj.setStatus(((JSONObject) src).getString(Constants.TAG_STATUS));
                } else {
                    userObj.setStatus("");
                }
            }

            //joblisr
            List<JobObject> joblist = new ArrayList<>();
            JobObject currentJob = new JobObject();

            if (((JSONObject) src).has(Constants.TAG_JOB_TITLE)) {
                if (!((JSONObject) src).getString(Constants.TAG_JOB_TITLE).equals("null")) {
                    currentJob.setJob_title(((JSONObject) src).getString(Constants.TAG_JOB_TITLE));
                } else {
                    currentJob.setJob_title("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_JOB_SUMMARY)) {
                if (!((JSONObject) src).getString(Constants.TAG_JOB_SUMMARY).equals("null")) {
                    currentJob.setJob_summary(((JSONObject) src).getString(Constants.TAG_JOB_SUMMARY));
                } else {
                    currentJob.setJob_summary("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_COMPANY)) {
                if (!((JSONObject) src).getString(Constants.TAG_COMPANY).equals("null")) {
                    currentJob.setCompany_name(((JSONObject) src).getString(Constants.TAG_COMPANY));
                } else {
                    currentJob.setCompany_name("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_LOCATION)) {
                if (!((JSONObject) src).getString(Constants.TAG_LOCATION).equals("null")) {
                    currentJob.setLocation(((JSONObject) src).getString(Constants.TAG_LOCATION));
                } else {
                    currentJob.setLocation("");
                }
            }
            joblist.add(currentJob);
            userObj.setJobsList(joblist);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return userObj;
    }

    // slide the view from below itself to the current position
    public static void slideUp(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                -view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public static void slideDown(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }
}
