package com.joostit.vfradar.Startup;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


import com.joostit.vfradar.R;
import com.joostit.vfradar.SysConfig;
import com.joostit.vfradar.geo.LatLon;

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
    private Button nextButton;
    private EditText latLonTextBox;
    private int selectedLocationOption = -1;

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
        View view = inflater.inflate(R.layout.fragment_setup_location, container, false);

        nextButton = view.findViewById(R.id.nextPageButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextButtonClicked();
            }
        });

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.centerLocationRadioButtonsGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                newRadioButtonSelected(group, checkedId);
            }
        });


        latLonTextBox = (EditText) view.findViewById(R.id.latLonBox);
        latLonTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LatLonBoxTextChanged(s, start, before, count);
            }
        });


        latLonTextBox.setEnabled(false);
        nextButton.setEnabled(false);
        this.latLonTextBox.setText(SysConfig.getCenterPosition().toString());
        return view;
    }

    private void LatLonBoxTextChanged(CharSequence s, int start, int before, int count) {
        determineValidInputState();
    }


    private Boolean determineLatLonValid() {
        boolean latLonValid = true;
        String txt = latLonTextBox.getText().toString();
        try {
            LatLon ll = LatLon.parseLatLon(txt);
        } catch (Exception e) {
            latLonValid = false;
        }

        if(latLonValid) {
            latLonTextBox.setBackgroundColor(0x00000000);
        }
        else{
            latLonTextBox.setBackgroundColor(0xFF4d0000);
        }

        return latLonValid;
    }


    private void determineValidInputState() {

        boolean validIinput = false;

        switch (selectedLocationOption){
            case R.id.useGpsRadioButton:
                validIinput = true;
                break;
            case R.id.useLocationPickerRadioButton:
                validIinput = true;
                break;
            case R.id.useLatLonRadioButton:
                validIinput = determineLatLonValid();
                break;
        }

        nextButton.setEnabled(validIinput);
        mListener.allowPageSwitching(validIinput);
    }


    private void nextButtonClicked() {
        mListener.userSelectedNextTab();
    }

    private void newRadioButtonSelected(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.useGpsRadioButton:
                latLonTextBox.setEnabled(false);
                selectedLocationOption = R.id.useGpsRadioButton;
                break;
            case R.id.useLocationPickerRadioButton:
                latLonTextBox.setEnabled(false);
                selectedLocationOption = R.id.useLocationPickerRadioButton;
                break;
            case R.id.useLatLonRadioButton:
                latLonTextBox.setEnabled(true);
                selectedLocationOption = R.id.useLatLonRadioButton;

                break;
        }

        determineValidInputState();

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
