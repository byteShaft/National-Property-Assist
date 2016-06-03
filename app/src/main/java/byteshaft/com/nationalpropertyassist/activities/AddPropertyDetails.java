package byteshaft.com.nationalpropertyassist.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import byteshaft.com.nationalpropertyassist.R;

public class AddPropertyDetails extends AppCompatActivity {

    private EditText mAddress;
    private EditText mPostCode;
    private EditText mDefaultProperty;
    private EditText mResidential;
    private EditText mOwner;
    private EditText mTypeOfProperty;
    private EditText mAgeOfProperty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property_detials);

        mAddress = (EditText) findViewById(R.id.et_address);
        mPostCode = (EditText) findViewById(R.id.et_post_code);
        mDefaultProperty = (EditText) findViewById(R.id.et_default_property);
        mResidential = (EditText) findViewById(R.id.et_residential);
        mOwner = (EditText) findViewById(R.id.et_owner);
        mTypeOfProperty = (EditText) findViewById(R.id.et_property_type);
        mAgeOfProperty = (EditText) findViewById(R.id.et_age_of_property);

    }
}
