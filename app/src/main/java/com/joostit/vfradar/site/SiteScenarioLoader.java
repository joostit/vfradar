package com.joostit.vfradar.site;

import com.joostit.vfradar.geodata.GeoDataLoader;
import com.joostit.vfradar.geodata.GeoObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 24-1-2018.
 */

public class SiteScenarioLoader {

    private List<SiteFeature> features = new ArrayList<>();
    private List<GeoObject> geoData = new ArrayList<>();
    private GeoDataLoader geoLoader = new GeoDataLoader();

    public SiteScenarioLoader() {

    }

    public void loadData() {
        SiteDataLoader siteLoader = new SiteDataLoader();
        List<SiteDataFile> siteDataFiles = siteLoader.loadSiteDataFiles();

        for (SiteDataFile dataFile : siteDataFiles) {
            features.addAll(dataFile.getAllFeatures());
        }

        geoData = geoLoader.loadAllFilesInFolder();

        for (GeoObject obj: geoData ) {
            obj.recalculateBoundingRect();
        }
    }

    public List<SiteFeature> getSite() {
        return new ArrayList<SiteFeature>(features);
    }

    public List<GeoObject> getGeoData() {
        return geoData;
    }


}
