package byteshaft.com.nationalpropertyassist.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import byteshaft.com.nationalpropertyassist.R;

public class WaterAssistActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup radioGroup;
    private RadioButton rbLeaking;

    private RadioButton rbPipe;

    private RadioButton rbInstallation;

    private EditText details;
    private Button submitButton;
    private String mRadioText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_water_assist);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        details = (EditText) findViewById(R.id.water_assist_et);
        submitButton = (Button) findViewById(R.id.submit);
        radioGroup.setOnCheckedChangeListener(this);
        rbPipe = (RadioButton) findViewById(R.id.renewal_pipe);
        rbInstallation = (RadioButton) findViewById(R.id.new_installation);
        rbLeaking = (RadioButton) findViewById(R.id.repair_to_leaking);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.repair_to_leaking:
                mRadioText = "Repair to leaking Supply Pipe";
                break;

            case R.id.renewal_pipe:
                mRadioText = "Renewal of Supply Pipe";
                break;

            case R.id.new_installation:
                mRadioText = "New Installation";
                break;
        }
        System.out.println(mRadioText);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                Log.i("Tag", "Test" + mRadioText);
//                String description = details.getText().toString();
//                new ServicesTask(WaterAssistActivity.this, description, mRadioText).execute();
                break;
        }
    }
}
