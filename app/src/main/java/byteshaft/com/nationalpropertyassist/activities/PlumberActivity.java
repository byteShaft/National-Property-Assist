package byteshaft.com.nationalpropertyassist.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.utils.ServicesTask;

public class PlumberActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {


    private RadioGroup radioGroup;
    private EditText details;
    private Button submitButton;
    private String mRadioText;

    private RadioButton repairPlumbing;
    private RadioButton renewal;
    private View headerView;
    private TextView headerStart;
    private TextView headerEnd;
    private static boolean sConfirmPayment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_plumber_assist);
        headerView = findViewById(R.id.plumber_assist_header);
        headerStart = (TextView) headerView.findViewById(R.id.header_start);
        headerEnd = (TextView) headerView.findViewById(R.id.header_end);
        headerStart.setText("Plumber");
        headerEnd.setText(" Assist");
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        details = (EditText) findViewById(R.id.plumber_assist_et);
        repairPlumbing = (RadioButton) findViewById(R.id.repair_plumbing);
        renewal = (RadioButton) findViewById(R.id.renewal_of_plumbing_installation);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        details = (EditText) findViewById(R.id.plumber_assist_et);
        submitButton = (Button) findViewById(R.id.submit);
        radioGroup.setOnCheckedChangeListener(this);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.repair_plumbing:
                mRadioText = "Repair to leaking plumbing Installation";
                break;
            case R.id.renewal_of_plumbing_installation:
                mRadioText = "Renewal of plumbing Installation";
                break;
        }

        System.out.println(mRadioText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppGlobals.serverIdForProperty != 2112) {
            submitButton.setText("Submit");
        } else if (AppGlobals.serverIdForProperty != 2112 && !sConfirmPayment){
            submitButton.setText("Confirm");
        } else {
            submitButton.setText("Select Property");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (AppGlobals.serverIdForProperty == 2112) {
                    Intent intent = new Intent(getApplicationContext(), SelectPropertyActivity.class);
                    startActivity(intent);
                } else if (AppGlobals.serverIdForProperty != 2112 && !sConfirmPayment) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlumberActivity.this);
                    alertDialogBuilder.setTitle("Payment Details");
                    String price = AppGlobals.getPriceDetails(mRadioText);
                    if (isNumeric(price)) {
                        alertDialogBuilder.setMessage(
                                String.format("You will be charged (%d£) for this services press ok to confirm.",
                                        Integer.valueOf(price)));
                    } else {
                        alertDialogBuilder.setMessage(
                                String.format("For these services %s.",
                                        price));
                    }
                    System.out.println(price);
                    alertDialogBuilder.setCancelable(false).setPositiveButton("Submit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    String description = details.getText().toString();
                                    new ServicesTask(PlumberActivity.this, description, mRadioText).execute();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
                break;
        }
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
