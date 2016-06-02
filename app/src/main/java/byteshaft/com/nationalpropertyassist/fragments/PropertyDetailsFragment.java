package byteshaft.com.nationalpropertyassist.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import byteshaft.com.nationalpropertyassist.R;


public class PropertyDetailsFragment extends Fragment {

    public View mBaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_property_details, container, false);
        return mBaseView;
    }
}
