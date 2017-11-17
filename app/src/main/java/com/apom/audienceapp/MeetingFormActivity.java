package com.apom.audienceapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.apom.audienceapp.apis.RequestAsyncTask;
import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.customViews.CustomFontTextView;
import com.apom.audienceapp.customViews.CustomFontTextViewLight;
import com.apom.audienceapp.customViews.EditTextWithFont;
import com.apom.audienceapp.interfaces.AsyncCallback;
import com.apom.audienceapp.interfaces.DialogCallback;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
        profile_image_sender = (CircleImageView) findViewById(R.id.profile_image_sender);
        name_sender = (CustomFontTextViewLight) findViewById(R.id.name_sender);
        profile_image_receiver = (CircleImageView) findViewById(R.id.profile_image_receiver);
        name_receiver = (CustomFontTextViewLight) findViewById(R.id.name_receiver);
        tv_date = (CustomFontTextView) findViewById(R.id.tv_date);
        tv_time = (CustomFontTextView) findViewById(R.id.tv_time);
        check_agree_terms = (CheckBox) findViewById(R.id.check_agree_terms);

        mUserObj_receiver = getIntent().getParcelableExtra(UserObject.class.toString());
        mUserObj_sender = GlobalUtils.getCurrentUserObj();

        setUpData();

        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
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
    }

    public void afterClickCancel(View view) {
        finish();
    }

    public void openPicker(View view) {
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

                        DateFormat dateFormat = new SimpleDateFormat("EEE-dd-MMMM");
                        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                        String date_str = dateFormat.format(date);
                        String time_str = timeFormat.format(date);

                        tv_date.setText(date_str);
                        tv_time.setText(time_str);

                    }
                }).display();
    }

    public void afterClickConfirm(View view) {
        //check validation
        if(et_purpose.getText() == null || et_purpose.getText().equals("") || et_purpose.getText().toString().isEmpty()){
            GlobalUtils.showInfoDialog(this,"Warning","Please write down the purpose the meeting",null,null);
            return;
        }else if(et_venue.getText() == null || et_venue.getText().equals("") || et_venue.getText().toString().isEmpty()){
            GlobalUtils.showInfoDialog(this,"Warning","Please select a venue",null,null);
            return;
        }else if(!check_agree_terms.isChecked()){
            GlobalUtils.showInfoDialog(this,"Warning","Please check the terms and condotion box",null,null);
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
        params.put(Constants.PARAM_EXPERT_APPROVAL, Constants.USER_NOT_YET_ARROVED);
        params.put(Constants.PARAM_CLIENT_ID, mUserObj_sender.getLinked_in_id());
        params.put(Constants.PARAM_CLIENT_NAME, mUserObj_sender.getFirstName());
        params.put(Constants.PARAM_CLIENT_APPROVAL, Constants.USER_ARROVED);
        params.put(Constants.PARAM_ADMIN_APPROVAL, Constants.USER_NOT_YET_ARROVED);
        params.put(Constants.PARAM_MEETING_TIME, tv_date.getText().toString() + " " +tv_time.getText().toString());
        params.put(Constants.PARAM_MEETING_VENUE,et_venue.getText().toString());
        params.put(Constants.PARAM_MEETING_PURPOSE, et_purpose.getText().toString());
        params.put(Constants.PARAM_MEETING_EXPECTATIO_ONE, et_expectation_one.getText().toString());
        params.put(Constants.PARAM_MEETING_EXPECTATIO_TWO, et_expectation_two.getText().toString());
        params.put(Constants.PARAM_MEETING_EXPECTATIO_THREE, et_expectation_three.getText().toString());
        params.put(Constants.PARAM_MEETING_TIME_CHANGED, Constants.NOT_CHANGED_TIME);
        params.put(Constants.PARAM_MEETING_VENUE_CHANGED, Constants.NOT_CHANGED_VENUE);
        params.put(Constants.PARAM_MEETING_REVIEW, Constants.NOT_YET_REVIEWED);


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
}
