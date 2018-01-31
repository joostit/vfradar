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
public class ConnectionPreferenceFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_connection);
        setHasOptionsMenu(true);

        Preference radarCoreUrlPref = findPreference(getString(R.string.key_vfradarcore_url));
        radarCoreUrlPref.setOnPreferenceChangeListener(this);
        SettingsActivity.updatePreferenceSummary(radarCoreUrlPref, null);

        SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_update_interval)));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        Boolean isOk = true;
        if (preference.getKey().equals(getContext().getResources().getString(R.string.key_vfradarcore_url))) {
            String newValue = (String) o;
            if (!isRadarCoreUrlValid((String) newValue)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Error");
                builder.setMessage("Invalid URL value.\nDefault: http://10.10.10.10:60002/live/all");
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

    private boolean isRadarCoreUrlValid(String string) {
        boolean isOk = true;
        try {
            URL url = new URL(string);
            url.toString(); // To prevent compiler optimization deleting the unused URL object
        } catch (MalformedURLException e) {
            e.printStackTrace();
            isOk = false;
        }
        return isOk;
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