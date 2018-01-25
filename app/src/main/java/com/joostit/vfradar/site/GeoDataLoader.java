package com.joostit.vfradar.site;

import android.os.Environment;

import java.io.File;

/**
 * Created by Joost on 25-1-2018.
 */

public class GeoDataLoader {

    public static final String geoDataPath = "/VFRadar/GeoData/";

    public GeoDataLoader() {

    }


    public void Load(String dataSetName) {

        try {

            File extDir = Environment.getExternalStorageDirectory();
            if (extDir.exists()) {
                System.out.println("exists");
            }

            File dataDir = new File(extDir, geoDataPath);
            if (dataDir.exists()) {
                System.out.println("exists");
            } else {
                dataDir.createNewFile();
            }

            File dataSetDir = new File(dataDir, dataSetName);
            if (dataSetDir.exists()) {
                System.out.println("exists");
            }





        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
