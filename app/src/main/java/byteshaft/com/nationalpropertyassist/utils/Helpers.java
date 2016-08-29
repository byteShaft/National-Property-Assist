package byteshaft.com.nationalpropertyassist.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import byteshaft.com.nationalpropertyassist.AppGlobals;

public class Helpers {

    public static SharedPreferences getPreferenceManager() {
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

    public static void saveInt(String key, Integer value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static Integer getInt(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getInt(key, 0);
    }

    public static void saveUserLogin(boolean value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(AppGlobals.KEY_USER_LOGIN, value).apply();
    }

    public static boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(AppGlobals.KEY_USER_LOGIN, false);
    }

    public static void setUserActive(boolean value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean("activation_key", value).apply();
    }

    public static boolean isUserActive() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean("activation_key", false);
    }

    public static void detailsStatus(boolean value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(AppGlobals.KEY_USER_DETAILS, value).apply();
    }

    public static boolean areDetailsSaved() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(AppGlobals.KEY_USER_DETAILS, false);

    }

    public static void alertDialog(Activity activity, String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg).setCancelable(false).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void clearSaveData() {
        getPreferenceManager().edit().clear().apply();
    }

    public static SpannableStringBuilder getFormattedTitle(String text, String nextText) {
        SpannableStringBuilder realText = new SpannableStringBuilder();
        SpannableString mandatorySpannable = new SpannableString(text);
            mandatorySpannable.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#6666ff")), 0, text.length(), 0);
            realText.append(mandatorySpannable);
        SpannableString whiteSpannable = new SpannableString(nextText);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, nextText.length(), 0);
        realText.append(whiteSpannable);
        return realText;
    }
}

