package byteshaft.com.nationalpropertyassist.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.utils.ServicesTask;


public class BuildingAssistActivity extends Activity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {

    private EditText details;
    private Button submitButton;
    private RadioGroup radioGroup;
    private String mRadioText;

    /// radio buttons
    private RadioButton buildingSurvey;
    private RadioButton structuralSurvey;
    private RadioButton damageRepair;
    private RadioButton insuranceClaimSurvery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_building_assist);

        buildingSurvey = (RadioButton) findViewById(R.id.building_survey);
        structuralSurvey = (RadioButton) findViewById(R.id.structural_survey);
        damageRepair = (RadioButton) findViewById(R.id.damage_repair_survey);
        insuranceClaimSurvery = (RadioButton) findViewById(R.id.insurance_claim_survey);

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

            case R.id.structural_survey:
                mRadioText = "Structural Survey";
                break;

            case R.id.damage_repair_survey:
                mRadioText = "Damage and Repair Survey";
                break;

            case R.id.insurance_claim_survey:
                mRadioText = "Insurance Claim Survey";
                break;
        }
        System.out.println(mRadioText);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                String description = details.getText().toString();
                new ServicesTask(BuildingAssistActivity.this, description, mRadioText).execute();
                break;
        }
    }
}
