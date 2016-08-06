package byteshaft.com.nationalpropertyassist;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.paypal.android.sdk.payments.PayPalConfiguration;

public class AppGlobals extends Application {

    private static Context sContext;
    public static final String KEY_USER_TOKEN = "token";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_PHONE = "mobile_phone";
    public static final String KEY_HOME_PHONE = "home_phone";
    public static final String USER_ACTIVATION_KEY = "activation_key";
    public static final String KEY_USER_LOGIN = "user_login";
    public static int responseCode = 0;
    public static int readresponseCode = 0;
    public static final String KEY_USER_DETAILS = "user_details";
    public static Typeface typeface;
    public static final String GET_ACTIVE_JOBS_URL = "http://178.62.37.43:8000/api/services/active";
    public static int serverIdForProperty = 2112;

    // paypal

    public static final int REQUEST_CODE_PAYMENT = 1;
    public static final String CONFIG_CLIENT_ID = "ARu_4mdznUCYfwTqE_xt93eZ9U4L17KiWhCnleEdoSjcDNah6FZbGxQIa3u5uNewlGlyNXjuPH7y-MkG";
    public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    public static PayPalConfiguration CONFIG = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ebrima.ttf");
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
