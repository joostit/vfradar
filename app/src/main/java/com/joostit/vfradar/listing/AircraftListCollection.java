package com.joostit.vfradar.listing;

import com.joostit.vfradar.data.TrackedAircraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AircraftListCollection {

    /**
     * An array of sample (dummy) items.
     */
    public final List<AircraftListItem> items = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public final Map<Integer, AircraftListItem> itemMap = new HashMap<>();

    private static final int COUNT = 25;

    public void UpdateItems(List<TrackedAircraft> ac){

    }

    public AircraftListCollection(){
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }


    private void addItem(AircraftListItem item) {
        items.add(item);
        itemMap.put(item.trackId, item);
    }

    private AircraftListItem createDummyItem(final int position) {
        return new AircraftListItem(){{
            trackId = position;
            name = "PH-12" + position;
            model = "Duo Discus XLT";
            cn = "T7";
            altitude = "1537";
            vRate = "+3.2";
            relativePosition = "15.8km @ 286°";
        }};
    }

    private String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

}
