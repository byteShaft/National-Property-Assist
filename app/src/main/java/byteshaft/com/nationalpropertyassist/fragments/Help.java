package byteshaft.com.nationalpropertyassist.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import byteshaft.com.nationalpropertyassist.R;

/**
 * Created by shahid on 02/06/2016.
 */
public class Help extends Fragment {

    public View mBaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_help, container, false);
        return mBaseView;
    }
}
