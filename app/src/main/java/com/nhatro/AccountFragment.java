package com.nhatro;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhatro.databinding.FragmentAccountBinding;
import com.nhatro.login.LoginFragment;
import com.nhatro.login.NotLoginFragment;
import com.nhatro.login.SignUpFragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class AccountFragment extends Fragment {

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_account, container, false);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragaccount, new NotLoginFragment()).commit();
        return inflate;
    }

}
