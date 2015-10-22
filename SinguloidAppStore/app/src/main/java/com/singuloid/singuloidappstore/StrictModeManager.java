package com.singuloid.singuloidappstore;

import android.os.StrictMode;

/**
 * Created by scguo on 15/10/20.
 *
 * Enable the StrictModeManager with default thus to detect all of the
 * wrong doing in the appStore
 *
 */
public final class StrictModeManager {

    private StrictModeManager() {
        throw new IllegalStateException("This class should not be instantiate ... ");
    }

    /**
     * Init a strictMode to close the activity and log to console when a violation occurs
     */
    public static void initStrictMode() {
        initStrictMode(newVmPolicyBuilderWithDefaults(), newThreadPolicyBuilderWithDefaults());

    }

    public static void initStrictMode(StrictMode.VmPolicy.Builder vmPolicyBuilder,
                                      StrictMode.ThreadPolicy.Builder threadPolicyBuilder) {

        StrictMode.setThreadPolicy(threadPolicyBuilder.penaltyDeath().build());
        StrictMode.setVmPolicy(vmPolicyBuilder.penaltyDeath().build());
    }

    /**
     * Init a strictMode to log to a console when a violation occurs
     */
    public static void initStrictModeLogOnly() {
        initStrictModeLogOnly(newVmPolicyBuilderWithDefaults(), newThreadPolicyBuilderWithDefaults());
    }

    public static void initStrictModeLogOnly(StrictMode.VmPolicy.Builder vmPolicyBuilder,
                                             StrictMode.ThreadPolicy.Builder threadPolicyBuilder) {
        StrictMode.setThreadPolicy(threadPolicyBuilder.build());
        StrictMode.setVmPolicy(vmPolicyBuilder.build());
    }

    public static StrictMode.ThreadPolicy.Builder newThreadPolicyBuilderWithDefaults() {
        return new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
    }

    public static StrictMode.VmPolicy.Builder newVmPolicyBuilderWithDefaults() {
        return new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();
    }

}
