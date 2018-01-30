package com.joostit.vfradar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
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

    public static void setCenterPosition(Context context, LatLon value){
        centerPosition = value;
        setLatLon(context, R.string.key_site_center_location, value);
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
        centerPosition = getLatLon(context, R.string.key_site_center_location, centerPosition);
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

    private static LatLon getLatLon(Context context, int resouceKeyId, LatLon defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = context.getResources().getString(resouceKeyId);
        return LatLon.parseLatLon(preferences.getString(preferenceKey, defaultValue.toString()));
    }


    private static void setLatLon(Context context, int resourceKeyId, LatLon value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(context.getString(resourceKeyId), value.toString());
        editor.commit();
    }

}
