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

public class SignUpFragment extends Fragment implements OnSignUpListener{
    private static final String TAG = "SignUpFragment";
    TextView txtpassword, txtcfpassword;
    ImageView imgpass, imgcfpass;
    Boolean flagpass = true, flagcfpass = true;
    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_sign_up, container, false);
        inflate.findViewById(R.id.btnsignin).setOnClickListener(v ->
                Toast.makeText(getContext(), "Đăng ký clicked", Toast.LENGTH_SHORT).show());
        imgpass = inflate.findViewById(R.id.imgshowpass);
        imgcfpass = inflate.findViewById(R.id.imgshowcfpass);
        txtpassword = inflate.findViewById(R.id.txtpassword);
        txtcfpassword = inflate.findViewById(R.id.txtcfpassword);
        imgpass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(flagpass) {
                    txtpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    txtpassword.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password
                }else {
                    txtpassword.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtpassword.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password
                }
                flagpass = !flagpass;
            }
        });
        imgcfpass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(flagcfpass) {
                    txtcfpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    txtcfpassword.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password
                }else {
                    txtcfpassword.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtcfpassword.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password
                }
                flagcfpass = !flagcfpass;
            }
        });
        return inflate;
    }

    @Override
    public void signUp() {
        //Toast.makeText(getContext(), "Sign up", Toast.LENGTH_SHORT).show();
    }
}
