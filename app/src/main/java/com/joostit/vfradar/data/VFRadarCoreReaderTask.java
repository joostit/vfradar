package com.joostit.vfradar.data;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 22-1-2018.
 */

public class VFRadarCoreReaderTask extends AsyncTask<URL, Integer, AircraftDataUpdate> {

    private AircraftDataListener listener;


    public VFRadarCoreReaderTask(AircraftDataListener listener) {
        this.listener = listener;
    }

    @Override
    protected AircraftDataUpdate doInBackground(URL... params) {

        String jsonString = null;
        AircraftDataUpdate retVal = null;
        StringBuilder result = new StringBuilder();
        AircraftDataBuilder dataBuilder = new AircraftDataBuilder();
        boolean connectionSuccess = false;
        boolean parseSuccess = false;
        List<AircraftState> jsonResult = new ArrayList<>();

        try {
            URL url = params[0];
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            jsonString = result.toString();
            connectionSuccess = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonString != null) {

            try {
                jsonResult = dataBuilder.parseJson(jsonString);
                parseSuccess = true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        retVal = new AircraftDataUpdate(jsonResult, connectionSuccess && parseSuccess);

        return retVal;

    }

    @Override
    protected void onPostExecute(AircraftDataUpdate ac) {

        listener.newAircraftDataReceived(ac);

    }
}
