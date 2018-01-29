package com.joostit.vfradar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.joostit.vfradar.geo.LatLon;

/**
 * Created by Joost on 20-1-2018.
 */

public class SysConfig {

    private static LatLon centerPosition;
    private static int maxValidRxAge;
    private static String vfradarCoreDataAddress;
    private static int connectionUpdateInterval;

    public static int getConnectionUpdateInterval(){
        return connectionUpdateInterval;
    }

    public static LatLon getCenterPosition() {
        return centerPosition;
    }

    public static int getMaxValidRxAge(){
        return maxValidRxAge;
    }

    public static String getVFRadarCoreDataAddress(){
        return vfradarCoreDataAddress;
    }

    static {
        setDefaults();
    }

    public static void loadSettings(Context context){

        setDefaults();

        vfradarCoreDataAddress = getString(context, R.string.key_vfradarcore_url, vfradarCoreDataAddress);
        connectionUpdateInterval = getInt(context, R.string.key_update_interval, connectionUpdateInterval);

    }

    private static void setDefaults() {
        vfradarCoreDataAddress = "http://192.168.178.101:60002/live/all";
        centerPosition = new LatLon(52.278758, 6.899437);
        maxValidRxAge = 15;
        connectionUpdateInterval = 500;
    }

    private static int getInt(Context context, int resouceKeyId, int defaultValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = context.getResources().getString(resouceKeyId);
        String val =  preferences.getString(preferenceKey, String.valueOf(defaultValue));
        return Integer.parseInt(val);
    }

    private static String getString(Context context, int resouceKeyId, String defaultValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = context.getResources().getString(resouceKeyId);
        return preferences.getString(preferenceKey, defaultValue);
    }

}
