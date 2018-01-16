package com.joostit.vfradar;

import android.os.Handler;

import com.joostit.vfradar.Geo.LatLon;
import com.joostit.vfradar.data.JSonTrackedAircraft;
import com.joostit.vfradar.data.TrackedAircraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Joost on 15-1-2018.
 */

public class DebugAircraftDataGenerator {

    private List<TrackedAircraft> aircraftList = new ArrayList<>();

    private int timerMs = 250;

    private long startTime = 0;


    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            timerHandler.postDelayed(this, timerMs);

            UpdateAircraft();
        }
    };


    private  void startTimer(){
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, timerMs);
    }


    public DebugAircraftDataGenerator(){
        createAircraft();
        startTimer();
    }

    private void createAircraft() {
        AddAircraft(aircraftList, 52.274143, 6.895478, "ABDEF1", "PH-TWM", "", 331, 501, 7.4,50, false, false, false);
        AddAircraft(aircraftList, 52.284818, 6.873783, "ABDEF2", "PH-TWK", "", 335, 301, -0.1,100, true, false, false);
        AddAircraft(aircraftList, 52.283179, 6.908719, "ABDEF3", "PH-712", "T4", 45, 140, -0.8,60, false, false, true);
        AddAircraft(aircraftList, 52.275418, 6.899414, "ABDEF4", "PH-798", "", 230, 230, -1.8,50,false, false, true);
        AddAircraft(aircraftList, 52.259443, 6.926437, "ABDEF5", "PH-648", "", 281, 1270, 0.5, 80,false, false, false);
        AddAircraft(aircraftList, 52.291826, 6.933838, "ABDEF6", "PH-1471", "T8", 346, 867, 2.8, 20,false, true, false);
        AddAircraft(aircraftList, 52.231618, 6.942367, "ABDEF7", "PH-1480", "T7", 160, 1560, 3.1, 90,false, false, false);
        AddAircraft(aircraftList, 52.243716, 6.865389, "ABDEF8", "PH-764", "", 20, 600, -3.2, 120,false, false, false);
        AddAircraft(aircraftList, 52.245094, 6.864513, "ABDEF9", "PH-974", "T6", 172, 1436, 1.4, 70,false, false, false);

    }

    public List<TrackedAircraft> GetAircraft(){
        return aircraftList;
    }


    private void AddAircraft(List<TrackedAircraft> list,
                             double lat,
                             double lon,
                             String icao24,
                             String reg,
                             String cn,
                             int track,
                             int alt,
                             double vRate,
                             int speed,
                             Boolean warn,
                             Boolean sel,
                             Boolean note){
        TrackedAircraft ta = new TrackedAircraft();
        ta.Data = new JSonTrackedAircraft();

        ta.Data.Lat = lat;
        ta.Data.Lon = lon;
        ta.Data.Icao24 = icao24;
        ta.Data.Reg = reg;
        ta.Data.Cn = cn;
        ta.Data.Track = track;
        ta.Data.Alt = alt;
        ta.Data.VRate = vRate;
        ta.Data.Speed = speed;
        ta.Data.Trackid = list.size();

        list.add(ta);

    }


    private synchronized void UpdateAircraft() {

        double threshold = 0.3;
        Random rd = new Random();

        for(TrackedAircraft ac : aircraftList){

            double val = rd.nextDouble();
            if(val < threshold) {
                ac.Data.Track += 6;
                ac.Data.Track = ac.Data.Track % 360;

                LatLon current = new LatLon(ac.Data.Lat, ac.Data.Lon);

                double dist = (1000.0 / timerMs) * ac.Data.Speed;

                LatLon newPoint = current.Move(ac.Data.Track, dist);

                ac.Data.Lat = newPoint.Latitude;
                ac.Data.Lon = newPoint.Longitude;
            }
        }

    }

}
