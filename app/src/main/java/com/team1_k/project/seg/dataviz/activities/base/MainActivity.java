package com.team1_k.project.seg.dataviz.activities.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.team1_k.project.seg.dataviz.R;

/**
 * The MainActivity will start the mainViewActivity,
 * once it has finished showing the starting logo for 5 seconds.
 *
 * @author Team2K
 */
public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        /**
         * A countDownTimer for displaying the logo for 5 seconds:
         */
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            /**
             * once timer is finished the mainViewActivity will be started
             */
            @Override
            public void onFinish() {
                //set the new Content the new mainViewActivity
                Intent mainViewActivity = new Intent(MainActivity.this, MainViewActivity.class);
                startActivity(mainViewActivity);
            }
        }.start();
    }
}
