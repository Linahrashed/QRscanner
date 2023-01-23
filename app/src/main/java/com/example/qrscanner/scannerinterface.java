package com.example.qrscanner;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface scannerinterface {
    @POST("/usernameexists")
    Call<User> getUser(@Body HashMap<String, String> map);

    @POST("/newreceipt")
    Call<Void> addReceipt(@Body HashMap<String, String> map);

    @POST("/newitem")
    Call<Void> newitem (@Body HashMap<String, String> map);
}
