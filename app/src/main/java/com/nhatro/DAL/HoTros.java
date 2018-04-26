package com.nhatro.DAL;

import android.util.Log;

import com.nhatro.model.HoTro;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CongHoang on 4/22/2018.
 */

public class HoTros {


    public void HoTros() {

    }

    public ArrayList<HoTro> getAll() {
        String URL_DISP_PLATFFORM = "https://nhatroservice.000webhostapp.com/getAllHoTros.php";
        ArrayList<HoTro> arrayList = new ArrayList<>();
        MyService jsonParser = new MyService();
        String json = jsonParser.callService(URL_DISP_PLATFFORM, MyService.GET);

        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj != null) {
                    JSONArray hotros = jsonObj.getJSONArray("hotros");
                    for (int i = 0; i < hotros.length(); i++) {
                        JSONObject obj = (JSONObject) hotros.get(i);
                        HoTro ht = new HoTro(obj.getInt("id"), obj.getString("email"), obj.getString("noidung"));
                        arrayList.add(ht);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("JSON Data", "Didn't receive any data from server!");
        }
        return arrayList;

    }

    public boolean addHoTro(HoTro a) {
        String URL_NEW = "https://nhatroservice.000webhostapp.com/themHoTro.php";
        //String URL_NEWs = "https://192.168.1.9:8080/firebase/themHoTro.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();
        args.add(new BasicNameValuePair("noidung", a.getNoidung()));
        args.add(new BasicNameValuePair("email", a.getEmal()));
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
}
