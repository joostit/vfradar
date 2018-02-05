package com.joostit.vfradar.radardrawing;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joostit.vfradar.R;
import com.joostit.vfradar.data.AircraftTrackingUpdate;
import com.joostit.vfradar.geo.GeoObject;
import com.joostit.vfradar.site.SiteFeature;

import java.util.List;


public class RadarViewFragment extends Fragment implements RadarView.OnRadarViewInteractionListener {

    private OnRadarViewInteractionListener mListener;

    public RadarViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radar_view, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

        RadarView rView = getView().findViewById(R.id.radarView);
        rView.AttachSelectionListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRadarViewInteractionListener) {
            mListener = (OnRadarViewInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRadarViewInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateAircraft(AircraftTrackingUpdate lastUpdateState) {
        RadarView rView = getView().findViewById(R.id.radarView);
        rView.updateAircraft(lastUpdateState);
    }

    @Override
    public void onUserSelectedAircraftChanged(Integer trackId) {
        mListener.onAircraftSelected(trackId);
    }

    public void UpdateSiteFeatures(List<SiteFeature> site) {
        RadarView rView = getView().findViewById(R.id.radarView);
        rView.updateSiteFeatures(site);
    }

    public void updateGeoData(List<GeoObject> geoData, List<GeoObject> geoFenceData) {
        RadarView rView = getView().findViewById(R.id.radarView);
        rView.updateGeoData(geoData, geoFenceData);
    }

    public void selectAircraft(Integer trackId) {
        RadarView rView = getView().findViewById(R.id.radarView);
        rView.selectAircraft(trackId);
    }

    public interface OnRadarViewInteractionListener {
        void onAircraftSelected(Integer trackid);
    }
}
