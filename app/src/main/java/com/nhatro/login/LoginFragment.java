package com.nhatro.login;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.R;

public class LoginFragment extends Fragment implements OnLoginListener{
    private static final String TAG = "LoginFragment";
    Boolean flag = true;
    ImageView img;
    TextView txtpassword;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_login, container, false);
        inflate.findViewById(R.id.btnlogin).setOnClickListener(v ->
                Toast.makeText(getContext(), "Đăng nhập clicked", Toast.LENGTH_SHORT).show());

        img = inflate.findViewById(R.id.imgshow);
        txtpassword = inflate.findViewById(R.id.txtpassword);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(flag) {
                    txtpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    txtpassword.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password
                }else {
                    txtpassword.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtpassword.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password
                }
                flag = !flag;
            }
        });
        return inflate;
    }

    @Override
    public void login() {
        //Toast.makeText(getContext(), "Login", Toast.LENGTH_SHORT).show();
    }
}
