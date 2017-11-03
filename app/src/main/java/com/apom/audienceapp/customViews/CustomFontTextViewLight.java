package com.apom.audienceapp.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Sabbir Ahmed on 1/21/2017.
 */

public class CustomFontTextViewLight extends TextView {
    public CustomFontTextViewLight(Context context) {
        super(context);
        init();
    }

    public CustomFontTextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomFontTextViewLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "light.ttf");
        setTypeface(tf);
    }
}
