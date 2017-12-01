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
import com.apom.audienceapp.objects.JobObject;
import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.Constants;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.GlobalUtils;
import com.apom.audienceapp.utils.SharedPreferencesUtils;
import com.apom.audienceapp.utils.UrlConstants;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_linkedin = null;
    CorrectSizeUtil mCorrectSize = null;
    private RequestAsyncTask mRequestAsync = null;
    private Context mContext = null;
    private static final String TAG_LOG = "LoginActivity";
    private UserObject mUserObj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        btn_linkedin = (Button) findViewById(R.id.btn_linkedin);
        btn_linkedin.setOnClickListener(this);
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_linkedin:
                loginWithLinkedIn();
                break;
        }
    }

    private void loginWithLinkedIn() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                fetchProfileInfo();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
                Log.e("LinkedIn Login", "error: " + error.toString());
            }
        }, true);

    }

    // Build the list of member permissions our LinkedIn session requires
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS, Scope.W_SHARE, Scope.RW_COMPANY_ADMIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void fetchProfileInfo() {
        GlobalUtils.showLoadingProgress(this);
        String url = UrlConstants.USER_PROFILE_URL;
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                GlobalUtils.dismissLoadingProgress();
                try {
                    JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                    Log.e("response", jsonObject + "");
                    String firstName = "";
                    String lastName = "";
                    String pictureUrl = "";
                    String emailAddress = "";
                    String id = "";
                    String industry = "";

                    if (jsonObject.has("firstName") && jsonObject.getString("firstName") != null) {
                        firstName = jsonObject.getString("firstName");
                    }
                    if (jsonObject.has("lastName") && jsonObject.getString("lastName") != null) {
                        lastName = jsonObject.getString("lastName");
                    }
                    if (jsonObject.has("pictureUrl") && jsonObject.getString("pictureUrl") != null) {
                        pictureUrl = jsonObject.getString("pictureUrl");
                    }
                    if (jsonObject.has("emailAddress") && jsonObject.getString("emailAddress") != null) {
                        emailAddress = jsonObject.getString("emailAddress");
                    }
                    if (jsonObject.has("id") && jsonObject.getString("id") != null) {
                        id = jsonObject.getString("id");
                    }
                    if (jsonObject.has("industry") && jsonObject.getString("industry") != null) {
                        industry = jsonObject.getString("industry");
                    }
                    List<JobObject> jobsList = new ArrayList<JobObject>();

                    if (jsonObject.has("positions")) {
                        JSONObject positionsJson = jsonObject.getJSONObject("positions");
                        if (positionsJson.has("values")) {
                            JSONArray positionArray = positionsJson.getJSONArray("values");

                            jobsList = new ArrayList<JobObject>();
                            for (int i = 0; i < positionArray.length(); i++) {
                                JSONObject jobJson = positionArray.getJSONObject(i);
                                String title = "";
                                String summary = "";
                                String company = "";
                                String location = "";


                                if (jobJson.has("title") && jobJson.getString("title") != null) {
                                    title = jobJson.getString("title");
                                }
                                if (jobJson.has("summary") && jobJson.getString("summary") != null) {
                                    summary = jobJson.getString("summary");
                                }

                                if (jobJson.has("company")) {
                                    JSONObject companyJson = jobJson.getJSONObject("company");
                                    if (companyJson.has("name") && companyJson.getString("name") != null) {
                                        company = companyJson.getString("name");
                                    }
                                }

                                if (jobJson.has("location")) {
                                    JSONObject locationJson = jobJson.getJSONObject("location");
                                    if (locationJson.has("name") && locationJson.getString("name") != null) {
                                        location = locationJson.getString("name");
                                    }
                                }

                                JobObject job = new JobObject();
                                job.setCompany_name(company);
                                job.setJob_title(title);
                                job.setJob_summary(summary);
                                job.setLocation(location);
                                jobsList.add(job);
                            }
                        }

                    }

                    UserObject mUserObject = new UserObject();
                    mUserObject.setEmail(emailAddress);
                    mUserObject.setFirstName(firstName);
                    mUserObject.setLastName(lastName);
                    mUserObject.setIndustry(industry);
                    mUserObject.setLinked_in_id(id);
                    mUserObject.setProfile_image_url(pictureUrl);
                    mUserObject.setJobsList(jobsList);

                    GlobalUtils.setCurrentUserObj(mUserObject);

                    getUserByUserId(mUserObject.getLinked_in_id());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
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
                        goToPhonePage();
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
        startActivity(new Intent(LoginActivity.this, VerificationActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void goToAdminMainPage() {
        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void goToClientMainPagePage() {
        startActivity(new Intent(LoginActivity.this, ClientHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
        finish();
    }

    private void goToExpertMainPagePage() {
        startActivity(new Intent(LoginActivity.this, ExpertHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
        finish();
    }

    private void goToPhonePage() {
        startActivity(new Intent(LoginActivity.this, PhoneActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    @Override
    public void onBackPressed() {
        //nothing happens
    }
}
