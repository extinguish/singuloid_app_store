package com.singuloid.singuloidappstore.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.singuloid.singuloidappstore.R;
import com.singuloid.singuloidappstore.adapter.bean.LocalInstalledAppEntry;
import com.singuloid.singuloidappstore.util.Utils;

import java.util.List;

/**
 * Created by scguo on 15/10/17.
 */
public class LocalInstalledAppListAdapter extends RecyclerView.Adapter<LocalInstalledAppListAdapter.LocalInstalledAppViewHolder> {
    private static final String TAG = "LocalInstalledAppListAdapter";

    private List<LocalInstalledAppEntry> mAppList;

    private Context mContext;
    private PackageManager mPkgManager;

    public LocalInstalledAppListAdapter(Context context, List<LocalInstalledAppEntry> appList) {
        this.mContext = context;
        this.mAppList = appList;
        this.mPkgManager = mContext.getPackageManager();
    }

    @Override
    public LocalInstalledAppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View appView = LayoutInflater.from(mContext).inflate(R.layout.local_app_item_layout, viewGroup, false);
        return new LocalInstalledAppViewHolder(appView);
    }

    @Override
    public void onBindViewHolder(LocalInstalledAppViewHolder viewHolder, int pos) {
        // if we bind the data to the ViewHolder, then we can process the data binding to the
        // ViewHolder view object we get
        final LocalInstalledAppEntry appEntry = mAppList.get(pos);
        String appVersionStr = String.format(mContext.getResources().getString(R.string.app_version_str), appEntry.getAppVersion());
        viewHolder.appVersion.setText(appVersionStr);
        viewHolder.appSize.setText(appEntry.getAppSize());
        viewHolder.appIcon.setImageDrawable(Utils.resizeDrawable(mContext, appEntry.getAppIcon()));
        viewHolder.appName.setText(appEntry.getAppName());

        // here we add the item view on click event handler
        viewHolder.appContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the specified activity
                Intent intent = mPkgManager.getLaunchIntentForPackage(appEntry.getAppInfo().packageName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptions ops = ActivityOptions.makeCustomAnimation(mContext,
                        R.anim.task_open_enter,
                        R.anim.no_anim);
                Bundle optsBundle = ops.toBundle();
                mContext.startActivity(intent, optsBundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppList != null ? mAppList.size() : 0;
    }

    // the view holder for the LocalAppEntry
    public static class LocalInstalledAppViewHolder extends RecyclerView.ViewHolder {
        TextView appName;
        TextView appSize;
        TextView appVersion;
        ImageView appIcon;
        Button appManageBtn;
        RelativeLayout appContainer;

        public LocalInstalledAppViewHolder(final View itemView) {
            super(itemView);
            appContainer = (RelativeLayout) itemView.findViewById(R.id.local_app_item_container);
            appName = (TextView) itemView.findViewById(R.id.app_item_title);
            appSize = (TextView) itemView.findViewById(R.id.app_item_size);
            appVersion = (TextView) itemView.findViewById(R.id.app_item_version);
            appIcon = (ImageView) itemView.findViewById(R.id.app_item_icon);
            appManageBtn = (Button) itemView.findViewById(R.id.btn_app_item_manage);

        }
    }

    // a simple version of adding app into the localInstalledAppList
    public void setData(List<LocalInstalledAppEntry> appList) {
        if (null != mAppList) {
            mAppList.clear();
            if (appList != null) {
                for (LocalInstalledAppEntry app : appList) {
                    mAppList.add(app);
                }
            }
        }
    }
}
