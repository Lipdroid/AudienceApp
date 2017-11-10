package com.apom.audienceapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class VerificationActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private UserObject mUserObj = null;
    private RequestAsyncTask mRequestAsync = null;
    private Context mContext = null;
    private static final String TAG_LOG = "VerificationActivity";
    private Button check_verification = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        mUserObj = GlobalUtils.getCurrentUserObj();
        mContext = this;
        check_verification = (Button) findViewById(R.id.check_verification) ;
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();

        check_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check user data agian
                getUserByUserId(mUserObj.getLinked_in_id());
            }
        });
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
                        mUserObj = GlobalUtils.parseUser(mainJsonObj.getJSONObject(Constants.TAG_USER));

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
                                        //goToVerificationPage();
                                        //already in verification page
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
                        goToPhonePage();
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
        startActivity(new Intent(VerificationActivity.this, VerificationActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void goToAdminMainPage() {
        startActivity(new Intent(VerificationActivity.this, AdminHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void goToClientMainPagePage() {
        startActivity(new Intent(VerificationActivity.this, ClientHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
        finish();
    }

    private void goToExpertMainPagePage() {
        startActivity(new Intent(VerificationActivity.this, ExpertHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
        finish();
    }

    private void goToPhonePage() {
        startActivity(new Intent(VerificationActivity.this, PhoneActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }
}
