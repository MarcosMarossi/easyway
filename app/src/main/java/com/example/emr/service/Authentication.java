package com.example.emr.service;

import com.example.emr.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Authentication {

    @POST("auth/jwt/login")
    Call<User> acessApp(@Body User token);

    @GET("auth/jwt/me")
    Call<User> getToken(@Header("x-access-token") String Token);

    @POST("auth/jwt/resetpassword")
    Call<User> resetEmail(@Body User email);

    @POST("auth/jwt/register")
    Call<User> registerNewUser(@Body User register);
}
