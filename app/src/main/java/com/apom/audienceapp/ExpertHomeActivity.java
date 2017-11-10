package com.apom.audienceapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.apom.audienceapp.utils.Constants;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.SharedPreferencesUtils;

public class ExpertHomeActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private ImageView btn_logout = null;
    private View header = null;
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_home);
        header = (View) findViewById(R.id.header);
        btn_logout = (ImageView) header.findViewById(R.id.btn_logout);
        mContext = this;
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterClickLogout();
            }
        });
    }

    private void afterClickLogout() {
        SharedPreferencesUtils.removeComponent(mContext, Constants.ALREADY_LOGGED_IN);
        SharedPreferencesUtils.removeComponent(mContext, Constants.ID);
        goToLoginPage();
        finish();
    }

    private void goToLoginPage() {
        startActivity(new Intent(ExpertHomeActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }
}
