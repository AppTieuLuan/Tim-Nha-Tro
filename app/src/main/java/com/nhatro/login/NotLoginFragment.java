package com.nhatro.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nhatro.IntroduceActivity;
import com.nhatro.LoginActivity;
import com.nhatro.R;
import com.nhatro.SupportActivity;

public class NotLoginFragment extends Fragment {

    LinearLayout btnlogin, btnsupport, btnintroduce;

    public NotLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_not_login, container, false);
        btnlogin = view.findViewById(R.id.login);
        btnsupport = view.findViewById(R.id.support);
        btnintroduce = view.findViewById(R.id.introduce);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        btnsupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SupportActivity.class);
                startActivity(intent);
            }
        });

        btnintroduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IntroduceActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
