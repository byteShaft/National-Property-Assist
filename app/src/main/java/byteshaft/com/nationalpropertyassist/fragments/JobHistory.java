package byteshaft.com.nationalpropertyassist.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.utils.WebServiceHelper;

public class JobHistory extends android.support.v4.app.Fragment {


    public View mBaseView;
    private CustomView mViewHolder;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_job_history, container, false);
        mRecyclerView = (RecyclerView) mBaseView.findViewById(R.id.job_history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.canScrollVertically(1);
        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setAdapter(mDetailsAdapter);
        new JobHistoryTask().execute();
        return mBaseView;
    }

    class JobHistoryTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebServiceHelper.showProgressDialog(getActivity(), "Loading JobHistory" );
        }

        @Override
        protected String doInBackground(String... params) {
            JSONArray jsonArray = new JSONArray();
            if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {
                try {
                    jsonArray = WebServiceHelper.getJobHistoryData();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    System.out.println(jsonArray);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            WebServiceHelper.dismissProgressDialog();
        }
    }

    class JobHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<HashMap> data;

        public JobHistoryAdapter(ArrayList<HashMap> data) {
            this.data = data;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            Log.i("TAG", "loading one");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_delegate,
                    parent, false);
            mViewHolder = new CustomView(view);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    public class CustomView extends RecyclerView.ViewHolder {

        private TextView address;
        private TextView description;
        private TextView purpose;
        private TextView paidFor;
        private TextView site;

        public CustomView(View itemView) {
            super(itemView);
            address = (TextView) itemView.findViewById(R.id.tv_address);
            description = (TextView) itemView.findViewById(R.id.tv_description);
            purpose = (TextView) itemView.findViewById(R.id.tv_purpose);
            paidFor = (TextView) itemView.findViewById(R.id.tv_paid_for);
            site = (TextView) itemView.findViewById(R.id.tv_paid_for);
        }
    }
}
