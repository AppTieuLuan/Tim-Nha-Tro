package com.nhatro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.nhatro.databinding.ActivityLoginBinding;
import com.nhatro.login.CallBackListener;
import com.nhatro.login.LoginFragment;
import com.nhatro.login.SignUpFragment;

import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity implements CallBackListener {
    private ActivityLoginBinding binding;
    private boolean isLogin = true;

    @SuppressLint({"WrongViewCast", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đăng nhập");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        LoginFragment bottomLoginFragment = new LoginFragment();
//        LoginFragment topLoginFragment = new LoginFragment();
        SignUpFragment bottomSignUpFragment = new SignUpFragment();
//        SignUpFragment topSignUpFragment = new SignUpFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottom_login, bottomSignUpFragment)
                //.replace(R.id.top_login, topSignUpFragment)
                .replace(R.id.bottom_sign_up, bottomLoginFragment)
                //.replace(R.id.top_sign_up, topLoginFragment)
                .commit();

        binding.bottomSignUp.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        binding.bottomSignUp.setFocusable(true);
        binding.bottomSignUp.setFocusableInTouchMode(true);
        binding.bottomSignUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                hideKeyboard(LoginActivity.this);//ẩn bàn phím
                return false;
            }
        });
//        binding.topLogin.setRotation(-90);
//        binding.topSignUp.setRotation(90);
        binding.bottomLogin.setVisibility(GONE);
        //binding.bottomSignUp.setVisibility(GONE);

        binding.button.setOnSignUpListener(bottomSignUpFragment);
        binding.button.setOnLoginListener(bottomLoginFragment);

        binding.button.setOnButtonSwitched(isLogin -> {
            if (!isLogin) {
                getSupportActionBar().setTitle("Đăng ký");
                binding.bottomLogin.setVisibility(VISIBLE);
                binding.bottomSignUp.setVisibility(GONE);
            } else {
                getSupportActionBar().setTitle("Đăng nhập");
                binding.bottomSignUp.setVisibility(VISIBLE);
                binding.bottomLogin.setVisibility(GONE);
            }
            binding.getRoot()
                    .setBackgroundColor(ContextCompat.getColor(
                            this,
                            isLogin ? R.color.mycolor : R.color.secondPage));
        });

        binding.bottomLogin.setAlpha(0f);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        binding.topLogin.setPivotX(binding.topLogin.getWidth() / 2);
//        binding.topLogin.setPivotY(binding.topLogin.getHeight());
//        binding.topSignUp.setPivotX(binding.topLogin.getWidth() / 2);
//        binding.topSignUp.setPivotY(binding.topLogin.getHeight());
    }

    public void switchFragment(View v) {
        if (isLogin) {
            //binding.topLogin.setAlpha(1f);
//            binding.topLogin.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    binding.bottomLogin.setAlpha(1f);
//                    //binding.topLogin.setRotation(-90);
//                }
//            });
            binding.bottomLogin.setAlpha(1f);
            binding.bottomSignUp.animate().alpha(0f);
        } else {
//            binding.topSignUp.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    binding.bottomSignUp.setAlpha(1f);
//                    //binding.topSignUp.setRotation(90);
//                }
//            });
            binding.bottomSignUp.setAlpha(1f);
            binding.bottomLogin.animate().alpha(0f);
        }

        isLogin = !isLogin;
        binding.button.startAnimation();
    }

    private int getBottomMargin() {
        return getResources().getDimensionPixelOffset(R.dimen.bottom_margin);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onCallBack() {
        finish();
    }
    public static void hideKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}