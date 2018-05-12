package com.nhatro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhatro.model.Token;
import com.nhatro.retrofit.APIUtils;
import com.nhatro.retrofit.DataClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportActivity extends AppCompatActivity {
    EditText txtemail, txtcontent;
    CircularProgressButton btnsend;
    String email, content;
    AlertDialog.Builder builder;
    ScrollView scrollView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hỗ trợ");

        txtemail = findViewById(R.id.txtemail);
        txtcontent = findViewById(R.id.txtcontent);
        btnsend = findViewById(R.id.btnsend);
        scrollView = findViewById(R.id.scrollView);
        builder = new AlertDialog.Builder(SupportActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        btnsend.setBackground(getResources().getDrawable(R.drawable.buttonsend));//set lại background cho button

        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                hideKeyboard(SupportActivity.this);//ẩn bàn phím
                return false;
            }
        });

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(SupportActivity.this);//ẩn bàn phím
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
                            email = txtemail.getText().toString();
                            content = txtcontent.getText().toString();
                            if(email.length() > 0 && content.length() > 0){
                                if(checkEmail(email)){
                                    DataClient dataClient = APIUtils.getData();
                                    retrofit2.Call<String> callback = dataClient.SendSupport(email, content);
                                    callback.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String res = response.body();
                                            stopAnimation();
                                            if (res.equals("ok")) {
                                                builder.setTitle("Thông báo")
                                                        .setMessage("Cảm ơn bạn đã đóng góp ý kiến để chúng tôi có thể hoàn thiện chức năng của ứng dụng!")
                                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // ok
                                                            }
                                                        })
                                                        .setIcon(android.R.drawable.ic_dialog_info)
                                                        .show();
                                            } else {
                                                builder.setTitle("Thông báo")
                                                        .setMessage("Có lỗi xảy ra!\nVui lòng thử lại!")
                                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // ok
                                                            }
                                                        })
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            stopAnimation();
                                            Toast.makeText(SupportActivity.this, "Vui lòng kiểm tra lại đường truyền mạng!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    stopAnimation();
                                    builder.setTitle("Thông báo")
                                            .setMessage("Email không đúng định dạng!\nVui lòng nhập lại email")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // ok
                                                    txtemail.setText("");
                                                    txtemail.requestFocus();
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
                btnsend.startAnimation();// chay animation
                loading.execute();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private static Boolean checkEmail(String email) {
        String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern regex = Pattern.compile(emailPattern);
        Matcher matcher = regex.matcher(email);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }
    public void stopAnimation(){
        btnsend.revertAnimation();//revert animation
        btnsend.setBackground(getResources().getDrawable(R.drawable.buttonsend));//set lại background cho button sau khi revert về defaul
    }
    public static void hideKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}