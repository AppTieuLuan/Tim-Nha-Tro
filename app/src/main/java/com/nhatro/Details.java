package com.nhatro;

import android.content.Intent;
import android.os.AsyncTask;
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


import com.nhatro.DAL.HinhAnhs;
import com.nhatro.adapter.MyCustomPagerAdapter;
import com.nhatro.tab_details.TabBanDo;
import com.nhatro.tab_details.TabBinhLuan;
import com.nhatro.tab_details.TabChiTiet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Details extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<String> images;

    MyCustomPagerAdapter myCustomPagerAdapter;
    TabChiTiet tabChiTiet = new TabChiTiet();
    TabBinhLuan tabBinhLuan;
    TabBanDo tabBanDo;
    private FragmentTabHost mTabHost;
    android.support.v4.app.FragmentManager fragmentManager;
    TextView btntabChiTiet, btntabBinhLuan, btntabBanDo;
    int tab;
    Fragment active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        active = tabChiTiet;
        btntabChiTiet = findViewById(R.id.btntabChiTiet);
        btntabBinhLuan = findViewById(R.id.btntabBinhLuan);
        btntabBanDo = findViewById(R.id.btntabBanDo);
        tab = 1;

        Intent callerIntent = getIntent();
        Bundle bundle = callerIntent.getBundleExtra("iditem");
        String iditem = bundle.getString("iditem");

        getSupportActionBar().setTitle(String.valueOf(iditem));

        images = new ArrayList<>();
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

        /*mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

       *//* mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Chi tiết", null),
                TabChiTiet.class, null);*//*


        Bundle bundle1 = new Bundle();
        bundle1.putString("id", iditem);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Chi tiết", null),
                TabChiTiet.class, bundle1);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Bình luận", null),
                TabBinhLuan.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator("Bản đồ", null),
                TabBanDo.class, null);*/

        LoadImages loadImages = new LoadImages();
        loadImages.execute(iditem);


        fragmentManager = getSupportFragmentManager();

        Bundle bundle1 = new Bundle();
        bundle1.putString("id", iditem);
        /*mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Chi tiết", null),
                TabChiTiet.class, bundle1);*/
        tabChiTiet.setArguments(bundle1);
        fragmentManager.beginTransaction().add(R.id.frameeee, tabChiTiet).commit();

        btntabChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tab != 1) {
                    fragmentManager.beginTransaction().hide(active).show(tabChiTiet).commit();
                    tab = 1;
                    active = tabChiTiet;
                }
            }
        });

        btntabBanDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabBanDo == null) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", iditem);
                    tabBanDo = new TabBanDo();
                    tabBanDo.setArguments(bundle1);

                    fragmentManager.beginTransaction().add(R.id.frameeee, tabBanDo).commit();
                    fragmentManager.beginTransaction().hide(active).show(tabBanDo).commit();
                    active = tabBanDo;
                    tab = 3;
                } else {
                    if (tab != 3) {
                        tab = 3;
                        fragmentManager.beginTransaction().hide(active).show(tabBanDo).commit();
                        active = tabBanDo;
                    }
                }
            }
        });

        btntabBinhLuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabBinhLuan == null) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", iditem);
                    tabBinhLuan = new TabBinhLuan();
                    tabBinhLuan.setArguments(bundle1);

                    fragmentManager.beginTransaction().add(R.id.frameeee, tabBinhLuan).commit();
                    fragmentManager.beginTransaction().hide(active).show(tabBinhLuan).commit();
                    active = tabBinhLuan;
                    tab = 2;
                } else {
                    if (tab != 2) {
                        tab = 2;
                        fragmentManager.beginTransaction().hide(active).show(tabBinhLuan).commit();
                        active = tabBinhLuan;
                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public class LoadImages extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            HinhAnhs hinhAnhs = new HinhAnhs();
            images = hinhAnhs.getImages(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            myCustomPagerAdapter = new MyCustomPagerAdapter(getApplicationContext(), images);
            viewPager.setAdapter(myCustomPagerAdapter);
        }
    }
}
