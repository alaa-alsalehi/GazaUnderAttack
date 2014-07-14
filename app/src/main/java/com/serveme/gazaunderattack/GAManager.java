package com.serveme.gazaunderattack;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

public class GAManager {

    private static volatile GAManager _gam = new GAManager();

    // Prevent hits from being sent to reports, i.e. during testing.
    private static final boolean GA_IS_DRY_RUN = false;

    // GA Logger verbosity.
    private static int GA_LOG_VERBOSITY = Logger.LogLevel.VERBOSE;

    public static GAManager getInstance() {
        synchronized (_gam) {
            return _gam;
        }
    }

    Tracker mTracker = null;
    synchronized Tracker getTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(GazaUnderAttackApplication.getContext());
            analytics.setDryRun(GA_IS_DRY_RUN);
            analytics.getLogger().setLogLevel(GA_LOG_VERBOSITY);
            mTracker = analytics.newTracker(R.xml.app_tracker);
        }
        return mTracker;
    }

    public void trackScreen(String location) {
        Tracker t = getTracker();
        t.setScreenName(location);
        t.send(new HitBuilders.AppViewBuilder().build());
        manualDispatch();
    }

    public void trackEvent(String category, String action, String label) {
        Tracker t = getTracker();
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
        manualDispatch();
    }

    private void manualDispatch() {
        GoogleAnalytics.getInstance(GazaUnderAttackApplication.getContext().getApplicationContext()).dispatchLocalHits();
    }
}