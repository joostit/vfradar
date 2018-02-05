package com.joostit.vfradar.geo;

import android.util.Xml;

import com.joostit.vfradar.config.SysConfig;
import com.joostit.vfradar.geo.geofencing.FencedArea;
import com.joostit.vfradar.utilities.XmlParse;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 25-1-2018.
 */

public class GeoDataLoader extends KmlLoader<GeoObject> {

    public static final String geoDataFolder = "GeoData/";

    public List<GeoObject> loadAllFilesInFolder() {
        List<GeoObject> retVal = new ArrayList<>();

        File rootDir = new File(SysConfig.getDataFolder());
        File geoDataDir = new File(rootDir, geoDataFolder);

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

    @Override
    protected GeoObject createNewPlaceholderObject() {
        return new GeoObject();
    }
}
