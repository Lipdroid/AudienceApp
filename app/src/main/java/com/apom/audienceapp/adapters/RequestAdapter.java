package com.apom.audienceapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apom.audienceapp.MeetingFormActivity;
import com.apom.audienceapp.R;
import com.apom.audienceapp.apis.RequestAsyncTask;
import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.customViews.CustomFontTextView;
import com.apom.audienceapp.customViews.CustomFontTextViewLight;
import com.apom.audienceapp.holders.MeetingHolder;
import com.apom.audienceapp.holders.UserHolder;
import com.apom.audienceapp.interfaces.AsyncCallback;
import com.apom.audienceapp.interfaces.DialogCallback;
import com.apom.audienceapp.interfaces.InputDialogCallback;
import com.apom.audienceapp.objects.MeetingObject;
import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.Constants;
import com.apom.audienceapp.utils.GlobalUtils;
import com.apom.audienceapp.utils.MultipleScreen;
import com.apom.audienceapp.utils.SharedPreferencesUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by mdmunirhossain on 11/20/17.
 */

public class RequestAdapter extends BaseAdapter {
    private static final String TAG_LOG = "RequestAdapter";
    private Context mContext = null;
    private Activity mActivity = null;
    private List<MeetingObject> mListData = null;
    private MeetingHolder mHolder = null;
    private RequestAsyncTask mRequestAsync = null;

    @Override
    public int getCount() {
        return mListData.size();
    }

    public RequestAdapter(Context mContext, List<MeetingObject> mListData) {
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
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_meeting_request, viewGroup, false);
            mHolder = new MeetingHolder();

