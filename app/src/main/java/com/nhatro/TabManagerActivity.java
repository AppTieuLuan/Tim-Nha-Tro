package com.nhatro;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.nhatro.model.LocDL;

public class TabManagerActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

//    LocDL locDL;
    Fragment fragmentPT, fragmentTT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tin đã đăng");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        //tabLayout.setBackgroundColor(Color.parseColor("#000000"));
//        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
//        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
//        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

//        locDL = new LocDL();

        fragmentPT = new ManagerPTFragment();
        fragmentTT = new ManagerTTFragment();

//        Bundle bundlemap2 = new Bundle();
//        bundlemap2.putInt("giamin", locDL.getGiamin());
//        bundlemap2.putInt("giamax", locDL.getGiamax());
//        bundlemap2.putInt("dientichmin", locDL.getDientichmin());
//        bundlemap2.putInt("dientichmax", locDL.getDientichmax());
//        bundlemap2.putInt("songuoio", locDL.getSonguoio());
//        bundlemap2.putString("loaitin", locDL.getLoaitin());
//        bundlemap2.putString("tiennghi", locDL.getTiennghi());
//        bundlemap2.putInt("doituong", locDL.getDoituong());
//        bundlemap2.putInt("giogiac", locDL.getGiogiac());
//        bundlemap2.putInt("idtp", locDL.getIdtp());
//        bundlemap2.putString("idqh", "");
//        bundlemap2.putInt("trang", 1);
//        fragmentPT.setArguments(bundlemap2);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return fragmentPT;
                case 1:
                    return fragmentTT;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Phòng trọ";
                case 1:
                    return "Tìm phòng";
                default:
                    return null;
            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
