package com.apom.audienceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apom.audienceapp.utils.CorrectSizeUtil;

public class ExpertHomeActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_home);
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }
}
