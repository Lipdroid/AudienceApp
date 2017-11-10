package com.apom.audienceapp.holders;

import android.widget.Button;
import android.widget.ImageView;

import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.customViews.CustomFontTextView;
import com.apom.audienceapp.customViews.CustomFontTextViewLight;

/**
 * Created by mdmunirhossain on 11/9/17.
 */

public class UserHolder {
    public CustomFontTextView name;
    public CustomFontTextViewLight designation;
    public CustomFontTextViewLight location;
    public CircleImageView profile_image;
    public Button btn_action;
    public ImageView verified_mark = null;
}
