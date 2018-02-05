package com.joostit.vfradar.geo.geofencing;

import com.joostit.vfradar.config.SysConfig;
import com.joostit.vfradar.geo.GeoObject;
import com.joostit.vfradar.geo.KmlLoader;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 5-2-2018.
 */

public class GeoFenceLoader extends KmlLoader {

    public static final String geoFenceDataFolder = "GeoFences/";

    public List<GeoObject> loadAllFilesInFolder() {
        List<GeoObject> retVal = new ArrayList<>();

        File rootDir = new File(SysConfig.getDataFolder());
        File geoDataDir = new File(rootDir, geoFenceDataFolder);

        File[] kmlFiles = geoDataDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().matches(kmlExtensionRegex);
            }
        });

        for (File kmlFile : kmlFiles) {
            retVal.addAll(loadFile(kmlFile));
        }

        return retVal;
    }


}
