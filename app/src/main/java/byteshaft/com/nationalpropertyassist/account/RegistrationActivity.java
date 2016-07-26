package byteshaft.com.nationalpropertyassist.account;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.utils.Helpers;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.utils.WebServiceHelper;


public class RegistrationActivity extends AppCompatActivity {

    private Button mRegisterButton;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailAddress;
    private EditText mMobileNumber;
    private EditText mHomeNumber;
    private EditText mPassword;

    private String mFname;
    private String mLname;
    public static String mEmail;
    private String mMobile;
    private String mHome;
    private String mPasswordEntry;
    private HttpURLConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delegate_registration);
        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mEmailAddress = (EditText) findViewById(R.id.email);
        mMobileNumber = (EditText) findViewById(R.id.mobile_number);
        mHomeNumber = (EditText) findViewById(R.id.home_number);
        mPassword = (EditText) findViewById(R.id.password);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(validateEditText());
                if (!validateEditText()) {
                    Toast.makeText(getApplicationContext(), "invalid credentials",
                            Toast.LENGTH_SHORT).show();
                } else {
                    new RegistrationTask().execute();
                }

            }
        });
//
        mFirstName.setText(LoginActivity.first_name);
        mLastName.setText(LoginActivity.last_name);
        mEmailAddress.setText(LoginActivity.email);

        mFname = LoginActivity.first_name;
        mLname = LoginActivity.last_name;
        mEmail = LoginActivity.email;
    }

    private boolean validateEditText() {

        boolean valid = true;
        mFname = mFirstName.getText().toString();
        mLname = mLastName.getText().toString();
        mMobile = mMobileNumber.getText().toString();
        mHome = mHomeNumber.getText().toString();
        mPasswordEntry = mPassword.getText().toString();
        mEmail = mEmailAddress.getText().toString();

        System.out.println(mFname);
        System.out.println(mLname);
        System.out.println(mEmail);
        System.out.println(mMobile);
        System.out.println(mHome);
        System.out.println(mPasswordEntry);

        if (mFname.trim().isEmpty() || mFname.length() < 3) {
            mFirstName.setError("enter at least 3 characters");
            valid = false;
        } else {
            mFirstName.setError(null);
        }

        if (mLname.trim().isEmpty() || mLname.length() < 3) {
            mLastName.setError("enter at least 3 characters");
            valid = false;
        } else {
            mLastName.setError(null);
        }

        if (mPasswordEntry.trim().isEmpty() || mPasswordEntry.length() < 3) {
            mPassword.setError("enter at least 3 characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (mMobile.trim().isEmpty() || mMobile.length() < 3) {
            mMobileNumber.setError("Enter Mobile Number");
            valid = false;
        } else {
            mMobileNumber.setError(null);
        }

        if (mHome.trim().isEmpty() || mHome.length() < 3) {
            mHomeNumber.setError("Enter Home Number");
            valid = false;
        } else {
            mHomeNumber.setError(null);
        }

        if (mEmail.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            mEmailAddress.setError("please provide a valid email");
            valid = false;
        } else {
            mEmailAddress.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    class RegistrationTask extends AsyncTask<String, String, String> {

        private boolean noInternet = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebServiceHelper.showProgressDialog(RegistrationActivity.this, "Registering");
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObject;
            if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {
                try {
                    jsonObject = WebServiceHelper.registerUser(mFname, mLname, mEmail, mMobile, mHome
                            , mPasswordEntry);
                    if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                        System.out.println(jsonObject + "working");
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
            } else {
                noInternet = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            WebServiceHelper.dismissProgressDialog();
            if (noInternet) {
                Helpers.alertDialog(RegistrationActivity.this, "Connection error",
                        "Check your internet connection");
            }
            if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                Toast.makeText(AppGlobals.getContext(),
                        "Activation code has been sent to you! Please check your Email",
                        Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(getApplicationContext(), CodeConfirmationActivity.class));
            } else if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                Toast.makeText(AppGlobals.getContext(), "Registration failed. Email already in use",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
