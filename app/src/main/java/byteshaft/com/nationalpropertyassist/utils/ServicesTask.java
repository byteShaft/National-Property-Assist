package byteshaft.com.nationalpropertyassist.utils;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ServicesTask extends AsyncTask<String, String, Integer> {

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
        WebServiceHelper.showProgressDialog(mActivity, "Sending assist request \n please wait..");
    }

    @Override
    protected Integer doInBackground(String... strings) {
        int response = 0;
        if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {
            try {
                response = WebServiceHelper.addServices(mDescription, mPurpose, mActivity);
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
        if (response == HttpURLConnection.HTTP_CREATED) {
            WebServiceHelper.dismissProgressDialog();
        }
    }
}
