package com.joostit.vfradar.InfoBar;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.joostit.vfradar.R;
import com.joostit.vfradar.data.AircraftTrackingUpdate;


public class MenuBarFragment extends Fragment {

    private OnMenuBarFragmentInteractionListener mListener;
    private ImageButton imageButton;
    private View rootView;

    public MenuBarFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_menu_bar, container, false);

        addListenerOnButton();

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnMenuBarFragmentInteractionListener) {
            mListener = (OnMenuBarFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMenuBarFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addListenerOnButton() {

        imageButton = (ImageButton) rootView.findViewById(R.id.preferencesButton);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mListener.onPreferencesPressed();
            }

        });

    }

    public void updateAircraft(AircraftTrackingUpdate lastUpdateState) {
        StatusBarView statusView = getView().findViewById(R.id.statusBarView);
        statusView.updateStatus(lastUpdateState);
    }

    public interface OnMenuBarFragmentInteractionListener {

        void onPreferencesPressed();
    }


}
