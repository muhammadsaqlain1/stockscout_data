package com.example.stockscout_android_take_home;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "items")
public class Item {

    @PrimaryKey
    @NonNull
    @SerializedName("itemCode")
    public String itemCode = "";

    @SerializedName("name")
    public String name = "";

    @SerializedName("unit")
    public String unit = "";

    @SerializedName("quantity")
    public int quantity;

    // Not stored directly
    public List<String> aliases;

    // Stored in DB
    public String aliasesJson;
}