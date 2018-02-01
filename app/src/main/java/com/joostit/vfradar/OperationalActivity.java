package com.joostit.vfradar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.joostit.vfradar.config.SysConfig;
import com.joostit.vfradar.data.AircraftDataListener;
import com.joostit.vfradar.data.AircraftDataUpdate;
import com.joostit.vfradar.data.AircraftState;
import com.joostit.vfradar.data.AircraftStateCollection;
import com.joostit.vfradar.data.AircraftTrackingUpdate;
import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.data.VFRadarCore;
import com.joostit.vfradar.infolisting.InfoListFragment;
import com.joostit.vfradar.radardrawing.RadarViewFragment;
import com.joostit.vfradar.settings.SettingsActivity;
import com.joostit.vfradar.site.SiteScenarioLoader;

import java.util.ArrayList;
import java.util.List;

public class OperationalActivity extends AppCompatActivity
        implements RadarViewFragment.OnRadarViewInteractionListener,
        InfoListFragment.OnListFragmentInteractionListener,
        MenuBarFragment.OnMenuBarFragmentInteractionListener,
        AircraftDataListener {


    private final int CONFIG_INTENT_REQUEST_CODE = 1;
    boolean doubleBackToExitPressedOnce = false;
    private AircraftStateCollection aircaft;
    private VFRadarCore radarCoreConnection;
    private SiteScenarioLoader site;
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
        timerHandler.postDelayed(runnable, SysConfig.getConnectionUpdateInterval());
    }

    private void startTimer() {
        int initialDelay = 100;
        timerHandler.postDelayed(timerRunnable, initialDelay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_operational);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        PermissionHelper.verifyWriteStoragePermissions(this);
        PermissionHelper.verifLocationAndGpsPermissions(this);
        SysConfig.loadSettings(getApplicationContext());
        createComponentObjects();
        startTimer();
    }

    private void createComponentObjects() {
        aircaft = new AircraftStateCollection();
        radarCoreConnection = new VFRadarCore(this);
        site = new SiteScenarioLoader();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    private void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        startTimer();
    }

    @Override
    public void onAircraftSelected(Integer trackId) {
        InfoListFragment acListFragment = (InfoListFragment) getFragmentManager().findFragmentByTag("aircraftListFragTag");
        acListFragment.selectAircraft(trackId);
    }

    @Override
    protected void onStart() {
        super.onStart();

        AircraftTrackingUpdate emptyUpdate = new AircraftTrackingUpdate(new ArrayList<TrackedAircraft>(), false);
        updateFragments(emptyUpdate);

        siteDataLoadertask = new LoadSiteDataTask(this);
        siteDataLoadertask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onAircraftSelectedFromList(Integer trackId) {
        RadarViewFragment rView = (RadarViewFragment) getFragmentManager().findFragmentByTag("radarViewFragTag");
        rView.selectAircraft(trackId);

        InfoListFragment acListFragment = (InfoListFragment) getFragmentManager().findFragmentByTag("aircraftListFragTag");
        acListFragment.selectAircraft(trackId);
    }

    @Override
    public void newAircraftDataReceived(AircraftDataUpdate ac) {
        AircraftTrackingUpdate lastUpdatedState = aircaft.doUpdate(ac);
        updateFragments(lastUpdatedState);
    }

    private void updateFragments(AircraftTrackingUpdate lastUpdateState) {
        RadarViewFragment rView = (RadarViewFragment) getFragmentManager().findFragmentByTag("radarViewFragTag");
        if (rView != null) {
            rView.UpdateAircraft(lastUpdateState);
        }

        InfoListFragment acListFragment = (InfoListFragment) getFragmentManager().findFragmentByTag("aircraftListFragTag");
        if (acListFragment != null) {
            acListFragment.UpdateAircraft(lastUpdateState);
        }

    }

    @Override
    public void onPreferencesPressed() {
        stopTimer();
        Intent myIntent = new Intent(OperationalActivity.this, SettingsActivity.class);
        OperationalActivity.this.startActivityForResult(myIntent, CONFIG_INTENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CONFIG_INTENT_REQUEST_CODE:
                this.recreate();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private class LoadSiteDataTask extends AsyncTask<Object, Void, Object> {

        private AppCompatActivity initiator;

        LoadSiteDataTask(AppCompatActivity initiator) {
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
            if (rView != null) {
                rView.UpdateSiteFeatures(site.getSite());
                rView.updateGeoData(site.getGeoData());
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }
}
