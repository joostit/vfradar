package com.joostit.vfradar.geo.geofencing;

import com.joostit.vfradar.config.SysConfig;
import com.joostit.vfradar.geo.GeoObject;
import com.joostit.vfradar.geo.KmlLoader;
import com.joostit.vfradar.utilities.XmlParse;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Joost on 5-2-2018.
 */

public class GeoFenceLoader extends KmlLoader<FencedArea> {

    public static final String geoFenceDataFolder = "GeoFences/";

    public List<FencedArea> loadAllFilesInFolder() {
        List<FencedArea> retVal = new ArrayList<>();

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


    @Override
    protected FencedArea createNewGenericObject() {
        return new FencedArea();
    }

    @Override
    protected boolean parseCustomPlaceholderElement(XmlPullParser parser, String elementName, FencedArea geoObject) throws XmlPullParserException, IOException {

        boolean isParsed = false;

        if (elementName.equals("top")) {
            String topString = XmlParse.readText(parser);
            geoObject.altitude.setTopM(parseDouble(topString));
            isParsed = true;
        } else if (elementName.equals("bottom")) {
            String bottomString = XmlParse.readText(parser);
            geoObject.altitude.setBottomM(parseDouble(bottomString));
            isParsed = true;
        }
        else if (elementName.equals("areaType")) {
            String areaType = XmlParse.readText(parser);
            geoObject.setAlertType(parseAreaType(areaType));
            isParsed = true;
        }

        return isParsed;
    }

    private FenceAlerts parseAreaType(String areaType) {

        switch (areaType.toLowerCase()){
            case "notification":
                return FenceAlerts.Notification;

            case "warning":
                return FenceAlerts.Warning;

            default:
                return FenceAlerts.Notification;
        }

    }


    private Double parseDouble(final String input){
        try{
            NumberFormat format = NumberFormat.getInstance(Locale.ROOT);
            Number number = format.parse(input);
            return number.doubleValue();

        }catch (ParseException e){
            e.printStackTrace();
            return null;
        }
    }
}
