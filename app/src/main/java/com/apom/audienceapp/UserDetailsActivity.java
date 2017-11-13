package com.apom.audienceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.CorrectSizeUtil;

public class UserDetailsActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private UserObject mUserObj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        mUserObj = getIntent().getParcelableExtra(UserObject.class.toString());

        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }
}
