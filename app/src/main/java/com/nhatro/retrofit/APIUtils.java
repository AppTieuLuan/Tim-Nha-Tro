package com.nhatro.retrofit;

public class APIUtils {
    public static final String Base_Url = "https://nhatroservice.000webhostapp.com/";

    public static DataClient getData(){
        return RetrofitClient.getClient(Base_Url).create(DataClient.class);
    }
}
