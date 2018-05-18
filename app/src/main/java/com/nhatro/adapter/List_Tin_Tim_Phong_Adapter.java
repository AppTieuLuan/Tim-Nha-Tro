package com.nhatro.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nhatro.R;
import com.nhatro.model.TinTimPhongItemList;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by CongHoang on 4/14/2018.
 */

public class List_Tin_Tim_Phong_Adapter extends ArrayAdapter {

    ArrayList<TinTimPhongItemList> data = new ArrayList<>();


    public List_Tin_Tim_Phong_Adapter(@NonNull Context context, int resource, ArrayList<TinTimPhongItemList> data) {
        super(context, resource);
        this.data = data;

    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public TinTimPhongItemList getItem(int i) {
        return this.data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;


        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_list_tin_tim_phong, null);

        TextView txttype = v.findViewById(R.id.txttype);
        TextView txtSex = v.findViewById(R.id.txtSex);

        if (data.get(i).getGioitinh() == 1) {
            txtSex.setText("Nam");
        } else {
            if (data.get(i).getGioitinh() == 2) {
                txtSex.setText("Nữ");
            } else {
                txtSex.setVisibility(View.GONE);
            }
        }

        TextView txtTitle = v.findViewById(R.id.txtTitle);
        TextView txtAddress = v.findViewById(R.id.txtAddress);
        TextView txtSoNguoi = v.findViewById(R.id.txtSoNguoi);
        TextView valueGiaTu = v.findViewById(R.id.valueGiaTu);
        TextView txtDangNgay = v.findViewById(R.id.txtDangNgay);
        if (data.get(i).getLoai() == 1) {
            txttype.setText("Cần tìm nhà trọ");
        } else {
            if (data.get(i).getLoai() == 2) {
                txttype.setText("Tìm nhà nguyên căn ");
            } else {
                txttype.setText("Tìm phòng ở ghép");
            }
        }

        txtTitle.setText(data.get(i).getTieude());
        txtAddress.setText(data.get(i).getQuanhuyen());
        txtSoNguoi.setText("Số lượng: " + String.valueOf(data.get(i).getSonguoi()) + " người");

        /*DecimalFormat formatter = new DecimalFormat("###,###,###");
        String tmp = formatter.format(data.get(i).get) + " VNĐ";*/

        valueGiaTu.setText("Giá từ: " + data.get(i).getGiaTu() + " vnđ");
        txtDangNgay.setText("Đăng ngày " + data.get(i).getNgaydang());
        return v;
    }
}
