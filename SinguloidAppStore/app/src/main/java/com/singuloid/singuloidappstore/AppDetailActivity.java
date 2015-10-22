package com.singuloid.singuloidappstore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.singuloid.singuloidappstore.adapter.AppIntroImgGalleryAdapter;
import com.singuloid.singuloidappstore.lib.view.ExpandableTextView;
import com.singuloid.singuloidappstore.util.AppConstants;

/**
 * Created by scguo on 15/10/21.
 *
 * The activity of which display the detailed information of the specified app
 *
 */
public class AppDetailActivity extends Activity {

    private static final String TAG = "AppDetailActivity";

    // with the appIntro, mAppUpdate and mDeveloperInfo we need to make separate network request to
    // get these information
    private ExpandableTextView mAppIntro;
    private TextView mAppUpdate, mDeveloperInfo;
    private RecyclerView mAppIntroGallery;
    private ImageView mAppIcon;
    private TextView mAppName, mAppSize, mAppDownloadNum, mAppVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail_layout);
        mAppIntro = (ExpandableTextView) findViewById(R.id.app_intro_info);
        mAppIntroGallery = (RecyclerView) findViewById(R.id.app_intro_gallery);
        setupAppIntroGallery();

        mAppIcon = (ImageView) findViewById(R.id.app_item_icon);
        mAppName = (TextView) findViewById(R.id.app_item_title);
        mAppSize = (TextView) findViewById(R.id.app_item_size);
        mAppDownloadNum = (TextView) findViewById(R.id.app_item_download_num);
        mAppUpdate = (TextView) findViewById(R.id.app_update_date);
        mAppVersion = (TextView) findViewById(R.id.app_item_version);
        mDeveloperInfo = (TextView) findViewById(R.id.app_developer_info);

        setupContent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupAppIntroGallery() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAppIntroGallery.setLayoutManager(layoutManager);
        // TODO : setup the adapter
        AppIntroImgGalleryAdapter adapter = new AppIntroImgGalleryAdapter(this, null);
        mAppIntroGallery.setAdapter(adapter);
    }

    /**
     *
     * @param intent this is the intent that transferred from the previous app item list adapter,
     *               and while the user is clicking on the item, we will put everything on the
     *               transferred intent, and we could retrieve all of the necessary information from
     *               the intent directly, without need to make the unnecessary network requests.
     *
     */
    private void setupContent(Intent intent) {
        if (intent != null) {
            Bundle appInfo = intent.getBundleExtra(AppConstants.APP_INFO_KEY);
            String appTitle = appInfo.getString(AppConstants.APP_INFO_TITLE);
            String appSize = appInfo.getString(AppConstants.APP_INFO_SIZE);
            String appDownloadNum = appInfo.getString(AppConstants.APP_INFO_DOWNLOAD_NUM);
            String appVersion = appInfo.getString(AppConstants.APP_INFO_VERSION);
            Bitmap appIcon = appInfo.getParcelable(AppConstants.APP_INFO_ICON);

            // then, we set the content we get into the appInfo object
            mAppIcon.setImageBitmap(appIcon);
            mAppName.setText(appTitle);
            mAppSize.setText(appSize);
            mAppVersion.setText(String.format(getString(R.string.app_version_str), appVersion));
            mAppDownloadNum.setText(String.format(getString(R.string.app_version_str), appDownloadNum));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
