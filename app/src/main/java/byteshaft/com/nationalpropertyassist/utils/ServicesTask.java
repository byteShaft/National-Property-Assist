package byteshaft.com.nationalpropertyassist.utils;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;

import byteshaft.com.nationalpropertyassist.AppGlobals;

public class ServicesTask extends AsyncTask<String, String, Integer> {

    private Activity mActivity;
    private String mDescription;
    private String mPurpose;
    private String price;

    public ServicesTask(Activity activity, String description, String purpose, String price) {
        mActivity = activity;
        mDescription = description;
        mPurpose = purpose;
        this.price = price;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        WebServiceHelper.showProgressDialog(mActivity, "Sending assist request \n please wait..");
    }

    @Override
    protected Integer doInBackground(String... strings) {
        int response = 0;
        if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {
            try {
                response = WebServiceHelper.addServices(mDescription, mPurpose, price);
                mActivity.finish();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(Integer response) {
        super.onPostExecute(response);
        AppGlobals.serverIdForProperty = 2112;
        if (response == HttpURLConnection.HTTP_CREATED) {
            WebServiceHelper.dismissProgressDialog();
        }
    }
}
