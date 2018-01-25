package com.joostit.vfradar.geodata;

import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 25-1-2018.
 */

public class GeoDataLoader {

    public static final int minimumPopulation = 10000;

    public static final String geoDataPath = "/VFRadar/GeoData/";

    private static final String ns = null;

    public GeoDataLoader() {

    }


    public List Load(String kmlFileName) {

        try {
            File extDir = Environment.getExternalStorageDirectory();
            File dataDir = new File(extDir, geoDataPath);
            File kmlFile = new File(dataDir, kmlFileName);

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
        return null;
    }


    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = null;

        parser.require(XmlPullParser.START_TAG, ns, "kml");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Document")) {
                entries = readDocument(parser);
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private List readDocument(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = null;

        parser.require(XmlPullParser.START_TAG, ns, "Document");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Folder")) {
                entries = readFolder(parser);
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private List readFolder(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "Folder");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Placemark")) {
                KmlPlacemarkEntry newentry = readPlacemark(parser);
                if(newentry != null) {
                    entries.add(newentry);
                }
            } else {
                skip(parser);
            }
        }
        return entries;
    }




    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private KmlPlacemarkEntry readPlacemark(XmlPullParser parser) throws XmlPullParserException, IOException {

        KmlPlacemarkEntry newEntry = new KmlPlacemarkEntry();

        parser.require(XmlPullParser.START_TAG, ns, "Placemark");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("ExtendedData")) {
                readExtendedData(parser, newEntry);

                if(newEntry.population < minimumPopulation){
                    newEntry = null;
                }
            } else {
                skip(parser);
            }

            System.out.println(name);
        }
        return newEntry;
    }


    private void readExtendedData(XmlPullParser parser, KmlPlacemarkEntry newEntry) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "ExtendedData");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("SchemaData")) {
                readSchemaData(parser, newEntry);
            } else {
                skip(parser);
            }
        }

    }

    private void readSchemaData(XmlPullParser parser, KmlPlacemarkEntry newEntry) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "SchemaData");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getAttributeValue(null,"name");
            if(elementName.equalsIgnoreCase("population")) {
                String populationString = readSimpleDataText(parser);
                newEntry.population = Integer.parseInt(populationString);

            } else if(elementName.equalsIgnoreCase("name")) {
                newEntry.name = readSimpleDataText(parser);

            } else
            {
                skip(parser);
            }
        }

    }

    private String readSimpleDataText(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "SimpleData");
        String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "SimpleData");
        return text;

    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
