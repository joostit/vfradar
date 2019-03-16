package com.joostit.vfradar;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.joostit.vfradar.settings.AppCompatPreferenceActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 25-1-2018.
 */

public class PermissionHelper {

    // Storage Permissions
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_LOCATION = 2;
    private static String[] REQUIRED_STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static String[] REQUIRED_LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static int PERMISSIONS_REQUEST_CODE = 3626;

    private static String[] AppPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    public static boolean hasAllNeededPermissions(Activity activity){
        return (getMissingPermissions(activity).size() == 0);
    }

    private static List<String> getMissingPermissions(Activity activity){
        List<String> permissionsNeeded = new ArrayList<>();

        for(String perm : AppPermissions){
            if(ContextCompat.checkSelfPermission(activity, perm) != PackageManager.PERMISSION_GRANTED){
                permissionsNeeded.add(perm);
            }
        }

        return permissionsNeeded;
    }

    public static boolean getAppPermissions(Activity activity){

        boolean allPermissionsWereGranted = false;

        List<String> permissionsNeeded = getMissingPermissions(activity);

        if(!permissionsNeeded.isEmpty()){

            allPermissionsWereGranted = false;

            ActivityCompat.requestPermissions(activity,
                                                permissionsNeeded.toArray(new String[permissionsNeeded.size()]),
                                                PERMISSIONS_REQUEST_CODE);
        }
        else{
            allPermissionsWereGranted = true;
        }

        return allPermissionsWereGranted;
    }


    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyWriteStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    REQUIRED_STORAGE_PERMISSIONS,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void verifyLocationAndGpsPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    REQUIRED_LOCATION_PERMISSIONS,
                    REQUEST_LOCATION
            );
        }
    }


}
