package com.joostit.vfradar.data;

import android.os.AsyncTask;

import com.joostit.vfradar.config.SysConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Joost on 22-1-2018.
 */

public class VFRadarCore implements AircraftDataListener {

    private AircraftDataListener listener;
    private URL url;
    private boolean isExecuting = false;

    public VFRadarCore(AircraftDataListener listener) {
        this.listener = listener;

        try {
            url = new URL(SysConfig.getVFRadarCoreDataAddress());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void triggerGetAircraftDataAsync() {
        if (!isExecuting) {
            isExecuting = true;
            VFRadarCoreReaderTask task = new VFRadarCoreReaderTask(this);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        }
    }

    @Override
    public void newAircraftDataReceived(AircraftDataUpdate ac) {
        isExecuting = false;
        if (ac != null) {
            listener.newAircraftDataReceived(ac);
        }
    }


}
