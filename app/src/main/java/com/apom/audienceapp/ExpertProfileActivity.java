package com.apom.audienceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.customViews.CustomFontTextView;
import com.apom.audienceapp.customViews.CustomFontTextViewLight;
import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.GlobalUtils;
import com.squareup.picasso.Picasso;

public class ExpertProfileActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private UserObject mUserObj = null;
    private CustomFontTextView name = null;
    private CustomFontTextViewLight designation = null;
    private CircleImageView profile_image = null;
    private CustomFontTextViewLight phone = null;
    private CustomFontTextViewLight job_summary = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_profile);

        name = (CustomFontTextView) findViewById(R.id.name);
        designation = (CustomFontTextViewLight) findViewById(R.id.designation);
        phone = (CustomFontTextViewLight) findViewById(R.id.phone);
        job_summary = (CustomFontTextViewLight) findViewById(R.id.job_summary);

        profile_image = (CircleImageView) findViewById(R.id.profile_image);

        mUserObj = GlobalUtils.getCurrentUserObj();
        setUpUser(mUserObj);
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }

    private void setUpUser(UserObject mUserObj) {
        name.setText(mUserObj.getFirstName());
        phone.setText(mUserObj.getMobile());
        job_summary.setText(mUserObj.getJobsList().get(0).getJob_summary());
        designation.setText(mUserObj.getJobsList().get(0).getJob_title() + " at " + mUserObj.getJobsList().get(0).getCompany_name());
        //Loading image from below url into imageView
        Picasso.with(this)
                .load(mUserObj.getProfile_image_url())
                .into(profile_image);
    }

    public void afterClickBack(View view) {
        finish();
    }


}
