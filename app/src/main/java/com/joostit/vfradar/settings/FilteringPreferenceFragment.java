package com.joostit.vfradar.settings;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.joostit.vfradar.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FilteringPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_filtering);
        setHasOptionsMenu(true);

        SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_plot_filter_maxAlt)));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_plot_filter_maxDist)));
        Preference filterEnabledPref = findPreference(getString(R.string.key_plot_filter_enabled));

        boolean currentValue = PreferenceManager
                .getDefaultSharedPreferences(filterEnabledPref.getContext())
                .getBoolean(filterEnabledPref.getKey(), Boolean.parseBoolean(getString(R.string.pref_default_filter_enabled)));
        updateEnabledStates(currentValue);

        filterEnabledPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isEnabled = (Boolean) newValue;
                updateEnabledStates(isEnabled);
                return true;
            }

        });
    }

    private void updateEnabledStates(boolean enabled) {
        findPreference(getString(R.string.key_plot_filter_maxAlt)).setEnabled(enabled);
        findPreference(getString(R.string.key_plot_filter_maxDist)).setEnabled(enabled);
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
