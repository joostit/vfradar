package com.joostit.vfradar.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.joostit.vfradar.R;
import com.joostit.vfradar.geo.LatLon;

/**
 * Created by Joost on 20-1-2018.
 */

public class SysConfig {

    private static LatLon centerPosition;
    private static int maxValidRxAge;
    private static String vfradarCoreDataAddress;
    private static int connectionUpdateInterval;
    private static String dataFolder;
    private static HeightUnits heightUnit;
    private static int siteElevation;
    private static VerticalRateUnits verticalRateUnit;
    private static boolean showGeoFences;
    private static boolean filterEnabled;
    private static int filterMaxAlt;
    private static int filterMaxDist;


    static {
        setDefaults();
    }

    public static HeightUnits getHeightUnit() {
        return heightUnit;
    }

    public static void setHeightUnits(Context context, HeightUnits value) {
        heightUnit = value;
        setString(context, R.string.key_units_height, heightUnit.name());
    }

    public static VerticalRateUnits getVerticalRateUnit() {
        return verticalRateUnit;
    }

    public static void setVerticalRateUnit(Context context, VerticalRateUnits value) {
        verticalRateUnit = value;
        setString(context, R.string.key_units_verticalRate, verticalRateUnit.name());
    }

    public static boolean isFilterEnabled() {
        return filterEnabled;
    }

    public static int getFilterMaxAlt() {
        return filterMaxAlt;
    }

    public static int getFilterMaxDist() {
        return filterMaxDist;
    }

    public static Boolean getShowGeoFences() {
        return showGeoFences;
    }

    public static int getSiteElevation() {
        return siteElevation;
    }

    public static void setSiteElevation(Context context, int elevation) {
        siteElevation = elevation;
        setIntToStringPreference(context, R.string.key_site_elevationM, siteElevation);
    }

    public static int getConnectionUpdateInterval() {
        return connectionUpdateInterval;
    }

    public static LatLon getCenterPosition() {
        return centerPosition;
    }

    public static void setCenterPosition(Context context, LatLon value) {
        centerPosition = value;
        setLatLon(context, R.string.key_site_center_location, value);
    }

    public static void setDataFolder(Context context, String value) {
        dataFolder = value;
        setString(context, R.string.key_data_datafolder, value);
    }

    public static String getDataFolder() {
        return dataFolder;
    }

    public static int getMaxValidRxAge() {
        return maxValidRxAge;
    }

    public static String getVFRadarCoreDataAddress() {
        return vfradarCoreDataAddress;
    }

    public static void loadSettings(Context context) {

        setDefaults();

        vfradarCoreDataAddress = getString(context, R.string.key_vfradarcore_url, vfradarCoreDataAddress);
        connectionUpdateInterval = getInt(context, R.string.key_update_interval, connectionUpdateInterval);
        centerPosition = getLatLon(context, R.string.key_site_center_location, centerPosition);
        siteElevation = getIntFromStringPreference(context, R.string.key_site_elevationM, siteElevation);
        dataFolder = getString(context, R.string.key_data_datafolder, dataFolder);
        heightUnit = HeightUnits.valueOf(getString(context, R.string.key_units_height, heightUnit.name()));
        verticalRateUnit = VerticalRateUnits.valueOf(getString(context, R.string.key_units_verticalRate, verticalRateUnit.name()));
        showGeoFences = getBoolean(context, R.string.key_appearance_showGeoFences, showGeoFences);
        filterEnabled = getBoolean(context, R.string.key_plot_filter_enabled, filterEnabled);
        filterMaxAlt = getIntFromStringPreference(context, R.string.key_plot_filter_maxAlt, filterMaxAlt);
        filterMaxDist = getIntFromStringPreference(context, R.string.key_plot_filter_maxDist, filterMaxDist);
    }

    private static void setDefaults() {
        vfradarCoreDataAddress = "http://10.10.10.10:60002/full";
        centerPosition = new LatLon(52.278788, 6.899626);
        siteElevation = 0;
        maxValidRxAge = 15;
        connectionUpdateInterval = 500;
        dataFolder = "sdcard/VFRadar";
        heightUnit = HeightUnits.Feet;
        verticalRateUnit = VerticalRateUnits.FeetPerMinute;
        showGeoFences = true;
        filterEnabled = false;
        filterMaxAlt = 40000;
        filterMaxDist = 240;
    }

    private static int getInt(Context context, int resouceKeyId, int defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = context.getResources().getString(resouceKeyId);
        String val = preferences.getString(preferenceKey, String.valueOf(defaultValue));
        return Integer.parseInt(val);
    }

    private static int getIntFromStringPreference(Context context, int resouceKeyId, Integer defaultValue) {
        String value = getString(context, resouceKeyId, defaultValue.toString());
        return Integer.parseInt(value);
    }

    private static String getString(Context context, int resouceKeyId, String defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = context.getResources().getString(resouceKeyId);
        return preferences.getString(preferenceKey, defaultValue);
    }

    private static Boolean getBoolean(Context context, int resouceKeyId, boolean defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = context.getResources().getString(resouceKeyId);
        return preferences.getBoolean(preferenceKey, defaultValue);
    }

    private static LatLon getLatLon(Context context, int resouceKeyId, LatLon defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = context.getResources().getString(resouceKeyId);
        return LatLon.parseLatLon(preferences.getString(preferenceKey, defaultValue.toString()));
    }

    private static void setInt(Context context, int resourceKeyId, int value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(context.getString(resourceKeyId), value);
        editor.commit();
    }

    private static void setIntToStringPreference(Context context, int resourceKeyId, Integer value) {
        setString(context, resourceKeyId, value.toString());
    }

    private static void setString(Context context, int resourceKeyId, String value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(context.getString(resourceKeyId), value);
        editor.commit();
    }

    private static void setLatLon(Context context, int resourceKeyId, LatLon value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(context.getString(resourceKeyId), value.toString());
        editor.commit();
    }

}
