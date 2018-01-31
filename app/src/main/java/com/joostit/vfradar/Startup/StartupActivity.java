package com.joostit.vfradar.Startup;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.joostit.vfradar.OperationalActivity;
import com.joostit.vfradar.PermissionHelper;
import com.joostit.vfradar.R;
import com.joostit.vfradar.settings.SettingsActivity;
import com.joostit.vfradar.SysConfig;

public class StartupActivity extends AppCompatActivity
        implements OnStartupFragmentInteractionListener{


    private final int CONFIG_INTENT_REQUEST_CODE = 1;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;
    private ImageButton imageButton;
    boolean doubleBackToExitPressedOnce = false;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private SwitchableViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SysConfig.loadSettings(this);

        PermissionHelper.verifyWriteStoragePermissions(this);
        PermissionHelper.verifLocationAndGpsPermissions(this);

        setContentView(R.layout.activity_startup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (SwitchableViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.startupTabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        imageButton = (ImageButton) findViewById(R.id.preferencesButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onPreferencesPressed();
            }

        });

        disableTabs();
    }


    public void onPreferencesPressed() {
        Intent myIntent = new Intent(StartupActivity.this, SettingsActivity.class);
        StartupActivity.this.startActivityForResult(myIntent, CONFIG_INTENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CONFIG_INTENT_REQUEST_CODE:
                this.recreate();
                break;

            default:
                break;
        }
    }

    private void disableTabs(){
        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        tabStrip.setEnabled(false);
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(false);
        }
        mViewPager.setEnableSwipe(false);
    }

    private void enableTabs(){
        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        tabStrip.setEnabled(true);
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(true);
        }
        mViewPager.setEnableSwipe(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_startup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void userSelectedNextTab() {
        int currentIndex = tabLayout.getSelectedTabPosition();

        if(currentIndex < mSectionsPagerAdapter.getCount() -1){
            TabLayout.Tab newTab = tabLayout.getTabAt(currentIndex+1);
            newTab.select();
        }
    }

    public void userFinishesSetup(){
        Intent myIntent = new Intent(this, OperationalActivity.class);
        finish();
        startActivity(myIntent);
    }

    public void allowPageSwitching(boolean allowed){
       if(allowed) {
          enableTabs();
       }else{
           disableTabs();
       }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch(position){
                case 0:
                    return SetupLocationFragment.newInstance();
                case 1:
                    return SetupSiteFragment.newInstance();
                case 2:
                    return SetupSummaryFragment.newInstance();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


    }
}
