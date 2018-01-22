package com.joostit.vfradar.listing;

import com.joostit.vfradar.SysConfig;
import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.utilities.DistanceString;
import com.joostit.vfradar.utilities.Numbers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AircraftListCollection {

    public final List<AircraftListItem> items = new ArrayList<>();
    public final Map<Integer, AircraftListItem> itemMap = new HashMap<>();


    public AircraftListCollection() {

    }


    public synchronized void UpdateItems(List<TrackedAircraft> aircraftUpdate) {


        List<AircraftListItem> itemsToRemove = new ArrayList<>(items);

        for (TrackedAircraft tracked : aircraftUpdate) {
            AircraftListItem acItem;
            if (hasItem(tracked.Data.trackId)) {
                acItem = getItem(tracked.Data.trackId);
                itemsToRemove.remove(acItem);
            } else {
                acItem = new AircraftListItem();
                acItem.trackId = tracked.Data.trackId;
                addItem(acItem);
            }

            updateListItemData(acItem, tracked);
        }

        // Remove aircraft
        for (AircraftListItem acToRemove : itemsToRemove) {
            removeItem(acToRemove.trackId);
        }
    }


    private void updateListItemData(AircraftListItem acItem, TrackedAircraft tracked) {

        if (tracked.Data.vRate != null) {
            double val = Numbers.round(tracked.Data.vRate, 1);
            if (val > 0) {
                acItem.vRate = "+" + String.valueOf(val);
            } else {
                acItem.vRate = String.valueOf(val);
            }
        } else {
            acItem.vRate = "";
        }


        acItem.altitude = tracked.Data.alt != null ? tracked.Data.alt.toString() : "";
        acItem.model = tracked.Data.model != null ? tracked.Data.model : "";
        acItem.name =tracked.getIdString();
        acItem.cn = tracked.Data.cn != null ? tracked.Data.cn.toString() : "";
        updateRelativePosition(acItem, tracked);
    }


    private void updateRelativePosition(AircraftListItem listItem, TrackedAircraft tracked) {

        LatLon acPos = new LatLon(tracked.Data.lat, tracked.Data.lon);
        LatLon here = SysConfig.getCenterPosition();

        double distance = here.DistanceTo(acPos);
        int bearing = (int) Math.round(here.BearingTo(acPos));

        listItem.relativeDistance = DistanceString.getString(distance);
        listItem.relativeBearing = getBearingIndicator(bearing) + " " + bearing + "°";
    }

    private synchronized boolean hasItem(int trackId) {
        return itemMap.containsKey(trackId);
    }

    private synchronized AircraftListItem getItem(int trackId) {
        return itemMap.get(trackId);
    }

    private synchronized void addItem(AircraftListItem item) {
        items.add(item);
        itemMap.put(item.trackId, item);
    }

    private synchronized void removeItem(int trackId) {
        AircraftListItem toRemove = itemMap.get(trackId);
        items.remove(toRemove);
        itemMap.remove(toRemove.trackId);
    }


    private String getBearingIndicator(int bearing) {
        int b = bearing % 360;

        if (b < 45) {
            return "↑";
        } else if (b < 135) {
            return "→";
        } else if (b < 225) {
            return "↓";
        } else if (b < 315) {
            return "←";
        } else if (b <= 360) {
            return "↑";
        } else {
            return " ";
        }
    }

}
