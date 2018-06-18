package com.emrehmrc.argememory.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.interfaces.DefaultActivitiy;
import com.squareup.picasso.Picasso;

public class SplashActivity extends AppCompatActivity  {

    Animation animation;
    ImageView imgArge;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();

        new CountDownTimer(Utils.SPLASH_TİME,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        }.start();


    }

    public void init() {
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.logo_rotate);
        imgArge=findViewById(R.id.imgArge);
        imgArge.setAnimation(animation);
        Picasso.get().load(R.drawable.logoargememeory).resize(256,256).into(imgArge);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
