package com.example.alertdialog.util;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesUtil {
    private static String PREFERENCE_NAME = "info";
    private static PreferencesUtil preferencesUtil;

    /*SharedPreferences 是 Android 中用于存储轻量级持久化数据的一种机制。
    它允许你存储和检索键值对数据，
    这些数据在应用关闭后仍然可以保持存在，
    并且可以在应用的不同组件之间共享数据。*/
    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;
    private Context context;

    public PreferencesUtil(Context context) {
        this.context = context.getApplicationContext();
        this.preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        this.editor = this.preferences.edit();
    }

    public static PreferencesUtil getInstance(Context context) {
        if (preferencesUtil == null) {
            preferencesUtil = new PreferencesUtil(context);
        }
        return preferencesUtil;
    }

    /**存储字符串*/
    public boolean putString(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }
    /**读取字符串*/
    public String getString(String key) {
        return getString(key, "");
    }
    /**读取字符串（带默认值的）*/
    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }
    /**存储整型数字*/
    public boolean putInt(String key, int value) {
        editor.putInt(key, value);
        return editor.commit();
    }
    /**读取整型数字*/
    public int getInt(String key) {
        return getInt(key, -1);
    }
    /**读取整型数字（带默认值的）*/
    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }
    /**存储长整型数字*/
    public boolean putLong(String key, long value) {
        editor.putLong(key, value);
        return editor.commit();
    }
    /**读取长整型数字*/
    public long getLong(String key) {
        return getLong(key, 0xffffffff);
    }
    /**读取长整型数字（带默认值的）*/
    public long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }
    /**存储Float数字*/
    public boolean putFloat(String key, float value) {
        editor.putFloat(key, value);
        return editor.commit();
    }
    /**读取Float数字*/
    public float getFloat(String key) {
        return getFloat(key, -1.0f);
    }
    /**读取Float数字（带默认值的）*/
    public float getFloat(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }
    /**存储boolean类型数据*/
    public boolean putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }
    /**读取boolean类型数据*/
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }
    /**读取boolean类型数据（带默认值的）*/
    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }
    /**清除数据*/
    public boolean clearPreferences() {
        editor.clear();
        return editor.commit();
    }
}
