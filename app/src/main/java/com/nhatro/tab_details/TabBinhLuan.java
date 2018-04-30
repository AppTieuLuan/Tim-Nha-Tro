package com.nhatro.tab_details;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nhatro.R;
import com.nhatro.adapter.Adapter_List_View_Binh_Luan;
import com.nhatro.model.BinhLuan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabBinhLuan extends Fragment {

    View v;
    ArrayList<BinhLuan> data;
    Adapter_List_View_Binh_Luan adapter_list_view_binh_luan;

    ListView listViewBinhLuan;
    ImageView imgSend;
    View footerView;
    View footerTaiThemBl;

    public TabBinhLuan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_tab_binh_luan, container, false);
        data = new ArrayList<>();
        Log.d("LOADDD ANH", "LOAD DLLLLLLLLLLL ANHHHHHHHHHHHHH");
        listViewBinhLuan = (ListView) v.findViewById(R.id.lstBinhLuan);
        imgSend = (ImageView) v.findViewById(R.id.btnSend);
        data.add(new BinhLuan(1, "https://i-vnexpress.vnecdn.net/2018/03/30/khaidonpng-1522370624_90x90.png", "user1", "Nguyễn Văn Thanh", "Với công trình không thể thi công hệ thống hút khói, thành phố đề xuất thay thế cửa mở ra hành lang bằng cửa chống cháy tự động đóng", "30/3/2018 15:20"));
        data.add(new BinhLuan(2, "https://i-vnexpress.vnecdn.net/2018/03/30/TongThienMa-1522389654_140x84.jpg", "user2", "Lê Thị Thiên Hương", "Tiền của nhà nước là tiền của dân, nhưng doanh nghiệp nhà nước thì chưa vì dân.", "30/3/2018 15:26"));
        data.add(new BinhLuan(3, "https://i-giaitri.vnecdn.net/2018/03/30/huynh-hieu-minh-5271-1522397016_140x84.jpg", "user3", "Phạm Trung Tuyến", "Nền văn hóa Á Đông tạo ra thói quen cho dân chơi cờ bạc: báo nhà trả nợ.", "30/3/2018 15:10"));
        data.add(new BinhLuan(4, "https://i-giadinh.vnecdn.net/2018/03/30/embe-1522379139-2335-1522379159_140x84.jpg", "user4", "Trần Hương Thùy", "Học sinh phổ thông có vấn đề về tâm lý, các con biết tìm ai để chia sẻ?", "30/3/2018 15:09"));
        data.add(new BinhLuan(5, "https://i-vnexpress.vnecdn.net/2018/03/29/dinhlathangtuyenan-1522292274-5901-1522292323_140x84.jpg", "user5", "Ngô Trọng Thanh", "Nếu chỉ dùng một từ để nói về anh Sáu Khải, đó là từ gì?", "30/3/2018 15:08"));
        data.add(new BinhLuan(6, "https://i-giaitri.vnecdn.net/2018/03/29/carolinesunshinet-1522293273-8229-1522294510_180x108.jpg", "user6", "Đinh Hồng Kỳ", "Kẻ thù lớn nhất của kiến thức không phải là sự ngu dốt, mà chính là ảo tưởng.", "30/3/2018 15:05"));

        adapter_list_view_binh_luan = new Adapter_List_View_Binh_Luan(getContext(), R.layout.layout_item_list_binh_luan, data);
        //adapter_list_view_binh_luan.notifyDataSetChanged();

        listViewBinhLuan.setAdapter(adapter_list_view_binh_luan);

        footerTaiThemBl = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_view_binh_luan_tai_them, null, false);
        footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_view_binh_luan, null, false);

        listViewBinhLuan.addFooterView(footerTaiThemBl);

        footerTaiThemBl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Tải thêm bl", Toast.LENGTH_SHORT).show();
                listViewBinhLuan.removeFooterView(footerTaiThemBl);
                listViewBinhLuan.addFooterView(footerView);
            }
        });


        EditText edt = (EditText) v.findViewById(R.id.edtNhapBl);

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),edt.getText().toString(),Toast.LENGTH_SHORT).show();
                if (edt.getText().toString().equals("")) {
                    Snackbar.make(getView(), "Nhập nội dung", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    BinhLuan blt = new BinhLuan(6, "https://i-giaitri.vnecdn.net/2018/03/29/carolinesunshinet-1522293273-8229-1522294510_180x108.jpg", "user6", "Đinh Hồng Kỳ", "Kẻ thù lớn nhất của kiến thức không phải là sự ngu dốt, mà chính là ảo tưởng.", "30/3/2018 15:05");

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    //System.out.println(dateFormat.format(date));

                    /*blt.setNgayViet(String.valueOf(date.getDay()) + "/" + String.valueOf(date.getMonth()) + "/" + String.valueOf(date.getYear()) + " " +
                                    String.valueOf(date.getHours()) + ":" + String.valueOf(date.getMinutes()));*/

                    DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                    blt.setNgayViet(dateFormat2.format(date).toString());


                    data.add(0, blt);

                    adapter_list_view_binh_luan.notifyDataSetChanged();

                    edt.setText("");
                }


            }
        });

        return v;
    }

}
