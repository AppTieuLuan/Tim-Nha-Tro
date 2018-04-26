package com.nhatro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class IntroduceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Giới thiệu");
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
