package com.nhatro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangeProfileActivity extends AppCompatActivity {
    CircularProgressButton btnchange;
    EditText txtname, txtphone, txtfacebook, txtaddress;
    LinearLayout layout;
    ImageView imgcheckfb, imgok;
    AlertDialog.Builder builder;
    TextView txtaccount;

    String name, phone, fb_url, account, address;
    SharedPreferences sharedPreferences;
    User user;
    Gson gson;
    Boolean flag = false, run = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thông tin & liên hệ");

        sharedPreferences = getSharedPreferences("Mydata", MODE_PRIVATE);
        String json = sharedPreferences.getString("MyUser", "");
        gson = new Gson();
        user = gson.fromJson(json, User.class);

        btnchange = findViewById(R.id.changeprofile);
        txtname = findViewById(R.id.txtname);
        txtphone = findViewById(R.id.txtphone);
        txtfacebook = findViewById(R.id.txtfacebook);
        imgcheckfb = findViewById(R.id.imagefbcheck);
        layout = findViewById(R.id.layout);
        txtaccount = findViewById(R.id.txtaccount);
        imgok = findViewById(R.id.imgok);
        txtaddress = findViewById(R.id.txtaddress);

        txtaccount.setText(user.getUsername());
        txtname.setText(user.getHoten());
        txtphone.setText(user.getSodt());
        txtaddress.setText(user.getDiachi());
        txtfacebook.setText(user.getFacebook());

        if(user.getFacebook().length() > 0)// khi người dùng không thay đổi địa chỉ fb cũ thì vẫn có thể cập nhật
            flag = true;

        builder = new AlertDialog.Builder(ChangeProfileActivity.this);

        layout.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                hideKeyboard(ChangeProfileActivity.this);//ẩn bàn phím
                return false;
            }
        });
        txtfacebook.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    builder.setTitle("Các bước lấy địa chỉ Facebook")
                            .setMessage(
                                    "1. Vào tường trang cá nhân của bạn.\n"+
                                    "2. Nhấn vào nút ba chấm(Khác) và chọn sao chép địa chỉ.\n"+
                                    "3. Sau khi nhập xong địa chỉ vui lòng nhấn vào biểu tượng Facebook để kiểm tra lại.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // ok
                                }
                            })
                            .show();
//                    Toast.makeText(ChangeProfileActivity.this, "Vui lòng nhấn vào biểu tượng Facebook để kiểm tra địa chỉ sau khi nhập xong!", Toast.LENGTH_LONG);
                }
            }
        });
        imgcheckfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlfb = txtfacebook.getText().toString();
                if(urlfb.length() > 0 ) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlfb));
                        startActivity(intent);
                        imgok.setImageResource(R.drawable.icon_ok);
                        flag = true;
                    } catch (Exception e) {
                        flag = false;
                        imgok.setImageResource(R.drawable.icon_fail);
                        Toast.makeText(ChangeProfileActivity.this, "Sai địa chỉ Facebook!\nVui lòng nhập lại!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChangeProfileActivity.this, "Vui lòng nhập địa chỉ Facebook!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                @SuppressLint("StaticFieldLeak")
                AsyncTask<String, String, String>
                        loading = new AsyncTask<String, String, String>() {
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
                            fb_url = txtfacebook.getText().toString();
                            name = txtname.getText().toString();
                            phone = txtphone.getText().toString();
                            account = txtaccount.getText().toString();
                            address = txtaddress.getText().toString();

                            if(fb_url.length() > 0){
                                if(flag)
                                    run = true;
                                else
                                    run = false;
                            }else {
                                run = true;
                            }

                            if(name.length() > 0 && phone.length() > 0){
                                DataClient dataClient = APIUtils.getData();
                                retrofit2.Call<String> stringCall = dataClient.ChangeInfo(account, name, phone, fb_url, address);
                                stringCall.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String res = response.body();
                                        stopAnimation();
                                        if(res.equals("ok")){
                                            builder.setTitle("Thông báo")
                                                    .setMessage("Thay đổi thông tin thành công!")
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // ok
                                                            //tiến hành cập nhật lại thông tin mới trong shareReference
                                                            user.setFacebook(fb_url);
                                                            user.setDiachi(address);
                                                            user.setHoten(name);
                                                            user.setSodt(phone);

                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            String json = gson.toJson(user);
                                                            editor.putString("MyUser", json).apply();
                                                        }
                                                    })
                                                    .show();
                                        }else{
                                            Toast.makeText(ChangeProfileActivity.this, "Thay đổi thất bại!\nVui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        stopAnimation();
                                        Toast.makeText(ChangeProfileActivity.this, "Vui lòng kiểm tra lại đường truyền mạng!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                stopAnimation();
                                builder.setTitle("Thông báo")
                                        .setMessage("1. Vui lòng nhập đầy đủ thông tin!\n2. Hoặc kiểm tra lại địa chỉ Facebook bằng cách nhấn vào biểu tượng Facebook!")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // ok
                                            }
                                        })
                                        .show();
                            }
                        }
                    }
                };
                btnchange.startAnimation();
                loading.execute();

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    public static void hideKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    public void stopAnimation(){
        btnchange.revertAnimation();//revert animation
        btnchange.setBackground(getResources().getDrawable(R.drawable.btnsignup));//set lại background cho button sau khi revert về defaul
    }
}
