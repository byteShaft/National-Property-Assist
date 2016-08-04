package byteshaft.com.nationalpropertyassist.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.MainActivity;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.utils.Helpers;
import byteshaft.com.nationalpropertyassist.utils.WebServiceHelper;


public class CurrentJobs extends Fragment {

    private View mBaseView;
    private String resultString = "";
    private ArrayList<HashMap> activeJobs;
    public RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static CustomView viewHolder;
    private CustomAdapter mAdapter;
    private RelativeLayout notFoundLayout;
    public OnItemClickListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_current_job, container, false);
        notFoundLayout = (RelativeLayout) mBaseView.findViewById(R.id.not_found_layout);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()
                .getApplicationContext());
        mRecyclerView = (RecyclerView) mBaseView.findViewById(R.id.active_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mBaseView.findViewById(R.id.active_job_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green,
                R.color.light_blue, R.color.gray);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.canScrollVertically(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetActiveTask().execute();
            }
        });
        new GetActiveTask().execute();
        return mBaseView;
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.sProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    class GetActiveTask extends AsyncTask<String, String, ArrayList<HashMap>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.sProgressBar.setVisibility(View.VISIBLE);
            notFoundLayout.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<HashMap> doInBackground(String... strings) {
            if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {
                try {
                    URL url = new URL(AppGlobals.GET_ACTIVE_JOBS_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Authorization", "Token " +
                            Helpers.getStringFromSharedPreferences("token"));
                    InputStream inputStream = httpURLConnection.getInputStream();
                    resultString = WebServiceHelper.convertInputStreamToString(inputStream);
                    JSONArray jsonArray = new JSONArray(resultString);
                    activeJobs = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", jsonObject.getString("id"));
                        hashMap.put("site", jsonObject.getString("site"));
                        hashMap.put("status", jsonObject.getString("status"));
                        hashMap.put("description", jsonObject.getString("description"));
                        hashMap.put("purpose", jsonObject.getString("purpose"));
                        hashMap.put("paid_for", jsonObject.getString("paid_for"));
                        hashMap.put("address", jsonObject.getString("address"));
                        activeJobs.add(hashMap);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            return activeJobs;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap> arrayList) {
            super.onPostExecute(arrayList);
            MainActivity.sProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            if (arrayList == null) {
                    notFoundLayout.setVisibility(View.VISIBLE);
            } else {
                if (!arrayList.isEmpty()) {
                    CustomAdapter customAdapter = new CustomAdapter(arrayList);
                    mRecyclerView.setAdapter(customAdapter);
                    mRecyclerView.addOnItemTouchListener(new CustomAdapter(arrayList,
                            AppGlobals.getContext(), new OnItemClickListener() {
                        @Override
                        public void onPayClick(String item) {
                            Log.i("TAG", "pay click" + item);
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setTitle("Payment");
                            alertDialogBuilder.setMessage("Do you want to pay via Paypal/Credit card")
                                    .setCancelable(false).setPositiveButton("Proceed",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            onBuyPressed();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }));
                }
            }
        }
    }

    class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
            RecyclerView.OnItemTouchListener {

        private ArrayList<HashMap> items;
        private GestureDetector mGestureDetector;

        public CustomAdapter(ArrayList<HashMap> categories, Context context, OnItemClickListener listener) {
            this.items = categories;
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        public CustomAdapter(ArrayList<HashMap> categories) {
            this.items = categories;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.active_job_delegate, parent, false);
            viewHolder = new CustomView(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            viewHolder.idTextView.setText(String.valueOf(items.get(position).get("id")));
            viewHolder.addressTextView.setText((CharSequence) items.get(position).get("address"));
            viewHolder.description.setText((CharSequence) items.get(position).get("description"));
            viewHolder.purpose.setText((CharSequence) items.get(position).get("purpose"));
            if ((String.valueOf(items.get(position).get("paid_for"))).equals("true")) {
                viewHolder.payButton.setBackground(AppGlobals.getContext().getResources()
                        .getDrawable(R.drawable.paid));
                viewHolder.payTextView.setText("Paid");
            } else {
                viewHolder.payButton.setBackground(AppGlobals.getContext().getResources()
                        .getDrawable(R.drawable.unpaid));
                viewHolder.payTextView.setText("unPaid");
            }
            viewHolder.payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(mListener == null);
                    System.out.println(items == null);
                    mListener.onPayClick((String) items.get(position).get("id"));
                }
            });

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
//                mListener.onItem(items.get(rv.getChildPosition(childView)), (TextView)
//                        rv.findViewHolderForAdapterPosition(rv.getChildPosition(childView)).
//                                itemView.findViewById(R.id.specific_category_title));
//                return true;
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

    public interface OnItemClickListener {
        void onPayClick(String item);
    }

    // custom viewHolder to access xml elements requires a view in constructor
    public static class CustomView extends RecyclerView.ViewHolder {
        public TextView idTextView;
        public TextView addressTextView;
        public Button payButton;
        public TextView payTextView;
        public TextView description;
        public TextView site;
        public TextView purpose;

        public CustomView(View itemView) {
            super(itemView);
            idTextView = (TextView) itemView.findViewById(R.id.hidden_id);
            addressTextView = (TextView) itemView.findViewById(R.id.active_address);
            payButton = (Button) itemView.findViewById(R.id.active_pay_button);
            payTextView = (TextView) itemView.findViewById(R.id.active_pay);
            description = (TextView) itemView.findViewById(R.id.active_description);
            site = (TextView) itemView.findViewById(R.id.active_site);
            purpose = (TextView) itemView.findViewById(R.id.active_purpose);
        }
    }

    // paypal part

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("2.00"), "USD", "sample item",
                paymentIntent);
    }

    public void onBuyPressed() {
        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(getActivity(), PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, AppGlobals.CONFIG);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, AppGlobals.REQUEST_CODE_PAYMENT);
    }

}
