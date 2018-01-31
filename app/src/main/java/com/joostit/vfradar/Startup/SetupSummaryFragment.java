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
 * Use the {@link SetupSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetupSummaryFragment extends Fragment {

    private OnStartupFragmentInteractionListener mListener;

    public SetupSummaryFragment() {
        // Required empty public constructor
    }


    public static SetupSummaryFragment newInstance() {
        SetupSummaryFragment fragment = new SetupSummaryFragment();
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
        View view = inflater.inflate(R.layout.fragment_setup_summary, container, false);

        Button button = view.findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.userFinishesSetup();
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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
