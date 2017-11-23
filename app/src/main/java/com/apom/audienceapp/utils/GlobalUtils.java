package com.apom.audienceapp.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apom.audienceapp.AudienceApplication;
import com.apom.audienceapp.R;
import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.customViews.CustomDialog;
import com.apom.audienceapp.customViews.CustomFontTextView;
import com.apom.audienceapp.customViews.CustomFontTextViewLight;
import com.apom.audienceapp.customViews.CustomProgressDialog;
import com.apom.audienceapp.interfaces.DialogCallback;
import com.apom.audienceapp.interfaces.DialogForValueCallback;
import com.apom.audienceapp.interfaces.InputDialogCallback;
import com.apom.audienceapp.objects.JobObject;
import com.apom.audienceapp.objects.MeetingObject;
import com.apom.audienceapp.objects.UserObject;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    public static boolean onClientPage = false;

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
                LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View loadingV = inflator.inflate(R.layout.layout_pd_loading, null);
                new MultipleScreen(context);
                MultipleScreen.resizeAllView((ViewGroup) loadingV);
                sPdLoading.setContentView(loadingV);
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

    public static void showInputDialog(final Context context, String image, String title, String body, String action, final InputDialogCallback dialogCallback) {
        final CustomDialog infoDialog = new CustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.dialog_feedback, null);

        new MultipleScreen(context);
        MultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        Button btnOK = (Button) infoDialog.findViewById(R.id.dialog_btn_positive);
        TextView tvTitle = (TextView) infoDialog.findViewById(R.id.dialog_tv_title);
        final EditText tvBody = (EditText) infoDialog.findViewById(R.id.dialog_tv_body);
        ImageView cancel = (ImageView) infoDialog.findViewById(R.id.cancel);
        CircleImageView profile_image = (CircleImageView) infoDialog.findViewById(R.id.profile_image);

        //Loading image from below url into imageView
        Picasso.with(context)
                .load(image)
                .into(profile_image);
        if (title == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

//        if (body == null) {
//            tvBody.setVisibility(View.GONE);
//        } else {
//            tvBody.setText(body);
//        }

        if (action != null) {
            btnOK.setText(action);
        }

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    if (tvBody.getText().toString().isEmpty()) {
                        Toast.makeText(context, "Please fill the comments area", Toast.LENGTH_LONG).show();
                    } else {
                        dialogCallback.onAction1(tvBody.getText().toString());
                        infoDialog.dismiss();
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialogCallback != null) {
                    dialogCallback.onAction2();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }

    public static void showStatusDialog(Context mContext, MeetingObject meetingObj, final DialogCallback dialogCallback) {
        final CustomDialog infoDialog = new CustomDialog(mContext, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.dialog_meeting_status, null);

        new MultipleScreen(mContext);
        MultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        CircleImageView profile_image = (CircleImageView) infoDialog.findViewById(R.id.profile_image);
        CustomFontTextView name = (CustomFontTextView) infoDialog.findViewById(R.id.name);
        CustomFontTextViewLight response = (CustomFontTextViewLight) infoDialog.findViewById(R.id.response);
        CustomFontTextViewLight status = (CustomFontTextViewLight) infoDialog.findViewById(R.id.status);
        CustomFontTextView date_tv = (CustomFontTextView) infoDialog.findViewById(R.id.date);
        CustomFontTextView time = (CustomFontTextView) infoDialog.findViewById(R.id.time);
        CustomFontTextView place = (CustomFontTextView) infoDialog.findViewById(R.id.place);
        ImageView cancel = (ImageView) infoDialog.findViewById(R.id.cancel);
        final Button btnOK = (Button) infoDialog.findViewById(R.id.action);
        final Button feedback_btn = (Button) infoDialog.findViewById(R.id.feedback_btn);


        //Loading image from below url into imageView
        Picasso.with(mContext)
                .load(meetingObj.getExpert_image_url())
                .into(profile_image);
        name.setText(meetingObj.getExpert_name());
        if (meetingObj.getApprove_message() == null || meetingObj.getApprove_message().equals("")) {
            response.setVisibility(View.GONE);
        } else {
            response.setText("\"" + meetingObj.getApprove_message() + "\"");
        }
        DateFormat dateFormat = new SimpleDateFormat("EEE-dd-MMMM");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat dateFormatDefault = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT + " hh:mm a");
        Date date = null;
        try {
            date = dateFormatDefault.parse(meetingObj.getMeeting_time());
            String date_str = dateFormat.format(date);
            String time_str = timeFormat.format(date);

            date_tv.setText(date_str);
            time.setText(time_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        place.setText(meetingObj.getMeeting_venue());

        if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getExpert_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getAdmin_approval().equals(Constants.USER_ARROVED)
                && !GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
            status.setTextColor(mContext.getResources().getColor(R.color.green1));
            status.setText("FINISH");
            if (GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_CLIENT) && meetingObj.getMeeting_review().equals(Constants.NOT_YET_REVIEWED)) {
                feedback_btn.setVisibility(View.VISIBLE);
                ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(feedback_btn, "scaleX", 0f, 1f);
                ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(feedback_btn, "scaleY", 0f, 1f);
                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(feedback_btn, "alpha", 0f, 1f);
                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
                animatorSet.setDuration(1500);
                animatorSet.setInterpolator(new CustomBounceInterpolator(0.2, 20));
                animatorSet.start();
            }
            response.setVisibility(View.VISIBLE);

        } else if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getExpert_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getAdmin_approval().equals(Constants.USER_ARROVED)
                && GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
            status.setTextColor(mContext.getResources().getColor(R.color.green1));
            status.setText("FIXED");
            //can add to callender
            if (!GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_ADMIN)) {
                btnOK.setText("ADD TO CALLENDER");
            }
            response.setVisibility(View.VISIBLE);
        } else if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getExpert_approval().equals(Constants.USER_NOT_YET_ARROVED)
                && meetingObj.getAdmin_approval().equals(Constants.USER_NOT_YET_ARROVED)
                && GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
            status.setTextColor(mContext.getResources().getColor(R.color.orange));
            status.setText("Pending");
            if (GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_CLIENT)) {
                response.setVisibility(View.GONE);
            }
        } else if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getExpert_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getAdmin_approval().equals(Constants.USER_NOT_YET_ARROVED)
                && GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
            if (GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_EXPERT)) {
                status.setTextColor(mContext.getResources().getColor(R.color.green1));
                status.setText("Done");
            } else {
                status.setTextColor(mContext.getResources().getColor(R.color.orange));
                status.setText("Pending");
            }
            if (GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_CLIENT)) {
                response.setVisibility(View.GONE);
            }

        } else if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getExpert_approval().equals(Constants.USER_REJECTED)
                && meetingObj.getAdmin_approval().equals(Constants.USER_NOT_YET_ARROVED)
                && GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
            status.setTextColor(mContext.getResources().getColor(R.color.common_red));
            status.setText("Rejected");
            if (GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_CLIENT)) {
                response.setVisibility(View.GONE);
            }
        } else if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getExpert_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getAdmin_approval().equals(Constants.USER_REJECTED)
                && GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
            status.setTextColor(mContext.getResources().getColor(R.color.common_red));
            status.setText("Rejected");
            if (GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_CLIENT)) {
                response.setVisibility(View.GONE);
            }
        }


        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    if (!btnOK.getText().toString().equals("OK"))
                        dialogCallback.onAction1();
                }
                infoDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction2();
                }
                infoDialog.dismiss();
            }
        });

        feedback_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction3();
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


    public static MeetingObject parseMeeting(JSONObject src) {
        MeetingObject meetingObj = new MeetingObject();
        try {
            if (((JSONObject) src).has(Constants.TAG_ID)) {
                if (!((JSONObject) src).getString(Constants.TAG_ID).equals("null")) {
                    meetingObj.setId(((JSONObject) src).getString(Constants.TAG_ID));
                } else {
                    meetingObj.setId("");
                }

            }

            if (((JSONObject) src).has(Constants.TAG_EXPERT_ID)) {
                if (!((JSONObject) src).getString(Constants.TAG_EXPERT_ID).equals("null")) {
                    meetingObj.setExpert_id(((JSONObject) src).getString(Constants.TAG_EXPERT_ID));
                } else {
                    meetingObj.setExpert_id("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_EXPERT_NAME)) {
                if (!((JSONObject) src).getString(Constants.TAG_EXPERT_NAME).equals("null")) {
                    meetingObj.setExpert_name(((JSONObject) src).getString(Constants.TAG_EXPERT_NAME));
                } else {
                    meetingObj.setExpert_name("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_EXPERT_APPROVAL)) {
                if (!((JSONObject) src).getString(Constants.TAG_EXPERT_APPROVAL).equals("null")) {
                    meetingObj.setExpert_approval(((JSONObject) src).getString(Constants.TAG_EXPERT_APPROVAL));
                } else {
                    meetingObj.setExpert_approval("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_CLIENT_ID)) {
                if (!((JSONObject) src).getString(Constants.TAG_CLIENT_ID).equals("null")) {
                    meetingObj.setClient_id(((JSONObject) src).getString(Constants.TAG_CLIENT_ID));
                } else {
                    meetingObj.setClient_id("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_CLIENT_NAME)) {
                if (!((JSONObject) src).getString(Constants.TAG_CLIENT_NAME).equals("null")) {
                    meetingObj.setClient_name(((JSONObject) src).getString(Constants.TAG_CLIENT_NAME));
                } else {
                    meetingObj.setClient_name("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_CLIENT_APPROVAL)) {
                if (!((JSONObject) src).getString(Constants.TAG_CLIENT_APPROVAL).equals("null")) {
                    meetingObj.setClient_approval(((JSONObject) src).getString(Constants.TAG_CLIENT_APPROVAL));
                } else {
                    meetingObj.setClient_approval("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_ADMIN_APPROVAL)) {
                if (!((JSONObject) src).getString(Constants.TAG_ADMIN_APPROVAL).equals("null")) {
                    meetingObj.setAdmin_approval(((JSONObject) src).getString(Constants.TAG_ADMIN_APPROVAL));
                } else {
                    meetingObj.setAdmin_approval("");
                }
            }


            if (((JSONObject) src).has(Constants.TAG_MEETING_TIME)) {
                if (!((JSONObject) src).getString(Constants.TAG_MEETING_TIME).equals("null")) {
                    meetingObj.setMeeting_time(((JSONObject) src).getString(Constants.TAG_MEETING_TIME));
                } else {
                    meetingObj.setMeeting_time("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_MEETING_VENUE)) {
                if (!((JSONObject) src).getString(Constants.TAG_MEETING_VENUE).equals("null")) {
                    meetingObj.setMeeting_venue(((JSONObject) src).getString(Constants.TAG_MEETING_VENUE));
                } else {
                    meetingObj.setMeeting_venue("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_MEETING_PURPOSE)) {
                if (!((JSONObject) src).getString(Constants.TAG_MEETING_PURPOSE).equals("null")) {
                    meetingObj.setMeeting_purpose(((JSONObject) src).getString(Constants.TAG_MEETING_PURPOSE));
                } else {
                    meetingObj.setMeeting_purpose("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_MEETING_EXPECTATIO_ONE)) {
                if (!((JSONObject) src).getString(Constants.TAG_MEETING_EXPECTATIO_ONE).equals("null")) {
                    meetingObj.setMeeting_expectation_one(((JSONObject) src).getString(Constants.TAG_MEETING_EXPECTATIO_ONE));
                } else {
                    meetingObj.setMeeting_expectation_one("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_MEETING_EXPECTATIO_TWO)) {
                if (!((JSONObject) src).getString(Constants.TAG_MEETING_EXPECTATIO_TWO).equals("null")) {
                    meetingObj.setMeeting_expectation_two(((JSONObject) src).getString(Constants.TAG_MEETING_EXPECTATIO_TWO));
                } else {
                    meetingObj.setMeeting_expectation_two("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_MEETING_EXPECTATIO_THREE)) {
                if (!((JSONObject) src).getString(Constants.TAG_MEETING_EXPECTATIO_THREE).equals("null")) {
                    meetingObj.setMeeting_expectation_three(((JSONObject) src).getString(Constants.TAG_MEETING_EXPECTATIO_THREE));
                } else {
                    meetingObj.setMeeting_expectation_three("");
                }
            }
            if (((JSONObject) src).has(Constants.TAG_MEETING_TIME_CHANGED)) {
                if (!((JSONObject) src).getString(Constants.TAG_MEETING_TIME_CHANGED).equals("null")) {
                    meetingObj.setMeeting_time_changed(((JSONObject) src).getString(Constants.TAG_MEETING_TIME_CHANGED));
                } else {
                    meetingObj.setMeeting_time_changed("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_MEETING_VENUE_CHANGED)) {
                if (!((JSONObject) src).getString(Constants.TAG_MEETING_VENUE_CHANGED).equals("null")) {
                    meetingObj.setMeeting_venue_changed(((JSONObject) src).getString(Constants.TAG_MEETING_VENUE_CHANGED));
                } else {
                    meetingObj.setMeeting_venue_changed("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_MEETING_REVIEW)) {
                if (!((JSONObject) src).getString(Constants.TAG_MEETING_REVIEW).equals("null")) {
                    meetingObj.setMeeting_review(((JSONObject) src).getString(Constants.TAG_MEETING_REVIEW));
                } else {
                    meetingObj.setMeeting_review("");
                }
            }


            if (((JSONObject) src).has(Constants.TAG_CLIENT_IMAGE_URL)) {
                if (!((JSONObject) src).getString(Constants.TAG_CLIENT_IMAGE_URL).equals("null")) {
                    meetingObj.setClient_image_url(((JSONObject) src).getString(Constants.TAG_CLIENT_IMAGE_URL));
                } else {
                    meetingObj.setClient_image_url("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_EXPERT_IMAGE_URL)) {
                if (!((JSONObject) src).getString(Constants.TAG_EXPERT_IMAGE_URL).equals("null")) {
                    meetingObj.setExpert_image_url(((JSONObject) src).getString(Constants.TAG_EXPERT_IMAGE_URL));
                } else {
                    meetingObj.setExpert_image_url("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_APPROVE_MESSAGE)) {
                if (!((JSONObject) src).getString(Constants.TAG_APPROVE_MESSAGE).equals("null")) {
                    meetingObj.setApprove_message(((JSONObject) src).getString(Constants.TAG_APPROVE_MESSAGE));
                } else {
                    meetingObj.setApprove_message("");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return meetingObj;
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

    public static boolean isDateValid(String d1) {
        try {
            // If you already have date objects then skip 1

            //1
            // Create 2 dates starts
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT + " hh:mm a");
            Date date1 = sdf.parse(d1);
            Date date2 = new Date();

            System.out.println("Date1" + sdf.format(date1));
            System.out.println("Date2" + sdf.format(date2));
            System.out.println();

            // Create 2 dates ends
            //1

            // Date object is having 3 methods namely after,before and equals for comparing
            // after() will return true if and only if date1 is after date 2
            if (date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                return true;
            }
            // before() will return true if and only if date1 is before date2
            if (date1.before(date2)) {
                System.out.println("Date1 is before Date2");
                return false;
            }

            //equals() returns true if both the dates are equal
            if (date1.equals(date2)) {
                System.out.println("Date1 is equal Date2");
                return true;
            }

            System.out.println();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static String formatDateString(String date) {
        return date.substring(0, date.length() - 5);

    }

    public static void addEventInCallender(Activity mActivity, MeetingObject meeting) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatDefault = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT + " hh:mm a");
        try {
            Date date = dateFormatDefault.parse(meeting.getMeeting_time());
            cal.setTime(date);
            long startTime = cal.getTimeInMillis();
            long endTime = cal.getTimeInMillis();
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);

            intent.putExtra(CalendarContract.Events.TITLE, "Meeting with " + meeting.getExpert_name());
            intent.putExtra(CalendarContract.Events.DESCRIPTION, "Meeting with " + meeting.getExpert_name());
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Location:" + meeting.getMeeting_venue());

            mActivity.startActivity(intent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static class CustomBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        public CustomBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }

}
