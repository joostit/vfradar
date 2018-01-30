package com.joostit.vfradar;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.joostit.vfradar.geo.LatLon;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private static void updatePreferenceSummary(Preference preference, Object value){
        if(value != null) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, value);
        }
        else{
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || ConnectionPreferenceFragment.class.getName().equals(fragmentName)
                || FilteringPreferenceFragment.class.getName().equals(fragmentName)
                || SitePreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
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



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ConnectionPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_connection);
            setHasOptionsMenu(true);

            Preference radarCoreUrlPref = findPreference(getString(R.string.key_vfradarcore_url));
            radarCoreUrlPref.setOnPreferenceChangeListener(this);
            updatePreferenceSummary(radarCoreUrlPref, null);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_update_interval)));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            Boolean isOk = true;
            if(preference.getKey().equals(getContext().getResources().getString(R.string.key_vfradarcore_url))){
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

            if(isOk){
                updatePreferenceSummary(preference, o);
            }

            return isOk;
        }

        private boolean isRadarCoreUrlValid(String string){
            boolean isOk = true;
            try {
                URL url = new URL(string);
                url.toString(); // To prevent compiler optimization deleting the unused URL object
            }
            catch (MalformedURLException e){
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class FilteringPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_filtering);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_plot_filter_maxAlt)));
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SitePreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener,
            Preference.OnPreferenceClickListener{

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

            updatePreferenceSummary(centerPref, null);
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
            if(preference.getKey().equals(getContext().getResources().getString(R.string.key_site_center_location))){
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

            if(isOk){
                updatePreferenceSummary(preference, o);
            }

            return isOk;
        }


        private boolean isLatLonStringIsValid(String input){

            Boolean isOk = true;
            LatLon test = null;
            try {
                test = LatLon.parseLatLon(input);
            } catch (NumberFormatException e){
                isOk = false;
            }

            if(isOk){
                if((test.Latitude < -90) || (test.Latitude > 90)){
                    isOk = false;
                }
                if((test.Longitude < -180) || (test.Longitude > 180)){
                    isOk = false;
                }
            }

            return isOk;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            Boolean isOk = true;
            if(preference.getKey().equals(getContext().getResources().getString(R.string.key_site_pick_location))){

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
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this.getContext(), data);
                    LatLng selected = place.getLatLng();
                    LatLon coordinates = new LatLon(selected.latitude, selected.longitude);
                    Preference centerPref = findPreference(getString(R.string.key_site_center_location));
                    SysConfig.setCenterPosition(getContext(), coordinates);
                    updatePreferenceSummary(centerPref, null);
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
    }
}
