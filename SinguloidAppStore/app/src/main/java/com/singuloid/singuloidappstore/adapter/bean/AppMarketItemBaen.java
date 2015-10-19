package com.singuloid.singuloidappstore.adapter.bean;

/**
 * Created by scguo on 15/10/16.
 */
public class AppMarketItemBaen {

    // TODO: find a better way to manage the icon resource,
    // we have two way to fetch the AppIcon, one way is
    // fetch it from the local DB directly, and another way
    // is to fetch it from the server
    private int mAppIconRes;
    private String mAppName;
    private float mAppSize;
    private long mAppDownloadNum;
    private boolean mInstalled;

    public AppMarketItemBaen(int appIcon, String appName, float appSize, long downloadNum, boolean installed) {
        this.mAppIconRes = appIcon;
        this.mAppName = appName;
        this.mAppSize = appSize;
        this.mAppDownloadNum = downloadNum;
        this.mInstalled = installed;
    }

    public void setAppIconRes(int iconRes) {
        this.mAppIconRes = iconRes;
    }

    public int getAppIconRes() {
        return mAppIconRes;
    }

    public String getAppName() {
        return this.mAppName;
    }

    public void setAppName(String appName) {
        this.mAppName = appName;
    }

    public void setAppSize(float appSize) {
        this.mAppSize = appSize;
    }

    public float getAppSize() {
        return mAppSize;
    }

    public long getAppDownloadNum() {
        return mAppDownloadNum;
    }

    public void setAppDownloadNum(long num) {
        this.mAppDownloadNum = num;
    }

    public boolean isAppInstalled() {
        return this.mInstalled;
    }

    public void setAppInstalled(boolean installed) {
        this.mInstalled = installed;
    }

}
