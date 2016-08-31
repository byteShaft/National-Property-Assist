package byteshaft.com.nationalpropertyassist.drainassist;

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

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.activities.SelectPropertyActivity;
import byteshaft.com.nationalpropertyassist.utils.ServicesTask;

public class SepticOrTreatmentTankActivity extends Activity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {

    private Button button_submit;
    private RadioGroup radioGroup;
    private EditText details;
    private RadioButton radioButton;
    private String details_text;
    private String mRadioText;
    private static boolean sConfirmPayment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.septic_or_treatment_tank_activity);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        button_submit = (Button) findViewById(R.id.submit);
        details = (EditText) findViewById(R.id.septic_or_treatment_tank_et);
        radioGroup.setOnCheckedChangeListener(this);
        details_text = details.getText().toString();
        button_submit.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        radioButton = (RadioButton) findViewById(checkedId);
        mRadioText = radioButton.getText().toString();
        System.out.println(mRadioText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppGlobals.serverIdForProperty != 2112) {
            button_submit.setText("Submit");
        } else if (AppGlobals.serverIdForProperty != 2112 && !sConfirmPayment){
            button_submit.setText("Confirm");
        } else {
            button_submit.setText("Select Property");
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SepticOrTreatmentTankActivity.this);
                    alertDialogBuilder.setTitle("Payment Details");
                    final String price = AppGlobals.getPriceDetails(mRadioText);
                    if (isNumeric(price)) {
                        alertDialogBuilder.setMessage(
                                String.format("You will be charged (%d£) for this service.",
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
                                    new ServicesTask(SepticOrTreatmentTankActivity.this, description,
                                            mRadioText, price).execute();
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
