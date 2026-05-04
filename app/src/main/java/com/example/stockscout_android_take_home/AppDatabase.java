package com.example.stockscout_android_take_home;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.stockscout_android_take_home.Converters;
import com.example.stockscout_android_take_home.Item;
import com.example.stockscout_android_take_home.ItemDao;
@Database(entities = {Item.class}, version = 2, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ItemDao itemDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "stock_db"
                    )
                    .fallbackToDestructiveMigration() // 🔥 FIX CRASH
                    .build();
        }
        return instance;
    }
}