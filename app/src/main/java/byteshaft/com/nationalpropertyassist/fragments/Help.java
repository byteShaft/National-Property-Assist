package byteshaft.com.nationalpropertyassist.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.activities.AssistMain;

public class Help extends Fragment {

    public View mBaseView;
    private Button testButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_help, container, false);
        testButton = (Button) mBaseView.findViewById(R.id.button_test);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AssistMain.class));
            }
        });

        return mBaseView;
    }
}
