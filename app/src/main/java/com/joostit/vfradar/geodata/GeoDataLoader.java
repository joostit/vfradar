package com.joostit.vfradar.geodata;

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
 * Created by Joost on 25-1-2018.
 */

public class GeoDataLoader {

    public static final String kmlExtensionRegex = "^([^\\s]+(\\.(?i)(kml))$)";
    public static final String geoDataFolder = "GeoData/";
    private static final String ns = null;

    public GeoDataLoader() {

    }


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


    public List<GeoObject> loadFile(File kmlFile) {

        try {
            InputStream inStream = new FileInputStream(kmlFile);

            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inStream, null);
                parser.nextTag();
                return readFeed(parser);
            } finally {
                inStream.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    private List<GeoObject> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<GeoObject> entries = null;

        parser.require(XmlPullParser.START_TAG, ns, "kml");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Document")) {
                entries = readDocument(parser);
            } else {
                XmlParse.skip(parser);
            }
        }
        return entries;
    }

    private List<GeoObject> readDocument(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = null;

        parser.require(XmlPullParser.START_TAG, ns, "Document");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Folder")) {
                entries = readFolder(parser);
            } else {
                XmlParse.skip(parser);
            }
        }
        return entries;
    }

    private List<GeoObject> readFolder(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<GeoObject> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "Folder");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Placemark")) {
                GeoObject newEntry = readPlacemark(parser);
                if (newEntry != null) {
                    entries.add(newEntry);
                }
            } else {
                XmlParse.skip(parser);
            }
        }
        return entries;
    }


    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private GeoObject readPlacemark(XmlPullParser parser) throws XmlPullParserException, IOException {

        GeoObject newEntry = new GeoObject();

        parser.require(XmlPullParser.START_TAG, ns, "Placemark");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("ExtendedData")) {
                readExtendedData(parser, newEntry);
            } else if (name.equals("Polygon")) {
                readPolygonData(parser, newEntry);
            } else if (name.equals("MultiGeometry")) {
                readMultiGeometryData(parser, newEntry);
            } else {
                XmlParse.skip(parser);
            }

        }

        if (newEntry != null) {
            if ((newEntry.shape == null)
                    || (newEntry.shape.polygons.size() == 0)) {
                newEntry = null;
            }
        }

        return newEntry;
    }


    private void readMultiGeometryData(XmlPullParser parser, GeoObject newEntry) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "MultiGeometry");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Polygon")) {
                readPolygonData(parser, newEntry);
            } else {
                XmlParse.skip(parser);
            }
        }
    }

    private void readPolygonData(XmlPullParser parser, GeoObject newEntry) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "Polygon");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            if (elementName.equals("outerBoundaryIs")) {
                readOuterBoundaryIsData(parser, newEntry);
            } else if (elementName.equals("innerBoundaryIs")) {
                readInnerBoundaryIsData(parser, newEntry);
            } else {
                XmlParse.skip(parser);
            }
        }

    }

    private void readInnerBoundaryIsData(XmlPullParser parser, GeoObject newEntry) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "innerBoundaryIs");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            if (elementName.equals("LinearRing")) {
                addLinearRingData(parser, newEntry);
            } else {
                XmlParse.skip(parser);
            }

        }
    }

    private void readOuterBoundaryIsData(XmlPullParser parser, GeoObject newEntry) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "outerBoundaryIs");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            if (elementName.equals("LinearRing")) {
                addLinearRingData(parser, newEntry);
            } else {
                XmlParse.skip(parser);
            }

        }
    }


    private void addLinearRingData(XmlPullParser parser, GeoObject newEntry) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "LinearRing");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String inLinearRingName = parser.getName();
            if (inLinearRingName.equals("coordinates")) {
                GeoPolygon points = readCoordinates(parser);
                newEntry.shape.polygons.add(points);
            } else {
                XmlParse.skip(parser);
            }
        }
    }

    private GeoPolygon readCoordinates(XmlPullParser parser) throws XmlPullParserException, IOException {
        GeoPolygon retVal = new GeoPolygon();

        parser.require(XmlPullParser.START_TAG, ns, "coordinates");
        String coordinateString = XmlParse.readText(parser);
        String[] coordinates = coordinateString.trim().split("\\s+");

        for (String latlon : coordinates) {
            String[] latLonSplit = latlon.trim().split(",");
            Double lon = Double.parseDouble(latLonSplit[0]);
            Double lat = Double.parseDouble(latLonSplit[1]);
            retVal.points.add(new LatLon(lat, lon));
        }

        return retVal;
    }


    private void readExtendedData(XmlPullParser parser, GeoObject newEntry) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "ExtendedData");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("SchemaData")) {
                readSchemaData(parser, newEntry);
            } else {
                XmlParse.skip(parser);
            }
        }

    }

    private void readSchemaData(XmlPullParser parser, GeoObject newEntry) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "SchemaData");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getAttributeValue(null, "name");
            if (elementName.equalsIgnoreCase("name")) {
                newEntry.name = XmlParse.readElementText(parser, "SimpleData");
            } else {
                XmlParse.skip(parser);
            }
        }

    }


}
