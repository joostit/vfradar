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

public class VFRadarCoreReaderTask extends AsyncTask<URL, Integer, String> {

    private AircraftDataListener listener;

    public VFRadarCoreReaderTask(AircraftDataListener listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(URL... params) {

        String resultString = null;
        StringBuilder result = new StringBuilder();

        try {
            URL url = params[0];
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            resultString = result.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultString;

    }

    @Override
    protected void onPostExecute(String jSon) {

        List<TrackedAircraft> ac = new ArrayList<>();
        if(jSon != null){
            System.out.println(jSon);
        }
        //Do something with the JSON string

        listener.newAircraftDataReceived(ac);

    }
}
