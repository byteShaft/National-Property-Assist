package byteshaft.com.nationalpropertyassist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import byteshaft.com.nationalpropertyassist.R;

public class AssistMain extends Fragment implements View.OnClickListener {

    private Button drainAssist;
    private Button buildingAssist;
    private Button homeBuyerAssist;
    private Button plumberAssist;
    private Button waterAssist;
    public View mBaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.activity_assist_main, container, false);
        drainAssist = (Button) mBaseView.findViewById(R.id.drain_assist);
        buildingAssist = (Button) mBaseView.findViewById(R.id.building_assist);
        homeBuyerAssist = (Button) mBaseView.findViewById(R.id.home_buyer_assist);
        plumberAssist = (Button) mBaseView.findViewById(R.id.plumber_assist);
        waterAssist = (Button) mBaseView.findViewById(R.id.water_assist);
        drainAssist.setOnClickListener(this);
        buildingAssist.setOnClickListener(this);
        homeBuyerAssist.setOnClickListener(this);
        plumberAssist.setOnClickListener(this);
        waterAssist.setOnClickListener(this);
        return mBaseView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drain_assist:
                startActivity(new Intent(getActivity().getApplicationContext(),
                        DrainAssistActivity.class));
                break;

            case R.id.water_assist:
                startActivity(new Intent(getActivity().getApplicationContext(),
                        WaterAssistActivity.class));
                break;

            case R.id.home_buyer_assist:
                startActivity(new Intent(getActivity().getApplicationContext(),
                        HomeAssistActivity.class));
                break;

            case R.id.plumber_assist:
                startActivity(new Intent(getActivity().getApplicationContext(),
                        PlumberActivity.class));
                break;

            case R.id.building_assist:
                startActivity(new Intent(getActivity().getApplicationContext(),
                        BuildingAssistActivity.class));
                break;
        }
    }
}
