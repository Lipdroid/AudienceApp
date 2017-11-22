package com.apom.audienceapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.gsm.GsmCellLocation;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.apom.audienceapp.apis.RequestAsyncTask;
import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.customViews.CustomFontTextView;
import com.apom.audienceapp.customViews.CustomFontTextViewLight;
import com.apom.audienceapp.customViews.EditTextWithFont;
import com.apom.audienceapp.interfaces.AsyncCallback;
import com.apom.audienceapp.interfaces.DialogCallback;
import com.apom.audienceapp.objects.MeetingObject;
import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.Constants;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.GlobalUtils;
import com.apom.audienceapp.utils.SharedPreferencesUtils;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL;

import static com.apom.audienceapp.R.id.phone;

public class MeetingFormActivity extends AppCompatActivity {
    private static final String TAG_LOG = "MeetingFormActivity";
    CorrectSizeUtil mCorrectSize = null;
    private EditText et_purpose = null;
    private EditText et_venue = null;
    private EditText et_expectation_one = null;
    private EditText et_expectation_two = null;
    private EditText et_expectation_three = null;
    private CircleImageView profile_image_sender = null;
    private CustomFontTextViewLight name_sender = null;
    private CircleImageView profile_image_receiver = null;
    private CustomFontTextViewLight name_receiver = null;
    private CustomFontTextView tv_date = null;
    private CustomFontTextView tv_time = null;
    private CheckBox check_agree_terms = null;
    private UserObject mUserObj_receiver;
    private UserObject mUserObj_sender;
    private RequestAsyncTask mRequestAsync = null;
    private Context mContext = null;
    private MeetingObject meetingObj = null;
    Button check_verification = null;
    private LinearLayout expert_ui = null;


    private TextView tv_purpose = null;
    private TextView tv_venue = null;
    private TextView tv_expectation_one = null;
    private TextView tv_expectation_two = null;
    private TextView tv_expectation_three = null;

