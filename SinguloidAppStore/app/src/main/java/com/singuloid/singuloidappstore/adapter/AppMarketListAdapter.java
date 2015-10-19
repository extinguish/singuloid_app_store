package com.singuloid.singuloidappstore.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.singuloid.singuloidappstore.R;
import com.singuloid.singuloidappstore.lib.button.AppManageBtn;

/**
 * Created by scguo on 15/10/17.
 */
public class AppMarketListAdapter extends RecyclerView.Adapter<AppMarketListAdapter.AppMarketItemViewHolder> {

    private static final String TAG = "AppMarketListAdapter";

    @Override
    public AppMarketItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(AppMarketItemViewHolder appMarketItemViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AppMarketItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mAppIcon;
        private TextView mAppTitle;
        private TextView mAppSize;
        private TextView mAppDownloadNum;
        private AppManageBtn mBtnManage;

        public AppMarketItemViewHolder(View itemView) {
            super(itemView);
            mAppIcon = (ImageView) itemView.findViewById(R.id.app_item_icon);
            mAppTitle = (TextView) itemView.findViewById(R.id.app_item_title);
            mAppSize = (TextView) itemView.findViewById(R.id.app_item_size);
            mAppDownloadNum = (TextView) itemView.findViewById(R.id.app_item_download_num);
            mBtnManage = (AppManageBtn) itemView.findViewById(R.id.btn_app_item_manage);
        }
    }

}
