package com.apom.audienceapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.apom.audienceapp.adapters.RequestAdapter;
import com.apom.audienceapp.adapters.UserGridAdapter;
import com.apom.audienceapp.apis.RequestAsyncTask;
import com.apom.audienceapp.customViews.CircleImageView;
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
import java.util.HashMap;
import java.util.List;

public class ExpertHomeActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private CircleImageView btn_profile = null;
    private View header = null;
    private Context mContext = null;
    private UserObject mUserObj = null;
    private RequestAsyncTask mRequestAsync = null;
    private static final String TAG = "ExpertHomeActivity";
    private PullToRefreshView mPullToRefreshView = null;
    private boolean is_pulled = false;
    private List<MeetingObject> mListMeeting = null;
    private RequestAdapter adapter = null;
    private ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_home);
        header = (View) findViewById(R.id.header);
        btn_profile = (CircleImageView) header.findViewById(R.id.btn_profile);
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
        mContext = this;
        mUserObj = GlobalUtils.getCurrentUserObj();
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
        getAllMettings();
        setUserInfo();
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterClickProfile();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void afterClickLogout() {
        SharedPreferencesUtils.removeComponent(mContext, Constants.ALREADY_LOGGED_IN);
        SharedPreferencesUtils.removeComponent(mContext, Constants.ID);
        goToLoginPage();
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
        adapter = new RequestAdapter(this, mListMeeting);
        listView.setAdapter(adapter);
    }

    private void afterClickProfile() {
        goToProfile();
    }

    private void goToProfile() {
        UserObject user = GlobalUtils.getCurrentUserObj();
        Intent intent = new Intent(ExpertHomeActivity.this, ExpertProfileActivity.class);
        intent.putExtra(UserObject.class.toString(), user);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }


    private void setUserInfo() {
        Picasso.with(mContext)
                .load(mUserObj.getProfile_image_url())
                .into(btn_profile);
    }

    private void goToLoginPage() {
        startActivity(new Intent(ExpertHomeActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }
}
