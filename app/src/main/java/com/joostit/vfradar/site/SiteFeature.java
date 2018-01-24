package com.joostit.vfradar.site;

/**
 * Created by Joost on 24-1-2018.
 */

public abstract class SiteFeature {

    private SiteFeatureTypes type;

    public SiteFeature(SiteFeatureTypes type){
        this.type = type;
    }

    public SiteFeatureTypes getType(){
        return type;
    }

}
