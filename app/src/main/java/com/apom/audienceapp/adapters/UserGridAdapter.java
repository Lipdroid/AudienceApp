package com.apom.audienceapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apom.audienceapp.AdminHomeActivity;
import com.apom.audienceapp.R;
import com.apom.audienceapp.apis.RequestAsyncTask;
import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.customViews.CustomFontTextView;
import com.apom.audienceapp.customViews.CustomFontTextViewLight;
import com.apom.audienceapp.holders.UserHolder;
import com.apom.audienceapp.interfaces.AsyncCallback;
import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.Constants;
import com.apom.audienceapp.utils.GlobalUtils;
import com.apom.audienceapp.utils.MultipleScreen;
import com.apom.audienceapp.utils.SharedPreferencesUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mdmunirhossain on 11/9/17.
 */

public class UserGridAdapter extends BaseAdapter {
    private Context mContext = null;
    private Activity mActivity = null;
    private List<UserObject> mListData = null;
    private UserHolder mHolder = null;
    private RequestAsyncTask mRequestAsync = null;

    @Override
    public int getCount() {
        return mListData.size();
    }

    public UserGridAdapter(Context mContext, List<UserObject> mListData) {
        this.mContext = mContext;
        mActivity = (Activity) mContext;
        this.mListData = mListData;

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_user, viewGroup, false);
            mHolder = new UserHolder();

            mHolder.name = (CustomFontTextView) convertView.findViewById(R.id.name);
            mHolder.designation = (CustomFontTextViewLight) convertView.findViewById(R.id.designation);
            mHolder.location = (CustomFontTextViewLight) convertView.findViewById(R.id.location);
            mHolder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);
            mHolder.btn_action = (Button) convertView.findViewById(R.id.btn_action);
            mHolder.verified_mark = (ImageView) convertView.findViewById(R.id.verified_mark);
            new MultipleScreen(mActivity);
            MultipleScreen.resizeAllView((ViewGroup) convertView);

            convertView.setTag(mHolder);
        } else {
            mHolder = (UserHolder) convertView.getTag();
        }

        UserObject user = mListData.get(position);
        mHolder.btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                after_click_action(position);
            }
        });

        showUser(user);
        return convertView;
    }

    private void after_click_action(int position) {
        UserObject user = mListData.get(position);
        if(user.getCategory().equals(Constants.USER_TYPE_EXPERT) && user.getStatus().equals(Constants.USER_STATUS_DEACTIVE)){
            requestStatusChange(user);
        }

    }

    private void requestStatusChange(final UserObject user) {
        //request to server
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.PARAM_TAG, Constants.TAG_UPDATE_USER_STATUS);
        params.put(Constants.PARAM_ID, user.getLinked_in_id());
        params.put(Constants.PARAM_STATUS, Constants.USER_STATUS_ACTIVE);


        mRequestAsync = new RequestAsyncTask(mContext, Constants.REQUEST_UPDATE_USER_BY_ID, params, new AsyncCallback() {
            @Override
            public void done(String result) {
                GlobalUtils.dismissLoadingProgress();
                JSONObject mainJsonObj = null;
                try {
                    mainJsonObj = new JSONObject(result);
                    if (mainJsonObj.getString("success").equals("1")) {
                        user.setStatus(Constants.USER_STATUS_ACTIVE);
                        if(mActivity instanceof AdminHomeActivity){
                            AdminHomeActivity activity = (AdminHomeActivity)mActivity;
                            activity.updateList(user);
                        }
                    } else {

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

    private void showUser(UserObject user) {
        mHolder.name.setText(user.getFirstName());
        mHolder.designation.setText(user.getJobsList().get(0).getJob_title() + " at " + user.getJobsList().get(0).getCompany_name());
        mHolder.location.setText(user.getJobsList().get(0).getLocation());
        //Loading image from below url into imageView
        Picasso.with(mActivity)
                .load(user.getProfile_image_url())
                .into(mHolder.profile_image);

        switch (user.getCategory()) {
            case Constants.USER_TYPE_EXPERT:
                switch (user.getStatus()) {
                    case Constants.USER_STATUS_ACTIVE:
                        mHolder.btn_action.setText("VERIFIED");
                        mHolder.verified_mark.setVisibility(View.VISIBLE);
                        break;
                    case Constants.USER_STATUS_DEACTIVE:
                        mHolder.verified_mark.setVisibility(View.GONE);
                        break;
                }
                break;
            case Constants.USER_TYPE_CLIENT:
                mHolder.btn_action.setText("CLIENT");
                break;
            case Constants.USER_TYPE_ADMIN:
                //go to admin page
                mHolder.btn_action.setText("ADMIN");
                break;
        }
    }
}
