package com.jang.user.miniproject2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class IntroActivity extends AppCompatActivity {

    RelativeLayout img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        img = findViewById(R.id.layout_img);

        Animation anim = AnimationUtils.loadAnimation(IntroActivity.this,  R.anim.fade_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img.startAnimation(anim);

    }
}
