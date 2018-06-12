package com.nhatro;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.model.LocDL;

public class ManageNewsActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    Fragment fragmentPT, fragmentTT, active;
    TextView txtpt, txttt;
    LocDL locDL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_news);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tin đã đăng");

        txtpt = findViewById(R.id.btnphongtro);
        txttt = findViewById(R.id.btntimphong);

        fragmentManager = getSupportFragmentManager();
        fragmentPT = new ManagerPTFragment();
        fragmentTT = new ManagerTTFragment();

        locDL = new LocDL();

        Bundle bundlemap2 = new Bundle();
        bundlemap2.putInt("giamin", locDL.getGiamin());
        bundlemap2.putInt("giamax", locDL.getGiamax());
        bundlemap2.putInt("dientichmin", locDL.getDientichmin());
        bundlemap2.putInt("dientichmax", locDL.getDientichmax());
        bundlemap2.putInt("songuoio", locDL.getSonguoio());
        bundlemap2.putString("loaitin", locDL.getLoaitin());
        bundlemap2.putString("tiennghi", locDL.getTiennghi());
        bundlemap2.putInt("doituong", locDL.getDoituong());
        bundlemap2.putInt("giogiac", locDL.getGiogiac());
        bundlemap2.putInt("idtp", locDL.getIdtp());
        bundlemap2.putString("idqh", "");
        bundlemap2.putInt("trang", 1);
        fragmentPT.setArguments(bundlemap2);

        fragmentManager.beginTransaction()
                .add(R.id.framecontent, fragmentPT)
                .add(R.id.framecontent, fragmentTT)
                .commit();

        active = fragmentPT;
        fragmentManager.beginTransaction().hide(fragmentTT).show(active).commit();

        txtpt.setTextColor(Color.parseColor("#1594E9"));

        txtpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(active != fragmentPT) {
                    txtpt.setTextColor(Color.parseColor("#1594E9"));
                    txttt.setTextColor(Color.parseColor("#000000"));
                    fragmentManager.beginTransaction().hide(active).show(fragmentPT).commit();
                    active = fragmentPT;
                }
            }
        });
        txttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(active != fragmentTT){
                    txttt.setTextColor(Color.parseColor("#1594E9"));
                    txtpt.setTextColor(Color.parseColor("#000000"));
                    fragmentManager.beginTransaction().hide(active).show(fragmentTT).commit();
                    active = fragmentTT;
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
