package byteshaft.com.nationalpropertyassist.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import byteshaft.com.nationalpropertyassist.R;

public class JobHistory extends android.support.v4.app.Fragment {

    public View mBaseView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_job_history, container, false);
        return mBaseView;
    }
}
