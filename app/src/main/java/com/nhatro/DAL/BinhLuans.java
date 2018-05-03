package com.nhatro.DAL;

import android.util.Log;

import com.nhatro.model.BinhLuan;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by CongHoang on 5/1/2018.
 */

public class BinhLuans {
    Variable variable = new Variable();

    public ArrayList<BinhLuan> danhSachBL(String idphong, int trang) {
        ArrayList<BinhLuan> binhLuans = new ArrayList<>();

        String URL_NEW = variable.getWebservice() + "danhSachBinhLuan.php";
        //String URL_NEW = "http://192.168.1.23:8080/firebase/danhSachPhong.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("idphong", idphong));
        args.add(new BasicNameValuePair("trang", String.valueOf(trang)));


        MyService jsonParser = new MyService();

        String json = jsonParser.callService(URL_NEW, MyService.POST, args);

        Log.d("JSON LOAD BL", json);
        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj != null) {
                    int kq = jsonObj.getInt("kq");
                    if (kq == 0) {
                        return null;
                    } else {
                        JSONArray binhluan = jsonObj.getJSONArray("binhluans");
                        for (int i = 0; i < binhluan.length(); i++) {
                            JSONObject obj = (JSONObject) binhluan.get(i);

                            BinhLuan bl = new BinhLuan();

                            bl.setUserName(obj.getString("username"));
                            bl.setNgayViet(obj.getString("ngay"));
                            bl.setAvatarUser(obj.getString("avatar"));
                            bl.setNoiDungBl(obj.getString("noidung"));
                            bl.setTenUser(obj.getString("hoten"));
                            bl.setId(obj.getString("idbl"));

                            binhLuans.add(bl);
                        }
                        return binhLuans;
                    }
                }
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }

    public String themBL(BinhLuan b) {
        String URL_NEW = variable.getWebservice() + "themBL.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();
        args.add(new BasicNameValuePair("noidung", b.getNoiDungBl()));
        args.add(new BasicNameValuePair("idphong", b.getIdPhong()));
        args.add(new BasicNameValuePair("iduser", String.valueOf(b.getIdUser())));
        // Lấy đối tượng JSON

        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("success");
                if (success == 1) {
                    return jsonObject.getString("id");
                } else {
                    return "-1";
                }
            } catch (JSONException e) {
                return "-1";
            }
        }
        return "-1";
    }
}
