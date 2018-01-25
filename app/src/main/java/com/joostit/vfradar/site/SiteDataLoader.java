package com.joostit.vfradar.site;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 24-1-2018.
 */

public class SiteDataLoader {

    private List<SiteFeature> features = new ArrayList<>();

    private GeoDataLoader geoLoader = new GeoDataLoader();

    public SiteDataLoader(){

    }

    public void loadData(){
        SiteEHTW ehtw = new SiteEHTW();
        features.addAll(ehtw.getSite());

        geoLoader.Load("Top10NL-Plaats_kern");
    }


    public List<SiteFeature> getSite(){
            return new ArrayList<SiteFeature>(features);
    }


}
