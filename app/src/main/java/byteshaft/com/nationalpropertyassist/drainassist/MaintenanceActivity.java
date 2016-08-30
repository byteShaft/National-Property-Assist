package byteshaft.com.nationalpropertyassist.drainassist;

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
import byteshaft.com.nationalpropertyassist.activities.SelectPropertyActivity;
import byteshaft.com.nationalpropertyassist.utils.ServicesTask;

public class MaintenanceActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    private Button submit_button;
    private EditText details;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String mRadioText = "Scale Removal";
    private View headerView;
    private TextView headerStart;
    private TextView headerEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenance_activity);
        headerView = findViewById(R.id.maintenance_header);
        headerStart = (TextView) headerView.findViewById(R.id.header_start);
        headerEnd = (TextView) headerView.findViewById(R.id.header_end);
        headerStart.setText("Maintenance");
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        submit_button = (Button) findViewById(R.id.submit);
        details = (EditText) findViewById(R.id.maintenance_et);
        radioGroup.setOnCheckedChangeListener(this);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppGlobals.serverIdForProperty == 2112) {
                    Intent intent = new Intent(getApplicationContext(), SelectPropertyActivity.class);
                    startActivity(intent);
                } else {
                    String description = details.getText().toString();
                    new ServicesTask(MaintenanceActivity.this, description, mRadioText).execute();
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        radioButton = (RadioButton) findViewById(checkedId);
        mRadioText = radioButton.getText().toString();
        System.out.println(mRadioText);
    }

    protected void onResume() {
        super.onResume();
        if (AppGlobals.serverIdForProperty != 2112) {
            submit_button.setText("Submit");
        } else {
            submit_button.setText("Select Property");
        }
    }
}
