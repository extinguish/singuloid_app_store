package com.singuloid.singuloidappstore.adapter.apploader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.singuloid.singuloidappstore.adapter.appobserver.InstalledAppsObserver;
import com.singuloid.singuloidappstore.adapter.appobserver.SystemLocaleObserver;
import com.singuloid.singuloidappstore.adapter.bean.LocalInstalledAppEntry;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by scguo on 15/10/17.
 *
 * An implementation of AsyncTaskLoader of which loads the all apps that installed
 * on current devices
 *
 */
public class LocalInstalledAppListLoader extends AsyncTaskLoader<List<LocalInstalledAppEntry>> {
    private static final String TAG = "installAppLoader";

    private List<LocalInstalledAppEntry> mAppList;
    public final PackageManager mPkgMgr;

    // the observer to notify when app state changed in the system
    private InstalledAppsObserver mInstalledAppsObserver;
    // the observer to notify when system locale changed in the system
    private SystemLocaleObserver mSystemLocaleObserver;

    public LocalInstalledAppListLoader(Context context) {
        super(context);
        // notice here we using getContext() to get the instance of PackageManager,
        // but do not use the context that provided by the parameter to get the instance
        // of PackageManager, this is because the context that transferred by the parameter
        // are bind to the Activity that start this AsyncTaskLoader instance, and as this
        // AsyncTaskLoader may be using by more than one Activity instance, and by the way
        // the AsyncTaskLoader will running in the background, thus she will hold the reference
        // to the Activity until she finish her job, this is really dangerous, and will cause
        // MemoryLeak obviously
        // so here, we using the getContext() to retrieve the PackageManager instance, as getContext()
        // holds the Application context
        this.mPkgMgr = getContext().getPackageManager();
    }

    /**
     * This method is called in background, and the AsyncTaskLoader will handle the
     * detailed process of generate one separate thread to handle this.
     *
     * @return a single installed app on this device
     */
    @Override
    public List<LocalInstalledAppEntry> loadInBackground() {
        // retrieve all the installed app list
        // and we using 0 as the flag of which corresponds to GET_META_DATA flag value
        List<ApplicationInfo> allInstalledApp = mPkgMgr.getInstalledApplications(0);
        if (allInstalledApp == null) {
            allInstalledApp = new ArrayList<>();
        }
        List<LocalInstalledAppEntry> appEntryList = new ArrayList<>(allInstalledApp.size());
        // create a corresponding array of entries and load their labels
        for (ApplicationInfo appInfo : allInstalledApp) {
            // parse out the downloaded app, what we need are the downloaded app only
            if ((mPkgMgr.getLaunchIntentForPackage(appInfo.packageName) != null) && ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)) {
                String appVersion = "0.0";
                // parse out the installed app version info
                try {
                    PackageInfo pkgInfo = mPkgMgr.getPackageInfo(appInfo.packageName, 0);
                    appVersion = pkgInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, " the app version we get are : " + appVersion);
                LocalInstalledAppEntry appEntry = new LocalInstalledAppEntry(this, appInfo);
                appEntry.loadAppName(getContext());
                appEntry.setAppVersion(appVersion);
                appEntryList.add(appEntry);

            }
        }
        // sort this list, and the we sort it in alpha by default
        // TODO: provides even more comparing rules at
        // TODO: here, just as other app store does
        // TODO: for example by the installing date or the APK file size
        Collections.sort(appEntryList, ALPHA_COMPARATOR);

        return appEntryList;
    }

    @Override
    public void deliverResult(List<LocalInstalledAppEntry> newData) {
        if (isReset()) {
            // if this load has been reset, if this loader is not started
            // first time been called, then the following process will be triggered
            // As the Loader has been reset, ignore the result and invalidate the
            // data we already got. This can happen when the loader is reset while
            // an async query is working in the background. That is when the background
            // thread is finishes her work, and attempts to deliver the result to the
            // client, she will she here that the Loader has been reset and discard any
            // result that has been retrieved so far as necessary
            if (newData != null) {
                releaseResource(newData);
                return;
            }
        }

        // Hold a reference to the old data so it does not get garbage collected
        // we must protect this data until the new data is available
        List<LocalInstalledAppEntry> oldAppList = mAppList;
        mAppList = newData;

        if (isStarted()) {
            // if the Loader is already in started state, then we need to have the super
            // class to deliver the result to the client
            super.deliverResult(newData);
        }

        // invalidate the old data, as we do not need it anymore
        if (oldAppList != null && oldAppList != newData) {
            Log.d(TAG, " we need to release the old app list ");
            releaseResource(oldAppList);
        }
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, " on start loading ");
        if (mAppList != null) {
            // deliver the previous loaded data immediately
            deliverResult(mAppList);
        }

        // register the observers that will notify the Loader when changes made
        if (null == mInstalledAppsObserver) {
            mInstalledAppsObserver = new InstalledAppsObserver(this);
        }

        if (null == mSystemLocaleObserver) {
            mSystemLocaleObserver = new SystemLocaleObserver(this);
        }

        if (takeContentChanged()) {
            // when the observer detects that the content has been changed in the system, she will call
            // the onContentChanged() on the Loader, will will cause the next call to takeContentChanged() to
            // return true, then, we need to call forceLoad() method
            Log.d(TAG, " the content has been changed in the ");
            forceLoad();
        } else if (mAppList == null) {
            // if the current data is null, we need to call forceLoad() to load the data
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {

        // the Loader has been put in a stopped state, so we should attempt to cancel
        // the current loading process ( if there is one)
        cancelLoad();

        // note that we leave the observer as is; while the loader is in stopped state
        // but still need to monitor the data source for changing, so that the loader
        // will know to force a new load if it is ever start again
    }

    @Override
    protected void onReset() {
        // first, to ensure that the loader is stopped
        onStopLoading();

        // at this point we can release the resource associated with 'apps'
        if (null != mAppList) {
            releaseResource(mAppList);
            mAppList = null;
        }

        // the loader is being reset, so we should stop monitoring for changes
        if (null != mInstalledAppsObserver) {
            getContext().unregisterReceiver(mInstalledAppsObserver);
            mInstalledAppsObserver = null;
        }

        if (null != mSystemLocaleObserver) {
            getContext().unregisterReceiver(mSystemLocaleObserver);
            mSystemLocaleObserver = null;
        }
    }

    @Override
    public void onCanceled(List<LocalInstalledAppEntry> data) {
        super.onCanceled(data);

        // the load has been cancelled, and we should also release the data
        // we load thus to save our memory
        releaseResource(data);
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

    private void releaseResource(List<LocalInstalledAppEntry> data) {
        // All resources associated with the Loader should be released at here
        data.clear();
    }



    private static final Comparator<LocalInstalledAppEntry> ALPHA_COMPARATOR = new Comparator<LocalInstalledAppEntry>() {
        Collator collator = Collator.getInstance();
        @Override
        public int compare(LocalInstalledAppEntry lhs, LocalInstalledAppEntry rhs) {
            return collator.compare(lhs.getAppName(), rhs.getAppName());
        }
    };

}

























