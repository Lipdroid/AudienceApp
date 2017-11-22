package com.apom.audienceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.customViews.CustomFontTextView;
import com.apom.audienceapp.customViews.CustomFontTextViewLight;
import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.Constants;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.GlobalUtils;
import com.squareup.picasso.Picasso;

public class UserDetailsActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private UserObject mUserObj = null;
    private CustomFontTextView name = null;
    private CustomFontTextViewLight designation = null;
    private CircleImageView profile_image = null;
    private Button btnAction = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        name = (CustomFontTextView) findViewById(R.id.name);
        designation = (CustomFontTextViewLight) findViewById(R.id.designation);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        btnAction= (Button) findViewById(R.id.btnAction);
        mUserObj = getIntent().getParcelableExtra(UserObject.class.toString());
        setUpUser(mUserObj);
        if(!GlobalUtils.getCurrentUserObj().getCategory().equals(Constants.USER_TYPE_CLIENT)){
            btnAction.setVisibility(View.GONE);
        }
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
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

    public void afterClickAppointment(View view) {
        openAppointmentPopup();
    }

    private void openAppointmentPopup() {
        Intent intent = new Intent(this, MeetingFormActivity.class);
        intent.putExtra(UserObject.class.toString(), mUserObj);
        startActivity(intent);
    }
}
