package com.joostit.vfradar.settings;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import com.joostit.vfradar.R;
import com.joostit.vfradar.SysConfig;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DataPreferencesFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener{

    private final int FOLDER_PICKER_REQUEST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_data);
        setHasOptionsMenu(true);

        SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_data_datafolder)));
        Preference filePickPref = findPreference(getString(R.string.key_data_datafolder));
        filePickPref.setOnPreferenceClickListener(this);

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
    public boolean onPreferenceClick(Preference preference) {
        Boolean isOk = true;
        if(preference.getKey().equals(getContext().getResources().getString(R.string.key_data_datafolder))){

            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(Intent.createChooser(i, "Choose folder"), FOLDER_PICKER_REQUEST);

            isOk = true;
        }

        return isOk;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FOLDER_PICKER_REQUEST) {
            if (resultCode == SettingsActivity.RESULT_OK) {
                String selectedFolder = data.getData().getPath();
                SysConfig.setDataFolder(getContext(), selectedFolder);

                Preference centerPref = findPreference(getString(R.string.key_data_datafolder));
                SettingsActivity.updatePreferenceSummary(centerPref, null);
            }
        }
    }
}