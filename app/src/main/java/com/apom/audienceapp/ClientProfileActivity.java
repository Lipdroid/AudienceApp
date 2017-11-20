package com.apom.audienceapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import com.squareup.picasso.Picasso;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientProfileActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private UserObject mUserObj = null;
    private CustomFontTextView name = null;
    private CustomFontTextViewLight designation = null;
    private CircleImageView profile_image = null;

    private static final String TAG = "ClientProfileActivity";
    private PullToRefreshView mPullToRefreshView = null;
    private boolean is_pulled = false;
    private List<MeetingObject> mListMeeting = null;
    private RequestAdapter adapter = null;
    private ListView listView = null;
    private Context mContext = null;
    private RequestAsyncTask mRequestAsync = null;
    private LinearLayout no_result_ui = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        mContext = this;
        name = (CustomFontTextView) findViewById(R.id.name);
        designation = (CustomFontTextViewLight) findViewById(R.id.designation);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        no_result_ui = (LinearLayout) findViewById(R.id.no_result_ui);
        mUserObj = getIntent().getParcelableExtra(UserObject.class.toString());

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        listView = (ListView) findViewById(R.id.req_list);

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
        setUpUser(mUserObj);
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getAllMettings();
        GlobalUtils.onClientPage = true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalUtils.onClientPage = false;
    }

    private void setUpUser(UserObject mUserObj) {
        name.setText(mUserObj.getFirstName());
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
        params.put(Constants.PARAM_ID, mUserObj.getLinked_in_id());
        params.put(Constants.PARAM_TYPE, mUserObj.getCategory());

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

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has(Constants.TAG_MEETING)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constants.TAG_MEETING);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
                            MeetingObject meeting = new MeetingObject();
                            meeting = GlobalUtils.parseMeeting(jsonObjectItem);
                            mListMeeting.add(meeting);
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
        if (mListMeeting.size() > 0) {
            no_result_ui.setVisibility(View.GONE);
        } else {
            no_result_ui.setVisibility(View.GONE);
        }
        adapter = new RequestAdapter(this, mListMeeting);
        listView.setAdapter(adapter);
    }
}
