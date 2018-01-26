package com.joostit.vfradar;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geodata.GeoObject;
import com.joostit.vfradar.radardrawing.RadarView;
import com.joostit.vfradar.site.SiteFeature;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RadarViewFragment.OnRadarViewInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RadarViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RadarViewFragment extends Fragment implements RadarView.OnRadarViewInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnRadarViewInteractionListener mListener;

    public RadarViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RadarViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RadarViewFragment newInstance(String param1, String param2) {
        RadarViewFragment fragment = new RadarViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRadarViewInteractionListener {
        void onAircraftSelected(Integer trackid);
    }
}
