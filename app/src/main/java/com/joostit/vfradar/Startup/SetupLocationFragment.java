package com.joostit.vfradar.Startup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
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
    private RadioGroup radioGroup;
    private boolean locationMethodApplied = false;
    private View rootView;

    private int PLACE_PICKER_REQUEST = 12;

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
        rootView = inflater.inflate(R.layout.fragment_setup_location, container, false);

        nextButton = rootView.findViewById(R.id.nextPageButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextButtonClicked();
            }
        });

        radioGroup = (RadioGroup) rootView.findViewById(R.id.centerLocationRadioButtonsGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                newRadioButtonSelected(group, checkedId);
            }
        });


        latLonTextBox = (EditText) rootView.findViewById(R.id.latLonBox);
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
        return rootView;
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
    }


    private void nextButtonClicked() {
        boolean moveToNextPage;
        if(!locationMethodApplied) {
            moveToNextPage = applyLocationOption();
        }
        else{
            moveToNextPage = true;
        }

        if(moveToNextPage) {
            moveToNextPage();
        }
    }

    private void moveToNextPage(){
        mListener.allowPageSwitching(true);
        mListener.userSelectedNextTab();
    }

    private boolean applyLocationOption(){
        boolean proceed = false;
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.useGpsRadioButton:

                break;
            case R.id.useLocationPickerRadioButton:
                getLocationFromLocationPicker();
                break;
            case R.id.useLatLonRadioButton:
                LatLon newLatLon = LatLon.parseLatLon(latLonTextBox.getText().toString());
                SysConfig.setCenterPosition(this.getContext(), newLatLon);
                proceed = true;
                break;
        }

        if(proceed) {
            setLocationMethodApplied(true);
        }
        return proceed;
    }

    private void setLocationMethodApplied(boolean success){
        locationMethodApplied = success;
        if(success){
            latLonTextBox.setText(SysConfig.getCenterPosition().toString());
        }
        setAllowChanges(!locationMethodApplied);
    }

    private void getLocationFromLocationPicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this.getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            showPlayServiceError();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            showPlayServiceError();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(this.getContext(), data);
                LatLng selected = place.getLatLng();
                LatLon coordinates = new LatLon(selected.latitude, selected.longitude);
                SysConfig.setCenterPosition(getContext(), coordinates);
                setLocationMethodApplied(true);
                moveToNextPage();
            }
        }
    }

    private void showPlayServiceError(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error");
        builder.setMessage("Google Play services is unavailable.");
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
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
                latLonTextBox.setEnabled(!locationMethodApplied);
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

    public void setAllowChanges(boolean allowChanges) {
        ((RadioButton) rootView.findViewById(R.id.useGpsRadioButton)).setEnabled(allowChanges);
        ((RadioButton) rootView.findViewById(R.id.useLocationPickerRadioButton)).setEnabled(allowChanges);
        ((RadioButton) rootView.findViewById(R.id.useLatLonRadioButton)).setEnabled(allowChanges);
        latLonTextBox.setEnabled(allowChanges);
    }
}
