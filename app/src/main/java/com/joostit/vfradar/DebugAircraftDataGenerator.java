package com.joostit.vfradar;

import com.joostit.vfradar.data.JSonTrackedAircraft;
import com.joostit.vfradar.data.TrackedAircraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 15-1-2018.
 */

public class DebugAircraftDataGenerator {


//        DrawAircraft(canvas, cX + 358, cY + 432, 170, "PH-973 (T6)", 1420, 1.4, AircraftStates.None);


    public List<TrackedAircraft> GetAircraft(){

        List<TrackedAircraft> retVal = new ArrayList<>();

        AddAircraft(retVal, 1, 1, "ABDEF1", "PH-TWM", "", 331, 501, 7.4, false, false, false);
        AddAircraft(retVal, 1, 1, "ABDEF2", "PH-TWK", "", 335, 301, -0.1, true, false, false);
        AddAircraft(retVal, 1, 1, "ABDEF3", "PH-712", "T4", 45, 140, -0.8, false, false, true);
        AddAircraft(retVal, 1, 1, "ABDEF4", "PH-798", "", 230, 230, -1.8, false, false, true);
        AddAircraft(retVal, 1, 1, "ABDEF5", "PH-648", "", 281, 1270, 0.5, false, false, false);
        AddAircraft(retVal, 1, 1, "ABDEF6", "PH-1471", "T8", 346, 867, 2.8, false, true, false);
        AddAircraft(retVal, 1, 1, "ABDEF7", "PH-1480", "T7", 160, 1560, 3.1, false, false, false);
        AddAircraft(retVal, 1, 1, "ABDEF8", "PH-764", "", 20, 600, -3.2, false, false, false);
        AddAircraft(retVal, 1, 1, "ABDEF9", "PH-974", "T6", 172, 1436, 1.4, false, false, false);

        return retVal;
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
        ta.Data.Trackid = list.size();

        list.add(ta);

    }

}
