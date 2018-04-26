package com.nhatro.DAL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CongHoang on 4/25/2018.
 */

public class HinhAnhs {
    public boolean xoaListHA(ArrayList<String> names){
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
}
