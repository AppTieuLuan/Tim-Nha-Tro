package com.nhatro.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.R;
import com.nhatro.model.Token;
import com.nhatro.model.User;
import com.nhatro.retrofit.APIUtils;
import com.nhatro.retrofit.DataClient;

import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment implements OnSignUpListener{
    private static final String TAG = "SignUpFragment";
    TextView txtpassword, txtcfpassword, txtname, txtphone, txtusername;
    ImageView imgpass, imgcfpass;
    CircularProgressButton btnsignup;
    Boolean flagpass = true, flagcfpass = true;
    String username, name, password, phone, cfpassword;
    LinearLayout layout;

    AlertDialog.Builder builder;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_sign_up, container, false);
        init(inflate);//khởi tạo

        layout = inflate.findViewById(R.id.layout);

        layout.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                hideKeyboard(Objects.requireNonNull(getActivity()));//ẩn bàn phím
                return false;
            }
        });
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

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = txtname.getText().toString();
                username = txtusername.getText().toString();
                phone = txtphone.getText().toString();
                password = txtpassword.getText().toString();
                cfpassword = txtcfpassword.getText().toString();
                @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> loading = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try{
                            Thread.sleep(1000);// sleep 1s
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (s.equals("done")) {
                            if(name.length() > 0 && username.length() > 0 &&
                                    phone.length() > 0 && password.length() > 0 && cfpassword.length() > 0){
                                if(password.equals(cfpassword)){//confirm pass correct
                                    DataClient dataClient = APIUtils.getData();
                                    retrofit2.Call<String> callback = dataClient.Register(name, username, phone, password);
                                    callback.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String res = response.body();
                                            stopAnimation();// dừng
                                            if(res.equals("THANH_CONG")){
                                                Toast.makeText(getContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                                builder.setTitle("Thông báo")
                                                        .setMessage("Đăng ký thành công!\nVui lòng quay lại đăng nhập để đăng nhập!")
                                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // ok
                                                                txtname.setText("");
                                                                txtusername.setText("");
                                                                txtphone.setText("");
                                                                txtpassword.setText("");
                                                                txtcfpassword.setText("");
                                                                txtname.requestFocus();
                                                            }
                                                        })
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .show();
                                            }else {
                                                Toast.makeText(getContext(), "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            stopAnimation();
                                            Toast.makeText(getContext(), "Vui lòng kiểm tra lại đường truyền!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{//confirm password incorrect
                                    stopAnimation();
                                    builder.setTitle("Thông báo")
                                            .setMessage("Xác nhận mật khẩu sai!")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // ok
                                                    txtcfpassword.setText("");
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            }else{
                                stopAnimation();
                                builder.setTitle("Thông báo")
                                        .setMessage("Vui lòng nhập đầy đủ thông tin!")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // ok
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }
                    }
                };
                btnsignup.startAnimation();
                loading.execute();//asynctask start

            }
        });

        return inflate;
    }

    private void init(View inflate) {
        imgpass = inflate.findViewById(R.id.imgshowpass);
        imgcfpass = inflate.findViewById(R.id.imgshowcfpass);
        txtpassword = inflate.findViewById(R.id.txtpassword);
        txtcfpassword = inflate.findViewById(R.id.txtcfpassword);
        btnsignup = inflate.findViewById(R.id.btnsignup);
        txtname = inflate.findViewById(R.id.txtname);
        txtusername = inflate.findViewById(R.id.txtusername);
        txtphone = inflate.findViewById(R.id.txtphone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
    }

    public void stopAnimation(){
        btnsignup.revertAnimation();//revert animation
        btnsignup.setBackground(getResources().getDrawable(R.drawable.btnsignup));//set lại background cho button sau khi revert về defaul
    }

    @Override
    public void signUp() {
        //Toast.makeText(getContext(), "Sign up", Toast.LENGTH_SHORT).show();
    }
    public static void hideKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
