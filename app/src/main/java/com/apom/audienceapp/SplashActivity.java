package com.apom.audienceapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apom.audienceapp.utils.Constants;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.SharedPreferencesUtils;

public class SplashActivity extends AppCompatActivity {
    //    Views:
    //    Variables:
    CorrectSizeUtil mCorrectSize = null;
    Handler handler = null;
    private long SPLASH_SCREEN_TIME = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        runSplash();
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }
    private void runSplash() {
        {
            handler = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {

                    if (SharedPreferencesUtils.getBoolean(SplashActivity.this, Constants.ALREADY_LOGGED_IN)) {
                        startActivity(new Intent(SplashActivity.this,ClientHomeActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    }
                    finish();
                }
            };
            handler.postDelayed(run, SPLASH_SCREEN_TIME);
        }
    }
}
