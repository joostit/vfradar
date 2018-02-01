package com.joostit.vfradar.settings;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import com.joostit.vfradar.R;

import java.net.MalformedURLException;
import java.net.URL;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class UnitsPreferenceFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_units);
        setHasOptionsMenu(true);

        SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_units_height)));
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
}