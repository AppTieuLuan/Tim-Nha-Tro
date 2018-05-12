package com.nhatro.DAL;

import com.nhatro.model.TinTimPhong;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CongHoang on 5/11/2018.
 */

public class DAL_TinTimPhong {
    Variable variable = new Variable();

    public boolean themTinMoi(TinTimPhong tinTimPhong) {
        boolean rs = false;

        String URL_NEW = variable.getWebservice() + "themTinTimPhong.php";
        //String URL_NEW = "http://192.168.1.9:8080/firebase/upTinPhongTro.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("tieude", tinTimPhong.getTieude()));
        args.add(new BasicNameValuePair("loaitin", String.valueOf(tinTimPhong.getLoaitin())));
        args.add(new BasicNameValuePair("songuoimin", String.valueOf(tinTimPhong.getSonguoimin())));
        args.add(new BasicNameValuePair("songuoimax", String.valueOf(tinTimPhong.getSonguoimax())));
        args.add(new BasicNameValuePair("giamin", String.valueOf(tinTimPhong.getGiamin())));
        args.add(new BasicNameValuePair("giamax", String.valueOf(tinTimPhong.getGiamax())));
        args.add(new BasicNameValuePair("giogiac", String.valueOf(tinTimPhong.getGiogiac())));
        args.add(new BasicNameValuePair("idtp", String.valueOf(tinTimPhong.getIdtp())));
        args.add(new BasicNameValuePair("idqh", tinTimPhong.getIdqh()));
        args.add(new BasicNameValuePair("lat", String.valueOf(tinTimPhong.getLat())));
        args.add(new BasicNameValuePair("lng", String.valueOf(tinTimPhong.getLng())));
        args.add(new BasicNameValuePair("bankinh", String.valueOf(tinTimPhong.getBankinh())));
        args.add(new BasicNameValuePair("tiennghi", tinTimPhong.getTiennghi()));
        args.add(new BasicNameValuePair("motathem", tinTimPhong.getMotathem()));
        args.add(new BasicNameValuePair("nhanthongbao", String.valueOf(tinTimPhong.getNhanthongbao())));
        args.add(new BasicNameValuePair("iduser", String.valueOf(tinTimPhong.getIduser())));
        args.add(new BasicNameValuePair("gioitinh", String.valueOf(tinTimPhong.getGioitinh())));
        args.add(new BasicNameValuePair("khuvuc", String.valueOf(tinTimPhong.getKhuvuc())));

        // Lấy đối tượng JSON
        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("success");
                if (success == 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (JSONException e) {
                return false;
            }
        }

        return rs;
    }
}
