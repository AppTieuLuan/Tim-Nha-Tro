package com.nhatro.DAL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CongHoang on 4/25/2018.
 */

public class HinhAnhs {
    Variable variable = new Variable();

    public boolean xoaListHA(ArrayList<String> names) {
        String rs = "-1";

        //String URL_NEW = "https://nhatroservice.000webhostapp.com/upTinPhongTro.php";
        String URL_NEW = "http://192.168.1.9:8080/firebase/upTinPhongTro.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();
        /*args.add(new BasicNameValuePair("lstha[]", names.Arr));*/

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

        return false;
    }

    public ArrayList<String> getImages(String id) {
        ArrayList<String> data = new ArrayList<>();

        /*if (locDL.isDanhsach() == 1) {*/

        String URL_NEW = variable.getWebservice() + "getImages.php";
        //String URL_NEW = "http://192.168.1.23:8080/firebase/danhSachPhong.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("id", id));

        MyService jsonParser = new MyService();

        String json = jsonParser.callService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj != null) {
                    int kq = jsonObj.getInt("kq");
                    if (kq == 0) {
                        return data;
                    } else {
                        JSONArray hotros = jsonObj.getJSONArray("hinhanhs");
                        for (int i = 0; i < hotros.length(); i++) {
                            JSONObject obj = (JSONObject) hotros.get(i);
                            data.add(obj.getString("link"));
                        }
                    }

                }
            } catch (JSONException e) {
                return data;
            }
        }

        return data;
    }
}
