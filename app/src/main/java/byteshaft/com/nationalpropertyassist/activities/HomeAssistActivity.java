package byteshaft.com.nationalpropertyassist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.utils.ServicesTask;

public class HomeAssistActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup radioGroup;
    private EditText details;
    private Button submitButton;
    private View headerView;
    private TextView headerStart;
    private TextView headerEnd;
    private String mRadioText;
    private RadioButton homeSurvey;
    private RadioButton drainSurvery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home_assist);
        headerView = findViewById(R.id.home_assist_header);
        headerStart = (TextView) headerView.findViewById(R.id.header_start);
        headerEnd = (TextView) headerView.findViewById(R.id.header_end);
        headerStart.setText("Home Buyer");
        headerEnd.setText(" Assist");
        headerStart.setTypeface(AppGlobals.typefaceItalic);
        headerEnd.setTypeface(AppGlobals.typefaceItalic);
        homeSurvey = (RadioButton) findViewById(R.id.home_buyer_survey);
        drainSurvery = (RadioButton) findViewById(R.id.home_buyer_drain_survey);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        details = (EditText) findViewById(R.id.home_assist_et);
        submitButton = (Button) findViewById(R.id.submit);
        radioGroup.setOnCheckedChangeListener(this);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.home_buyer_survey:
                mRadioText = "Home Buyer Survey";
                break;
            case R.id.home_buyer_drain_survey:
                mRadioText = "Pre Purchase Drain Survey";
                break;
        }
        System.out.println(mRadioText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppGlobals.serverIdForProperty != 2112) {
            submitButton.setText("Submit");
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
                } else {
                    String description = details.getText().toString();
                    new ServicesTask(HomeAssistActivity.this, description, mRadioText).execute();
                }
                break;
        }
    }
}
