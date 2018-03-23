package com.joostit.vfradar.scenarios;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 23-3-2018.
 */

public class ScenarioLoader {

    public List<Scenario> getAvailableScenarios(){
        List<Scenario> retVal = new ArrayList<>();

        retVal.add(new Scenario("EHTW Gliding test on 05.xml"));
        retVal.add(new Scenario("EHTW Gliding test on 23.xml"));
        retVal.add(new Scenario("EHTW GA test on RWY05.xml"));
        retVal.add(new Scenario("EHTW GA test on RWY23.xml"));
        retVal.add(new Scenario("EHTW Gliding test on 11.xml"));
        retVal.add(new Scenario("EHTW Gliding test on 29.xml"));
        retVal.add(new Scenario("EHTW GA test on RWY11.xml"));
        retVal.add(new Scenario("EHTW GA test on RWY29.xml"));

        return retVal;
    }

}
