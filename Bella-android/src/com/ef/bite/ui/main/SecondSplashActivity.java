package com.ef.bite.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.ef.bite.R;

/**
 * This splash show first
 * Created by yang on 15/5/12.
 */
public class SecondSplashActivity extends Activity{
    private final static long DELAY_TIME=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_splash);
        setupViews();
    }

    private void setupViews() {
        delay();
    }

    private void delay(){
        new Handler().postDelayed(new Runnable(){
            public void run() {
                finish();
            }
        }, DELAY_TIME);
    }

    @Override
    public void finish() {
        Intent intent=new Intent();
        intent.putExtra("test", "testvvvvvvv");
        setResult(RESULT_OK, intent);

        super.finish();
        this.overridePendingTransition(0, R.anim.activity_sliding_out_vertical);
    }
}
