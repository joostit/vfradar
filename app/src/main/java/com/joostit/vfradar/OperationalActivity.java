package com.joostit.vfradar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OperationalActivity extends AppCompatActivity
        implements SidePanelFragment.OnSidePanelInteractionListener,
        RadarViewFragment.OnRadarViewInteractionListener,
        AircraftListFragment.OnAircraftListInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operational);
    }


    @Override
    public void onAircraftSelected(Integer trackid) {

    }
}
