package byteshaft.com.nationalpropertyassist.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.utils.ServicesTask;

public class HomeAssistActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    RadioGroup radioGroup;
    EditText details;
    Button submitButton;
    private String mRadioText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home_assist);
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
                mRadioText = "Home Buyer Drain Survey";
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                String description = details.getText().toString();
                new ServicesTask(HomeAssistActivity.this, description, mRadioText).execute();
                break;
        }
    }
}
