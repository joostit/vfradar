package com.joostit.vfradar.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Joost on 22-1-2018.
 */

public class VFRadarCore implements AircraftDataListener {

    private AircraftDataListener listener;
    URL url;
    boolean isExecuting = false;

    public VFRadarCore(AircraftDataListener listener){
        this.listener = listener;

        try {
            url = new URL("http://192.168.178.101:60002/live/all");
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
    }


    public void triggerGetAircraftDataAsync() {
        if (!isExecuting) {
            isExecuting = true;
            VFRadarCoreReaderTask task = new VFRadarCoreReaderTask(this);
            task.execute(url);
        }
    }

    @Override
    public void newAircraftDataReceived(List<TrackedAircraft> ac) {
        isExecuting = false;
        if(ac != null) {
            listener.newAircraftDataReceived(ac);
        }
    }


}
