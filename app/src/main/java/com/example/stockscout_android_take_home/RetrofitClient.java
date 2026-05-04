package com.example.stockscout_android_take_home;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // 🔥 Replace with your real GitHub raw URL
    private static final String BASE_URL =
            "https://raw.githubusercontent.com/muhammadsaqlain1/stockscout_data/main/";

    public static ApiService getApi() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }
}