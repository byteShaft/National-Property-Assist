package byteshaft.com.nationalpropertyassist.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.utils.ServicesTask;


public class BuildingAssistActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private EditText details;
    private Button submitButton;
    private RadioGroup radioGroup;
    private String mRadioText;
    private View headerView;
    private TextView headerStart;
    private TextView headerEnd;
    private static boolean sConfirmPayment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_building_assist);
        headerView = findViewById(R.id.building_assist_header);
        headerStart = (TextView) headerView.findViewById(R.id.header_start);
        headerEnd = (TextView) headerView.findViewById(R.id.header_end);
        headerStart.setText("Building");
        headerEnd.setText(" Assist");
        headerStart.setTypeface(AppGlobals.typefaceItalic);
        headerEnd.setTypeface(AppGlobals.typefaceItalic);
        details = (EditText) findViewById(R.id.building_assist_et);
        submitButton = (Button) findViewById(R.id.submit);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        submitButton.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.building_survey:
                mRadioText = "Building Survey";
                break;

            case R.id.repair_survey:
                mRadioText = "Repair Survey";
                break;

            case R.id.insurance_survey:
                mRadioText = "Insurance Survey";
                break;
        }
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BuildingAssistActivity.this);
                    alertDialogBuilder.setTitle("Payment Details");
                    alertDialogBuilder.setMessage(String.format("You will be charged %d for this services press ok to confirm.", 20)).setCancelable(false).setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {
                    String description = details.getText().toString();
                    new ServicesTask(BuildingAssistActivity.this, description, mRadioText).execute();

                }
                break;
        }
    }
}