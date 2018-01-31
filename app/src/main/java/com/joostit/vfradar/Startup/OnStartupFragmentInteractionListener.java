package com.joostit.vfradar.Startup;

/**
 * Created by Joost on 30-1-2018.
 */

public interface OnStartupFragmentInteractionListener {
    void userSelectedNextTab();

    void userFinishesSetup();

    void allowPageSwitching(boolean allowed);
}
