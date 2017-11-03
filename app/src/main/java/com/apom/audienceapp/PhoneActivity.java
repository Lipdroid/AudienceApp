package com.apom.audienceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apom.audienceapp.customViews.EditTextWithFont;
import com.apom.audienceapp.interfaces.DialogCallback;
import com.apom.audienceapp.objects.UserObject;
import com.apom.audienceapp.utils.CorrectSizeUtil;
import com.apom.audienceapp.utils.GlobalUtils;
import com.squareup.picasso.Picasso;

public class PhoneActivity extends AppCompatActivity {
    CorrectSizeUtil mCorrectSize = null;
    private ImageView profile_image = null;
    private TextView welcome_text = null;
    private Button btn_continue = null;
    private EditTextWithFont et_phone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        UserObject mUserObject = GlobalUtils.getCurrentUserObj();
        profile_image = (ImageView) findViewById(R.id.profile_image);
        welcome_text = (TextView) findViewById(R.id.welcome_text);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        et_phone = (EditTextWithFont) findViewById(R.id.et_phone);

        if(mUserObject != null){
            //Loading image from below url into imageView
            Picasso.with(this)
                    .load(mUserObject.getProfile_image_url())
                    .into(profile_image);
            welcome_text.setText("Welcome \n" + mUserObject.getFullName());

        }
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterClickContinue();
            }
        });
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }

    private void afterClickContinue() {
        //check phone is available
        String phone = et_phone.getText().toString().trim();
        if (phone != null && !phone.equals("")) {
            //show the user choose type popup
            GlobalUtils.chooseUserPopup(this, new DialogCallback() {
                @Override
                public void onAction1() {
                    //request api to update that user
                    //expert
                    goToExpertMainPagePage();
                }

                @Override
                public void onAction2() {
                    //request api to update that user
                    //client
                    goToClientMainPagePage();
                }

                @Override
                public void onAction3() {

                }

                @Override
                public void onAction4() {

                }
            });
        } else {
            GlobalUtils.showInfoDialog(this, "Warning", "Please enter a phone number for next step", null, null);
        }

    }

    private void goToClientMainPagePage() {
        startActivity(new Intent(PhoneActivity.this, ClientHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
        finish();
    }

    private void goToExpertMainPagePage() {
        startActivity(new Intent(PhoneActivity.this, ExpertHomeActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
        finish();
    }
}
