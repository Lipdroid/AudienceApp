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
import com.squareup.picasso.Picasso;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UserDetailsActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private UserObject mUserObj = null;
    private CustomFontTextView name = null;
    private CustomFontTextViewLight designation = null;
    private CircleImageView profile_image = null;
    private Button btnAction = null;
    private FeedbackAdapter adapter = null;

    private List<MeetingObject> mListMeeting = null;
    private ListView listView = null;
    private Context mContext = null;
    private static final String TAG = "UserDetailsActivity";
    private PullToRefreshView mPullToRefreshView = null;
    private boolean is_pulled = false;
    private RequestAsyncTask mRequestAsync = null;
    private LinearLayout no_review_ui = null;
    private int count_success = 0;
    private int count_fail = 0;
    private int count_reject = 0;
    private CustomFontTextViewLight tv_success = null;
    private CustomFontTextViewLight tv_fail = null;
    private CustomFontTextViewLight tv_reject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        name = (CustomFontTextView) findViewById(R.id.name);
        designation = (CustomFontTextViewLight) findViewById(R.id.designation);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        btnAction = (Button) findViewById(R.id.btnAction);
        no_review_ui = (LinearLayout) findViewById(R.id.no_review_ui);
        mUserObj = getIntent().getParcelableExtra(UserObject.class.toString());
        tv_success = (CustomFontTextViewLight) findViewById(R.id.tv_success);
        tv_fail = (CustomFontTextViewLight) findViewById(R.id.tv_fail);
        tv_reject = (CustomFontTextViewLight) findViewById(R.id.tv_reject);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        listView = (ListView) findViewById(R.id.list_feedback);

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
        setUpUser(mUserObj);
        if (!GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_CLIENT)) {
            btnAction.setVisibility(View.GONE);
        }
        getAllMettings();

        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }

    private void setUpUser(UserObject mUserObj) {
        name.setText(mUserObj.getFirstName());
        designation.setText(mUserObj.getJobsList().get(0).getJob_title() + " at " + mUserObj.getJobsList().get(0).getCompany_name());
        try {
            if (!mUserObj.getProfile_image_url().equals("")) {
                //Loading image from below url into imageView
                Picasso.with(this)
                        .load(mUserObj.getProfile_image_url())
                        .into(profile_image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void afterClickBack(View view) {
        finish();
    }

    public void afterClickAppointment(View view) {
        openAppointmentPopup();
    }

    private void openAppointmentPopup() {
        if (btnAction.getText().toString().equals("Apply For Appointment")) {
            Intent intent = new Intent(this, MeetingFormActivity.class);
            intent.putExtra(UserObject.class.toString(), mUserObj);
            startActivity(intent);
        }else{
            GlobalUtils.showInfoDialog(mContext,"Info","You have already fixed a meeting with this expert,you can get another appointment after finishing that",null,null);
        }
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
        tv_fail.setText(count_fail + " \nFailure");
        tv_success.setText(count_success + " \nSuccess");
        tv_reject.setText(count_reject + " \nReject");

        if (mListMeeting.size() > 0) {
            Collections.reverse(mListMeeting);
            no_review_ui.setVisibility(View.GONE);
        } else {
            no_review_ui.setVisibility(View.VISIBLE);
        }
        adapter = new FeedbackAdapter(this, mListMeeting);
        listView.setAdapter(adapter);

        for (MeetingObject meeting : GlobalUtils.booked_meetings
                ) {
            if (mUserObj.getLinked_in_id().equals(meeting.getExpert_id()) && meeting.getClient_approval().equals(Constants.USER_ARROVED)
                    && meeting.getExpert_approval().equals(Constants.USER_ARROVED)
                    && meeting.getAdmin_approval().equals(Constants.USER_ARROVED)
                    && GlobalUtils.isDateValid(meeting.getMeeting_time())) {
                btnAction.setText("BOOKED");
                return;
            }

        }
    }
}
