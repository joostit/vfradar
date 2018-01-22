package com.joostit.vfradar.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joost on 22-1-2018.
 */

public class AircraftStateCollection {


    public final List<TrackedAircraft> trackedAc = new ArrayList<>();
    public final Map<Integer, TrackedAircraft> trackedAcMap = new HashMap<>();


    public synchronized List<TrackedAircraft> doUpdate(List<AircraftState> updates){

        List<TrackedAircraft> toRemove = new ArrayList<TrackedAircraft>(trackedAc);

        for(AircraftState newState : updates){
            if(hasTrack(newState.trackId)){
                TrackedAircraft existing = getItem(newState.trackId);
                updateTrackedAc(existing, newState);
                toRemove.remove(existing);
            }
            else{
                TrackedAircraft newTrack = new TrackedAircraft();
                newTrack.Data = newState;
                addTrack(newTrack);
            }
        }


        for(TrackedAircraft removeMe : toRemove){
            removeTrack(removeMe.Data.trackId);
        }

        return new ArrayList<TrackedAircraft>(trackedAc);
    }


    private void updateTrackedAc(TrackedAircraft item, AircraftState newState) {

        // ToDo: We're replacing the object reference instead of changing its values. Is this bad...?
        item.Data = newState;
    }


    private synchronized boolean hasTrack(int trackId) {
        return trackedAcMap.containsKey(trackId);
    }

    private synchronized TrackedAircraft getItem(int trackId) {
        return trackedAcMap.get(trackId);
    }

    private synchronized void addTrack(TrackedAircraft item) {
        trackedAc.add(item);
        trackedAcMap.put(item.Data.trackId, item);
    }

    private synchronized void removeTrack(int trackId) {
        TrackedAircraft toRemove = trackedAcMap.get(trackId);
        trackedAc.remove(toRemove);
        trackedAcMap.remove(trackId);
    }

}
