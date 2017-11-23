package com.apom.audienceapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.apom.audienceapp.adapters.FeedbackAdapter;
import com.apom.audienceapp.adapters.RequestAdapter;
import com.apom.audienceapp.apis.RequestAsyncTask;
import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.customViews.CustomFontTextView;
import com.apom.audienceapp.customViews.CustomFontTextViewLight;
import com.apom.audienceapp.interfaces.AsyncCallback;
import com.apom.audienceapp.objects.MeetingObject;
import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.Constants;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.GlobalUtils;
import com.apom.audienceapp.utils.SharedPreferencesUtils;
import com.squareup.picasso.Picasso;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ExpertProfileActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private UserObject mUserObj = null;
    private CustomFontTextView name = null;
    private CustomFontTextViewLight designation = null;
    private CircleImageView profile_image = null;
    private CustomFontTextViewLight phone = null;
    private CustomFontTextViewLight job_summary = null;
    private List<MeetingObject> mListMeeting = null;
    private FeedbackAdapter adapter = null;
    private ListView listView = null;
    private Context mContext = null;
    private static final String TAG = "ExpertProfileActivity";
    private PullToRefreshView mPullToRefreshView = null;
    private boolean is_pulled = false;
    private RequestAsyncTask mRequestAsync = null;
    private LinearLayout no_review_ui = null;
    private LinearLayout phone_ui = null;
    private int count_success = 0;
    private int count_fail = 0;
    private int count_reject = 0;
    private CustomFontTextViewLight tv_success = null;
    private CustomFontTextViewLight tv_fail = null;
    private CustomFontTextViewLight tv_reject = null;
    private Button sign_out_btn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_profile);

        name = (CustomFontTextView) findViewById(R.id.name);
        designation = (CustomFontTextViewLight) findViewById(R.id.designation);
        phone = (CustomFontTextViewLight) findViewById(R.id.phone);
        job_summary = (CustomFontTextViewLight) findViewById(R.id.job_summary);
        tv_success = (CustomFontTextViewLight) findViewById(R.id.tv_success);
        tv_fail = (CustomFontTextViewLight) findViewById(R.id.tv_fail);
        tv_reject = (CustomFontTextViewLight) findViewById(R.id.tv_reject);
        phone_ui = (LinearLayout) findViewById(R.id.phone_ui);
        no_review_ui = (LinearLayout) findViewById(R.id.no_review_ui);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        mUserObj = getIntent().getParcelableExtra(UserObject.class.toString());
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        listView = (ListView) findViewById(R.id.list_feedback);
        sign_out_btn = (Button) findViewById(R.id.sign_out_btn);

        if(GlobalUtils.getCurrentUserObj().getCategory().equals(mUserObj.getCategory())){
            sign_out_btn.setVisibility(View.VISIBLE);
        }else{
            sign_out_btn.setVisibility(View.GONE);
        }
        if(GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_CLIENT)){
            phone_ui.setVisibility(View.GONE);
        }
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // mPullToRefreshView.setRefreshing(false);
                        is_pulled = true;
                        getAllMettings();
                    }
                }, 1000);
            }
        });
        mContext = this;
        if (mUserObj != null) {
            setUpUser(mUserObj);
        }
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllMettings();

    }

    private void setUpUser(UserObject mUserObj) {
        name.setText(mUserObj.getFirstName());
        phone.setText(mUserObj.getMobile());
        job_summary.setText(mUserObj.getJobsList().get(0).getJob_summary());
        designation.setText(mUserObj.getJobsList().get(0).getJob_title() + " at " + mUserObj.getJobsList().get(0).getCompany_name());
        //Loading image from below url into imageView
        Picasso.with(this)
                .load(mUserObj.getProfile_image_url())
                .into(profile_image);
    }

    public void afterClickBack(View view) {
        finish();
    }

    public void getAllMettings() {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.PARAM_TAG, Constants.TAG_GET_MEETINGS_EXPERT);
        params.put(Constants.PARAM_TYPE, Constants.USER_TYPE_EXPERT);
        params.put(Constants.PARAM_ID, mUserObj.getLinked_in_id());

        mRequestAsync = new RequestAsyncTask(mContext, Constants.REQUEST_GET_ALL_MEETINGS_BY_EXPERT_ID, params, new AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG, result);

                if (is_pulled) {
                    mPullToRefreshView.setRefreshing(false);
                    is_pulled = !is_pulled;
                } else {
                    GlobalUtils.dismissLoadingProgress();
                }
                mListMeeting = new ArrayList<MeetingObject>();
                count_success = 0;
                count_fail = 0;
                count_reject = 0;

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has(Constants.TAG_MEETING)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constants.TAG_MEETING);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
                            MeetingObject meetingObj = new MeetingObject();
                            meetingObj = GlobalUtils.parseMeeting(jsonObjectItem);
                            if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                                    && meetingObj.getExpert_approval().equals(Constants.USER_ARROVED)
                                    && meetingObj.getAdmin_approval().equals(Constants.USER_ARROVED)
                                    && !GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
                                mListMeeting.add(meetingObj);
                                count_success++;
                            }
                            if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                                    && meetingObj.getExpert_approval().equals(Constants.USER_REJECTED)
                                    && meetingObj.getAdmin_approval().equals(Constants.USER_NOT_YET_ARROVED)
                                    && GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
                                count_fail++;


                            }
                            if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                                    && meetingObj.getExpert_approval().equals(Constants.USER_ARROVED)
                                    && meetingObj.getAdmin_approval().equals(Constants.USER_REJECTED)
                                    && GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
                                count_reject++;


                            }

                        }


                    }
                    populateList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void progress() {
                if (!is_pulled)
                    GlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void onInterrupted(Exception e) {
                if (is_pulled) {
                    mPullToRefreshView.setRefreshing(false);
                    is_pulled = !is_pulled;
                } else {
                    GlobalUtils.dismissLoadingProgress();
                }
            }

            @Override
            public void onException(Exception e) {
                if (is_pulled) {
                    mPullToRefreshView.setRefreshing(false);
                    is_pulled = !is_pulled;
                } else {
                    GlobalUtils.dismissLoadingProgress();
                }
            }
        });

        mRequestAsync.execute();

    }

    private void populateList() {
        Collections.reverse(mListMeeting);

        tv_fail.setText(count_fail+" \nFailure");
        tv_success.setText(count_success+" \nSuccess");
        tv_reject.setText(count_reject+" \nReject");

        if (mListMeeting.size() > 0) {
            no_review_ui.setVisibility(View.GONE);
        } else {
            no_review_ui.setVisibility(View.VISIBLE);
        }
        adapter = new FeedbackAdapter(this, mListMeeting);
        listView.setAdapter(adapter);
    }

    public void afterClickSignOut(View view) {
        afterClickLogout();
    }

    private void afterClickLogout() {
        SharedPreferencesUtils.removeComponent(mContext, Constants.ALREADY_LOGGED_IN);
        SharedPreferencesUtils.removeComponent(mContext, Constants.ID);
        GlobalUtils.setCurrentUserObj(null);
        goToLoginPage();
        finish();
    }
    private void goToLoginPage() {
        startActivity(new Intent(ExpertProfileActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }
}
