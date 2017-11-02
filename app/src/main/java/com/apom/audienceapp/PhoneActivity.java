package com.apom.audienceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.GlobalUtils;
import com.squareup.picasso.Picasso;

public class PhoneActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private ImageView profile_image= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        UserObject mUserObject = GlobalUtils.getCurrentUserObj();
        profile_image = (ImageView) findViewById(R.id.profile_image);
        //Loading image from below url into imageView
        Picasso.with(this)
                .load(mUserObject.getProfile_image_url())
                .into(profile_image);
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }
}
