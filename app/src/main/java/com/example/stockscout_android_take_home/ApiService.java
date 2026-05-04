package com.example.stockscout_android_take_home;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("items.json")
    Call<List<Item>> getItems();

    @POST("pick")
    Call<Void> sendPick(@Body PickRequest request);
}