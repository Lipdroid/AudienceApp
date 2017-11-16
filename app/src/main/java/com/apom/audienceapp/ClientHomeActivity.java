package com.apom.audienceapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apom.audienceapp.adapters.UserGridAdapter;
import com.apom.audienceapp.apis.RequestAsyncTask;
import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.customViews.CustomFontTextViewLight;
import com.apom.audienceapp.interfaces.AsyncCallback;
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
import java.util.Timer;
import java.util.TimerTask;

public class ClientHomeActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private CircleImageView btn_profile = null;
    private View header = null;
    private Context mContext = null;
    private LinearLayout user_info = null;


    private CircleImageView profile_image = null;
    private CustomFontTextViewLight welcome_text = null;
    private Button btn_slide_hide = null;
    boolean isDown;
    private Timer _timer = new Timer();
    private long entry_timeInterval = 2000;
    private long exit_timeInterval = 5000;

    private UserObject mUserObj = null;
    private GridView gridview = null;
    private String TAG = "ClientHomeActivity";
    private RequestAsyncTask mRequestAsync = null;
    private List<UserObject> mListUser = null;
    private UserGridAdapter adapter = null;
    private PullToRefreshView mPullToRefreshView = null;
    private boolean is_pulled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);
        header = (View) findViewById(R.id.header);
        btn_profile = (CircleImageView) header.findViewById(R.id.btn_profile);
        user_info = (LinearLayout) findViewById(R.id.user_info);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        welcome_text = (CustomFontTextViewLight) findViewById(R.id.welcome_text);
        btn_slide_hide = (Button) findViewById(R.id.btn_slide_hide);
        gridview = (GridView) findViewById(R.id.gridview);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // mPullToRefreshView.setRefreshing(false);
                        is_pulled = true;
                        getAllUsersExpert();
                    }
                }, 1000);
            }
        });
        mUserObj = GlobalUtils.getCurrentUserObj();

        mContext = this;
        GlobalUtils.computePakageHash(this);
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();

        setUserInfo();
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterClickProfile();
            }

        });

        btn_slide_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_slide();
                _timer.cancel();
            }
        });

        getAllUsersExpert();

        isDown = false;

        _timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // use runOnUiThread(Runnable action)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toggle_slide();
                        _timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                // use runOnUiThread(Runnable action)
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toggle_slide();
                                        _timer.cancel();
                                    }
                                });
                            }
                        }, exit_timeInterval);
                    }
                });
            }
        }, entry_timeInterval);

    }

    private void setUserInfo() {
        welcome_text.setText("Welcome " + mUserObj.getFirstName() + ",These are the people you can book an appointment and meet with");
        //Loading image from below url into imageView
        Picasso.with(mContext)
                .load(mUserObj.getProfile_image_url())
                .into(profile_image);
        Picasso.with(mContext)
                .load(mUserObj.getProfile_image_url())
                .into(btn_profile);
    }

    private void toggle_slide() {
        if (isDown) {
            slide_out_view();

        } else {
            slide_in_view();

        }
        isDown = !isDown;
    }

    private void slide_in_view() {
        GlobalUtils.slideDown(user_info);
    }

    private void slide_out_view() {
        GlobalUtils.slideUp(user_info);
    }

    private void afterClickProfile() {
        goToProfile();
    }

    private void goToLoginPage() {
        startActivity(new Intent(ClientHomeActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }
    private void goToProfile() {
        startActivity(new Intent(ClientHomeActivity.this, ClientProfileActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }
    public void getAllUsersExpert() {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.PARAM_TAG, Constants.TAG_GET_USER_EXPERT);


        mRequestAsync = new RequestAsyncTask(mContext, Constants.REQUEST_GET_ALL_USERS, params, new AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG, result);
                if (is_pulled) {
                    mPullToRefreshView.setRefreshing(false);
                    is_pulled = !is_pulled;
                } else {
                    GlobalUtils.dismissLoadingProgress();
                }
                mListUser = new ArrayList<UserObject>();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has(Constants.TAG_USER)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constants.TAG_USER);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
                            UserObject user = new UserObject();
                            user = GlobalUtils.parseUser(jsonObjectItem);
                            mListUser.add(user);
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
        adapter = new UserGridAdapter(this, mListUser);
        gridview.setAdapter(adapter);
    }
}
