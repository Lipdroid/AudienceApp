package com.apom.audienceapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.Constants;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.GlobalUtils;
import com.apom.audienceapp.utils.SharedPreferencesUtils;
import com.squareup.picasso.Picasso;

public class ExpertHomeActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private CircleImageView btn_profile = null;
    private View header = null;
    private Context mContext = null;
    private UserObject mUserObj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_home);
        header = (View) findViewById(R.id.header);
        btn_profile = (CircleImageView) header.findViewById(R.id.btn_profile);
        mContext = this;
        mUserObj = GlobalUtils.getCurrentUserObj();
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
        setUserInfo();
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterClickProfile();
            }
        });
    }

    private void afterClickLogout() {
        SharedPreferencesUtils.removeComponent(mContext, Constants.ALREADY_LOGGED_IN);
        SharedPreferencesUtils.removeComponent(mContext, Constants.ID);
        goToLoginPage();
        finish();
    }

    private void afterClickProfile() {
        goToProfile();
    }

    private void goToProfile() {
        startActivity(new Intent(ExpertHomeActivity.this, ExpertProfileActivity.class));
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
