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

public class MaintenanceActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    private Button submit_button;
    private EditText details;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String mRadioText = "Scale Removal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenance_activity);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        submit_button = (Button) findViewById(R.id.submit);
        details = (EditText) findViewById(R.id.maintenance_et);
        radioGroup.setOnCheckedChangeListener(this);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = details.getText().toString();
                new ServicesTask(MaintenanceActivity.this, description, mRadioText).execute();
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        radioButton = (RadioButton) findViewById(checkedId);
        mRadioText = radioButton.getText().toString();
        System.out.println(mRadioText);
    }
}
