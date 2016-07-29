package byteshaft.com.nationalpropertyassist.utils;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ServicesTask extends AsyncTask<String,String,String> {

    Activity mActivity;
    String mDescription;
    String mPurpose;

    public ServicesTask(Activity activity, String description, String purpose) {
        mActivity = activity;
        mDescription = description;
        mPurpose = purpose;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        WebServiceHelper.showProgressDialog(mActivity, "Please wait");
    }

    @Override
    protected String doInBackground(String... strings) {
        JSONObject jsonObject = null;
        if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {
            try {
                jsonObject = WebServiceHelper.addServices(mDescription, mPurpose);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            System.out.println(jsonObject);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        WebServiceHelper.dismissProgressDialog();
    }
}
