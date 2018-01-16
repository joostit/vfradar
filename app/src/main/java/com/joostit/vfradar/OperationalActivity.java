package com.joostit.vfradar;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.joostit.vfradar.data.TrackedAircraft;

import java.util.List;

public class OperationalActivity extends AppCompatActivity
        implements SidePanelFragment.OnSidePanelInteractionListener,
        RadarViewFragment.OnRadarViewInteractionListener,
        AircraftListFragment.OnAircraftListInteractionListener{


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


    private  void startTimer(){
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
    public void onAircraftSelected(Integer trackId) {

    }

    public void UpdateAircraft(List<TrackedAircraft> ac){
        RadarViewFragment rView = (RadarViewFragment) getFragmentManager().findFragmentByTag("radarViewFragTag");



        //if(rView != null) {
            rView.UpdateAircraft(ac);
        //}
    }
}
