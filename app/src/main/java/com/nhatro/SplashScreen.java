package com.nhatro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView txtLogo = findViewById(R.id.txtLogo);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/UTM Azuki.ttf");
        txtLogo.setTypeface(tf);
        CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        countDownTimer.start();
    }
    @Override
    public void onBackPressed() {

    }
}
