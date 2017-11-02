package com.apom.audienceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.apom.audienceapp.objects.JobObject;
import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.GlobalUtils;
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
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_linkedin = null;
    CorrectSizeUtil mCorrectSize = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                    String firstName = jsonObject.getString("firstName");
                    String lastName = jsonObject.getString("lastName");
                    String pictureUrl = jsonObject.getString("pictureUrl");
                    String emailAddress = jsonObject.getString("emailAddress");
                    String id = jsonObject.getString("id");
                    String industry = jsonObject.getString("industry");

                    JSONObject positionsJson = jsonObject.getJSONObject("positions");
                    JSONArray positionArray = positionsJson.getJSONArray("values");
                    List<JobObject> jobsList = new ArrayList<JobObject>();
                    for (int i = 0; i < positionArray.length(); i++) {
                        JSONObject jobJson = positionArray.getJSONObject(i);
                        String title = jobJson.getString("title");
                        String summary = jobJson.getString("summary");
                        String company = jobJson.getJSONObject("company").getString("name");
                        String location = jobJson.getJSONObject("location").getString("name");

                        JobObject job = new JobObject();
                        job.setCompany_name(company);
                        job.setJob_title(title);
                        job.setJob_summary(summary);
                        job.setLocation(location);
                        jobsList.add(job);
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

                    goToPhonePage();

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

    private void goToPhonePage() {
        startActivity(new Intent(LoginActivity.this, PhoneActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }
}