package byteshaft.com.nationalpropertyassist.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import byteshaft.com.nationalpropertyassist.AppGlobals;

public class Helpers {

    private static SharedPreferences getPreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(AppGlobals.getContext());
    }

    public static void saveDataToSharedPreferences(String key, String value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putString(key, value).apply();
    }


    public static String getStringFromSharedPreferences(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(key, "");

    }
}
