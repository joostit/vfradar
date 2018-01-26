package com.joostit.vfradar.site;

import com.joostit.vfradar.geodata.GeoDataLoader;
import com.joostit.vfradar.geodata.GeoObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 24-1-2018.
 */

public class SiteDataLoader {

    private List<SiteFeature> features = new ArrayList<>();
    private List<GeoObject> geoData = new ArrayList<>();
    private GeoDataLoader geoLoader = new GeoDataLoader();

    public SiteDataLoader(){

    }

    public void loadData(){
        SiteEHTW ehtw = new SiteEHTW();
        features.addAll(ehtw.getSite());

        geoData = geoLoader.Load("UrbanAreasNL_2017_12.kml");
    }


    public List<SiteFeature> getSite(){
            return new ArrayList<SiteFeature>(features);
    }

    public List<GeoObject> getGeoData(){
        return geoData;
    }


}
