package com.nhatro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;


import com.nhatro.adapter.MyCustomPagerAdapter;
import com.nhatro.tab_details.TabBanDo;
import com.nhatro.tab_details.TabBinhLuan;
import com.nhatro.tab_details.TabChiTiet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Details extends AppCompatActivity {

    ViewPager viewPager;
    String images[] = {"https://nhatroservice.000webhostapp.com/images/.20180423224728ee.jpg",
            "https://znews-photo-td.zadn.vn/w660/Uploaded/natmzz/2018_04_01/0_2.jpg",
            "https://znews-photo-td.zadn.vn/w660/Uploaded/natmzz/2018_04_01/0.jpg",
            "https://znews-photo-td.zadn.vn/w660/Uploaded/fcivbqmv/2018_03_31/Facebookhacker.jpg",
            "https://znews-photo-td.zadn.vn/w660/Uploaded/natmzz/2018_03_31/1_13.jpg",
            "https://znews-stc.zdn.vn/static/campaign/tuyendung/2018/tuyendung2018_2.jpg",
            "https://znews-photo-td.zadn.vn/w660/Uploaded/natmzz/2018_03_31/1_8.jpg"};

    MyCustomPagerAdapter myCustomPagerAdapter;

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent callerIntent = getIntent();
        Bundle bundle = callerIntent.getBundleExtra("iditem");
        int iditem = bundle.getInt("iditem");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(String.valueOf(iditem));


        ImageView imageView = new ImageView(getSupportActionBar().getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.icon_call);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        getSupportActionBar().setCustomView(imageView);


        //getSupportActionBar().hide();
        //getSupportActionBar().setTitle("Khách sạn VÍPfhgyt678797897898ghfghfghfghfgh");
        viewPager = (ViewPager) findViewById(R.id.pager);

        myCustomPagerAdapter = new MyCustomPagerAdapter(getApplicationContext(), images);
        viewPager.setAdapter(myCustomPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

       /* mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Chi tiết", null),
                TabChiTiet.class, null);*/


        Bundle bundle1 = new Bundle();
        bundle1.putInt("id",iditem);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Chi tiết", null),
                TabChiTiet.class, bundle1);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Bình luận", null),
                TabBinhLuan.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator("Bản đồ", null),
                TabBanDo.class, null);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
