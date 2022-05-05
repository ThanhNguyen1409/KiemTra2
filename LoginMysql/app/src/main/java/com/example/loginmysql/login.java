package com.example.loginmysql;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface login {
    String LOGINURL = "http://192.168.1.112/loginandroid/";
    @FormUrlEncoded
    @POST("login.php")
    Call<String> getUserLogin(

            @Field("username") String uname,
            @Field("password") String password
    );

}
