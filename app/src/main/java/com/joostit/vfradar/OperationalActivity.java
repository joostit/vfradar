package com.joostit.vfradar;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.joostit.vfradar.data.AircraftDataListener;
import com.joostit.vfradar.data.AircraftState;
import com.joostit.vfradar.data.AircraftStateCollection;
import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.data.VFRadarCore;
import com.joostit.vfradar.infolisting.InfoListFragment;
import com.joostit.vfradar.site.SiteDataLoader;

import java.util.List;

public class OperationalActivity extends AppCompatActivity
        implements RadarViewFragment.OnRadarViewInteractionListener,
        InfoListFragment.OnListFragmentInteractionListener,
        AircraftDataListener {

    private AircraftStateCollection aircaft = new AircraftStateCollection();
    private VFRadarCore radarCoreConnection = new VFRadarCore(this);
    private SiteDataLoader site = new SiteDataLoader();

    private LoadSiteDataTask siteDataLoadertask;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            doAircraftUpdate(this);
        }
    };

    private void doAircraftUpdate(Runnable runnable) {

        radarCoreConnection.triggerGetAircraftDataAsync();

        timerHandler.postDelayed(runnable, 500);

    }


    private void startTimer() {
        timerHandler.postDelayed(timerRunnable, 1000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operational);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        PermissionHelper.verifyWriteStoragePermissions(this);

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
    protected void onStart() {
        super.onStart();

        siteDataLoadertask = new LoadSiteDataTask(this);
        siteDataLoadertask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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

        InfoListFragment acListFragment = (InfoListFragment) getFragmentManager().findFragmentByTag("aircraftListFragTag");
        if(acListFragment != null) {
            acListFragment.UpdateAircraft(ac);
        }
    }


    private class LoadSiteDataTask extends AsyncTask<Object, Void, Object> {

        private AppCompatActivity initiator;

        LoadSiteDataTask(AppCompatActivity initiator){
            this.initiator = initiator;
        }

        @Override
        protected Object doInBackground(Object... params) {
            site.loadData();
            return true;
        }

        @Override
        protected void onPostExecute(Object na) {
            RadarViewFragment rView = (RadarViewFragment) initiator.getFragmentManager().findFragmentByTag("radarViewFragTag");
            if(rView != null) {
                rView.UpdateSiteFeatures(site.getSite());
                rView.updateGeoData(site.getGeoData());
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
