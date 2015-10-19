package com.singuloid.singuloidappstore.adapter.apploader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

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
    private static final String TAG = "LocalInstalledAppListLoader";

    private List<LocalInstalledAppEntry> mAppList;
    public final PackageManager mPkgMgr;

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
            LocalInstalledAppEntry appEntry = new LocalInstalledAppEntry(this, appInfo);
            appEntry.loadAppName(getContext());
            appEntryList.add(appEntry);
        }
        // sort this list, and the we sort it in alpha by default
        // TODO: provides even more comparing rules at
        // TODO: here, just as other app store does
        // TODO: for example by the installing date or the APK file size
        Collections.sort(appEntryList, ALPHA_COMPARATOR);

        return appEntryList;
    }

    @Override
    public void deliverResult(List<LocalInstalledAppEntry> data) {
        if (isReset()) {
            // if this load has been reset, if this loader is not started
            // first time been called, then the following process will be triggered
            // As the Loader has been reset, ignore the result and invalidate the
            // data we already got. This can happen when the loader is reset while
            // an async query is working in the background. That is when the background
            // thread is finishes her work, and attempts to deliver the result to the
            // client, she will she here that the Loader has been reset and discard any
            // result that has been retrieved so far as necessary
            if (data != null) {

                return;
            }

        }

        super.deliverResult(data);

    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }

    @Override
    protected void onReset() {
        // first, to ensure that the loader is stopped


        super.onReset();
    }

    private void releaseResource(List<LocalInstalledAppEntry> data) {
        // All resources associated with the Loader should be released at here
        // TODO: provide the detailed implementation at here

    }

    private static final Comparator<LocalInstalledAppEntry> ALPHA_COMPARATOR = new Comparator<LocalInstalledAppEntry>() {
        Collator collator = Collator.getInstance();
        @Override
        public int compare(LocalInstalledAppEntry lhs, LocalInstalledAppEntry rhs) {
            return collator.compare(lhs.getAppName(), rhs.getAppName());
        }
    };

}

























