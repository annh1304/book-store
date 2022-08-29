package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

public class WelcomeActivity extends AppCompatActivity {
//    private static int SPLASH_SCREEN_TIMEOUT = 2000;
    private static  int SPLASH_SCREEN = 3000;
    Animation topAnim , bottomAnim , fade;
    ImageView imgWelcome;
    TextView tvWelcome , tvWelcome1;
    CardView cv1 , cv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_welcome);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_welcome);
        fade = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        imgWelcome = this.findViewById(R.id.imgWelcome);
        tvWelcome = this.findViewById(R.id.tvWelcome);
        tvWelcome1 = this.findViewById(R.id.tvWelcome1);
        cv1 = this.findViewById(R.id.cv1);
        cv2 = this.findViewById(R.id.cv2);

        imgWelcome.setAnimation(topAnim);
        tvWelcome.setAnimation(bottomAnim);
        tvWelcome1.setAnimation(bottomAnim);
        cv1.setAnimation(fade);
        cv2.setAnimation(fade);

//        Animation fadeOut = new AlphaAnimation(1,0);
//        fadeOut.setInterpolator(new AccelerateInterpolator());
//        fadeOut.setStartOffset(500);
//        fadeOut.setDuration(1800);
//        ImageView wlcmImg = findViewById(R.id.imageView);
//
//        wlcmImg.setAnimation(fadeOut);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        }, SPLASH_SCREEN);

    }
}