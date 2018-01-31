package com.joostit.vfradar.data;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Joost on 22-1-2018.
 */

public class VFRadarCoreReaderTask extends AsyncTask<URL, Integer, List<AircraftState>> {

    private AircraftDataListener listener;


    public VFRadarCoreReaderTask(AircraftDataListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<AircraftState> doInBackground(URL... params) {

        String jsonString = null;
        List<AircraftState> retVal = null;
        StringBuilder result = new StringBuilder();
        AircraftDataBuilder dataBuilder = new AircraftDataBuilder();

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonString != null) {
            retVal = dataBuilder.parseJson(jsonString);
        }

        return retVal;

    }

    @Override
    protected void onPostExecute(List<AircraftState> ac) {

        listener.newAircraftDataReceived(ac);

    }
}