            mHolder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);
            mHolder.client_name = (TextView) convertView.findViewById(R.id.client_name);
            mHolder.purpose = (CustomFontTextViewLight) convertView.findViewById(R.id.purpose);
            mHolder.venue = (CustomFontTextView) convertView.findViewById(R.id.venue);
            mHolder.date = (CustomFontTextView) convertView.findViewById(R.id.date);
            mHolder.time = (CustomFontTextView) convertView.findViewById(R.id.time);
            mHolder.status = (CustomFontTextView) convertView.findViewById(R.id.status);
            mHolder.expectation_count = (CustomFontTextView) convertView.findViewById(R.id.expectation_count);
            mHolder.root = (LinearLayout) convertView.findViewById(R.id.root);

            new MultipleScreen(mActivity);
            MultipleScreen.resizeAllView((ViewGroup) convertView);

            convertView.setTag(mHolder);

        } else {
            mHolder = (MeetingHolder) convertView.getTag();
        }
        final MeetingObject meeting = mListData.get(position);

        mHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!GlobalUtils.onClientPage) {
                    if (GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_CLIENT
                    )) {
                        //status
                        final MeetingObject meeting = mListData.get(position);
                        GlobalUtils.showStatusDialog(mContext, meeting, new DialogCallback() {
                            @Override
                            public void onAction1() {
                                GlobalUtils.addEventInCallender(mActivity,mListData.get(position));
                            }

                            @Override
                            public void onAction2() {

                            }

                            @Override
                            public void onAction3() {
                                GlobalUtils.showInputDialog(mContext, meeting.getExpert_image_url(), "Please give a feedback about this meeting", "", null, new InputDialogCallback() {
                                    @Override
                                    public void onAction1(String response) {
                                        updateFeedbackMessage(meeting,response);
                                    }

                                    @Override
                                    public void onAction2() {
                                    }
                                });

                            }

                            @Override
                            public void onAction4() {

                            }
                        });

                    } else if (GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_EXPERT
                    )) {
                        MeetingObject meetingObj = mListData.get(position);
                        if (meetingObj.getExpert_approval().equals(Constants.USER_NOT_YET_ARROVED)) {
                            afterClickMain(position);
                        } else {
                            //status
                            GlobalUtils.showStatusDialog(mContext, meetingObj, new DialogCallback() {
                                @Override
                                public void onAction1() {
                                    GlobalUtils.addEventInCallender(mActivity,mListData.get(position));
                                }

                                @Override
                                public void onAction2() {

                                }

                                @Override
                                public void onAction3() {

                                }

                                @Override
                                public void onAction4() {

                                }
                            });
                        }

                    } else if (GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_ADMIN
                    )) {
                        MeetingObject meetingObj = mListData.get(position);
                        if (meetingObj.getAdmin_approval().equals(Constants.USER_NOT_YET_ARROVED)) {
                            afterClickMain(position);
                        } else {
                            //status
                            GlobalUtils.showStatusDialog(mContext, meetingObj, new DialogCallback() {
                                @Override
                                public void onAction1() {

                                }

                                @Override
                                public void onAction2() {

                                }

                                @Override
                                public void onAction3() {

                                }

                                @Override
                                public void onAction4() {

                                }
                            });
                        }
                    }
                } else {
                    //open status pop up on for view
                    final MeetingObject meetingObj = mListData.get(position);

                    //status
                    GlobalUtils.showStatusDialog(mContext, meetingObj, new DialogCallback() {
                        @Override
                        public void onAction1() {
                            GlobalUtils.addEventInCallender(mActivity,mListData.get(position));
                        }

                        @Override
                        public void onAction2() {

                        }

                        @Override
                        public void onAction3() {
                            GlobalUtils.showInputDialog(mContext, meeting.getExpert_image_url(), "Please give a feedback about this meeting", "", null, new InputDialogCallback() {
                                @Override
                                public void onAction1(String response) {
                                    updateFeedbackMessage(meeting,response);
                                }

                                @Override
                                public void onAction2() {
                                }
                            });
                        }

                        @Override
                        public void onAction4() {

                        }
                    });
                }
            }
        });

        setViews(meeting);

        return convertView;
    }

    private void updateFeedbackMessage(MeetingObject meeting, String response) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.PARAM_TAG, Constants.TAG_UPDATE_REVIEW_MESSAGE);
        params.put(Constants.PARAM_ID, meeting.getId());
        params.put(Constants.PARAM_MEETING_REVIEW, response);


        mRequestAsync = new RequestAsyncTask(mContext, Constants.REQUEST_UPDATE_REVIEW_MESSAGE, params, new AsyncCallback() {
            @Override
            public void done(String result) {
                GlobalUtils.dismissLoadingProgress();
                JSONObject mainJsonObj = null;
                try {
                    mainJsonObj = new JSONObject(result);
                    if (mainJsonObj.getString("success").equals("1")) {
                    } else {
                        GlobalUtils.showInfoDialog(mContext, null, "Sorry,something went wrong please try again", null, null);
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

    public void setViews(MeetingObject meetingObj) {

        Picasso.with(mActivity)
                .load(meetingObj.getClient_image_url())
                .into(mHolder.profile_image);

        mHolder.client_name.setText(meetingObj.getClient_name());
        mHolder.purpose.setText(meetingObj.getMeeting_purpose());
        mHolder.venue.setText(meetingObj.getMeeting_venue());

        if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getExpert_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getAdmin_approval().equals(Constants.USER_ARROVED)
                && !GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
            mHolder.status.setTextColor(mContext.getResources().getColor(R.color.green1));
            mHolder.status.setText("FINISH");

        } else if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getExpert_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getAdmin_approval().equals(Constants.USER_ARROVED)
                && GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
            mHolder.status.setTextColor(mContext.getResources().getColor(R.color.green1));
            mHolder.status.setText("FIXED");
            //can add to callender

        } else if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getExpert_approval().equals(Constants.USER_NOT_YET_ARROVED)
                && meetingObj.getAdmin_approval().equals(Constants.USER_NOT_YET_ARROVED)
                && GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
            mHolder.status.setTextColor(mContext.getResources().getColor(R.color.orange));
            mHolder.status.setText("Pending");

        } else if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getExpert_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getAdmin_approval().equals(Constants.USER_NOT_YET_ARROVED)
                && GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
            if (GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_EXPERT)) {
                mHolder.status.setTextColor(mContext.getResources().getColor(R.color.green1));
                mHolder.status.setText("Done");
            } else {
                mHolder.status.setTextColor(mContext.getResources().getColor(R.color.orange));
                mHolder.status.setText("Pending");
            }

        } else if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getExpert_approval().equals(Constants.USER_REJECTED)
                && meetingObj.getAdmin_approval().equals(Constants.USER_NOT_YET_ARROVED)
                && GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
            mHolder.status.setTextColor(mContext.getResources().getColor(R.color.common_red));
            mHolder.status.setText("Rejected");

        } else if (meetingObj.getClient_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getExpert_approval().equals(Constants.USER_ARROVED)
                && meetingObj.getAdmin_approval().equals(Constants.USER_REJECTED)
                && GlobalUtils.isDateValid(meetingObj.getMeeting_time())) {
            mHolder.status.setTextColor(mContext.getResources().getColor(R.color.common_red));
            mHolder.status.setText("Rejected");

        }


        DateFormat dateFormat = new SimpleDateFormat("EEE-dd-MMMM");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat dateFormatDefault = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT + " hh:mm a");
        Date date = null;
        try {
            date = dateFormatDefault.parse(meetingObj.getMeeting_time());
            String date_str = dateFormat.format(date);
            String time_str = timeFormat.format(date);

            mHolder.date.setText(date_str);
            mHolder.time.setText(time_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int count = 0;
        if (!meetingObj.getMeeting_expectation_one().equals("")) {
            count++;
        }
        if (!meetingObj.getMeeting_expectation_two().equals("")) {
            count++;
        }
        if (!meetingObj.getMeeting_expectation_three().equals("")) {
            count++;
        }

        mHolder.expectation_count.setText(count + "");

    }

    public void afterClickMain(int position) {
        MeetingObject meeting = mListData.get(position);
        Intent intent = new Intent(mContext, MeetingFormActivity.class);
        intent.putExtra(MeetingObject.class.toString(), meeting);
        mContext.startActivity(intent);

    }


    private void setImageByUserId(String user_id) {
        //request to server
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.PARAM_TAG, Constants.TAG_GET_USER);
        params.put(Constants.PARAM_ID, user_id);

        mRequestAsync = new RequestAsyncTask(mContext, Constants.REQUEST_GET_USER_BY_ID, params, new AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);
                JSONObject mainJsonObj = null;
                try {
                    mainJsonObj = new JSONObject(result);
                    if (mainJsonObj.getString("success").equals("1")) {
                        //parse the user
                        UserObject mUserObj = GlobalUtils.parseUser(mainJsonObj.getJSONObject(Constants.TAG_USER));
                        //Loading image from below url into imageView
                        Picasso.with(mActivity)
                                .load(mUserObj.getProfile_image_url())
                                .into(mHolder.profile_image);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void progress() {
            }

            @Override
            public void onInterrupted(Exception e) {
            }

            @Override
            public void onException(Exception e) {
            }
        });

        mRequestAsync.execute();

    }



}
