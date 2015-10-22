package com.singuloid.singuloidappstore.adapter.appobserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.singuloid.singuloidappstore.adapter.apploader.LocalInstalledAppListLoader;
import com.singuloid.singuloidappstore.adapter.bean.LocalInstalledAppEntry;

/**
 * Created by scguo on 15/10/17.
 *
 * An observer of which intercepts the system-wide locale changes events,
 * for example if the user switch his context from China TingTao to China
 * MainLand, then we need to notify the language and the timeZone has changed,
 * and by the way the App list also need reload to fits in such change.
 *
 */
public class SystemLocaleObserver extends BroadcastReceiver{
    private static final String TAG = "SystemLocaleObserver";

    private LocalInstalledAppListLoader mAppListLoader;

    public SystemLocaleObserver(LocalInstalledAppListLoader loader) {
        this.mAppListLoader = loader;
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
        mAppListLoader.getContext().registerReceiver(this, intentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, " the user locale has been changed");
        if (null != mAppListLoader) {
            mAppListLoader.onContentChanged();
        }
    }
}
