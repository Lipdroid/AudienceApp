package com.apom.audienceapp.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by lipuhossain on 1/19/17.
 */

public class EditTextWithFont extends EditText {

    public EditTextWithFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "light.ttf");
            setTypeface(tf);
        }
    }

}
