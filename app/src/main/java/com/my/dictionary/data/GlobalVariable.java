package com.my.dictionary.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.my.dictionary.R;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalVariable extends Application {

    public static final String IDX_KEY_COLOR = "idx_key_color";
    public static final int DEFAULT_IDX = 5; //color blue

    public static final String S_KEY_TABLE = "s_key_table";

    public static final String I_KEY_FAV_COUNTE = "i_key_fav_count";

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.admob_app_id));
    }

    /**
     * Universal shared preference
     * for boolean
     */
    public boolean getBooleanPref(String key_val, boolean def_val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        return pref.getBoolean(key_val, def_val);
    }

    public void setBooleanPref(String key_val, boolean val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putBoolean(key_val, val);
        prefEditor.commit();
    }

    /**
     * Universal shared preference
     * for integer
     */
    public int getIntPref(String key_val, int def_val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        return pref.getInt(key_val, def_val);
    }

    public void setIntPref(String key_val, int val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putInt(key_val, val);
        prefEditor.commit();
    }

    /**
     * Universal shared preference
     * for string
     */
    public String getStringPref(String key_val, String def_val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        return pref.getString(key_val, def_val);
    }

    public void setStringPref(String key_val, String val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putString(key_val, val);
        prefEditor.commit();
    }


    public String generateCurrentDate(int format_key) {
        Date curDate = new Date();
        String DateToStr = "";
        //default 11-5-2014 11:11:51
        SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        switch (format_key) {
            case 1:

                format = new SimpleDateFormat("dd/MM/yyy");
                DateToStr = format.format(curDate);
                break;

            case 2:
                //May 11, 2014 11:37 PM
                DateToStr = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(curDate);
                break;
            case 3:
                //11-5-2014 11:11:51
                format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                DateToStr = format.format(curDate);
                break;
        }
        return DateToStr;
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }
        setIdxColor(DEFAULT_IDX);
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public int getIntColor() {
        String code = getResources().getStringArray(R.array.arr_main_color_code)[getIdxColor()];
        return Color.parseColor(code);
    }

    public int getIntDarkColor() {
        String code = getResources().getStringArray(R.array.arr_main_color_code_dark)[getIdxColor()];
        return Color.parseColor(code);
    }


    public int getIdxColor() {
        int index = getIntPref(IDX_KEY_COLOR, DEFAULT_IDX);
        Log.i("MY_DICT", index + "");
        return index;
    }

    public void setIdxColor(int value) {
        setIntPref(IDX_KEY_COLOR, value);
    }

    public String getStrColor() {
        return getResources().getStringArray(R.array.arr_main_color_name)[getIdxColor()];
    }

    public void increaseFavorites() {
        setIntPref(I_KEY_FAV_COUNTE, getFavorites() + 1);
    }

    public int getFavorites() {
        return getIntPref(I_KEY_FAV_COUNTE, 0);
    }

    public void decreaseFavorites() {
        setIntPref(I_KEY_FAV_COUNTE, getFavorites() - 1);
    }
}
