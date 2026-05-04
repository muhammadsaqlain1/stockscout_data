package com.example.stockscout_android_take_home;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    @TypeConverter
    public static String fromList(List<String> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static List<String> fromString(String value) {
        Type type = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, type);
    }
}