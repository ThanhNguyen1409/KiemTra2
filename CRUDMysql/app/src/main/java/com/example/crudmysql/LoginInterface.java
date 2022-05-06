package com.example.crudmysql;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterface {

    String LOGINURL = "http://192.168.0.3/CRUDMysql/";
    @FormUrlEncoded
    @POST("login.php")
    Call<String> getUserLogin(

            @Field("name") String name,
            @Field("password") String password

    );
}
