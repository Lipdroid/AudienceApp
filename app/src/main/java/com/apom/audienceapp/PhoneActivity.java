package com.apom.audienceapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apom.audienceapp.apis.RequestAsyncTask;
import com.apom.audienceapp.customViews.EditTextWithFont;
import com.apom.audienceapp.interfaces.AsyncCallback;
import com.apom.audienceapp.interfaces.DialogCallback;
import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.Constants;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.GlobalUtils;
import com.apom.audienceapp.utils.SharedPreferencesUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.R.attr.cacheColorHint;
import static android.R.attr.name;
import static android.R.attr.password;
import static android.R.attr.switchMinWidth;

public class PhoneActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private ImageView profile_image = null;
    private TextView welcome_text = null;
    private Button btn_continue = null;
    private EditTextWithFont et_phone = null;
    private UserObject mUserObj = null;
    private RequestAsyncTask mRequestAsync = null;
    private String phone = null;
    private Context mContext = null;
    private static final String TAG_LOG = "PhoneActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        mContext = this;
        mUserObj = GlobalUtils.getCurrentUserObj();
        profile_image = (ImageView) findViewById(R.id.profile_image);
        welcome_text = (TextView) findViewById(R.id.welcome_text);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        et_phone = (EditTextWithFont) findViewById(R.id.et_phone);

        if (mUserObj != null) {
            try {
                //Loading image from below url into imageView
                if (!mUserObj.getProfile_image_url().equals("")) {
                    Picasso.with(this)
                            .load(mUserObj.getProfile_image_url())
                            .into(profile_image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            welcome_text.setText("Welcome \n" + mUserObj.getFullName());

        }
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterClickContinue();
            }
        });
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }

    private void afterClickContinue() {
        //check phone is available
        phone = et_phone.getText().toString().trim();
        if (phone != null && !phone.equals("")) {
            //show the user choose type popup
            GlobalUtils.chooseUserPopup(this, new DialogCallback() {
                @Override
                public void onAction1() {
                    //request api to update that user
                    //expert
                    mUserObj.setCategory(Constants.USER_TYPE_EXPERT);
                    mUserObj.setStatus(Constants.USER_STATUS_DEACTIVE);
                    requestForSignUp();
                }

                @Override
                public void onAction2() {
                    //request api to update that user
                    //client
                    mUserObj.setCategory(Constants.USER_TYPE_CLIENT);
                    mUserObj.setStatus(Constants.USER_STATUS_ACTIVE);
                    requestForSignUp();
                }

                @Override
                public void onAction3() {

                }

                @Override
                public void onAction4() {

                }
            });
        } else {
            GlobalUtils.showInfoDialog(this, "Warning", "Please enter a phone number for next step", null, null);
        }

    }

    private void goToClientMainPagePage() {
        startActivity(new Intent(PhoneActivity.this, ClientHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
        finish();
    }

    private void goToExpertMainPagePage() {
        startActivity(new Intent(PhoneActivity.this, ExpertHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
        finish();
    }

    private void requestForSignUp() {
        //request to server
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.PARAM_TAG, Constants.SIGN_UP_TAG);
        params.put(Constants.PARAM_ID, mUserObj.getLinked_in_id());
        params.put(Constants.PARAM_NAME, mUserObj.getFullName());
        params.put(Constants.PARAM_EMAIL, mUserObj.getEmail());
        params.put(Constants.PARAM_IMAGE_URL, mUserObj.getProfile_image_url());
        params.put(Constants.PARAM_MOBILE, phone);
        params.put(Constants.PARAM_TYPE, mUserObj.getCategory());
        params.put(Constants.PARAM_INDUSTRY, mUserObj.getIndustry());
        params.put(Constants.PARAM_COMPANY, mUserObj.getJobsList().get(0).getCompany_name());
        params.put(Constants.PARAM_JOB_TITLE, mUserObj.getJobsList().get(0).getJob_title());
        params.put(Constants.PARAM_JOB_SUMMARY, mUserObj.getJobsList().get(0).getJob_summary());
        params.put(Constants.PARAM_LOCATION, mUserObj.getJobsList().get(0).getLocation());
        params.put(Constants.PARAM_STATUS, mUserObj.getStatus());


        mRequestAsync = new RequestAsyncTask(mContext, Constants.REQUEST_REGISTER_USER, params, new AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);
                GlobalUtils.dismissLoadingProgress();
                JSONObject mainJsonObj = null;
                try {
                    mainJsonObj = new JSONObject(result);
                    if (mainJsonObj.getString("success").equals("1")) {
                        SharedPreferencesUtils.putString(mContext, Constants.ID, mUserObj.getLinked_in_id());
                        SharedPreferencesUtils.putBoolean(mContext, Constants.ALREADY_LOGGED_IN, true);
                        GlobalUtils.setCurrentUserObj(mUserObj);
                        switch (mUserObj.getCategory()) {
                            case Constants.USER_TYPE_EXPERT:
                                switch (mUserObj.getStatus()) {
                                    case Constants.USER_STATUS_ACTIVE:
                                        goToExpertMainPagePage();
                                        break;
                                    case Constants.USER_STATUS_DEACTIVE:
                                        goToVerificationPage();
                                        break;
                                }

                                break;
                            case Constants.USER_TYPE_CLIENT:
                                goToClientMainPagePage();
                                break;
                            case Constants.USER_TYPE_ADMIN:
                                //go to admin page
                                goToAdminMainPage();
                                break;
                        }
                    } else {
                        GlobalUtils.showInfoDialog(mContext, null, "Sorry,Not registered yet", null, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void progress() {
                GlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void onInterrupted(Exception e) {
                GlobalUtils.dismissLoadingProgress();
            }

            @Override
            public void onException(Exception e) {
                GlobalUtils.dismissLoadingProgress();
            }
        });

        mRequestAsync.execute();

    }

    private void goToVerificationPage() {
        startActivity(new Intent(PhoneActivity.this, VerificationActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void goToAdminMainPage() {
        startActivity(new Intent(PhoneActivity.this, AdminHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

}
