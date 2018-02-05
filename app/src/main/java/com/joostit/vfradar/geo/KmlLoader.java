package com.joostit.vfradar.geo;

import android.util.Xml;
import com.joostit.vfradar.utilities.XmlParse;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 5-2-2018.
 */

public abstract class KmlLoader <placeHolderObject extends GeoObject> {

    public static final String kmlExtensionRegex = "^([^\\s]+(\\.(?i)(kml))$)";
    protected static final String ns = null;


    public List<placeHolderObject> loadFile(File kmlFile) {

        List<placeHolderObject> geoObjects = new ArrayList<>();
        try {
            InputStream inStream = new FileInputStream(kmlFile);

            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inStream, null);
                parser.nextTag();
                geoObjects = readXmlDocument(parser);
            } finally {
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(placeHolderObject geoObject : geoObjects){
            geoObject.updateBoundingRect();
        }

        return geoObjects;
    }


    private List<placeHolderObject> readXmlDocument(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<placeHolderObject> entries = null;

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

    private List<placeHolderObject> readDocument(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = null;

        parser.require(XmlPullParser.START_TAG, ns, "Document");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Folder")) {
                entries = readFolder(parser);
            } else if (name.equals("Placemark")) {
                placeHolderObject newEntry = readPlacemark(parser);
                if (newEntry != null) {
                    entries = new ArrayList<>();
                    entries.add(newEntry);
                }
            } else {
                XmlParse.skip(parser);
            }
        }
        return entries;
    }

    private List<placeHolderObject> readFolder(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<placeHolderObject> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "Folder");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Placemark")) {
                placeHolderObject newEntry = readPlacemark(parser);
                if (newEntry != null) {
                    entries.add(newEntry);
                }
            } else {
                XmlParse.skip(parser);
            }
        }
        return entries;
    }


    private placeHolderObject readPlacemark(XmlPullParser parser) throws XmlPullParserException, IOException {

        placeHolderObject newEntry = createNewPlaceholderObject();

        parser.require(XmlPullParser.START_TAG, ns, "Placemark");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String elementName = parser.getName();
            boolean isParsed = false;

            if (elementName.equals("name")) {
                newEntry.name = XmlParse.readText(parser);
                isParsed = true;
            } else if (elementName.equals("Polygon")) {
                readPolygonData(parser, newEntry);
                isParsed = true;
            } else if (elementName.equals("MultiGeometry")) {
                readMultiGeometryData(parser, newEntry);
                isParsed = true;
            }
            else{
                isParsed = parseCustomPlaceholderElement(parser, elementName, newEntry);
            }

            if(!isParsed)
            {
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

    private void readMultiGeometryData(XmlPullParser parser, placeHolderObject newEntry) throws XmlPullParserException, IOException {

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

    private void readPolygonData(XmlPullParser parser, placeHolderObject newEntry) throws XmlPullParserException, IOException {

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

    private void readInnerBoundaryIsData(XmlPullParser parser, placeHolderObject newEntry) throws XmlPullParserException, IOException {

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

    private void readOuterBoundaryIsData(XmlPullParser parser, placeHolderObject newEntry) throws XmlPullParserException, IOException {

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


    private void addLinearRingData(XmlPullParser parser, placeHolderObject newEntry) throws XmlPullParserException, IOException {

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


    protected abstract placeHolderObject createNewPlaceholderObject();

    protected boolean parseCustomPlaceholderElement(XmlPullParser parser, String elementName, placeHolderObject geoObject)  throws XmlPullParserException, IOException {
        return false;
    }

}
