package byteshaft.com.nationalpropertyassist.drainassist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.activities.SelectPropertyActivity;
import byteshaft.com.nationalpropertyassist.utils.ServicesTask;

public class MaintenanceActivity extends Activity {

    private Button submit_button;
    private EditText details;
    private String mRadioText = "Scale Removal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenance_activity);
        submit_button = (Button) findViewById(R.id.submit);
        details = (EditText) findViewById(R.id.maintenance_et);
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
    protected void onResume() {
        super.onResume();
        if (AppGlobals.serverIdForProperty != 2112) {
            submit_button.setText("Submit");
        } else {
            submit_button.setText("Select Property");
        }
    }
}
