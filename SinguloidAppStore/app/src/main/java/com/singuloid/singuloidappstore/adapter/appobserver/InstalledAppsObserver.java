package com.singuloid.singuloidappstore.adapter.appobserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.singuloid.singuloidappstore.adapter.apploader.LocalInstalledAppListLoader;

/**
 * Created by scguo on 15/10/17.
 */
public class InstalledAppsObserver extends BroadcastReceiver {
    private static final String TAG = "InstalledAppsObserver";

    private LocalInstalledAppListLoader mAppListLoader;

    public InstalledAppsObserver(LocalInstalledAppListLoader loader) {
        this.mAppListLoader = loader;

        // register this BroadcastReceiver
        // register for events like Package installed/updated/uninstalled
        IntentFilter pkgIntentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        pkgIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        pkgIntentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        pkgIntentFilter.addDataScheme("package");
        mAppListLoader.getContext().registerReceiver(this, pkgIntentFilter);

        // register the action for sdcard mounted or unmounted
        // user may install app on the external sdcard
        IntentFilter sdcardIntentFilter = new IntentFilter(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        sdcardIntentFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        mAppListLoader.getContext().registerReceiver(this, sdcardIntentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, " the event action we received are : " + intent.getAction().toString());
        // notify the load has been
        if (null != mAppListLoader) {
            mAppListLoader.onContentChanged();
        }
    }
}
