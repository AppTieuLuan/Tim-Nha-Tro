package com.nhatro.retrofit;

public class APIUtils {
    public static final String Base_Url = "http://luciferwilling.ddns.net/appnhatro/";

    public static DataClient getData(){
        return RetrofitClient.getClient(Base_Url).create(DataClient.class);
    }
}
