package com.singuloid.singuloidappstore.data;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by scguo on 15/10/21.
 *
 * The util class of which performs the network connectivity checking job
 *
 */
public class NetworkChecker {

    private final ConnectivityManager mConnectivityManager;
    private final TelephonyManager mTeleManager;

    public NetworkChecker(Activity activity) {
        this.mConnectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.mTeleManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public boolean isConnected() {
        return isConnectToCellWork() || isConnectedToWifi();
    }

    private boolean isConnectToCellWork() {
        int simState = mTeleManager.getSimState();
        if (simState == TelephonyManager.SIM_STATE_READY) {
            NetworkInfo networkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return networkInfo.isConnected();
        }
        return false;
    }

    private boolean isConnectedToWifi() {
        return mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }

}
