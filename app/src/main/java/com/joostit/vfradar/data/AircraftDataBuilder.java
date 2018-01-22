package com.joostit.vfradar.data;

import com.joostit.vfradar.utilities.StringValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 22-1-2018.
 */

public class AircraftDataBuilder {


    public List<AircraftState> parseJson(String jSon){

        List<AircraftState> retVal = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jSon);

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonAc = jsonArray.getJSONObject(i);
                AircraftState ac = buildAircraftObject(jsonAc);
                if(ac != null){
                    retVal.add(ac);
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return retVal;
    }

    private AircraftState buildAircraftObject(JSONObject jsonAc) {
        AircraftState ac = new AircraftState();

        try {

            ac.trackId = jsonAc.getInt(JsonKeys.trackId);
            ac.flarmId = getNullableString(jsonAc, JsonKeys.flarmId);
            ac.icao24 = getNullableString(jsonAc, JsonKeys.icao24);
            ac.ognId = getNullableString(jsonAc, JsonKeys.ognId);
            ac.reg = getNullableString(jsonAc, JsonKeys.reg);
            ac.cn = getNullableString(jsonAc, JsonKeys.cn);
            ac.callSign = getNullableString(jsonAc, JsonKeys.callSign);
            ac.model = getNullableString(jsonAc, JsonKeys.model);
            ac.type = getNullableString(jsonAc, JsonKeys.type);
            ac.lat = jsonAc.getDouble(JsonKeys.lat);
            ac.lon = jsonAc.getDouble(JsonKeys.lon);
            ac.speed = getNullableInt(jsonAc, JsonKeys.speed);
            ac.track = getNullableInt(jsonAc, JsonKeys.track);
            ac.turnRate = getNullableDouble(jsonAc, JsonKeys.turnRate);
            ac.alt = getNullableInt(jsonAc, JsonKeys.alt);
            ac.vRate = getNullableDouble(jsonAc, JsonKeys.vRate);
            ac.rxStation = getNullableString(jsonAc, JsonKeys.rxStation);
            ac.rxChannel = getNullableString(jsonAc, JsonKeys.rxChannel);
            ac.squawk = getNullableInt(jsonAc, JsonKeys.squawk);

        }catch (JSONException e){
            e.printStackTrace();
            ac = null;
        }
        return ac;
    }

    private String getNullableString(JSONObject jsonAc, String key) throws JSONException {
        String raw = jsonAc.optString(key);

        if(StringValue.nullOrEmpty(raw)){
            return null;
        }
        else{
            return raw;
        }
    }


    private Double getNullableDouble(JSONObject jsonAc, String key) throws JSONException {
        String raw = jsonAc.optString(key);

        if(StringValue.nullOrEmpty(raw)){
            return null;
        }
        else{
            return Double.valueOf(raw);
        }
    }

    private Integer getNullableInt(JSONObject jsonAc, String key) throws JSONException {
        String raw = jsonAc.optString(key);

        if(StringValue.nullOrEmpty(raw)){
            return null;
        }
        else{
            return Integer.valueOf(raw);
        }
    }

}
