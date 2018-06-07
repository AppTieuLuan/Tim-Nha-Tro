package com.nhatro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.nhatro.DAL.DAL_PhongTro;
import com.nhatro.DAL.HinhAnhs;
import com.nhatro.adapter.MyCustomPagerAdapter;
import com.nhatro.model.Parameter_Luu;
import com.nhatro.model.User;
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
    ImageView edtImage;
    boolean daluu;
    Menu menu;
    Parameter_Luu parameter_luu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghim_details, menu);
        this.menu = menu;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        daluu = false;
        active = tabChiTiet;
        btntabChiTiet = findViewById(R.id.btntabChiTiet);
        btntabBinhLuan = findViewById(R.id.btntabBinhLuan);
        btntabBanDo = findViewById(R.id.btntabBanDo);
        edtImage = findViewById(R.id.edtImage);
        tab = 1;

        Intent callerIntent = getIntent();
        Bundle bundle = callerIntent.getBundleExtra("iditem");
        String iditem = bundle.getString("iditem");
        String tieude = bundle.getString("tieude");
        getSupportActionBar().setTitle(tieude);

        parameter_luu = new Parameter_Luu();
        parameter_luu.setIdphong(iditem);

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

        LoadImages loadImages = new LoadImages();
        loadImages.execute(iditem);

        LoadLuu loadLuu = new LoadLuu();
        loadLuu.execute(iditem);

        SharedPreferences sharedPreferences = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        String userrt = sharedPreferences.getString("MyUser", "");
        if (userrt.equals("") || userrt == null) {
            edtImage.setVisibility(View.GONE);
        } else {
            Gson gsonUser = new Gson();
            User users1 = new User();
            users1 = gsonUser.fromJson(userrt, User.class);

            Parameter_Luu tmps = new Parameter_Luu();
            tmps.setIduser(Integer.parseInt(users1.getId()));
            tmps.setIdphong(iditem);

            isOwner isOwner = new isOwner();
            isOwner.execute(tmps);
        }
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
                    bundle1.putString("tieude", tieude);
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

        edtImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Details.this, EditImage.class);

                Bundle bundle2 = new Bundle();
                bundle2.putStringArrayList("data", images);
                bundle2.putString("iditem", iditem);
                intent.putExtra("data", bundle2);
                startActivityForResult(intent, 1900);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.xong:
                daluu = !daluu;
                //Toast.makeText(getApplicationContext(), "asdasd", Toast.LENGTH_SHORT).show();
                //Drawable drawable = item.getIcon();
                //drawable.mutate();

                //item.getIcon().getColorFilter(Color.parseColor("#008efc"));
                if (daluu) {
                    parameter_luu.setLuu(1);
                    Luu_HuyLuu luu_huyLuu = new Luu_HuyLuu();
                    luu_huyLuu.execute(parameter_luu);
                    item.getIcon().setColorFilter(getResources().getColor(R.color.blues), PorterDuff.Mode.SRC_IN);
                } else {
                    parameter_luu.setLuu(0);
                    Luu_HuyLuu luu_huyLuu = new Luu_HuyLuu();
                    luu_huyLuu.execute(parameter_luu);
                    item.getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class LoadLuu extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... string) {

            SharedPreferences sharedPreferences = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            String user = sharedPreferences.getString("MyUser", "");

            int kq = 0;
            if (user.equals("") || user == null) {
                /*DAL_PhongTro dal_phongTro = new DAL_PhongTro();
                phongTros = dal_phongTro.thongTinPhong(strings[0], -1);*/

            } else {
                Gson gsonUser = new Gson();
                User users1 = new User();
                users1 = gsonUser.fromJson(user, User.class);

                DAL_PhongTro dal_phongTro = new DAL_PhongTro();
                kq = dal_phongTro.isLuu(string[0], Integer.parseInt(users1.getId()));
            }

            return kq;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                daluu = true;
                menu.getItem(0).getIcon().setColorFilter(getResources().getColor(R.color.blues), PorterDuff.Mode.SRC_IN);
            } else {
                daluu = false;
                menu.getItem(0).getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
            }
        }
    }

    class Luu_HuyLuu extends AsyncTask<Parameter_Luu, Void, Integer> {
        @Override
        protected Integer doInBackground(Parameter_Luu... parameter_luus) {

            SharedPreferences sharedPreferences = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            String user = sharedPreferences.getString("MyUser", "");


            if (user.equals("") || user == null) {
                parameter_luus[0].setIduser(-1);

            } else {
                Gson gsonUser = new Gson();
                User users1 = new User();
                users1 = gsonUser.fromJson(user, User.class);

                parameter_luus[0].setIduser(Integer.parseInt(users1.getId()));
            }


            DAL_PhongTro dal_phongTro = new DAL_PhongTro();
            return dal_phongTro.Luu_BoLuu(parameter_luus[0].getIdphong(), parameter_luus[0].getIduser(), parameter_luus[0].getLuu());
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }

    class isOwner extends AsyncTask<Parameter_Luu, Void, Integer> {
        @Override
        protected Integer doInBackground(Parameter_Luu... parameter_luus) {
            DAL_PhongTro dal_phongTro = new DAL_PhongTro();
            return dal_phongTro.isOwner(parameter_luus[0].getIdphong(), parameter_luus[0].getIduser());
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                edtImage.setVisibility(View.VISIBLE);
            } else {
                edtImage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == 10101) {
                Bundle bundles = data.getBundleExtra("data");
                images.clear();
                images.addAll(bundles.getStringArrayList("data"));
                myCustomPagerAdapter = new MyCustomPagerAdapter(getApplicationContext(), images);
                viewPager.setAdapter(myCustomPagerAdapter);
            }
        }
    }
}
