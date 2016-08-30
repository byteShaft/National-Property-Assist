package byteshaft.com.nationalpropertyassist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.drainassist.DrainageRepairsActivity;
import byteshaft.com.nationalpropertyassist.drainassist.DrainageSurveyActivity;
import byteshaft.com.nationalpropertyassist.drainassist.EmergencyUnblockActivity;
import byteshaft.com.nationalpropertyassist.drainassist.MaintenanceActivity;
import byteshaft.com.nationalpropertyassist.drainassist.NewInstallationActivity;
import byteshaft.com.nationalpropertyassist.drainassist.SepticOrTreatmentTankActivity;

public class DrainAssistActivity extends AppCompatActivity implements View.OnClickListener{

    private Button drainage_repairs;
    private Button drainage_survey;
    private Button new_installation;
    private Button maintenance;
    private Button septic_or_treatment_tank;
    private Button emergency_unblock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Drain Assist");
        setContentView(R.layout.layout_activity_drain);
        emergency_unblock = (Button) findViewById(R.id.emergency_unblock);
        drainage_repairs = (Button) findViewById(R.id.drainage_repairs);
        drainage_survey = (Button) findViewById(R.id.drainage_survey);
        new_installation = (Button) findViewById(R.id.new_installation);
        maintenance = (Button) findViewById(R.id.maintenance);
        septic_or_treatment_tank = (Button) findViewById(R.id.septic_or_treatment_tank);
        emergency_unblock.setOnClickListener(this);
        drainage_repairs.setOnClickListener(this);
        drainage_survey.setOnClickListener(this);
        new_installation.setOnClickListener(this);
        maintenance.setOnClickListener(this);
        septic_or_treatment_tank.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emergency_unblock:
                startActivity(new Intent(getApplicationContext(), EmergencyUnblockActivity.class));
                break;
            case R.id.drainage_survey:
                startActivity(new Intent(getApplicationContext(), DrainageSurveyActivity.class));
                break;
            case R.id.drainage_repairs:
                startActivity(new Intent(getApplicationContext(), DrainageRepairsActivity.class));
                break;
            case R.id.new_installation:
                startActivity(new Intent(getApplicationContext(), NewInstallationActivity.class));
                break;
            case R.id.maintenance:
                startActivity(new Intent(getApplicationContext(), MaintenanceActivity.class));
                break;
            case R.id.septic_or_treatment_tank:
                startActivity(new Intent(getApplicationContext(), SepticOrTreatmentTankActivity.class));
                break;
        }
    }
}
