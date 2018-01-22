package com.joostit.vfradar;

import android.os.Handler;

import com.joostit.vfradar.data.AircraftState;
import com.joostit.vfradar.geo.LatLon;
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


    private void startTimer() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, timerMs);
    }


    public DebugAircraftDataGenerator() {
        createAircraft();
        startTimer();
    }

    private void createAircraft() {
//        AddAircraft(aircraftList, 52.27874982, 6.85548136, "ZBDEF5", "3_KM_WEST", "", null, null, null,null, false, false, false);
//        AddAircraft(aircraftList, 52.25179713, 6.899437, "ZBDEF4", "3_KM_SOUTH", "", null, null, null,null, false, false, false);
//        AddAircraft(aircraftList, 52.27874982, 6.94339264, "ZBDEF3", "3_KM_EAST", "", null, null, null,null, false, false, false);
//        AddAircraft(aircraftList, 52.30571874, 6.899437, "ZBDEF2", "3_KM_NORTH", "", null, null, null,null, false, false, false);
//        AddAircraft(aircraftList, 52.278758, 6.899437, "ZBDEF1", "CENTER", "", null, null, null,null, false, false, false);
        AddAircraft(aircraftList, 52.274143, 6.895478, "ABDEF1", "PH-TWM", "", "Cessna 172P", 331, 501, 7.4, 50, false, false, false);
        AddAircraft(aircraftList, 52.284818, 6.873783, "ABDEF2", "PH-TWK", "", "Cessna 172SP", 335, 301, -0.1, 100, true, false, false);
        AddAircraft(aircraftList, 52.284318, 6.873763, "ABDBF9", "PH-RRR", "", "Piper PA-2", 25, 1040, -3.3, 40, false, false, false);
        AddAircraft(aircraftList, 52.283179, 6.908719, "ABDEF3", "PH-712", "T4", "ASK-21", 45, 140, -0.8, 60, false, false, true);
        AddAircraft(aircraftList, 52.275418, 6.899414, "ABDEF4", "PH-798", "", "ASK-23", 230, 230, -1.8, 50, false, false, true);
        AddAircraft(aircraftList, 52.259443, 6.926437, "ABDEF5", "PH-648", "", "", 281, 1270, 0.5, 80, false, false, false);
        AddAircraft(aircraftList, 52.291826, 6.933838, "ABDEF6", "PH-1471", "T8", "Duo Discus T", 346, 867, 2.8, 20, false, true, false);
        AddAircraft(aircraftList, 52.231618, 6.942367, "ABDEF7", "PH-1480", "T7", "LS4b", 160, 1560, 3.1, 90, false, false, false);
        AddAircraft(aircraftList, 52.243716, 6.865389, "ABDEF8", "PH-764", "", "ASK-23", 20, 600, -3.2, 120, false, false, false);
        AddAircraft(aircraftList, 52.245094, 6.864513, "ABDEF9", "PH-974", "T6", "LS4", 172, 1436, 1.4, 70, false, false, false);

    }

    public synchronized List<TrackedAircraft> GetAircraft() {
        List<TrackedAircraft> aircraftUpdateList = new ArrayList<>();
        double threshold = 1.1;
        Random rd = new Random();

        for (TrackedAircraft ac : aircraftList) {
            double val = rd.nextDouble();
            if (val < threshold) {
                aircraftUpdateList.add(ac);
            }
        }

        return aircraftUpdateList;
    }


    private void AddAircraft(List<TrackedAircraft> list,
                             double lat,
                             double lon,
                             String icao24,
                             String reg,
                             String cn,
                             String model,
                             Integer track,
                             Integer alt,
                             Double vRate,
                             Integer speed,
                             Boolean warn,
                             Boolean sel,
                             Boolean highlight) {
        TrackedAircraft ta = new TrackedAircraft();
        ta.Data = new AircraftState();

        ta.Data.lat = lat;
        ta.Data.lon = lon;
        ta.Data.icao24 = icao24;
        ta.Data.reg = reg;
        ta.Data.model = model;
        ta.Data.cn = cn;
        ta.Data.track = Integer.valueOf(track);
        ta.Data.alt = Integer.valueOf(alt);
        ta.Data.vRate = Double.valueOf(vRate);
        ta.Data.speed = Integer.valueOf(speed);
        ta.Data.Trackid = list.size();

        ta.isHighlighted = highlight;
        ta.isSelected = sel;
        ta.isWarning = warn;

        list.add(ta);

    }


    private synchronized void UpdateAircraft() {

        double threshold = 1.1;
        Random rd = new Random();

        for (TrackedAircraft ac : aircraftList) {

            if ((ac.Data.speed != null) && (ac.Data.speed != 0)) {

                if (ac.Data.track != null) {
                    ac.Data.track += 4;
                    ac.Data.track = ac.Data.track % 360;
                }

                if (ac.Data.alt != null) {
                    ac.Data.alt += rd.nextInt(10) - 5;
                    if (ac.Data.alt < 200) {
                        ac.Data.alt += 10;
                    }
                }

                if (ac.Data.vRate != null) {
                    ac.Data.vRate += (rd.nextInt(10) - 5.0) / 10.0;
                }

                LatLon current = new LatLon(ac.Data.lat, ac.Data.lon);

                double dist = ((1000.0 / timerMs) * ac.Data.speed) / (1.8 * 3.6);

                LatLon newPoint = current.Move(ac.Data.track, dist);

                ac.Data.lat = newPoint.Latitude;
                ac.Data.lon = newPoint.Longitude;
            }
        }

    }

}
