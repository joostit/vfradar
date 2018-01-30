package com.joostit.vfradar.Startup;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.joostit.vfradar.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStartupFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetupSiteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetupSiteFragment extends Fragment {

    private OnStartupFragmentInteractionListener mListener;

    public SetupSiteFragment() {
        // Required empty public constructor
    }


    public static SetupSiteFragment newInstance() {
        SetupSiteFragment fragment = new SetupSiteFragment();

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
        View view =  inflater.inflate(R.layout.fragment_setup_site, container, false);

        Button button = view.findViewById(R.id.nextPageButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.userSelectedNextTab();
            }
        });

        return view;
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
