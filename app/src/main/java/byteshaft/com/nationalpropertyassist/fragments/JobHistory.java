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
    private JobHistoryAdapter jobHistoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_job_history, container, false);
        mRecyclerView = (RecyclerView) mBaseView.findViewById(R.id.job_history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.canScrollVertically(1);
        mRecyclerView.setHasFixedSize(true);
        new JobHistoryTask().execute();
        return mBaseView;
    }

    class JobHistoryTask extends AsyncTask<String, String, ArrayList<HashMap>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebServiceHelper.showProgressDialog(getActivity(), "Loading JobHistory" );
        }

        @Override
        protected ArrayList<HashMap> doInBackground(String... params) {
            JSONArray jsonArray = new JSONArray();
            ArrayList<HashMap> arrayList = new ArrayList<>();
            if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {
                try {
                    jsonArray = WebServiceHelper.getJobHistoryData();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = (JSONObject) jsonArray.get(i);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("site", jsonObject.getString("site"));
                            hashMap.put("description", jsonObject.getString("description"));
                            hashMap.put("purpose", jsonObject.getString("purpose"));
                            hashMap.put("paid_for", jsonObject.getString("paid_for"));
                            hashMap.put("address", jsonObject.getString("address"));
                            arrayList.add(hashMap);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap> arrayList) {
            super.onPostExecute(arrayList);
            WebServiceHelper.dismissProgressDialog();
            jobHistoryAdapter = new JobHistoryAdapter(arrayList);
            mRecyclerView.setAdapter(jobHistoryAdapter);
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
            mViewHolder.address.setText(
                    "Address: " + data.get(position).get("address"));
            mViewHolder.description.setText(
                    "Description: " + data.get(position).get("description"));
            mViewHolder.purpose.setText(
                    "Purpose: " + data.get(position).get("purpose"));
            mViewHolder.paidFor.setText
                    ("Paid For: " + data.get(position).get("paid_for"));
            mViewHolder.site.setText
                    ("Site: " + data.get(position).get("site"));

        }

        @Override
        public int getItemCount() {
            return data.size();
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
            site = (TextView) itemView.findViewById(R.id.tv_site);
        }
    }
}
