package com.nhatro.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhatro.DAL.DAL_PhongTro;
import com.nhatro.Newpost;
import com.nhatro.R;
import com.nhatro.model.Parameter_Luu;
import com.nhatro.model.PhongTro;
import com.nhatro.model.User;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by CongHoang on 2/11/2018.
 */

public class CustomListViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<PhongTro> data;

    public CustomListViewAdapter(Context activity, ArrayList<PhongTro> data) {
        this.context = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public PhongTro getItem(int i) {
        return this.data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.itemlistview, null);

        TextView title = (TextView) view.findViewById(R.id.txtTitle);
        TextView address = (TextView) view.findViewById(R.id.txtAddress);
        TextView price = (TextView) view.findViewById(R.id.txtPrice);
        TextView area = (TextView) view.findViewById(R.id.area);
        TextView sex = (TextView) view.findViewById(R.id.sex);
        ImageView img = (ImageView) view.findViewById(R.id.imageItemListView);

        TextView type = (TextView) view.findViewById(R.id.type);
        ImageView imgSave = (ImageView) view.findViewById(R.id.iconSavePost);
        LinearLayout buttonSavePost = (LinearLayout) view.findViewById(R.id.layoutIconSavePost);


        // getting movie data for the row
        PhongTro pt = data.get(i);
        Picasso.with(context).load(pt.getHinhanh()).into(img);
        // title
        title.setText(pt.getTieude());
        address.setText(pt.getDiachi());


        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String tmp = formatter.format(pt.getGia()) + " VNĐ";
        price.setText(tmp);

        area.setText("Diện tích: " + String.valueOf(pt.getDientich()) + "(" + String.valueOf(pt.getChieudai()) + "m x " +
                String.valueOf(pt.getChieurong()) + "m)");


        if (pt.getLoaitintuc() == 1) {
            type.setText("Nhà trọ cho thuê");
        } else {
            if (pt.getLoaitintuc() == 2) {
                type.setText("Tìm bạn ở ghép");
            } else {
                if (pt.getLoaitintuc() == 3) {
                    type.setText("Nhà nguyên căn");
                }
            }
        }
        if (pt.getGioitinh().equals("3")) {
            sex.setVisibility(View.INVISIBLE);
        } else {
            if (pt.getGioitinh().equals("1")) {
                sex.setText("Nam");
            } else {
                sex.setText("Nữ");
            }
        }
        if (pt.isDaluu()) {
            imgSave.setColorFilter(Color.parseColor("#008efc"));
        } else {
            imgSave.setColorFilter(Color.parseColor("#ffffff"));
        }

        buttonSavePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = context.getSharedPreferences("Mydata", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("MyUser", "");
/*

                Log.d("USERSSS", user);
                Gson gsonUser = new Gson();
                User user1 = gsonUser.fromJson(user, User.class);
*/

                if (user == null || user.equals("")) {
                    Toast.makeText(context, "Đăng nhập trước khi thực hiện thao tác này !!", Toast.LENGTH_SHORT).show();
                } else {
                   /* Intent intents = new Intent(getContext(), Newpost.class);
                    startActivity(intents);*/
                    if (pt.isDaluu()) {
                        Parameter_Luu parameter_luu = new Parameter_Luu();
                        parameter_luu.setIdphong(pt.getId());

                        Gson gsonUser = new Gson();
                        User user1 = gsonUser.fromJson(user, User.class);
                        parameter_luu.setIduser(Integer.parseInt(user1.getId()));

                        parameter_luu.setLuu(0);

                        Luu_HuyLuu luu_huyLuu = new Luu_HuyLuu();
                        luu_huyLuu.execute(parameter_luu);

                        imgSave.setColorFilter(Color.parseColor("#ffffff"));
                        pt.setDaluu(false);

                        //Toast.makeText(v.getContext(), "Hủy lưu", Toast.LENGTH_SHORT).show();
                    } else {

                        Parameter_Luu parameter_luu = new Parameter_Luu();
                        parameter_luu.setIdphong(pt.getId());

                        Gson gsonUser = new Gson();
                        User user1 = gsonUser.fromJson(user, User.class);
                        parameter_luu.setIduser(Integer.parseInt(user1.getId()));

                        parameter_luu.setLuu(1);

                        Luu_HuyLuu luu_huyLuu = new Luu_HuyLuu();
                        luu_huyLuu.execute(parameter_luu);


                        imgSave.setColorFilter(Color.parseColor("#008efc"));
                        pt.setDaluu(true);

                        //Toast.makeText(v.getContext(), "Đã lưu", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        return view;
    }

    class Luu_HuyLuu extends AsyncTask<Parameter_Luu, Void, Integer> {
        @Override
        protected Integer doInBackground(Parameter_Luu... parameter_luus) {
            DAL_PhongTro dal_phongTro = new DAL_PhongTro();
            return dal_phongTro.Luu_BoLuu(parameter_luus[0].getIdphong(), parameter_luus[0].getIduser(), parameter_luus[0].getLuu());
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }
}
