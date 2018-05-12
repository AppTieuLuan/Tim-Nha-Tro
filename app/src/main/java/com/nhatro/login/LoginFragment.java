package com.nhatro.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.nhatro.ChangePassActivity;
import com.nhatro.R;
import com.nhatro.model.User;
import com.nhatro.model.Token;
import com.nhatro.retrofit.APIUtils;
import com.nhatro.retrofit.DataClient;

import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment implements OnLoginListener{
    private static final String TAG = "LoginFragment";
    Boolean flag = true;
    ImageView img;
    TextView txtpassword, txtusername;
    String username, password;
    private CallBackListener callBackListener;

    AlertDialog.Builder builder;
    CircularProgressButton circularProgressButton;
    LinearLayout layout;

    public LoginFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_login, container, false);

        img = inflate.findViewById(R.id.imgshow);
        txtpassword = inflate.findViewById(R.id.txtpassword);
        txtusername = inflate.findViewById(R.id.txtusername);

        builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);

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
//        Button btn = (Button) inflate.findViewById(R.id.btnlogin);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                username = txtusername.getText().toString();
//                password = txtpassword.getText().toString();
//                if(username.length() > 0 && password.length() > 0){
//                    DataClient dataClient = APIUtils.getData();
//                    retrofit2.Call<Login> callback = dataClient.Login(username, password);
//                    callback.enqueue(new Callback<Login>() {
//                        @Override
//                        public void onResponse(Call<Login> call, Response<Login> response) {
//                            Login obj = (Login) response.body();
//                            String token = obj.getToken();
//                            if(token.equals("Null")){
//                                Toast.makeText(getContext(),"Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
//                            }else{
//                                User user = obj.getUser();
//                                Toast.makeText(getContext(), user.getHoten(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Login> call, Throwable t) {
//                            Log.d("NNN", call.toString());
//                        }
//                    });
//                }else{
//                    builder.setTitle("Thông báo")
//                            .setMessage("Vui lòng nhập đầy đủ thông tin!")
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // ok
////                                    if(callBackListener != null)
////                                        callBackListener.onCallBack();
//                                }
//                            })
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
//                }
////                if(callBackListener != null)
////                    callBackListener.onCallBack();
//            }
//        });
        circularProgressButton = (CircularProgressButton) inflate.findViewById(R.id.btnlogin);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //doLogin();
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
                        if(s.equals("done")){
                            username = txtusername.getText().toString();
                            password = txtpassword.getText().toString();
                            if(username.length() > 0 && password.length() > 0){
                                DataClient dataClient = APIUtils.getData();
                                retrofit2.Call<Token> callback = dataClient.Login(username, password);
                                callback.enqueue(new Callback<Token>() {
                                    @Override
                                    public void onResponse(Call<Token> call, Response<Token> response) {
                                        Token obj = (Token) response.body();
                                        String token = obj.getToken();
                                        stopAnimation();// dừng animation
                                        if(token.equals("Null")){
                                            Toast.makeText(getContext(),"Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                                        }else{
                                            User user = obj.getUser();
                                            //lưu lại thông tin user dưới dạng json string
                                            Gson gson = new Gson();
                                            SharedPreferences mPrefs = getActivity().getSharedPreferences("Mydata", getContext().MODE_PRIVATE);
                                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                            String userJson = gson.toJson(user);

                                            prefsEditor.putString("MyToken", token);
                                            prefsEditor.putString("MyUser", userJson);
                                            prefsEditor.commit();

                                            //Toast.makeText(getContext(),user.getHoten(), Toast.LENGTH_SHORT).show();
                                            if(callBackListener != null){
                                                callBackListener.onCallBack();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Token> call, Throwable t) {
                                        stopAnimation();// dừng animation
                                        Toast.makeText(getContext(), "Vui lòng kiểm tra lại đường truyền mạng!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                stopAnimation();//dừng animation
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
                circularProgressButton.startAnimation();// chay animation
                loading.execute();
            }
        });
        return inflate;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getActivity() is fully created in onActivityCreated and instanceOf differentiate it between different Activities
        if (getActivity() instanceof CallBackListener)
            callBackListener = (CallBackListener) getActivity();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    public void stopAnimation(){
        circularProgressButton.revertAnimation();//revert animation
        circularProgressButton.setBackground(getResources().getDrawable(R.drawable.btnlogin));//set lại background cho button sau khi revert về defaul
    }
    @Override
    public void login() {
        //Toast.makeText(getContext(), "Login", Toast.LENGTH_SHORT).show();
    }
    public static void hideKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
