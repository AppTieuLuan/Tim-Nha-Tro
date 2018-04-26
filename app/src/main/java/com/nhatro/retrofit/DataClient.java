package com.nhatro.retrofit;

import com.nhatro.model.Token;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataClient {
    //Đăng ký tài khoản
    @FormUrlEncoded
    @POST("register.php")
    Call<String> Register(@Field("name") String name,
                          @Field("username") String username,
                          @Field("phone") String phone,
                          @Field("password") String password);

    //Đăng nhập
    @FormUrlEncoded
    @POST("login.php")
    Call<Token> Login(@Field("username") String username,
                      @Field("password") String password);

    //Upload avatar
    @Multipart
    @POST("upload_avatar.php")
    Call<String> UploadAvatar(@Part MultipartBody.Part photo);

    //Update avatar url
    @FormUrlEncoded
    @POST("update_avatar_url.php")
    Call<String> UpdateAvatar(@Field("username") String username,
                              @Field("path") String path);

    //Kiểm tra đăng nhập
    @FormUrlEncoded
    @POST("check_login.php")
    Call<Token> CheckLogin(@Field("token") String token);

    //Cập nhật lại token
    @FormUrlEncoded
    @POST("refresh_token.php")
    Call<String> RefreshToken(@Field("token") String token);

    //Gứi phản hồi giúp đỡ
    @FormUrlEncoded
    @POST("send_support.php")
    Call<String> SendSupport(@Field("email") String email,
                             @Field("content") String content);

    //Đổi mật khẩu
    @FormUrlEncoded
    @POST("change_pass.php")
    Call<String> ChangePass(@Field("username") String username,
                            @Field("oldpass") String oldpass,
                            @Field("newpass") String newpass);

    //Đổi thông tin người dùng
    @FormUrlEncoded
    @POST("change_info.php")
    Call<String> ChangeInfo(@Field("username") String username,
                            @Field("name") String name,
                            @Field("phone") String phone,
                            @Field("fburl") String url,
                            @Field("address") String address);
}
