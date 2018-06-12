package com.nhatro;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.google.gson.Gson;
import com.nhatro.login.LoginFragment;
import com.nhatro.model.Token;
import com.nhatro.model.User;
import com.nhatro.retrofit.APIUtils;
import com.nhatro.retrofit.DataClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    AHBottomNavigation bottomNavigation;
    final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    HomeFragment homeFragment = new HomeFragment();
    NotifyFragment notifyFragment = new NotifyFragment();

    AccountFragment accountFragment = new AccountFragment();
    tim_o_ghep tim_o_ghep = new tim_o_ghep();
    Fragment active = homeFragment;
    private int t1, t2, t3, t4 = 0;

    @Override
    public void onBackPressed() {
        if (active != homeFragment) {
            bottomNavigation.setCurrentItem(0, false);
            fragmentManager.beginTransaction().hide(active).show(homeFragment).commit();
            active = homeFragment;
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActionBar actionBar =getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setLogo(R.drawable.home);
//        actionBar.setDisplayUseLogoEnabled(true);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_layout_actionbar);

        checkLogin();//kiểm tra đăng nhập
        refreshToken();//refresh token

        TextView txtLogo = findViewById(R.id.txtLogo);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/UTM Azuki.ttf");
        txtLogo.setTypeface(tf);
        //actionBar.hide();

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        /////////////
        // Test
        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setForceTint(true);

        // Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        //////////


        final AHBottomNavigationItem item1 = new AHBottomNavigationItem("Tìm phòng", R.drawable.home);
        bottomNavigation.addItem(item1);

        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Tìm ở ghép", R.drawable.news);
        bottomNavigation.addItem(item2);


        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Thông báo", R.drawable.notify);
        bottomNavigation.addItem(item3);

        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Tài khoản", R.drawable.account);
        bottomNavigation.addItem(item4);
        bottomNavigation.setCurrentItem(0);

        //bottomNavigation.setNotification("5", 2);

        // Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#1594E9"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    bottomNavigation.setCurrentItem(0, false);
                    if (active != homeFragment) {
                        fragmentManager.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                    }
                } else {
                    if (position == 1) {
                        // Active item ở vị trí 1
                        bottomNavigation.setCurrentItem(1, false);
                        if (t1 == 1) { // Đã add Fragment post
                            // Nếu post fragment không phải đang active
                            if (active != tim_o_ghep) {
                                fragmentManager.beginTransaction().hide(active).show(tim_o_ghep).commit();
                                //Set active là post Fragment
                                active = tim_o_ghep;
                            }
                            //active = postFragment;
                        } else { // chưa add Fragment post
                            t1 = 1; // Set là đã add
                            // Add vào
                            fragmentManager.beginTransaction().add(R.id.frame, tim_o_ghep).commit();
                            fragmentManager.beginTransaction().hide(active).show(tim_o_ghep).commit();
                            active = tim_o_ghep;
                        }
                    } else {
                        if (position == 2) {
                            bottomNavigation.setCurrentItem(2, false);
                            if (t2 == 1) { // Đã add Fragment thông báo
                                // Nếu notify fragment không phải đang active
                                if (active != notifyFragment) {
                                    fragmentManager.beginTransaction().hide(active).show(notifyFragment).commit();
                                    //Set active là notify Fragment
                                    active = notifyFragment;
                                }
                            } else { // chưa add Fragment notify
                                t2 = 1; // Set là đã add
                                // Add vào
                                fragmentManager.beginTransaction().add(R.id.frame, notifyFragment, "notify").commit();
                                fragmentManager.beginTransaction().hide(active).show(notifyFragment).commit();
                                active = notifyFragment;
                            }

                        } else {
                            if (position == 3) {
                                bottomNavigation.setCurrentItem(3, false);
                                if (t3 == 1) { // Đã add Fragment account
                                    // Nếu account fragment không phải đang active
                                    if (active != accountFragment) {
                                        fragmentManager.beginTransaction().hide(active).show(accountFragment).commit();
                                        //Set active là account Fragment
                                        active = accountFragment;
                                    }
                                } else { // chưa add Fragment account
                                    t3 = 1; // Set là đã add
                                    // Add vào
                                    fragmentManager.beginTransaction().add(R.id.frame, accountFragment).commit();
                                    fragmentManager.beginTransaction().hide(active).show(accountFragment).commit();
                                    active = accountFragment;
                                }
                            } else {
                                // possition = 2

                                /*bottomNavigation.setCurrentItem(2, false);
                                if (t4 == 1) { // Đã add Fragment Nhận thông báo
                                    // Nếu nhận thông báo fragment không phải đang active
                                    if (active != dangKyNhanTinFragment) {
                                        fragmentManager.beginTransaction().hide(active).show(dangKyNhanTinFragment).commit();
                                        //Set active là account Fragment
                                        active = dangKyNhanTinFragment;
                                    }
                                } else { // chưa add Fragment nhận thông báo
                                    t4 = 1; // Set là đã add
                                    // Add vào
                                    fragmentManager.beginTransaction().add(R.id.frame, dangKyNhanTinFragment).commit();
                                    fragmentManager.beginTransaction().hide(active).show(dangKyNhanTinFragment).commit();
                                    active = dangKyNhanTinFragment;
                                }*/


                            }

                        }
                    }
                }
                return false;
            }
        });
        fragmentManager.beginTransaction().add(R.id.frame, homeFragment).commit(); // Add và hiện thị fragment home khi khởi động

        SharedPreferences sharedPreferences = getSharedPreferences("Mydata", Context.MODE_PRIVATE);

        int ss = sharedPreferences.getInt("ischangedState", -1);
        if (ss == -1) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("ischangedState", 0).commit();
        }
        //SharedPreferences mPrefs = getSharedPreferences("Mydata", MODE_PRIVATE);


    }

    public void checkLogin() {
        SharedPreferences mPrefs = getSharedPreferences("Mydata", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        String token = getToken();
        if (token.length() > 3) {
            DataClient dataClient = APIUtils.getData();
            retrofit2.Call<Token> tokenCall = dataClient.CheckLogin(token);
            tokenCall.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    Log.d("FAFA", "OKKKKK");
                    Token obj = response.body();
                    if (obj.getToken().equals("expire") || obj.getToken().equals("token_is_incorrect")) {//nếu token hết hạn hoặc sai thì xóa khỏi sharereferences
                        editor.remove("MyToken");
                        editor.remove("MyUser");
                        editor.commit();
                    } else {
                        User user = obj.getUser();
                        Gson gson = new Gson();
                        String userJson = gson.toJson(user);
                        //Cập nhật lại token và info user
                        editor.putString("MyToken", obj.getToken()).commit();
                        editor.putString("MyUser", userJson).commit();
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
//                    Toast.makeText(MainActivity.this, "Vui lòng kiểm tra lại đường truyền mạng!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void refreshToken() {
        SharedPreferences mPrefs = getSharedPreferences("Mydata", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();

        new Thread() {
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String token = getToken();
                            if (token.length() > 3) {
                                DataClient dataClient = APIUtils.getData();
                                retrofit2.Call<String> stringCall = dataClient.RefreshToken(getToken());
                                stringCall.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String mess = response.body();
                                        try {
                                            if (mess.equals("token_correct") || mess.equals("expire")) {// hết hạn hoặc sai
                                                editor.remove("MyToken");
                                                editor.remove("MyUser");
                                                editor.commit();// xóa user khi phiên làm việc đã kết thúc
                                            } else {
                                                editor.putString("MyToken", mess).commit();
                                            }
                                        } catch (Exception e) {

                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
//                                        Toast.makeText(MainActivity.this, "Vui lòng kiểm tra lại đường truyền mạng!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public String getToken() {
        SharedPreferences mPrefs = getSharedPreferences("Mydata", MODE_PRIVATE);
        return mPrefs.getString("MyToken", "");
    }
}