    private ScrollView scrollView_two = null;
    private ScrollView scrollView_three = null;
    private String date_str_api = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_meeting_form);

        mContext = this;

        et_purpose = (EditText) findViewById(R.id.et_purpose);
        et_venue = (EditText) findViewById(R.id.et_venue);
        et_expectation_one = (EditText) findViewById(R.id.et_expectation_one);
        et_expectation_two = (EditText) findViewById(R.id.et_expectation_two);
        et_expectation_three = (EditText) findViewById(R.id.et_expectation_three);


        tv_purpose = (TextView) findViewById(R.id.tv_purpose);
        tv_venue = (TextView) findViewById(R.id.tv_venue);
        tv_expectation_one = (TextView) findViewById(R.id.tv_expectation_one);
        tv_expectation_two = (TextView) findViewById(R.id.tv_expectation_two);
        tv_expectation_three = (TextView) findViewById(R.id.et_expectation_three);

        scrollView_two = (ScrollView) findViewById(R.id.scrollView_two);
        scrollView_three = (ScrollView) findViewById(R.id.scrollView_three);

        profile_image_sender = (CircleImageView) findViewById(R.id.profile_image_sender);
        name_sender = (CustomFontTextViewLight) findViewById(R.id.name_sender);
        profile_image_receiver = (CircleImageView) findViewById(R.id.profile_image_receiver);
        name_receiver = (CustomFontTextViewLight) findViewById(R.id.name_receiver);
        tv_date = (CustomFontTextView) findViewById(R.id.tv_date);
        tv_time = (CustomFontTextView) findViewById(R.id.tv_time);
        check_agree_terms = (CheckBox) findViewById(R.id.check_agree_terms);
        check_verification = (Button) findViewById(R.id.check_verification);
        expert_ui = (LinearLayout) findViewById(R.id.expert_ui);
        meetingObj = getIntent().getParcelableExtra(MeetingObject.class.toString());

        if (meetingObj != null) {
            setUpDataWithMeeting(meetingObj);
        } else {
            mUserObj_receiver = getIntent().getParcelableExtra(UserObject.class.toString());
            mUserObj_sender = GlobalUtils.getCurrentUserObj();
            meetingObj = new MeetingObject();
            meetingObj.setClient_id(mUserObj_sender.getLinked_in_id());
            meetingObj.setExpert_id(mUserObj_receiver.getLinked_in_id());

            setUpData();
        }

        profile_image_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserById(meetingObj.getExpert_id());

            }
        });

        profile_image_sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserById(meetingObj.getClient_id());

            }
        });
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }

    private void setUpDataWithMeeting(MeetingObject meetingObj) {

        et_purpose.setText(meetingObj.getMeeting_purpose());
        et_venue.setText(meetingObj.getMeeting_venue());

        et_purpose.setVisibility(View.GONE);
        tv_purpose.setText(et_purpose.getText().toString());
        tv_purpose.setVisibility(View.VISIBLE);

        et_venue.setVisibility(View.GONE);
        tv_venue.setText(et_venue.getText().toString());
        tv_venue.setVisibility(View.VISIBLE);


        if (!meetingObj.getMeeting_expectation_one().equals("")) {
            et_expectation_one.setText(meetingObj.getMeeting_expectation_one());
        } else {
            et_expectation_one.setText("No expectation");

        }
        et_expectation_one.setVisibility(View.GONE);
        tv_expectation_one.setText(et_expectation_one.getText().toString());
        tv_expectation_one.setVisibility(View.VISIBLE);

        if (!meetingObj.getMeeting_expectation_two().equals("")) {
            et_expectation_two.setText(meetingObj.getMeeting_expectation_two());
            et_expectation_two.setVisibility(View.GONE);
            tv_expectation_two.setText(et_expectation_two.getText().toString());
            tv_expectation_two.setVisibility(View.VISIBLE);
        } else {
            et_expectation_two.setVisibility(View.GONE);
            tv_expectation_two.setVisibility(View.GONE);
            scrollView_two.setVisibility(View.GONE);
        }
        if (!meetingObj.getMeeting_expectation_three().equals("")) {
            et_expectation_three.setText(meetingObj.getMeeting_expectation_three());
            et_expectation_three.setVisibility(View.GONE);
            tv_expectation_three.setText(et_expectation_three.getText().toString());
            tv_expectation_three.setVisibility(View.VISIBLE);
        } else {
            et_expectation_three.setVisibility(View.GONE);
            tv_expectation_three.setVisibility(View.GONE);
            scrollView_three.setVisibility(View.GONE);
        }
        check_agree_terms.setVisibility(View.INVISIBLE);


        DateFormat dateFormat = new SimpleDateFormat("EEE-dd-MMMM");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat dateFormatDefault = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT + " hh:mm a");
        Date date = null;
        try {
            date = dateFormatDefault.parse(meetingObj.getMeeting_time());
            String date_str = dateFormat.format(date);
            String time_str = timeFormat.format(date);

            tv_date.setText(date_str);
            tv_time.setText(time_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        check_verification.setVisibility(View.INVISIBLE);
        check_verification.setVisibility(View.GONE);
        expert_ui.setVisibility(View.VISIBLE);

        name_receiver.setText(meetingObj.getExpert_name());
        name_sender.setText(meetingObj.getClient_name());

        //Loading image from below url into imageView
        Picasso.with(MeetingFormActivity.this)
                .load(meetingObj.getClient_image_url())
                .into(profile_image_sender);
        //Loading image from below url into imageView
        Picasso.with(MeetingFormActivity.this)
                .load(meetingObj.getExpert_image_url())
                .into(profile_image_receiver);


    }

    private void setUpData() {
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("EEE-dd-MMMM");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String date_str = dateFormat.format(currentDate);
        String time_str = timeFormat.format(currentDate);

        tv_date.setText(date_str);
        tv_time.setText(time_str);
        name_receiver.setText(mUserObj_receiver.getFirstName());
        name_sender.setText(mUserObj_sender.getFirstName());
        Picasso.with(this)
                .load(mUserObj_sender.getProfile_image_url())
                .into(profile_image_sender);
        Picasso.with(this)
                .load(mUserObj_receiver.getProfile_image_url())
                .into(profile_image_receiver);

        date_str_api = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT).format(Calendar.getInstance().getTime());
    }

    public void afterClickCancel(View view) {
        finish();
    }

    public void openPicker(View view) {
        if (GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_CLIENT)) {
            new SingleDateAndTimePickerDialog.Builder(this)
                    .bottomSheet()
                    .curved()
                    .minDateRange(Calendar.getInstance().getTime())
                    .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                        @Override
                        public void onDisplayed(SingleDateAndTimePicker picker) {
                            //retrieve the SingleDateAndTimePicker
                        }
                    })

                    .title("")
                    .listener(new SingleDateAndTimePickerDialog.Listener() {
                        @Override
                        public void onDateSelected(Date date) {

                            DateFormat dateFormat = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
                            DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                            String date_str = dateFormat.format(date);
                            String time_str = timeFormat.format(date);
                            date_str_api = date_str;
                            tv_date.setText(GlobalUtils.formatDateString(date_str));
                            tv_time.setText(time_str);

                        }
                    }).display();
        }
    }

    public void afterClickConfirm(View view) {
        //check validation
        if (et_purpose.getText() == null || et_purpose.getText().equals("") || et_purpose.getText().toString().isEmpty()) {
            GlobalUtils.showInfoDialog(this, "Warning", "Please write down the purpose the meeting", null, null);
            return;
        } else if (et_venue.getText() == null || et_venue.getText().equals("") || et_venue.getText().toString().isEmpty()) {
            GlobalUtils.showInfoDialog(this, "Warning", "Please select a venue", null, null);
            return;
        } else if (!check_agree_terms.isChecked()) {
            GlobalUtils.showInfoDialog(this, "Warning", "Please check the terms and condotion box", null, null);
            return;
        }

        //request api

        requestForSubmiteeting();
    }

    private void requestForSubmiteeting() {
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put(Constants.PARAM_TAG, Constants.MEETING_TAG);
        params.put(Constants.PARAM_EXPERT_ID, mUserObj_receiver.getLinked_in_id());
        params.put(Constants.PARAM_EXPERT_NAME, mUserObj_receiver.getFirstName());
        params.put(Constants.PARAM_EXPERT_IMAGE_URL, mUserObj_receiver.getProfile_image_url());
        params.put(Constants.PARAM_EXPERT_APPROVAL, Constants.USER_NOT_YET_ARROVED);
        params.put(Constants.PARAM_CLIENT_ID, mUserObj_sender.getLinked_in_id());
        params.put(Constants.PARAM_CLIENT_NAME, mUserObj_sender.getFirstName());
        params.put(Constants.PARAM_CLIENT_IMAGE_URL, mUserObj_sender.getProfile_image_url());
        params.put(Constants.PARAM_CLIENT_APPROVAL, Constants.USER_ARROVED);
        params.put(Constants.PARAM_ADMIN_APPROVAL, Constants.USER_NOT_YET_ARROVED);
        params.put(Constants.PARAM_MEETING_TIME, date_str_api + " " + tv_time.getText().toString());
        params.put(Constants.PARAM_MEETING_VENUE, et_venue.getText().toString());
        params.put(Constants.PARAM_MEETING_PURPOSE, et_purpose.getText().toString());
        params.put(Constants.PARAM_MEETING_EXPECTATIO_ONE, et_expectation_one.getText().toString());
        params.put(Constants.PARAM_MEETING_EXPECTATIO_TWO, et_expectation_two.getText().toString());
        params.put(Constants.PARAM_MEETING_EXPECTATIO_THREE, et_expectation_three.getText().toString());
        params.put(Constants.PARAM_MEETING_TIME_CHANGED, Constants.NOT_CHANGED_TIME);
        params.put(Constants.PARAM_MEETING_VENUE_CHANGED, Constants.NOT_CHANGED_VENUE);
        params.put(Constants.PARAM_MEETING_REVIEW, Constants.NOT_YET_REVIEWED);
        params.put(Constants.PARAM_APPROVE_MESSAGE, Constants.NOT_YET_MESSAGE);


        mRequestAsync = new RequestAsyncTask(mContext, Constants.REQUEST_ADD_MEETING, params, new AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);
                GlobalUtils.dismissLoadingProgress();
                JSONObject mainJsonObj = null;
                try {
                    mainJsonObj = new JSONObject(result);
                    if (mainJsonObj.getString("success").equals("1")) {

                        GlobalUtils.showInfoDialog(mContext, null, "Your meeting request is submitted,you can check the status of the meeting in your profile", null, new DialogCallback() {
                            @Override
                            public void onAction1() {
                                finish();
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

                    } else {
                        GlobalUtils.showInfoDialog(mContext, null, "Sorry,something went wrong,please try again", null, null);
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

    private void getUserById(String user_id) {
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
                        UserObject mUserObj = GlobalUtils.parseUser(mainJsonObj.getJSONObject(Constants.TAG_USER));
                        if (!mUserObj.getCategory().equals(Constants.USER_TYPE_CLIENT) &&(GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_CLIENT) || GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_ADMIN))) {
                            //go to expert Profile
                            UserObject user = mUserObj;
                            Intent intent = new Intent(MeetingFormActivity.this, ExpertProfileActivity.class);
                            intent.putExtra(UserObject.class.toString(), user);
                            startActivity(intent);

                        }else if (!mUserObj.getCategory().equals(Constants.USER_TYPE_EXPERT) && (GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_EXPERT) || GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_ADMIN))){
                            //go to client Profile
                            UserObject user = mUserObj;
                            Intent intent = new Intent(MeetingFormActivity.this, ClientProfileActivity.class);
                            intent.putExtra(UserObject.class.toString(), user);
                            startActivity(intent);

                        }

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

    public void afterClickDecline(View view) {
        requestStatusChange(meetingObj, Constants.USER_REJECTED);

    }

    private void requestStatusChange(final MeetingObject meetingObj, String chamge_type) {
        //request to server
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.PARAM_TAG, Constants.TAG_UPDATE_MEETING_STATUS);
        params.put(Constants.PARAM_ID, meetingObj.getId());
        params.put(Constants.PARAM_TYPE, GlobalUtils.getCurrentUserObj().getCategory());
        params.put(Constants.PARAM_STATUS, chamge_type);


        mRequestAsync = new RequestAsyncTask(mContext, Constants.REQUEST_UPDATE_MEETING_BY_ID, params, new AsyncCallback() {
            @Override
            public void done(String result) {
                GlobalUtils.dismissLoadingProgress();
                JSONObject mainJsonObj = null;
                try {
                    mainJsonObj = new JSONObject(result);
                    if (mainJsonObj.getString("success").equals("1")) {
                        finish();
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

    public void afterClickApprove(View view) {
        requestStatusChange(meetingObj, Constants.USER_ARROVED);

    }
}
