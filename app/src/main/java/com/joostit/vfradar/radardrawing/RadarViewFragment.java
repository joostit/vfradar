package com.joostit.vfradar.radardrawing;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joostit.vfradar.R;
import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geodata.GeoObject;
import com.joostit.vfradar.radardrawing.RadarView;
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

    public void UpdateAircraft(List<TrackedAircraft> ac) {
        RadarView rView = getView().findViewById(R.id.radarView);
        rView.UpdateAircraft(ac);
    }

    @Override
    public void onUserSelectedAircraftChanged(Integer trackId) {
        mListener.onAircraftSelected(trackId);
    }

    public void UpdateSiteFeatures(List<SiteFeature> site) {
        RadarView rView = getView().findViewById(R.id.radarView);
        rView.updateSiteFeatures(site);
    }

    public void updateGeoData(List<GeoObject> geoData) {
        RadarView rView = getView().findViewById(R.id.radarView);
        rView.updateGeoData(geoData);
    }

    public void selectAircraft(Integer trackId){
        RadarView rView = getView().findViewById(R.id.radarView);
        rView.selectAircraft(trackId);
    }

    public interface OnRadarViewInteractionListener {
        void onAircraftSelected(Integer trackid);
    }
}
