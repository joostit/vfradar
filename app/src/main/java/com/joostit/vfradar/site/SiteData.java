package com.joostit.vfradar.site;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 24-1-2018.
 */

public class SiteData {

    private List<SiteFeature> features = new ArrayList<>();


    public SiteData(){
        SiteEHTW ehtw = new SiteEHTW();
        features.addAll(ehtw.getSite());
    }

    public List<SiteFeature> getSite(){
            return new ArrayList<SiteFeature>(features);
    }


}
