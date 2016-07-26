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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.utils.Helpers;
import byteshaft.com.nationalpropertyassist.MainActivity;
import byteshaft.com.nationalpropertyassist.R;
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


    public static LoginActivity getInstance() {
        return sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInstance = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.delegate_login);
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
                    new  LoginTask().execute();
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
                            if (!jsonObject.has("email")){
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
            }  else if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN) {
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
                    String first_name = jsonObject.getString(AppGlobals.KEY_FIRSTNAME);
                    String last_name = jsonObject.getString(AppGlobals.KEY_LASTNAME);
                    String email = jsonObject.getString(AppGlobals.KEY_EMAIL);
                    String mobile_phone = jsonObject.getString(AppGlobals.KEY_MOBILEPHONE);
                    String home_phone = jsonObject.getString(AppGlobals.KEY_HOMEPHONE);
                    //saving values
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_FIRSTNAME, first_name);
                    Log.i("First name", " " + Helpers.getStringFromSharedPreferences(AppGlobals.KEY_FIRSTNAME));
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_LASTNAME, last_name);
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_EMAIL, email);
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_MOBILEPHONE, mobile_phone);
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_HOMEPHONE, home_phone);
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
            if (isForeGround) {
                WebServiceHelper.dismissProgressDialog();
                LoginActivity.getInstance().finish();
            }

        }
    }
}
