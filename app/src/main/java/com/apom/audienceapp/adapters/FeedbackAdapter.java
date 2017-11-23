package com.apom.audienceapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apom.audienceapp.R;
import com.apom.audienceapp.apis.RequestAsyncTask;
import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.customViews.CustomFontTextView;
import com.apom.audienceapp.customViews.CustomFontTextViewLight;
import com.apom.audienceapp.holders.FeedbackHolder;
import com.apom.audienceapp.holders.MeetingHolder;
import com.apom.audienceapp.objects.MeetingObject;
import com.apom.audienceapp.utils.MultipleScreen;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mdmunirhossain on 11/23/17.
 */

public class FeedbackAdapter extends BaseAdapter {

    private static final String TAG_LOG = "RequestAdapter";
    private Context mContext = null;
    private Activity mActivity = null;
    private List<MeetingObject> mListData = null;
    private FeedbackHolder mHolder = null;
    private RequestAsyncTask mRequestAsync = null;

    @Override
    public int getCount() {
        return mListData.size();
    }

    public FeedbackAdapter(Context mContext, List<MeetingObject> mListData) {
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
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_meeting_feedback, viewGroup, false);
            mHolder = new FeedbackHolder();

            mHolder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);
            mHolder.client_name = (TextView) convertView.findViewById(R.id.client_name);
            mHolder.feedback = (TextView) convertView.findViewById(R.id.feedback);
            mHolder.root = (LinearLayout) convertView.findViewById(R.id.root);

            new MultipleScreen(mActivity);
            MultipleScreen.resizeAllView((ViewGroup) convertView);

            convertView.setTag(mHolder);

        } else {
            mHolder = (FeedbackHolder) convertView.getTag();
        }
        final MeetingObject meetingObj = mListData.get(position);
        Picasso.with(mActivity)
                .load(meetingObj.getClient_image_url())
                .into(mHolder.profile_image);

        mHolder.client_name.setText(meetingObj.getClient_name());
        mHolder.feedback.setText(meetingObj.getMeeting_review());

        return convertView;

    }
}
