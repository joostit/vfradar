package com.joostit.vfradar.Startup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.joostit.vfradar.R;
import com.joostit.vfradar.config.SysConfig;
import com.joostit.vfradar.geo.LatLon;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStartupFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetupLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetupLocationFragment extends Fragment implements LocationHelper.LocationUpdateHandler, DialogInterface.OnCancelListener {

    private final int minimumGpsAccuracy = 20;
    private final int minimumAccurateFixes = 5;
    private final int maxAccurateGpsSearchTimeMs = 30000;

    private OnStartupFragmentInteractionListener mListener;
    private Button nextButton;
    private Button cancelButton;
    private EditText latLonTextBox;
    private int selectedLocationOption = -1;
    private RadioGroup radioGroup;
    private boolean locationMethodApplied = false;
    private View rootView;
    private LocationHelper locationHelper;
    private TextView accuracyView;
    private ProgressDialog gpsProgressDialog;
    private int accurateGpsFixesCount = 0;
    private int PLACE_PICKER_REQUEST = 12;
    private LatLon lastGpsFix;
    private boolean gpsCancelledOnPurpose = false;
    private Handler timerHandler = new Handler();
    private boolean gpsRunning = false;
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            onGpsSearchTimeExpired();
        }
    };

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

        cancelButton = rootView.findViewById(R.id.cancelGPSButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelButtonClicked();
            }
        });

        accuracyView = (TextView) rootView.findViewById(R.id.accuracyBox);

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
        accuracyView.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);
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

        if (latLonValid) {
            latLonTextBox.setBackgroundColor(0x00000000);
        } else {
            latLonTextBox.setBackgroundColor(0xFF4d0000);
        }

        return latLonValid;
    }


    private void determineValidInputState() {

        boolean validIinput = false;

        switch (selectedLocationOption) {
            case R.id.fromScenarioRadioButton:
                validIinput = true;
                break;
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
        if (!locationMethodApplied) {
            moveToNextPage = applyLocationOption();
        } else {
            moveToNextPage = true;
        }

        if (moveToNextPage) {
            moveToNextPage();
        }
    }

    private void moveToNextPage() {
        setUseScenario();
        mListener.allowPageSwitching(true);
        mListener.userSelectedNextTab();
    }

    private void setUseScenario() {
        mListener.useScenarioSelection(selectedLocationOption == R.id.fromScenarioRadioButton);
    }

    private boolean applyLocationOption() {
        boolean proceed = false;

        switch (radioGroup.getCheckedRadioButtonId()) {

            case R.id.fromScenarioRadioButton:
                proceed = true;
                break;

            case R.id.useGpsRadioButton:
                startGpsUpdates();
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

        if (proceed) {
            setLocationMethodApplied(true);
        }
        return proceed;
    }

    private void startGpsUpdates() {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        gpsCancelledOnPurpose = false;
        gpsRunning = true;
        accurateGpsFixesCount = 0;
        locationHelper = new LocationHelper();
        locationHelper.startLocationUpdates(this.getActivity(), this);
        cancelButton.setVisibility(View.VISIBLE);
        setAllowChanges(false);

        String message = "Waiting for GPS fix...";
        gpsProgressDialog = ProgressDialog.show(getContext(), "GPS", message, false, true, this);
        gpsProgressDialog.setMax(minimumAccurateFixes);
        gpsProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        startGpsTimer();
    }

    private void setLocationMethodApplied(boolean success) {
        locationMethodApplied = success;
        if (success) {
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

    private void showPlayServiceError() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error");
        builder.setMessage("Google Play services is unavailable.");
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    private void newRadioButtonSelected(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.fromScenarioRadioButton:
                latLonTextBox.setEnabled(false);
                selectedLocationOption = R.id.fromScenarioRadioButton;
                break;

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
        ((RadioButton) rootView.findViewById(R.id.fromScenarioRadioButton)).setEnabled(allowChanges);
        ((RadioButton) rootView.findViewById(R.id.useGpsRadioButton)).setEnabled(allowChanges);
        ((RadioButton) rootView.findViewById(R.id.useLocationPickerRadioButton)).setEnabled(allowChanges);
        ((RadioButton) rootView.findViewById(R.id.useLatLonRadioButton)).setEnabled(allowChanges);
        latLonTextBox.setEnabled(allowChanges && (selectedLocationOption == R.id.useLatLonRadioButton));
    }

    @Override
    public void onLocationChanged(Location loc) {
        double lat = loc.getLatitude();
        double lon = loc.getLongitude();
        double acc = loc.getAccuracy();

        lastGpsFix = new LatLon(lat, lon);
        latLonTextBox.setText(lastGpsFix.toString());

        accuracyView.setText(String.valueOf(Math.round(acc)));
        accuracyView.setVisibility(View.VISIBLE);
        gpsProgressDialog.setProgress(accurateGpsFixesCount);

        if (acc <= minimumGpsAccuracy) {
            accurateGpsFixesCount++;
        } else {
            accurateGpsFixesCount = 0;
        }

        if (hasAccurateGpsFix()) {
            gpsCancelledOnPurpose = true;
            stopGps();
            applyLastGpsFix();
        }
    }

    private void applyLastGpsFix() {
        SysConfig.setCenterPosition(getContext(), lastGpsFix);
        setLocationMethodApplied(true);
        latLonTextBox.setText(lastGpsFix.toString());
        moveToNextPage();
    }

    private void stopGps() {
        gpsRunning = false;
        stopGpsTimer();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        locationHelper.stopLocationUpdates();

        if (gpsProgressDialog != null) {
            gpsProgressDialog.cancel();
        }

        accuracyView.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);
    }

    private void cancelGps() {
        stopGps();
        latLonTextBox.setText(SysConfig.getCenterPosition().toString());
        setAllowChanges(true);
    }

    private void cancelButtonClicked() {
        cancelGps();
    }

    private boolean hasAccurateGpsFix() {
        return accurateGpsFixesCount >= minimumAccurateFixes;
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        if (!gpsCancelledOnPurpose) {
            cancelGps();
        }
    }

    private void startGpsTimer() {

        timerHandler.postDelayed(timerRunnable, maxAccurateGpsSearchTimeMs);
    }

    private void stopGpsTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }


    private void onGpsSearchTimeExpired() {

        gpsCancelledOnPurpose = true;
        cancelGps();

        // Assume that we have a fix in this case
        if (accurateGpsFixesCount > 0) {
            applyLastGpsFix();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (gpsRunning) {
            gpsCancelledOnPurpose = true;
            stopGps();
        }
    }
}
