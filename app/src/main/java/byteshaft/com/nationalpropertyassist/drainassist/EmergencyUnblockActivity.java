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
import android.widget.TextView;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.activities.SelectPropertyActivity;
import byteshaft.com.nationalpropertyassist.utils.ServicesTask;

public class EmergencyUnblockActivity extends Activity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {

    private Button button_submit;
    private RadioGroup radioGroup;
    private EditText details;
    private RadioButton radioButton;
    private String mRadioText;
    private View headerView;
    private TextView headerStart;
    private TextView headerEnd;
    private static boolean sConfirmPayment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_unblock_activity);
        headerView = findViewById(R.id.emergency_header);
        headerStart = (TextView) headerView.findViewById(R.id.header_start);
        headerEnd = (TextView) headerView.findViewById(R.id.header_end);
        headerStart.setText("Emergency");
        headerEnd.setText(" Unblock");
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        button_submit = (Button) findViewById(R.id.submit);
        details = (EditText) findViewById(R.id.emergency_unblock_et);
        radioGroup.setOnCheckedChangeListener(this);

        button_submit.setOnClickListener(this);
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EmergencyUnblockActivity.this);
                    alertDialogBuilder.setTitle("Payment Details");
                    String price = AppGlobals.getPriceDetails(mRadioText);
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
                                    new ServicesTask(EmergencyUnblockActivity.this, description, mRadioText).execute();
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


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        radioButton = (RadioButton) findViewById(checkedId);
        mRadioText = radioButton.getText().toString();
        System.out.println(mRadioText);
    }
}
