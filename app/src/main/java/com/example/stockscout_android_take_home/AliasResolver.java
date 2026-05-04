package com.example.stockscout_android_take_home;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
public class AliasResolver {

    public static Item resolve(String query, List<Item> items) {

        if (items == null) return null;

        for (Item item : items) {

            if (item.itemCode.equalsIgnoreCase(query)) return item;
            if (item.name.equalsIgnoreCase(query)) return item;

            if (item.aliases != null) {
                for (String alias : item.aliases) {
                    if (alias.equalsIgnoreCase(query)) {
                        return item;
                    }
                }
            }
        }
        return null;
    }
}