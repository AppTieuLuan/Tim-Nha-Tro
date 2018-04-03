package com.nhatro;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.adapter.MyCustomPagerAdapter;
import com.nhatro.adapter.Pager_Image_View_Full_Screen_Adapter;

public class Image_View extends AppCompatActivity {

    int index;
    TextView txtViTri;
    ViewPager viewPager;
    String images[] = {"https://pm1.narvii.com/6016/2f719b81e29dd3ca5afcc722614560f0132a98aa_hq.jpg",
            "https://znews-photo-td.zadn.vn/w660/Uploaded/natmzz/2018_04_01/0_2.jpg",
            "https://znews-photo-td.zadn.vn/w660/Uploaded/natmzz/2018_04_01/0.jpg",
            "https://znews-photo-td.zadn.vn/w660/Uploaded/fcivbqmv/2018_03_31/Facebookhacker.jpg",
            "https://znews-photo-td.zadn.vn/w660/Uploaded/natmzz/2018_03_31/1_13.jpg",
            "https://znews-stc.zdn.vn/static/campaign/tuyendung/2018/tuyendung2018_2.jpg",
            "https://znews-photo-td.zadn.vn/w660/Uploaded/natmzz/2018_03_31/1_8.jpg"};
    Pager_Image_View_Full_Screen_Adapter pager_image_view_full_screen_adapter;
    int sum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image__view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().hide();
        getSupportActionBar().setTitle("Hình ảnh");

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");


        index = bundle.getInt("index");
        sum = images.length;

        txtViTri = (TextView) findViewById(R.id.vitri);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        pager_image_view_full_screen_adapter = new Pager_Image_View_Full_Screen_Adapter(getApplicationContext(), images);
        viewPager.setAdapter(pager_image_view_full_screen_adapter);

        viewPager.setCurrentItem(index);
        txtViTri.setText(String.valueOf(index + 1) + "/" + String.valueOf(sum));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();

                txtViTri.setText(String.valueOf(position + 1) + "/" + String.valueOf(sum));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
