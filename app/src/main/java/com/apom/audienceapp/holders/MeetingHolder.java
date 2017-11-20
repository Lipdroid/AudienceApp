package com.apom.audienceapp.holders;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.apom.audienceapp.customViews.CircleImageView;
import com.apom.audienceapp.customViews.CustomFontTextView;
import com.apom.audienceapp.customViews.CustomFontTextViewLight;

/**
 * Created by mdmunirhossain on 11/20/17.
 */

public class MeetingHolder {
    public CircleImageView profile_image = null;
    public TextView client_name = null;
    public CustomFontTextViewLight purpose = null;
    public CustomFontTextView venue = null;
    public CustomFontTextView date = null;
    public CustomFontTextView time = null;
    public CustomFontTextView status = null;
    public CustomFontTextView expectation_count = null;
    public LinearLayout root = null;
}
