package byteshaft.com.nationalpropertyassist.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.activities.AddPropertyDetails;
import byteshaft.com.nationalpropertyassist.database.AddPropertyDetailsDatabase;


public class PropertyDetails extends android.support.v4.app.Fragment {

    public View mBaseView;
    private CustomView mViewHolder;
    private RecyclerView mRecyclerView;
    private PropertyDetailsAdapter mDetailsAdapter;
    private AddPropertyDetailsDatabase database;

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
        mDetailsAdapter = new PropertyDetailsAdapter(database.getAllRecords());
        mRecyclerView.setAdapter(mDetailsAdapter);
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

    class PropertyDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<HashMap> data;

        public PropertyDetailsAdapter(ArrayList<HashMap> data) {
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
            holder.setIsRecyclable(false);
            mViewHolder.address.setText(
                    "Address: " + data.get(position).get("address"));
            mViewHolder.ageOfProperty.setText(
                    "Age of Property: " + data.get(position).get("property_age"));
            mViewHolder.typeOfProperty.setText(
                    "Type of Property: " + data.get(position).get("property_type"));
            mViewHolder.postCode.setText
                    ("Postal code: " + data.get(position).get("postal_code"));
            if (data.get(position).get("commercial").equals("0")) {
                mViewHolder.residential.setText
                        ("Residential/Commercial: " + "Residential");
            } else {
                mViewHolder.residential.setText
                        ("Residential/Commercial: " +"Commercial");
            }

        }

        @Override
        public int getItemCount() {
            return data.size();
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
}
