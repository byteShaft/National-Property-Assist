package byteshaft.com.nationalpropertyassist;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.util.HashMap;

public class AppGlobals extends Application {

    private static Context sContext;
    public static HashMap<String, String> hashMap = null;
    public static final String KEY_USER_TOKEN = "token";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_PHONE = "mobile_phone";
    public static final String KEY_HOME_PHONE = "home_phone";
    public static final String KEY_USER_LOGIN = "user_login";
    public static int responseCode = 0;
    public static final String KEY_USER_DETAILS = "user_details";
    public static Typeface typeface;
    public static Typeface typefaceItalic;
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
        hashMap = new HashMap<>();
        addHashMapValue();
        sContext = getApplicationContext();
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/bold.ttf");
        typefaceItalic = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/italic.ttf");
    }

    public static String getPriceDetails(String key) {
        return hashMap.get(key);
    }

    private void addHashMapValue() {

        /*  fields that have no price in front of them are "Estimate to be provided" */
        
        // for water assist
        hashMap.put( "Repair to leaking Supply Pipe" , "600");
        hashMap.put("Renewal of Supply Pipe", "Estimate to be provided");
        hashMap.put("New Installation", "Estimate to be provided");

        // plumbing Installation
        hashMap.put("Repair to leaking plumbing Installation", "250");
        hashMap.put("Renewal of plumbing Installation", "250");

        // home buyer survey
        hashMap.put("Home Buyer Survey", "250");
        hashMap.put("Buyer Drain Survey", "250");

        // Building assist survey
        hashMap.put("Building Survey", "80");
        hashMap.put("Structural Survey", "Estimate to be provided");
        hashMap.put("Damage and Repair Survey", "80");
        hashMap.put("Insurance Claim Survey" , "80");

        // Emergency Unblock
        hashMap.put("Blocked Toilet" , "80");
        hashMap.put("Blocked Kitchen Facilities" , "80");
        hashMap.put("Blocked bath/shower" , "80");
        hashMap.put("Blocked Septic Tank" , "80");
        hashMap.put("Blocked Rain Water Pipes" , "80");
        hashMap.put("Other" , "80");

        // Drainage Survey
        hashMap.put("Toilet" , "150");
        hashMap.put("Kitchen Facilities" , "150");
        hashMap.put("Bath/Shower" , "150");
        hashMap.put("Septic Tank" , "150");
        hashMap.put("Rain water gully" , "150");
        hashMap.put("Rodent Issues" , "150");
        hashMap.put("Subsidence" , "150");
        hashMap.put("Other" , "150");

        // Drainage Repairs
        hashMap.put("Excavation", "Estimate to be provided");
        hashMap.put("Lining", "Estimate to be provided");
        hashMap.put("Root Cutting", "Estimate to be provided");
        hashMap.put("Other", "Estimate to be provided");

        // Septic or treatment tank
        hashMap.put("Maintenance and Desudging", "250");
        hashMap.put("Investigate a Problem", "150");
        hashMap.put("Repair", "Estimate to be provided");
        hashMap.put("Other", "Estimate to be provided");

        // new Installation
        hashMap.put("Foul Drainage", "Estimate to be provided");
        hashMap.put("Surface Water Drainage", "Estimate to be provided");
        hashMap.put("Septic Tank", "Estimate to be provided");
        hashMap.put("Other", "Estimate to be provided");

        // Maintenance
        hashMap.put("Scale Removal", "80");
        hashMap.put("Repairs to Manhole", "Estimate to be provided");
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
