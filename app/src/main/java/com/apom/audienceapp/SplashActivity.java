package com.apom.audienceapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.apom.audienceapp.apis.RequestAsyncTask;
import com.apom.audienceapp.interfaces.AsyncCallback;
import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.Constants;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.GlobalUtils;
import com.apom.audienceapp.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {
    //    Views:
    //    Variables:
    CorrectSizeUtil mCorrectSize = null;
    Handler handler = null;
    private long SPLASH_SCREEN_TIME = 2000;
    private Context mContext = null;
    private static final String TAG_LOG = "SplashActivity";
    private RequestAsyncTask mRequestAsync = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GlobalUtils.computePakageHash(this);
        mContext = this;
//        SharedPreferencesUtils.putString(mContext, Constants.ID, Constants.USER_ID_ADMIN);
//        SharedPreferencesUtils.putBoolean(mContext, Constants.ALREADY_LOGGED_IN, true);
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        runSplash();
    }

    private void runSplash() {
        {
            handler = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {

                    if (SharedPreferencesUtils.getBoolean(SplashActivity.this, Constants.ALREADY_LOGGED_IN)) {
                        String linked_in_id = SharedPreferencesUtils.getString(SplashActivity.this, Constants.ID);
                        if (linked_in_id != null) {
                            getUserByUserId(linked_in_id);
                        }
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();

                    }
                }
            };
            handler.postDelayed(run, SPLASH_SCREEN_TIME);
        }
    }


    private void getUserByUserId(String user_id) {
        //request to server
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.PARAM_TAG, Constants.TAG_GET_USER);
        params.put(Constants.PARAM_ID, user_id);

        mRequestAsync = new RequestAsyncTask(mContext, Constants.REQUEST_GET_USER_BY_ID, params, new AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);
                GlobalUtils.dismissLoadingProgress();
                JSONObject mainJsonObj = null;
                try {
                    mainJsonObj = new JSONObject(result);
                    if (mainJsonObj.getString("success").equals("1")) {
                        //parse the user
                        UserObject mUserObj = GlobalUtils.parseUser(mainJsonObj.getJSONObject(Constants.TAG_USER));
                        GlobalUtils.setCurrentUserObj(mUserObj);

                        SharedPreferencesUtils.putString(mContext, Constants.ID, mUserObj.getLinked_in_id());
                        SharedPreferencesUtils.putBoolean(mContext, Constants.ALREADY_LOGGED_IN, true);

                        switch (mUserObj.getCategory()) {
                            case Constants.USER_TYPE_EXPERT:
                                switch (mUserObj.getStatus()) {
                                    case Constants.USER_STATUS_ACTIVE:
                                        goToExpertMainPagePage();
                                        finish();
                                        break;
                                    case Constants.USER_STATUS_DEACTIVE:
                                        goToVerificationPage();
                                        finish();
                                        break;
                                }
                                break;
                            case Constants.USER_TYPE_CLIENT:
                                goToClientMainPagePage();
                                finish();
                                break;
                            case Constants.USER_TYPE_ADMIN:
                                //go to admin page
                                goToAdminMainPage();
                                finish();
                                break;
                        }
                    } else {
                        goToLoginPage();
                        finish();
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
        startActivity(new Intent(SplashActivity.this, VerificationActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void goToAdminMainPage() {
        startActivity(new Intent(SplashActivity.this, AdminHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void goToLoginPage() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void goToClientMainPagePage() {
        startActivity(new Intent(SplashActivity.this, ClientHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
        finish();
    }

    private void goToExpertMainPagePage() {
        startActivity(new Intent(SplashActivity.this, ExpertHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
        finish();
    }
}
