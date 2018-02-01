package com.joostit.vfradar.site;

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
 * Created by Joost on 31-1-2018.
 */

public class SiteDataLoader {

    public static final String xmlExtensionRegex = "^([^\\s]+(\\.(?i)(xml))$)";
    public static final String siteDataFolder = "siteData/";
    private static final String ns = null;

    public List<SiteDataFile> loadSiteDataFiles() {

        List<SiteDataFile> allFiles = new ArrayList<>();

        File rootDir = new File(SysConfig.getDataFolder());
        File geoDataDir = new File(rootDir, siteDataFolder);

        File[] xmlFiles = geoDataDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().matches(xmlExtensionRegex);
            }
        });

        for (File xmlFile : xmlFiles) {
            try {
                SiteDataFile loaded = loadFile(xmlFile);
                if (loaded != null) {
                    allFiles.add(loaded);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return allFiles;
    }


    public SiteDataFile loadFile(File toLoad) {
        SiteDataFile loadedFile = null;

        try {
            InputStream inStream = new FileInputStream(toLoad);

            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inStream, null);
                parser.nextTag();
                loadedFile = getDataFileContents(parser);
                loadedFile.fileName = toLoad.getName();
            } finally {
                inStream.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            loadedFile = null;
        }

        return loadedFile;
    }

    private SiteDataFile getDataFileContents(XmlPullParser parser) throws XmlPullParserException, IOException {
        SiteDataFile retVal = new SiteDataFile();

        parser.require(XmlPullParser.START_TAG, ns, "vfradarSiteData");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();

            if (elementName.equals("fileInfo")) {
                readFileInfoElement(parser, retVal);
            } else if (elementName.equals("reportingPoints")) {
                readReportingPointsElement(parser, retVal);
            } else if (elementName.equals("runways")) {
                readRunwaysElement(parser, retVal);
            } else if (elementName.equals("routes")) {
                readRoutesElement(parser, retVal);
            } else {
                XmlParse.skip(parser);
            }
        }
        return retVal;
    }

    private void readRoutesElement(XmlPullParser parser, SiteDataFile dataFile) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "routes");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            if (elementName.equals("route")) {
                RouteLine rLine = readRouteElement(parser);
                if (rLine != null) {
                    dataFile.routes.add(rLine);
                }
            } else {
                XmlParse.skip(parser);
            }
        }
    }

    private RouteLine readRouteElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        RouteLine retVal = new RouteLine();
        parser.require(XmlPullParser.START_TAG, ns, "route");
        retVal.name = parser.getAttributeValue(null, "name");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            if (elementName.equals("point")) {
                retVal.points.add(readPointElement(parser));
            } else {
                XmlParse.skip(parser);
            }
        }
        return retVal;
    }


    private void readFileInfoElement(XmlPullParser parser, SiteDataFile dataFile) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "fileInfo");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();

            if (elementName.equals("name")) {
                dataFile.name = XmlParse.readElementText(parser, "name");
            } else if (elementName.equals("author")) {
                dataFile.author = XmlParse.readElementText(parser, "author");
            } else if (elementName.equals("description")) {
                dataFile.description = XmlParse.readElementText(parser, "description");
            } else if (elementName.equals("lastUpdated")) {
                dataFile.lastUpdated = XmlParse.readElementText(parser, "lastUpdated");
            } else {
                XmlParse.skip(parser);
            }
        }
    }

    private void readRunwaysElement(XmlPullParser parser, SiteDataFile dataFile) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "runways");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            if (elementName.equals("runway")) {
                Runway rway = readRunwayElement(parser);
                if (rway != null) {
                    dataFile.runways.add(rway);
                }
            } else {
                XmlParse.skip(parser);
            }
        }
    }

    private Runway readRunwayElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        Runway retVal = new Runway();
        parser.require(XmlPullParser.START_TAG, ns, "runway");
        retVal.name = parser.getAttributeValue(null, "name");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            if (elementName.equals("width")) {
                retVal.widthM = Integer.parseInt(XmlParse.readText(parser));
            } else if (elementName.equals("from")) {
                retVal.nameA = parser.getAttributeValue(null, "name");
                parser.next();
                parser.next();
                retVal.pointA = readPointElement(parser);
                parser.next();
                parser.next();
            } else if (elementName.equals("to")) {
                retVal.nameB = parser.getAttributeValue(null, "name");
                parser.next();
                parser.next();
                retVal.pointB = readPointElement(parser);
                parser.next();
                parser.next();
            } else {
                XmlParse.skip(parser);
            }
        }
        return retVal;
    }

    private void readReportingPointsElement(XmlPullParser parser, SiteDataFile dataFile) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "reportingPoints");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            if (elementName.equals("reportingPoint")) {

                ReportingPoint rPoint = readReportingPointElement(parser);
                if (rPoint != null) {
                    dataFile.reportingPoints.add(rPoint);
                }
            } else {
                XmlParse.skip(parser);
            }
        }
    }

    private ReportingPoint readReportingPointElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        ReportingPoint retVal = new ReportingPoint();
        parser.require(XmlPullParser.START_TAG, ns, "reportingPoint");
        retVal.name = parser.getAttributeValue(null, "name");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            if (elementName.equals("point")) {
                retVal.position = readPointElement(parser);
            } else {
                XmlParse.skip(parser);
            }
        }
        return retVal;
    }

    private LatLon readPointElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "point");
        String coordinate = XmlParse.readText(parser);
        LatLon retVal = LatLon.parseLatLon(coordinate);
        return retVal;
    }

}
