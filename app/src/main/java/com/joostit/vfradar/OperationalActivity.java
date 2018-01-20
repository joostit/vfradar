package com.joostit.vfradar;

import android.app.FragmentManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.listing.AircraftListFragment;

import java.util.List;

public class OperationalActivity extends AppCompatActivity
        implements RadarViewFragment.OnRadarViewInteractionListener,
        AircraftListFragment.OnListFragmentInteractionListener {


    private DebugAircraftDataGenerator debugGenerator = new DebugAircraftDataGenerator();

    private long startTime = 0;
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            timerHandler.postDelayed(this, 500);

            UpdateAircraft(debugGenerator.GetAircraft());
        }
    };


    private void startTimer() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 1000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operational);

        startTimer();
    }


    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        startTimer();
    }

    @Override
    public void onAircraftSelected(Integer trackId) {

    }

    public void UpdateAircraft(List<TrackedAircraft> ac) {
        RadarViewFragment rView = (RadarViewFragment) getFragmentManager().findFragmentByTag("radarViewFragTag");
        rView.UpdateAircraft(ac);

        AircraftListFragment acListFragment = (AircraftListFragment) getFragmentManager().findFragmentByTag("aircraftListFragTag");
        acListFragment.UpdateAircraft(ac);
    }

    @Override
    public void onAircraftSelectedFromList(Integer trackId) {

    }
}
