package byteshaft.com.nationalpropertyassist.drainassist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.utils.ServicesTask;

public class SepticOrTreatmentTankActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    private Button button_submit;
    private RadioGroup radioGroup;
    private EditText details;
    private RadioButton radioButton;
    private String details_text;
    private String mReceverEmail;
    private String mRadioText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.septic_or_treatment_tank_activity);
        mReceverEmail = getString(R.string.email_string);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        button_submit = (Button) findViewById(R.id.submit);
        details = (EditText) findViewById(R.id.septic_or_treatment_tank_et);
        radioGroup.setOnCheckedChangeListener(this);

        details_text = details.getText().toString();

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = details.getText().toString();
                new ServicesTask(SepticOrTreatmentTankActivity.this, description, mRadioText).execute();
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        radioButton = (RadioButton) findViewById(checkedId);
        mRadioText = radioButton.getText().toString();
        System.out.println(mRadioText);
    }
}
