package com.nhatro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhatro.model.Token;
import com.nhatro.model.User;
import com.nhatro.retrofit.APIUtils;
import com.nhatro.retrofit.DataClient;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassActivity extends AppCompatActivity {
    CircularProgressButton btnchange;
    TextView txtoldpass, txtnewpass, txtcfnewpass;
    ImageView imgoldpass, imgnewpass, imgcfpass;
    Boolean flagoldpass = true, flagnewpass = true, flagcfpass = true;
    String oldpass, newpass, confirmnewpass;
    AlertDialog.Builder builder;
    LinearLayout layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đổi mật khẩu");

        btnchange = findViewById(R.id.btnchange);

        txtoldpass = findViewById(R.id.txtoldpass);
        txtnewpass = findViewById(R.id.txtpassword);
        txtcfnewpass = findViewById(R.id.txtcfpassword);

        imgoldpass = findViewById(R.id.imgshowoldpass);
        imgnewpass = findViewById(R.id.imgshowpass);
        imgcfpass = findViewById(R.id.imgshowcfpass);
        layout = findViewById(R.id.layout);

        layout.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                hideKeyboard(ChangePassActivity.this);//ẩn bàn phím
                return false;
            }
        });

        builder = new AlertDialog.Builder(ChangePassActivity.this, android.R.style.Theme_Material_Dialog_Alert);

        imgoldpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagoldpass) {
                    txtoldpass.setInputType(InputType.TYPE_CLASS_TEXT);
                    txtoldpass.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password
                }else {
                    txtoldpass.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtoldpass.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password
                }
                flagoldpass = !flagoldpass;
            }
        });
        imgnewpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagnewpass) {
                    txtnewpass.setInputType(InputType.TYPE_CLASS_TEXT);
                    txtnewpass.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password
                }else {
                    txtnewpass.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtnewpass.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password
                }
                flagnewpass = !flagnewpass;
            }
        });

        imgcfpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagcfpass) {
                    txtcfnewpass.setInputType(InputType.TYPE_CLASS_TEXT);
                    txtcfnewpass.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password
                }else {
                    txtcfnewpass.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtcfnewpass.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password
                }
                flagcfpass = !flagcfpass;
            }
        });
        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> loading = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try{
                            publishProgress();// GỌI HÀM onProgressUpdate RUN
                            Thread.sleep(1000);// sleep 1s
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        oldpass = txtoldpass.getText().toString();
                        newpass = txtnewpass.getText().toString();
                        confirmnewpass = txtcfnewpass.getText().toString();

                        if(oldpass.length() > 0 && newpass.length() > 0 && confirmnewpass.length() > 0){
                            if(newpass.equals(confirmnewpass)) {
                                SharedPreferences mPrefs = ChangePassActivity.this.getSharedPreferences("Mydata", MODE_PRIVATE);
                                Gson gson = new Gson();
                                String json = mPrefs.getString("MyUser", "");
                                User user = gson.fromJson(json, User.class);

                                DataClient dataClient = APIUtils.getData();
                                retrofit2.Call<String> callback = dataClient.ChangePass(user.getUsername(), oldpass, newpass);
                                callback.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String res = response.body();
                                        stopAnimation();
                                        if(res.equals("ok")){
                                            builder.setTitle("Thông báo")
                                                    .setMessage("Thay đổi thành công!")
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // ok
                                                            txtoldpass.setText("");
                                                            txtnewpass.setText("");
                                                            txtcfnewpass.setText("");
                                                            txtoldpass.requestFocus();
                                                        }
                                                    })
                                                    .show();
                                        }else {
                                            if(res.equals("fail_old_pass")){
                                                Toast.makeText(ChangePassActivity.this, "Sai mật khẩu cũ!", Toast.LENGTH_SHORT).show();
                                                txtoldpass.setText("");
                                                txtoldpass.requestFocus();
                                            }else {
                                                Toast.makeText(ChangePassActivity.this, "Thay đổi mật khẩu thất bại!\nVui lòng thử lại!"+res, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(ChangePassActivity.this, "Vui lòng kiểm tra lại đường truyền mạng!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(ChangePassActivity.this, "Xác nhận mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
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
                        super.onProgressUpdate(values);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if(s.equals("done")){
                            // sau khi run xong animation sẽ được tắt
                            stopAnimation();
                        }
                    }
                };
                btnchange.startAnimation();// chay animation
                loading.execute();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    public void stopAnimation(){
        btnchange.revertAnimation();//revert animation
        btnchange.setBackground(getResources().getDrawable(R.drawable.btnsignup));//set lại background cho button sau khi revert về defaul
    }
    public static void hideKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}