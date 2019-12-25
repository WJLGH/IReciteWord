package com.sdut.soft.ireciteword.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sdut.soft.ireciteword.DetailActivity;

public class SettingUtils {

    public static String getDefaultSearchUnit(Context context) {
        String unit = Const.DEFAULT_UNIT;
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            unit = sharedPreferences.getString("searchUnit", Const.DEFAULT_UNIT);
        }
        return unit;
    }

    public static  void setDefaultSearchUnit(Context context, String unitName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("searchUnit", unitName);
        edit.commit();
    }

    public static int getPerDay(Context context) {
        int perDay = Const.PER_DAY;
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            perDay = sharedPreferences.getInt("per_day", Const.PER_DAY);
        }
        return perDay;
    }

    public static  void setPerDay(Context context, int perDay) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("per_day", perDay);
        edit.commit();
    }
}
