package com.joostit.vfradar.Startup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;


import com.joostit.vfradar.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStartupFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetupLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetupLocationFragment extends Fragment {

    private OnStartupFragmentInteractionListener mListener;

    public SetupLocationFragment() {
        // Required empty public constructor
    }

    public static SetupLocationFragment newInstance() {
        SetupLocationFragment fragment = new SetupLocationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setup_location, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStartupFragmentInteractionListener) {
            mListener = (OnStartupFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStartupFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
