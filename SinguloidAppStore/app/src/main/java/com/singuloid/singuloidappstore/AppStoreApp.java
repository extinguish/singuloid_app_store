package com.singuloid.singuloidappstore;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

/**
 * Created by scguo on 15/10/20.
 *
 * open the strictMode in the main application class using to monitor
 * the bad practice in the AppStoreApp application instance
 *
 */
public class AppStoreApp extends Application {
    private static final String TAG = "Sing_AppStoreApp";

    private static Context sContext;

    // Log detected violations to the system log
    private static StrictMode.VmPolicy.Builder newVmPolicyBuilder() {
        return new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedRegistrationObjects()
                .penaltyLog();
    }

    // Log detected violations to the system log
    private static StrictMode.ThreadPolicy.Builder newThreadPolicyBuilder() {
        return new StrictMode.ThreadPolicy.Builder()
                .detectCustomSlowCalls()
                .penaltyLog();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        // start the strict mode
        startUp();
    }

    private void startUp() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                Log.d(TAG, " start the strict mode in the background thread ... ");
                StrictModeManager.initStrictMode(newVmPolicyBuilder(),
                        newThreadPolicyBuilder());
                return null;
            }
        }.execute();
    }
}
