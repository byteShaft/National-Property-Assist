package byteshaft.com.nationalpropertyassist.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import byteshaft.com.nationalpropertyassist.R;

public class Settings extends Fragment{

    public View mBaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_settings, container, false);
        return mBaseView;
    }
}
