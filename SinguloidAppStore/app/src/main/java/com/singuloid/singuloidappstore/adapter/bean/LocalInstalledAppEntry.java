package com.singuloid.singuloidappstore.adapter.bean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import com.singuloid.singuloidappstore.adapter.apploader.LocalInstalledAppListLoader;
import com.singuloid.singuloidappstore.util.Utils;

import java.io.File;

/**
 * Created by scguo on 15/10/17.
 * The bean file of which represent the local installed app
 *
 */
public class LocalInstalledAppEntry {
    private final LocalInstalledAppListLoader mLoader;
    private final ApplicationInfo mAppInfo;
    private final File mApkFile;
    private String mAppName;
    private Drawable mAppIcon;
    private boolean mMounted;
    private String mAppVersion = "0.0";

    public LocalInstalledAppEntry(LocalInstalledAppListLoader loader, ApplicationInfo appInfo) {
        this.mLoader = loader;
        this.mAppInfo = appInfo;
        this.mApkFile = new File(appInfo.sourceDir);
    }

    public ApplicationInfo getAppInfo() {
        return this.mAppInfo;
    }

    public String getAppName() {
        return this.mAppName;
    }

    public Drawable getAppIcon() {

        if (mAppIcon == null) {
            if (mApkFile.exists()) {
                mAppIcon = mAppInfo.loadIcon(mLoader.mPkgMgr);
                return mAppIcon;
            } else {
                mMounted = false;
            }
        } else if (!mMounted) {
            // If the app wasn't mounted, but now is mounted
            // then, we just reload its icon
            if (mApkFile.exists()) {
                mMounted = true;
                mAppIcon = mAppInfo.loadIcon(mLoader.mPkgMgr);
                return mAppIcon;
            }
        } else {
            return mAppIcon;
        }
        // if something exception happened
        // possibly for the cause of the busy working
        // in such case, we just load the default icon will be enough
        // TODO: in future implementation, we just encapsulate the getDrawable()
        // TODO: method in a better non deprecated version
        return mLoader.getContext().getResources().getDrawable(android.R.drawable.sym_def_app_icon);
    }

    @Override
    public String toString() {
        return mAppName;
    }

    public void loadAppName(Context context) {
        if (mAppName == null || !mMounted) {
            if (!mApkFile.exists()) {
                mMounted = false;
                // using the package name as instead
                mAppName = mAppInfo.packageName;
            } else {
                mMounted = true;
                CharSequence name = mAppInfo.loadLabel(mLoader.mPkgMgr);
                mAppName = name != null ? name.toString() : mAppInfo.packageName;
            }
        }
    }

    public void setAppVersion(String num) {
        this.mAppVersion = num;
    }

    public String getAppVersion() {
        return mAppVersion;
    }

    public String getAppSize() {
        if (mApkFile.exists()) {
            // get the raw size of the APK file
            // and the returned value are in bytes.
            long fileSize = mApkFile.length();
            // convert the bytes to MillionBytes
            return Utils.convertByteToMB(fileSize);
        }
        return "0.0M";
    }
}
