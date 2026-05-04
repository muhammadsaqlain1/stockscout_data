package com.example.stockscout_android_take_home

import android.content.Context
import androidx.work.Data
import androidx.work.Data.Builder
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

fun enqueuePick(context: Context, code: String?, qty: Int) {
    val data: Data = Builder()
        .putString("code", code)
        .putInt("qty", qty)
        .build()

    val work =
        OneTimeWorkRequest.Builder(PickWorker::class.java)
            .setInputData(data)
            .build()

    WorkManager.getInstance(context).enqueue(work)
}