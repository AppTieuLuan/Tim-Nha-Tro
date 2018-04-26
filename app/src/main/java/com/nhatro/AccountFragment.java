package com.nhatro;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhatro.databinding.FragmentAccountBinding;
import com.nhatro.login.CallBackListener;
import com.nhatro.login.LoginFragment;
import com.nhatro.login.NotLoginFragment;
import com.nhatro.login.SignUpFragment;
import com.nhatro.model.User;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class AccountFragment extends Fragment implements CallBackListener {
    FragmentManager fragmentManager;
    Fragment fragmentAccount, fragmentNotLogin, active;
    Boolean flag;
    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_account, container, false);
        fragmentManager = getChildFragmentManager();
        flag = false;

        fragmentNotLogin = new NotLoginFragment();
        fragmentAccount = new PersonalFragment();
        //Thêm tất cả các fragment vào
        fragmentManager.beginTransaction()
                .add(R.id.fragaccount, fragmentAccount)
                .add(R.id.fragaccount, fragmentNotLogin).commit();

        //Ẩn các fragment không có giá trị tại thời điểm hiện tại
        active = fragmentNotLogin;
        if(CheckUser()){// đã đăng nhập từ trước
            fragmentManager.beginTransaction().hide(active).show(fragmentAccount).commit();
            active = fragmentAccount;
        }else{// chưa đăng nhập
            fragmentManager.beginTransaction().hide(active).show(fragmentNotLogin).commit();
            active = fragmentNotLogin;
        }
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(CheckUser()){//đã đăng nhập
            if(active != fragmentAccount){// active hiện tại không phải là fragment account thì set lại còn đang là account thì để nguyên
                fragmentManager.beginTransaction().hide(active).show(fragmentAccount).commit();
            }
            active = fragmentAccount;//set lại active là fragment account
        }else {//nếu chưa đăng nhập
            if(active != fragmentNotLogin) {
                fragmentManager.beginTransaction().hide(active).show(fragmentNotLogin).commit();
            }
            active = fragmentNotLogin;//set lại active là fragment notlogin
        }
    }

    @Override
    public void onCallBack() {
        fragmentManager.beginTransaction().hide(active).show(fragmentNotLogin).commit();
        active = fragmentNotLogin;

    }
    public Boolean CheckUser(){
        SharedPreferences mPrefs = getActivity().getSharedPreferences("Mydata", getContext().MODE_PRIVATE);
        String json = mPrefs.getString("MyUser", "");
        if(json.length() > 0) {
            return true;
        }
        return false;
    }
}

