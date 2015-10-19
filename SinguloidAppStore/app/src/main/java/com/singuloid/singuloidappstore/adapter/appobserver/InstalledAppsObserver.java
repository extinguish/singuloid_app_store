package com.singuloid.singuloidappstore.adapter.appobserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

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
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addDataScheme("package");
        // register the action for
        mAppListLoader.getContext().registerReceiver(this, intentFilter);

    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
