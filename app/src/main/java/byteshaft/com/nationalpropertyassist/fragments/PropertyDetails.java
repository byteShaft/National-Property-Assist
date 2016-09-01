package byteshaft.com.nationalpropertyassist.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.activities.AddPropertyDetails;
import byteshaft.com.nationalpropertyassist.database.AddPropertyDetailsDatabase;
import byteshaft.com.nationalpropertyassist.utils.Helpers;
import byteshaft.com.nationalpropertyassist.utils.WebServiceHelper;


public class PropertyDetails extends android.support.v4.app.Fragment {

    public View mBaseView;
    private CustomView mViewHolder;
    private RecyclerView mRecyclerView;
    private PropertyDetailsAdapter mDetailsAdapter;
    private AddPropertyDetailsDatabase database;
    private ArrayList<HashMap> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_property_details, container, false);
        database = new AddPropertyDetailsDatabase(getActivity().getApplicationContext());
        mRecyclerView = (RecyclerView) mBaseView.findViewById(R.id.property_details_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.canScrollVertically(1);
        mRecyclerView.setHasFixedSize(true);
        return mBaseView;
    }

    @Override
    public void onResume() {
        super.onResume();
        data = database.getAllRecords();
        Log.i("TAG", String.valueOf(data));
        mDetailsAdapter = new PropertyDetailsAdapter(data);
        mRecyclerView.setAdapter(mDetailsAdapter);
        mRecyclerView.addOnItemTouchListener(new PropertyDetailsAdapter(data
                , getActivity().getApplicationContext(), new OnItemClickListener() {
            @Override
            public void onItem(int id) {
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        AddPropertyDetails.class);
                intent.putExtra(AppGlobals.PROPERTY_ID, id);
                startActivity(intent);
            }
        }));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            System.out.println("add item");
            startActivity(new Intent(getActivity(), AddPropertyDetails.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class PropertyDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
            RecyclerView.OnItemTouchListener {

        private ArrayList<HashMap> data;
        private OnItemClickListener mListener;
        private GestureDetector mGestureDetector;

        public PropertyDetailsAdapter(ArrayList<HashMap> data) {
            this.data = data;

        }


        public PropertyDetailsAdapter(final ArrayList<HashMap> data, Context context,
                                      OnItemClickListener listener) {
            this.data = data;
            mListener = listener;
            mGestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            super.onLongPress(e);
                            System.out.println("Long press detected");
                            final View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setMessage("Do you want to delete this property?");
                            alertDialogBuilder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            String[] keys = {(String)
                                                    data.get(mRecyclerView.getChildPosition(childView))
                                                            .get("property_id"), (String)
                                                    data.get(mRecyclerView.getChildPosition(childView))
                                                            .get("unique_id"), String.valueOf(mRecyclerView
                                                    .getChildPosition(childView))};
                                            new DeletePropertyTask().execute(keys);
//                                            database.deleteEntry((Integer.valueOf((String)
//                                                    data.get(mRecyclerView.getChildPosition(childView))
//                                                    .get("property_id"))));
//                                            data.remove(mRecyclerView
//                                                    .getChildPosition(childView));
//                                            mRecyclerView.getAdapter().notifyDataSetChanged();
                                        }
                                    });

                            alertDialogBuilder.setNegativeButton("cancel",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                        }
                                    });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    });

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
            holder.setIsRecyclable(false);
            String address = (String) data.get(position).get("address");
            String upperCaseString = address.substring(0, 1).toUpperCase() + address.substring(1);
            mViewHolder.address.setText(
                    "Address: " + upperCaseString);
            mViewHolder.ageOfProperty.setText(
                    "Age of Property: " + String.valueOf(data.get(position)
                            .get("property_age")) + " months");
            mViewHolder.typeOfProperty.setText(
                    "Type of Property: " + String.valueOf(data
                            .get(position).get("property_type")));
            mViewHolder.postCode.setText
                    ("Postal code: " + String.valueOf(data
                            .get(position).get("postal_code")));
            if (data.get(position).get("commercial").equals("0")) {
                mViewHolder.residential.setText
                        ("Residential/Commercial: " + "Residential");
            } else {
                mViewHolder.residential.setText
                        ("Residential/Commercial: " + "Commercial");
            }
//            mViewHolder.address.setTypeface(AppGlobals.typefaceItalic);
//            mViewHolder.postCode.setTypeface(AppGlobals.typefaceItalic);
//            mViewHolder.residential.setTypeface(AppGlobals.typefaceItalic);
//            mViewHolder.typeOfProperty.setTypeface(AppGlobals.typefaceItalic);
//            mViewHolder.ageOfProperty.setTypeface(AppGlobals.typefaceItalic);

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                Log.i("TAG", String.valueOf(mListener == null));
                mListener.onItem(Integer.valueOf((String) data.get(rv.getChildPosition(childView))
                        .get("property_id")));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    // custom class getting view  by giving view in constructor.
    public class CustomView extends RecyclerView.ViewHolder {

        private TextView address;
        private TextView postCode;
        private TextView residential;
        private TextView typeOfProperty;
        private TextView ageOfProperty;

        public CustomView(View itemView) {
            super(itemView);
            address = (TextView) itemView.findViewById(R.id.tv_address);
            address.setTypeface(AppGlobals.typeface);
            postCode = (TextView) itemView.findViewById(R.id.tv_postcode);
            postCode.setTypeface(AppGlobals.typeface);
            residential = (TextView) itemView.findViewById(R.id.tv_residential);
            residential.setTypeface(AppGlobals.typeface);
            typeOfProperty = (TextView) itemView.findViewById(R.id.tv_type_of_property);
            typeOfProperty.setTypeface(AppGlobals.typeface);
            ageOfProperty = (TextView) itemView.findViewById(R.id.tv_age_of_property);
            ageOfProperty.setTypeface(AppGlobals.typeface);
        }
    }

    public interface OnItemClickListener {
        void onItem(int id);
    }

    class DeletePropertyTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebServiceHelper.showProgressDialog(getActivity(), "Deleting Property");
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String url = "http://178.62.37.43:8000/api/properties/"+strings[0];
            HttpURLConnection connection;
            try {
                connection = WebServiceHelper.openConnectionForUrl(url, "DELETE");
                connection.setRequestProperty("Authorization", "Token " + Helpers.getStringFromSharedPreferences("token"));
                if (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
                    database.deleteEntry(Integer.valueOf(strings[1]));
                    int index = Integer.parseInt(strings[2]);
                    data.remove(index);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            WebServiceHelper.dismissProgressDialog();
            if (aBoolean) {
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
