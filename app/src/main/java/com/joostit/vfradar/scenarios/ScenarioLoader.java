package com.joostit.vfradar.scenarios;

import android.util.Xml;

import com.joostit.vfradar.config.SysConfig;
import com.joostit.vfradar.geo.LatLon;
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
 * Created by Joost on 23-3-2018.
 */

public class ScenarioLoader {

    public static final String scenarioFolder = "Scenarios/";
    public static final String xmlExtensionRegex = "^(.+(\\.(?i)(xml))$)";

    public List<Scenario> getAvailableScenarios() {


        List<Scenario> retVal = new ArrayList<>();

        File rootDir = new File(SysConfig.getDataFolder());
        File geoDataDir = new File(rootDir, scenarioFolder);

        File[] scenarioFiles = geoDataDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().matches(xmlExtensionRegex);
            }
        });

        for (File scenarioFile : scenarioFiles) {
            Scenario loaded = loadFile(scenarioFile);

            if(loaded == null){
                loaded = new Scenario();
                loaded.description = "ERROR while loading scenario file";
            }

            loaded.fileName = scenarioFile.getName();
            retVal.add(loaded);
        }

        return retVal;
    }


    protected static final String ns = null;


    public Scenario loadFile(File scenarioFile) {
        Scenario loadedScenario = null;

        try {
            InputStream inStream = new FileInputStream(scenarioFile);

            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inStream, null);
                parser.nextTag();
                loadedScenario = readFromRoot(parser);
            } finally {
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loadedScenario;

    }

    private Scenario readFromRoot(XmlPullParser parser) throws XmlPullParserException, IOException {
        Scenario loadedScenario = new Scenario();

        parser.require(XmlPullParser.START_TAG, ns, "vfradarScenario");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("fileInfo")) {
                readFileInfoElement(parser, loadedScenario);
            } else if (name.equals("siteDataFiles")) {
                readSiteDataFileNames(parser, loadedScenario);
            } else if (name.equals("GeoFenceFiles")) {
                readGeoFenceFileNames(parser, loadedScenario);
            } else if (name.equals("centerlocation")) {
                readCenterLocationElement(parser, loadedScenario);
            } else {
                XmlParse.skip(parser);
            }
        }
        return loadedScenario;
    }


    private void readFileInfoElement(XmlPullParser parser, Scenario scenario) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "fileInfo");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();

            if (elementName.equals("name")) {
                scenario.name = XmlParse.readElementText(parser, "name");
            } else if (elementName.equals("author")) {
                scenario.author = XmlParse.readElementText(parser, "author");
            } else if (elementName.equals("description")) {
                scenario.description = XmlParse.readElementText(parser, "description");
            } else if (elementName.equals("lastUpdated")) {
                scenario.lastUpdated = XmlParse.readElementText(parser, "lastUpdated");
            } else {
                XmlParse.skip(parser);
            }
        }
    }


    private void readSiteDataFileNames(XmlPullParser parser, Scenario scenario) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "siteDataFiles");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            if (elementName.equals("file")) {
                String fileName = readFileElement(parser);
                if (fileName != null) {
                    scenario.siteDataFiles.add(fileName);
                }
            } else {
                XmlParse.skip(parser);
            }
        }
    }

    private void readGeoFenceFileNames(XmlPullParser parser, Scenario scenario) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "GeoFenceFiles");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            if (elementName.equals("file")) {
                String fileName = readFileElement(parser);
                if (fileName != null) {
                    scenario.geoFenceFiles.add(fileName);
                }
            } else {
                XmlParse.skip(parser);
            }
        }
    }

    private void readCenterLocationElement(XmlPullParser parser, Scenario scenario) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "centerlocation");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            if (elementName.equals("point")) {

                LatLon centerPoint = readPointElement(parser);
                if (centerPoint != null) {
                    scenario.centerLocation = centerPoint;
                }
            } else {
                XmlParse.skip(parser);
            }
        }
    }

    private LatLon readPointElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "point");
        String coordinate = XmlParse.readText(parser);
        LatLon retVal = LatLon.parseLatLon(coordinate);
        return retVal;
    }

    private String readFileElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "file");
        String file = XmlParse.readText(parser);
        return file;
    }


}
