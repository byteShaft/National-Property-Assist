package byteshaft.com.nationalpropertyassist.account;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.MainActivity;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.database.AddPropertyDetailsDatabase;
import byteshaft.com.nationalpropertyassist.utils.Helpers;
import byteshaft.com.nationalpropertyassist.utils.WebServiceHelper;

public class LoginActivity extends AppCompatActivity {

    private Button mLoginButton;
    private EditText mEmailAddress;
    private EditText mPassword;
    private TextView mSignUpText;
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    private String mPasswordEntry;
    private String mEmail;
    public static String first_name;
    public static String last_name;
    public static String email;
    public JSONObject jsonObject;
    private static LoginActivity sInstance;
    private static boolean isForeGround = false;
    public static AddPropertyDetailsDatabase database;

    public static LoginActivity getInstance() {
        return sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInstance = this;
        database = new AddPropertyDetailsDatabase(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);
        callbackManager = CallbackManager.Factory.create();
        mEmailAddress = (EditText) findViewById(R.id.email_address);
        mPassword = (EditText) findViewById(R.id.password_entry);
        fbLoginButton = (LoginButton) findViewById(R.id.login_button);
        mLoginButton = (Button) findViewById(R.id.login);
        mSignUpText = (TextView) findViewById(R.id.signup_text);
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }

        });


        mSignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(validate());
                if (validate()) {
                    new LoginTask().execute();
                } else {
                    Toast.makeText(LoginActivity.this, "please fix the following errors", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeGround = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeGround = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.getInstance().finish();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        jsonObject = response.getJSONObject();
                        try {
                            first_name = jsonObject.getString("first_name");
                            last_name = jsonObject.getString("last_name");
                            if (!jsonObject.has("email")) {
                                System.out.println("not found");
                                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                            }
                            System.out.println(first_name);
                            System.out.println(last_name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public boolean validate() {
        boolean valid = true;

        mEmail = mEmailAddress.getText().toString();
        mPasswordEntry = mPassword.getText().toString();

        if (mEmail.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.
                matcher(mEmail).matches()) {
            mEmailAddress.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailAddress.setError(null);
        }

        if (mPasswordEntry.isEmpty() || mPassword.length() < 4 || mPassword.length() > 10) {
            mPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }
        return valid;
    }

    class LoginTask extends AsyncTask<String, String, String> {

        private boolean noInternet = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebServiceHelper.showProgressDialog(LoginActivity.this, "LoggingIn");
        }

        @Override
        protected String doInBackground(String... params) {
            String data = null;
            if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {
                try {
                    data = WebServiceHelper.userLogin(mEmail, mPasswordEntry);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } else {
                noInternet = true;
            }
            return data;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (noInternet) {
                WebServiceHelper.dismissProgressDialog();
                Helpers.alertDialog(LoginActivity.this, "Connection error",
                        "Check your internet connection");
            } else if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN) {
                WebServiceHelper.dismissProgressDialog();
                Toast.makeText(AppGlobals.getContext(), "Login Failed! Account not activated",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), CodeConfirmationActivity.class));
            } else if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Helpers.saveDataToSharedPreferences(AppGlobals.KEY_USER_TOKEN, response);
                Log.i("Token", " " + Helpers.getStringFromSharedPreferences(AppGlobals.KEY_USER_TOKEN));
                Helpers.setUserActive(true);
                new GetUserDataTask().execute();
                Helpers.saveUserLogin(true);
            } else {
                WebServiceHelper.dismissProgressDialog();
                Toast.makeText(AppGlobals.getContext(), "Login Failed! Invalid Email or Password",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class GetUserDataTask extends AsyncTask<Void, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject;
            try {
                jsonObject = WebServiceHelper.userData();
                if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    System.out.println(jsonObject + "userData");
                    String first_name = jsonObject.getString(AppGlobals.KEY_FIRST_NAME);
                    String last_name = jsonObject.getString(AppGlobals.KEY_LAST_NAME);
                    String email = jsonObject.getString(AppGlobals.KEY_EMAIL);
                    String mobile_phone = jsonObject.getString(AppGlobals.KEY_MOBILE_PHONE);
                    String home_phone = jsonObject.getString(AppGlobals.KEY_HOME_PHONE);
                    //saving values
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_FIRST_NAME, first_name);
                    Log.i("First name", " " + Helpers.getStringFromSharedPreferences(AppGlobals.KEY_FIRST_NAME));
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_LAST_NAME, last_name);
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_EMAIL, email);
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_MOBILE_PHONE, mobile_phone);
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_HOME_PHONE, home_phone);
                    Helpers.saveUserLogin(true);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println(isForeGround);
            if (isForeGround) {
                WebServiceHelper.dismissProgressDialog();
                LoginActivity.getInstance().finish();
            }
            if (Helpers.isUserLoggedIn()) {
                new GetSavedProperties().execute();
            }

        }
    }

    public static class GetSavedProperties extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result;
            String location = "http://178.62.37.43:8000/api/properties";
            try {
                URL url = new URL(location);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Token " +
                        Helpers.getStringFromSharedPreferences("token"));
                InputStream inputStream = connection.getInputStream();
                result = WebServiceHelper.convertInputStreamToString(inputStream);
                JSONArray array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = (JSONObject) array.get(i);
                    String address = jsonObject.getString("address");
                    String ageOfProperty = jsonObject.getString("age");
                    int categoryPrimary = jsonObject.getInt("category_primary");
                    String categorySecondary = jsonObject.getString("category_secondary");
                    int iD = jsonObject.getInt("id");
                    String postCode = jsonObject.getString("postcode");

                    database.createNewEntry(address, postCode, categoryPrimary,
                            categorySecondary, ageOfProperty, iD);
                }

                Log.i("TAG", result);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
