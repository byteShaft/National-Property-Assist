package byteshaft.com.nationalpropertyassist.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.database.AddPropertyDetailsDatabase;
import byteshaft.com.nationalpropertyassist.utils.Helpers;
import byteshaft.com.nationalpropertyassist.utils.WebServiceHelper;

public class AddPropertyDetails extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private EditText mAddress;
    private EditText mPostCode;
    private Spinner mResidential;
    private Spinner mTypeOfProperty;
    private EditText mAgeOfProperty;
    private Button mSaveButton;
    private String mAddressString;
    private String mPostCodeString;
    private String mResidentialString;
    private String mTypeOfPropertyString;
    private String mAgeOfPropertyString;
    private AddPropertyDetailsDatabase addPropertyDetailsDatabase;
    private int propertyId = 0;
    private boolean mUpdate = false;
    private int mDataBaseId = 0;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property_detials);
        mAddress = (EditText) findViewById(R.id.et_address);
        mPostCode = (EditText) findViewById(R.id.et_post_code);
        mResidential = (Spinner) findViewById(R.id.et_residential);
        mResidential.setOnItemSelectedListener(this);
        mTypeOfProperty = (Spinner) findViewById(R.id.property_type);
        mAgeOfProperty = (EditText) findViewById(R.id.et_age_of_property);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(this);
        addPropertyDetailsDatabase = new AddPropertyDetailsDatabase(getApplicationContext());
        if (getIntent() != null) {
            propertyId = getIntent().getIntExtra(AppGlobals.PROPERTY_ID, 0);
            if (propertyId != 0) {
                mUpdate = true;
                HashMap<String, String> property = addPropertyDetailsDatabase.getPropertyById(propertyId);
                mAddress.setText(property.get("address"));
                mPostCode.setText(property.get("postal_code"));
                if (property.get("commercial").equals("0")) {
                    mResidential.setSelection(0);
                } else {
                    mResidential.setSelection(1);
                }
                List arrayList;
                arrayList = Arrays.asList(getResources().getStringArray(R.array.types_of_property));
                Log.i("TAG", String.valueOf(arrayList));
                int index = arrayList.indexOf(property.get("property_type"));

                mTypeOfProperty.setSelection(index);
                mAgeOfProperty.setText(property.get("property_age"));
                mDataBaseId = Integer.parseInt(property.get("unique_id"));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
                if (!validateEditText()) {
                    Toast.makeText(getApplicationContext(), "invalid credentials",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (mUpdate) {
                        new PropertyDetailsTask().execute("PUT");
                    } else {
                        new PropertyDetailsTask().execute("POST");
                    }
                }
        }
    }

    private boolean validateEditText() {
        boolean valid = true;
        mAddressString = mAddress.getText().toString();
        mPostCodeString = mPostCode.getText().toString();
//        mResidentialString = mResidential.getText().toString();
        mTypeOfPropertyString = mTypeOfProperty.getSelectedItem().toString();
        mAgeOfPropertyString = mAgeOfProperty.getText().toString();

        if (mAddressString.trim().isEmpty()) {
            mAddress.setError("please enter your address");
            valid = false;
        } else {
            mAddress.setError(null);
        }

        if (mPostCodeString.trim().isEmpty()) {
            mPostCode.setError("please enter your postCode");
            valid = false;
        } else {
            mPostCode.setError(null);
        }

//        if (mTypeOfPropertyString.trim().isEmpty()) {
//            mTypeOfProperty.setError("please enter property type");
//            valid = false;
//        } else {
//            mTypeOfProperty.setError(null);
//        }

        if (mAgeOfPropertyString.trim().isEmpty()) {
            mAgeOfProperty.setError("please enter age of property");
            valid = false;
        } else {
            mAgeOfProperty.setError(null);
        }
        return valid;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("TAG", "Item "+ adapterView.getItemAtPosition(i));
        if (adapterView.getItemAtPosition(i).equals("Residential")) {
            mResidentialString = "0";
        } else {
            mResidentialString = "1";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mResidentialString = "0";

    }

    class PropertyDetailsTask extends AsyncTask<String, String, JSONObject> {

        private boolean noInternet = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mUpdate) {
                WebServiceHelper.showProgressDialog(AddPropertyDetails.this, "Updating property details");
            } else {
                WebServiceHelper.showProgressDialog(AddPropertyDetails.this, "Saving property details");
            }
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = new JSONObject();
            if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {
                String url = "http://178.62.37.43:8000/api/properties";
                if (mUpdate) {
                    url = url + "/" + propertyId;
                }
                try {
                    jsonObject = WebServiceHelper.addPropertyDetails(url,
                            mAddressString,
                            mAgeOfPropertyString,
                            mPostCodeString,
                            Integer.valueOf(mResidentialString),
                            mTypeOfPropertyString, params[0]);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } else {
                noInternet = true;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            WebServiceHelper.dismissProgressDialog();
            if (noInternet) {
                Helpers.alertDialog(AddPropertyDetails.this, "Connection error",
                        "Check your internet connection");
            } else if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                String addpropertyId = null;
                try {
                    addpropertyId = jsonObject.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addPropertyDetailsDatabase.createNewEntry(
                        mAddressString,
                        mPostCodeString,
                        Integer.valueOf(mResidentialString),
                        mTypeOfPropertyString,
                        mAgeOfPropertyString,
                        Integer.valueOf(addpropertyId));

                Log.e("TAg", "name " + mAddressString);
                finish();
            }  else if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (mUpdate) {
                    try {
                        addPropertyDetailsDatabase.updateEntries(mDataBaseId,
                                mAddressString,
                                mPostCodeString,
                                Integer.valueOf(mResidentialString),
                                mTypeOfPropertyString,
                                mAgeOfPropertyString,
                                jsonObject.getInt("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("TAg", "name " + mAddressString);
                    Toast.makeText(AddPropertyDetails.this, "Updated", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        }
    }
}
