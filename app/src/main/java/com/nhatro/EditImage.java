package com.nhatro;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nhatro.DAL.DAL_PhongTro;
import com.nhatro.adapter.Grid_Add_Image_Adapter;
import com.nhatro.adapter.Grid_Edit_Image_Adapter;
import com.nhatro.adapter.ImageFilePath;
import com.nhatro.model.EditImg;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class EditImage extends AppCompatActivity {

    boolean isposting;
    GridView gridHA;
    Grid_Edit_Image_Adapter grid_add_image_adapter;
    ArrayList<EditImg> dataGrid;
    ArrayList<String> imageAddNew;
    ArrayList<String> imageDelete;
    ArrayList<String> imageAdded;
    RoundedImageView idbtnAddImage;
    TextView btnOK;
    ArrayList<EditImg> orginal;
    String iditem;
    LinearLayout layoutOverlay;
    LoadToast lt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().hide();
        getSupportActionBar().setTitle("Sửa ảnh");

        isposting = false;

        findView();
        grid_add_image_adapter = new Grid_Edit_Image_Adapter(getApplicationContext(), R.layout.item_grid_chon_hinh_anh, dataGrid);
        gridHA.setAdapter(grid_add_image_adapter);

        getData();
        setEvent();

    }

    public void setEvent() {
        idbtnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10002);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataGrid.size() < 3) {
                    Toast.makeText(getApplicationContext(), "Chọn ít nhất 3 tấm hình !!", Toast.LENGTH_SHORT).show();
                } else {
                    getDeletedImage();
                    getAddNewImage();

                    UpdateImage updateImage = new UpdateImage();
                    updateImage.execute();
                }
            }
        });
    }

    public void getDeletedImage() {
        for (int i = 0; i < orginal.size(); i++) {
            boolean rs1 = false;
            for (int j = 0; j < dataGrid.size(); j++) {
                if (orginal.get(i).getPath().equals(dataGrid.get(j).getPath())) {
                    rs1 = true;
                    break;
                }
            }
            if (!rs1) {
                imageDelete.add(orginal.get(i).getPath().substring(47));
            }
        }
    }

    public void getAddNewImage() {
        for (int i = 0; i < dataGrid.size(); i++) {
            if (dataGrid.get(i).isIslocal()) {
                imageAddNew.add(dataGrid.get(i).getPath());
            }
        }
    }

    public void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        iditem = bundle.getString("iditem");
        ArrayList<String> tmps = new ArrayList<>();
        tmps.addAll(bundle.getStringArrayList("data"));
        for (int i = 0; i < tmps.size(); i++) {
            dataGrid.add(new EditImg(tmps.get(i)));
        }
        orginal.addAll(dataGrid);
        grid_add_image_adapter.notifyDataSetChanged();

        int ss = 0;
    }

    void findView() {
        gridHA = findViewById(R.id.gridHA);
        idbtnAddImage = findViewById(R.id.idbtnAddImage);
        btnOK = findViewById(R.id.btnOK);
        dataGrid = new ArrayList<>();
        imageAddNew = new ArrayList<>();
        imageDelete = new ArrayList<>();
        orginal = new ArrayList<>();
        imageAdded = new ArrayList<>();
        layoutOverlay = findViewById(R.id.layoutOverlay);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;

        lt = new LoadToast(this);
        lt.setText("Đang xử lý...");
        lt.setTranslationY(height / 2 - 100);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (!isposting) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        } else {
            if (requestCode == 10002) {
                try {
                    if (resultCode == RESULT_OK && null != data) {

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        ArrayList<String> imagesEncodedList = new ArrayList<String>();

                        if (data.getData() != null) {

                            Uri mImageUri = data.getData();
                            String kq = ImageFilePath.getPath(EditImage.this, data.getData());
                            //add_images.add(kq);
                            dataGrid.add(new EditImg(kq, true));
                            grid_add_image_adapter.notifyDataSetChanged();

                        } else {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {

                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    mArrayUri.add(uri);
                                    String kq = ImageFilePath.getPath(EditImage.this, uri);

                                    dataGrid.add(new EditImg(kq, true));

                                    //add_images.add(kq);
                                }
                                grid_add_image_adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        //Toast.makeText(this, "Bạn chưa chọn ảnh nào", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Có lỗi xảy ra!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public class UpdateImage extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            boolean kqHa = true; // Kết quả up hình ảnh

            for (int i = 0; i < imageAddNew.size(); i++) {
                String rs = upImage(imageAddNew.get(i));
                if (rs.equals("-1")) {
                    kqHa = false;
                    break;
                } else {
                    rs = rs.substring(1);
                    imageAdded.add(rs);
                }
            }
            if (kqHa = false) {
                //Xóa ảnh đã up lên mà bị lỗi...
                return 0;
            } else {
                DAL_PhongTro dal_phongTro = new DAL_PhongTro();
                return dal_phongTro.upDateImage(iditem, imageAdded, imageDelete);
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                lt.success();

                Intent intents = getIntent();
                Bundle bundle = new Bundle();
                imageAddNew.clear();

                for (int i = 0; i < dataGrid.size(); i++) {
                    if (!dataGrid.get(i).isIslocal()) {
                        imageAddNew.add(dataGrid.get(i).getPath());
                    }
                }
                for (int i = 0; i < imageAdded.size(); i++) {
                    imageAddNew.add("https://nhatroservice.000webhostapp.com/images/" + imageAdded.get(i));
                }
                bundle.putStringArrayList("data", imageAddNew);
                intents.putExtra("data", bundle);
                setResult(10101, intents); // phương thức này sẽ trả kết quả cho Activity trước
                finish(); // Đóng Activity hiện tại

            } else {
                lt.error();
            }
            isposting = false;
            layoutOverlay.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lt.show();
            layoutOverlay.setVisibility(View.VISIBLE);
            isposting = true;

        }
    }

    public String upImage(String filePath) {
        String rs = "-1";
        try {
            String sourceFileUri = filePath;

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(sourceFileUri);

            if (sourceFile.isFile()) {

                try {
                    String upLoadServerUri = "https://nhatroservice.000webhostapp.com/images/upload_image.php";
                    //String upLoadServerUri = "http://192.168.1.9:8080/firebase/images/upimg.php";

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE",
                            "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("bill", sourceFileUri);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                            + sourceFileUri + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math
                                .min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0,
                                bufferSize);

                    }

                    // send multipart form data necesssary after file
                    // data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens
                            + lineEnd);

                    // Responses from the server (code and message)
                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn
                            .getResponseMessage();

                    if (serverResponseCode == 200) {
                        //return serverResponseMessage;
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        fileInputStream.close();
                        dos.flush();
                        dos.close();
                        return response.toString();
                    }

                    // close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (Exception e) {

                    // dialog.dismiss();
                    e.printStackTrace();

                }
                // dialog.dismiss();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rs;
    }
}
