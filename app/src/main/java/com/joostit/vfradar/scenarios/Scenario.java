package com.joostit.vfradar.scenarios;

/**
 * Created by Joost on 23-3-2018.
 */

public class Scenario {

    public String fileName;

    public Scenario(){};

    public Scenario(String fileName){
        this.fileName = fileName;
    }


    @Override
    public String toString() {
        if(fileName == null) {
            return super.toString();
        }
        else{
            return fileName;
        }
    }
}
