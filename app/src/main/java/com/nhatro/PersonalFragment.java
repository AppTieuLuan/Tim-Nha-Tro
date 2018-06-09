package com.nhatro;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhatro.adapter.ImageFilePath;
import com.nhatro.login.CallBackListener;
import com.nhatro.model.User;
import com.nhatro.retrofit.APIUtils;
import com.nhatro.retrofit.DataClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class PersonalFragment extends Fragment {
    LinearLayout btnchangepass, btnsave, btnprofile, btnlogout, btnintroduce, btnsupport;
    CallBackListener callBackListener;
    ScrollView scrollView;
    CircleImageView imgavatar;
    TextView txtname;
    int REQUEST_FILE = 123, REQUEST_CAMERA = 321;
    String username;

    public PersonalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_personal, container, false);

        btnchangepass = inflate.findViewById(R.id.changepass);
        btnlogout = inflate.findViewById(R.id.logout);
        btnprofile = inflate.findViewById(R.id.information);
        btnsave = inflate.findViewById(R.id.save);
        btnsupport = inflate.findViewById(R.id.support);
        scrollView = inflate.findViewById(R.id.scrollpersonal);
        imgavatar = inflate.findViewById(R.id.imgavatar);
        txtname = inflate.findViewById(R.id.txtname);
        btnintroduce = inflate.findViewById(R.id.introduce);

        imgavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        setUser();
        return inflate;
    }

    public void setUser() {
        SharedPreferences mPrefs = getActivity().getSharedPreferences("Mydata", getContext().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyUser", "");
        if (json.length() > 0) {
            User user = gson.fromJson(json, User.class);
            txtname.setText(user.getHoten());
            username = user.getUsername();
            Picasso.with(getContext()).load(user.getAvatar()).into(imgavatar);

        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Chụp ảnh", "Chọn ảnh từ thư viện",
                "Hủy"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tải ảnh lên!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());
                String userChoosenTask;
                if (items[item].equals("Chụp ảnh")) {
                    userChoosenTask = "Chụp ảnh";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Chọn ảnh từ thư viện")) {
                    userChoosenTask = "Chọn ảnh từ thư viện";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Hủy")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //set camera intent
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    //set select gallery file
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_FILE);
    }


    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    //set resource cho img
    private void setResourceImage(Intent data) {
        Uri selectedImage = data.getData();
        String realpath = getRealPathFromURI(selectedImage);
        File file = new File(realpath);
        String file_path = file.getAbsolutePath();
        String[] mangtenfile = file_path.split("\\.");

        file_path = mangtenfile[0] + System.currentTimeMillis() + "." + mangtenfile[1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        String[] mang_path = file_path.split("\\/");
        String path = "images/" + mang_path[mang_path.length - 1];

        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file_path, requestBody);
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.UploadAvatar(body);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    String mess = response.body();
                    if (mess.equals("ok")) {//upload ảnh lên server thành công
                        //tiến hành cập nhật chuỗi url avatar
                        DataClient dataClient1 = APIUtils.getData();
                        retrofit2.Call<String> callback1 = dataClient1.UpdateAvatar(username, path);
                        callback1.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String message = response.body();
                                if (message.equals("ok")) {
                                    Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    imgavatar.setImageURI(selectedImage);
                                } else {
                                    Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(getContext(), "Vui lòng kiểm tra lại đường truyền mạng!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Vui lòng kiểm tra lại đường truyền mạng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_FILE)
                setResourceImage(data);
            else if (requestCode == REQUEST_CAMERA)
                setResourceImage(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mPrefs = getActivity().getSharedPreferences("Mydata", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString("MyUser", "");
                editor.putString("MyToken", "");
                editor.putInt("ischangedState", 1); // chỗ này lưu -> Lưu để làm gì .. lưu để load lại :))
                editor.commit();

                if (callBackListener != null)
                    callBackListener.onCallBack();
            }
        });
        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePassActivity.class);
                startActivity(intent);
            }
        });
        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeProfileActivity.class);
                startActivity(intent);
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManagerRoomActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), "Save click!", Toast.LENGTH_SHORT).show();
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
    }

    public void onAttachToParentFragment(Fragment fragment) {
        try {
            callBackListener = (CallBackListener) fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachToParentFragment(getParentFragment());
    }

    @Override
    public void onResume() {
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        setUser();
        super.onResume();
    }
}
