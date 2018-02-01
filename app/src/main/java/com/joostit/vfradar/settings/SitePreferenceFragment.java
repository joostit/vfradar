package com.joostit.vfradar.settings;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.joostit.vfradar.R;
import com.joostit.vfradar.config.SysConfig;
import com.joostit.vfradar.geo.LatLon;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SitePreferenceFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    int PLACE_PICKER_REQUEST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_site);
        setHasOptionsMenu(true);

        Preference centerPref = findPreference(getString(R.string.key_site_center_location));
        centerPref.setOnPreferenceChangeListener(this);

        Preference filePickPref = findPreference(getString(R.string.key_site_pick_location));
        filePickPref.setOnPreferenceClickListener(this);

        SettingsActivity.updatePreferenceSummary(centerPref, null);
        SettingsActivity.updatePreferenceSummary(findPreference(getString(R.string.key_site_elevationM)), null);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        Boolean isOk = true;
        if (preference.getKey().equals(getContext().getResources().getString(R.string.key_site_center_location))) {
            String newValue = (String) o;
            if (!isLatLonStringIsValid((String) newValue)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Error");
                builder.setMessage("Invalid Latitude/Longitude value.\nUse the '000.00000, 000.00000' format.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
                isOk = false;
            }
        }

        if (isOk) {
            SettingsActivity.updatePreferenceSummary(preference, o);
        }

        return isOk;
    }


    private boolean isLatLonStringIsValid(String input) {

        Boolean isOk = true;
        LatLon test = null;
        try {
            test = LatLon.parseLatLon(input);
        } catch (NumberFormatException e) {
            isOk = false;
        }

        if (isOk) {
            if ((test.Latitude < -90) || (test.Latitude > 90)) {
                isOk = false;
            }
            if ((test.Longitude < -180) || (test.Longitude > 180)) {
                isOk = false;
            }
        }

        return isOk;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Boolean isOk = true;
        if (preference.getKey().equals(getContext().getResources().getString(R.string.key_site_pick_location))) {

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
        return isOk;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == SettingsActivity.RESULT_OK) {
                Place place = PlacePicker.getPlace(this.getContext(), data);
                LatLng selected = place.getLatLng();
                LatLon coordinates = new LatLon(selected.latitude, selected.longitude);
                Preference centerPref = findPreference(getString(R.string.key_site_center_location));
                SysConfig.setCenterPosition(getContext(), coordinates);
                SettingsActivity.updatePreferenceSummary(centerPref, null);
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
}
