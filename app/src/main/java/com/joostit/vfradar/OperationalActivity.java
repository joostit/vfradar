package com.joostit.vfradar;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.joostit.vfradar.data.AircraftDataListener;
import com.joostit.vfradar.data.AircraftState;
import com.joostit.vfradar.data.AircraftStateCollection;
import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.data.VFRadarCore;
import com.joostit.vfradar.data.VFRadarCoreReaderTask;
import com.joostit.vfradar.listing.AircraftListFragment;

import java.util.List;

public class OperationalActivity extends AppCompatActivity
        implements RadarViewFragment.OnRadarViewInteractionListener,
        AircraftListFragment.OnListFragmentInteractionListener,
        AircraftDataListener {


    //private DebugAircraftDataGenerator debugGenerator = new DebugAircraftDataGenerator();
    private AircraftStateCollection aircaft = new AircraftStateCollection();

    private VFRadarCore radarCoreConnection = new VFRadarCore(this);

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            doAircraftUpdate(this);
        }
    };

    private void doAircraftUpdate(Runnable runnable) {

        radarCoreConnection.triggerGetAircraftDataAsync();

        timerHandler.postDelayed(runnable, 500);

        //UpdateAircraft(debugGenerator.GetAircraft());
    }


    private void startTimer() {
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


    @Override
    public void onAircraftSelectedFromList(Integer trackId) {

    }

    @Override
    public void newAircraftDataReceived(List<AircraftState> ac) {
        List<TrackedAircraft> newState = aircaft.doUpdate(ac);
        updateFragments(newState);
    }

    private void updateFragments(List<TrackedAircraft> ac){
        RadarViewFragment rView = (RadarViewFragment) getFragmentManager().findFragmentByTag("radarViewFragTag");
        if(rView != null) {
            rView.UpdateAircraft(ac);
        }

        AircraftListFragment acListFragment = (AircraftListFragment) getFragmentManager().findFragmentByTag("aircraftListFragTag");
        if(acListFragment != null) {
            acListFragment.UpdateAircraft(ac);
        }
    }
}
