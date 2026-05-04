package com.example.stockscout_android_take_home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import retrofit2.Response;

public class PickWorker extends Worker {

    public PickWorker(@NonNull Context context,
                      @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        String code = getInputData().getString("code");
        int qty = getInputData().getInt("qty", 0);

        try {
            PickRequest request = new PickRequest();
            request.itemCode = code;
            request.quantity = qty;
            request.timestamp = System.currentTimeMillis();

            Response<Void> response =
                    RetrofitClient.getApi().sendPick(request).execute();

            if (response.isSuccessful()) {
                return Result.success();
            } else {
                return Result.retry();
            }

        } catch (Exception e) {
            return Result.retry();
        }
    }
}