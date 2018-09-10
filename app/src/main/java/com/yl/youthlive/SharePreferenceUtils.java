package com.yl.youthlive;

import android.content.Context;
import android.content.SharedPreferences;


public class SharePreferenceUtils {

    private static String PREFERENCE_NAME = "yl";
    private static SharePreferenceUtils sharePreferenceUtils;
    private SharedPreferences sharedPreferences;

    private SharePreferenceUtils(Context context) {
        PREFERENCE_NAME = PREFERENCE_NAME + context.getPackageName();
        this.sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static SharePreferenceUtils getInstance() {
        if (sharePreferenceUtils == null) {
            sharePreferenceUtils = new SharePreferenceUtils(bean.getContext());
        }
        return sharePreferenceUtils;
    }

    public void saveString(String key, String Val) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, Val);
        editor.commit();
    }

    public String getString(String key, String defVal) {
        return sharedPreferences.getString(key, defVal);
    }


    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void saveInt(String key, int Val) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, Val);
        editor.commit();
    }

    public int getInteger(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void deletePref() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}
