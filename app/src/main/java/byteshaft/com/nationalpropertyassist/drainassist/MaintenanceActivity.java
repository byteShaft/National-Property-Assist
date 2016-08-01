package byteshaft.com.nationalpropertyassist.drainassist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.utils.ServicesTask;

public class MaintenanceActivity extends Activity {

    private Button submit_button;
    private EditText details;
    private String mRadioText = "Scale Removal";
    private String mRecoveryEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenance_activity);
        mRecoveryEmail = getString(R.string.email_string);
        submit_button = (Button) findViewById(R.id.submit);
        details = (EditText) findViewById(R.id.maintenance_et);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = details.getText().toString();
                new ServicesTask(MaintenanceActivity.this, description, mRadioText).execute();
            }
        });
    }
}
