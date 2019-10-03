package com.bsalazar.kekomo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bsalazar on 06/07/2017.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Fade());
        }


        ImageView background_splash = (ImageView) findViewById(R.id.background_splash);
        double rnd = Math.random();
        if(rnd < 0.25d)
            background_splash.setImageResource(R.drawable.background1);
        else if(rnd < 0.5d)
            background_splash.setImageResource(R.drawable.background2);
        else if(rnd < 0.75d)
            background_splash.setImageResource(R.drawable.background3);
        else
            background_splash.setImageResource(R.drawable.background4);


        final Activity activity = this;
        TimerTask task = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                runOnUiThread(() -> {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
                    startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), REQUEST_EXIT, options.toBundle());
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 2000);
    }

    private int REQUEST_EXIT = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();

            }
        }
    }
}
