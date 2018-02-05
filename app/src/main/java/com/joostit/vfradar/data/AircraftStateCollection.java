package com.joostit.vfradar.data;

import com.joostit.vfradar.config.SysConfig;
import com.joostit.vfradar.geo.geofencing.GeoFenceHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joost on 22-1-2018.
 */

public class AircraftStateCollection {

    private GeoFenceHandler areaHandler;

    public final List<TrackedAircraft> trackedAc = new ArrayList<>();
    public final Map<Integer, TrackedAircraft> trackedAcMap = new HashMap<>();

    public AircraftStateCollection(){

    }

    public void setGeoFences(GeoFenceHandler areaHandler){
        this.areaHandler = areaHandler;
    }

    public synchronized AircraftTrackingUpdate doUpdate(AircraftDataUpdate updates) {

        List<TrackedAircraft> toRemove = new ArrayList<>(trackedAc);
        List<AircraftState> filteredAircraft = performFilter(updates);

        for (AircraftState newState : filteredAircraft) {
            if (hasTrack(newState.trackId)) {
                TrackedAircraft existing = getItem(newState.trackId);
                updateTrackedAc(existing, newState);
                toRemove.remove(existing);
            } else {
                TrackedAircraft newTrack = new TrackedAircraft();
                newTrack.data = newState;
                addTrack(newTrack);
            }
        }

        for (TrackedAircraft removeMe : toRemove) {
            removeTrack(removeMe.data.trackId);
        }

        if(areaHandler != null) {
            areaHandler.applyFences(trackedAc);
        }

        AircraftTrackingUpdate retVal = new AircraftTrackingUpdate(new ArrayList<>(trackedAc), updates.getUpdateSuccess());

        return retVal;
    }

    private List<AircraftState> performFilter(AircraftDataUpdate rawUpdate) {

        if(!SysConfig.isFilterEnabled()){
            return rawUpdate.trackedAircraft;
        }

        List<AircraftState> filteredList = new ArrayList<>();

        for(AircraftState rawState : rawUpdate.trackedAircraft){

            if(SysConfig.getCenterPosition().DistanceTo(rawState.position) <= SysConfig.getFilterMaxDist() * 1000){

                // If we don't have an altitude, always keep the track, just in case :')
                if(rawState.hasAltitude()){
                    if(rawState.alt <= SysConfig.getFilterMaxAlt()){
                        filteredList.add(rawState);
                    }
                }
                else{
                    filteredList.add(rawState);
                }
            }

        }

        return filteredList;
    }


    private void updateTrackedAc(TrackedAircraft item, AircraftState newState) {

        // ToDo: We're replacing the object reference instead of changing its values. Is this bad...?
        item.data = newState;
    }


    private synchronized boolean hasTrack(int trackId) {
        return trackedAcMap.containsKey(trackId);
    }

    private synchronized TrackedAircraft getItem(int trackId) {
        return trackedAcMap.get(trackId);
    }

    private synchronized void addTrack(TrackedAircraft item) {
        trackedAc.add(item);
        trackedAcMap.put(item.data.trackId, item);
    }

    private synchronized void removeTrack(int trackId) {
        TrackedAircraft toRemove = trackedAcMap.get(trackId);
        trackedAc.remove(toRemove);
        trackedAcMap.remove(trackId);
    }

}
