package byteshaft.com.nationalpropertyassist;

import android.app.Application;
import android.content.Context;

public class AppGlobals extends Application {

    private static Context sContext;

    public static final String KEY_USER_TOKEN = "token";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILEPHONE = "mobile_phone";
    public static final String KEY_HOMEPHONE = "home_phone";
    public static final String USER_ACTIVATION_KEY = "activation_key";
    public static final String KEY_USER_LOGIN = "activation_key";
    public static int responseCode = 0;
    public static int readresponseCode = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
    public static Context getContext() {
        return sContext;
    }

    public static void setResponseCode(int code) {
        responseCode = code;
    }

    public static int getResponseCode() {
        return responseCode;
    }
}
