package com.joostit.vfradar.infolisting;

import android.content.Context;

import com.joostit.vfradar.R;
import com.joostit.vfradar.config.SysConfig;
import com.joostit.vfradar.config.UserUnitConvert;
import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.utilities.DistanceString;
import com.joostit.vfradar.utilities.StringValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AircraftListCollection {

    public final List<InfoListItemData> items = new ArrayList<>();
    private final Map<Integer, InfoListItemData> itemMap = new HashMap<>();
    private UserUnitConvert userUnitConverter = new UserUnitConvert();
    ;

    public AircraftListCollection() {
    }

    public synchronized List<InfoListItemData> getListItems() {
        return new ArrayList<>(items);
    }

    public synchronized InfoListUpdateResults updateItems(List<TrackedAircraft> aircraftUpdate, Context context) {

        InfoListUpdateResults retVal = new InfoListUpdateResults();

        List<InfoListItemData> itemsToRemove = new ArrayList<>(items);

        for (TrackedAircraft tracked : aircraftUpdate) {
            InfoListItemData acItem;
            if (hasItem(tracked.Data.trackId)) {
                acItem = getItem(tracked.Data.trackId);
                itemsToRemove.remove(acItem);
            } else {
                acItem = new InfoListItemData();
                acItem.trackId = tracked.Data.trackId;
                addItem(acItem);
                retVal.added.add(acItem);
            }

            updateListItemData(acItem, tracked, context);
        }

        // Remove aircraft
        for (InfoListItemData acToRemove : itemsToRemove) {
            retVal.removed.add(acToRemove);
            removeItem(acToRemove.trackId);
        }

        return retVal;
    }


    private void updateListItemData(InfoListItemData acItem, TrackedAircraft tracked, Context context) {

        acItem.vRate = userUnitConverter.getVerticalRateString(tracked.Data.vRate);

        TrackedAircraft.IdTypes nameType = tracked.getUserIdType();

        if (tracked.Data.alt != null) {
            acItem.altitude = userUnitConverter.getHeight(tracked.Data.alt) + " " + userUnitConverter.getHeightUnitIndicator();
        } else {
            acItem.altitude = "";
        }
        acItem.model = sanitizeModelString(tracked.Data.model, tracked.Data.type, context);
        acItem.name = tracked.getId(nameType);
        acItem.nameType = getNameTypeTranslation(nameType, context);
        acItem.cn = tracked.Data.cn != null ? tracked.Data.cn.toString() : "";
        updateRelativePosition(acItem, tracked);

        acItem.hasAdsb = determineUpdateValid(tracked.Data.adsbStation, tracked.Data.adsbAge);
        acItem.hasOgn = determineUpdateValid(tracked.Data.ognStation, tracked.Data.ognAge);

    }

    private String sanitizeModelString(String rawModel, String type, Context context) {
        String modelString = "";
        String typeString = "";

        if (!StringValue.nullOrEmpty(rawModel)
                && !rawModel.equalsIgnoreCase("Unknown")) {
            modelString = rawModel;
        }

        if (!StringValue.nullOrEmpty(type)
                && !type.equalsIgnoreCase("UNKNOWN")) {
            typeString = getTypeTranslation(type, context);
        }

        if (StringValue.nullOrEmpty(modelString) && StringValue.nullOrEmpty(typeString)) {
            return "-";
        }

        if (!StringValue.nullOrEmpty(modelString) && StringValue.nullOrEmpty(typeString)) {
            return modelString;
        }

        if (StringValue.nullOrEmpty(modelString) && !StringValue.nullOrEmpty(typeString)) {
            return typeString;
        }

        if (!StringValue.nullOrEmpty(modelString) && !StringValue.nullOrEmpty(typeString)) {
            return typeString + ": " + modelString;
        }


        return rawModel;
    }

    private String getTypeTranslation(String type, Context context) {

        String lowercaseType = type.toLowerCase();

        switch (lowercaseType) {
            case "unknown":
                return context.getResources().getString(R.string.ognTypesUnknown);
            case "glider":
                return context.getResources().getString(R.string.ognTypesGlider);
            case "tow_plane":
                return context.getResources().getString(R.string.ognTypesTowPlane);
            case "helicopter_rotorcraft":
                return context.getResources().getString(R.string.ognTypesHelicopter);
            case "parachute":
                return context.getResources().getString(R.string.ognTypesParachute);
            case "drop_plane":
                return context.getResources().getString(R.string.ognTypesDropPlane);
            case "hang_glider":
                return context.getResources().getString(R.string.ognTypesHangGlider);
            case "para_glider":
                return context.getResources().getString(R.string.ognTypesParaGlider);
            case "powered_aircraft":
                return context.getResources().getString(R.string.ognTypesPoweredAircraft);
            case "jet_aircraft":
                return context.getResources().getString(R.string.ognTypesJetAircraft);
            case "ufo":
                return context.getResources().getString(R.string.ognTypesUfo);
            case "balloon":
                return context.getResources().getString(R.string.ognTypesBalloon);
            case "airship":
                return context.getResources().getString(R.string.ognTypesAirship);
            case "uav":
                return context.getResources().getString(R.string.ognTypesUav);
            case "static_object":
                return context.getResources().getString(R.string.ognTypesStaticObject);
            default:
                return lowercaseType;
        }

    }


    private String getNameTypeTranslation(TrackedAircraft.IdTypes idType, Context context) {
        switch (idType) {
            case Callsign:
                return context.getResources().getString(R.string.idTypeCallSign) + ":";
            case Registration:
                return context.getResources().getString(R.string.idTypeRegistration) + ":";
            case Icao24:
                return context.getResources().getString(R.string.idTypeIcao24) + ":";
            case FlarmId:
                return context.getResources().getString(R.string.idTypeFlarmId) + ":";
            case OgnId:
                return context.getResources().getString(R.string.idTypeOgnId) + ":";
            case Cn:
                return context.getResources().getString(R.string.idTypeCn) + ":";
            default:
                return "UNKNOWN NAME TYPE";
        }
    }


    private Boolean determineUpdateValid(String stationId, Integer age) {
        if (stationId == null) {
            return false;
        }

        if (age == null) {
            return false;
        }

        return (age < SysConfig.getMaxValidRxAge());
    }


    private void updateRelativePosition(InfoListItemData listItem, TrackedAircraft tracked) {

        LatLon acPos = new LatLon(tracked.Data.lat, tracked.Data.lon);
        LatLon here = SysConfig.getCenterPosition();

        double distance = here.DistanceTo(acPos);
        int bearing = (int) Math.round(here.BearingTo(acPos));

        listItem.relativeDistance = DistanceString.getString(distance);
        listItem.relativeDegrees = bearing;
        listItem.relativeBearing = bearing + "°";
    }

    private synchronized boolean hasItem(int trackId) {
        return itemMap.containsKey(trackId);
    }

    private synchronized InfoListItemData getItem(int trackId) {
        return itemMap.get(trackId);
    }

    private synchronized void addItem(InfoListItemData item) {
        items.add(item);
        itemMap.put(item.trackId, item);
    }

    private synchronized void removeItem(int trackId) {
        InfoListItemData toRemove = itemMap.get(trackId);
        items.remove(toRemove);
        itemMap.remove(toRemove.trackId);
    }
}
