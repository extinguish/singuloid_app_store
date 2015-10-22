package com.singuloid.singuloidappstore;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.singuloid.singuloidappstore.appfragment.DownloadManageFragment;
import com.singuloid.singuloidappstore.appfragment.LocalAppManageFragment;
import com.singuloid.singuloidappstore.lib.fragment.FragmentPagerItem;
import com.singuloid.singuloidappstore.lib.fragment.FragmentPagerItemAdapter;
import com.singuloid.singuloidappstore.lib.fragment.FragmentPagerItems;
import com.singuloid.singuloidappstore.lib.tabview.STabLayout;

/**
 * Created by scguo on 15/10/21.
 *
 * The activity for personal management activity
 *
 */
public class PersonalCenterActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "PersonalCenterActivity";

    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_center_activity_layout);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();
        mToolbarTitle = (TextView) findViewById(R.id.personal_title);
        mToolbarTitle.setText(getString(R.string.activity_app_management));
        ImageView imgSearch = (ImageView) findViewById(R.id.personal_search);
        imgSearch.setOnClickListener(this);

        ViewGroup tabContainer = (ViewGroup) findViewById(R.id.tab);
        LayoutInflater inflater = LayoutInflater.from(this);
        tabContainer.addView(inflater.inflate(R.layout.appstore_tabindicator_layout, tabContainer, false));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        STabLayout viewPagerTab = (STabLayout) findViewById(R.id.viewpagertab);

        // now, we init the ViewPager adapter
        FragmentPagerItems pagerItems = new FragmentPagerItems(this);

        pagerItems.add(FragmentPagerItem.of(getString(R.string.fragment_personal_installed_title_str), LocalAppManageFragment.class));
        pagerItems.add((FragmentPagerItem.of(getString(R.string.fragment_personal_download_manage_str), DownloadManageFragment.class)));

        FragmentPagerItemAdapter fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pagerItems);

        viewPager.setAdapter(fragmentPagerItemAdapter);
        viewPagerTab.setViewPager(viewPager);
        setupWindowTransitionAnim();
    }

    private void setupWindowTransitionAnim() {
        Explode explode = (Explode) TransitionInflater.from(this).inflateTransition(R.transition.activity_explode);
        getWindow().setEnterTransition(explode);
    }

    private void setupToolbar() {
        // add some additional attributes to the toolbar
        mToolbar.setNavigationIcon(android.R.drawable.arrow_down_float);
        mToolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
            case R.id.toolbar:
                Log.d(TAG, " on the back navigation button clicked on ... ");
                NavUtils.navigateUpFromSameTask(PersonalCenterActivity.this);
                break;
            case R.id.personal_search:
                // start the search panel with transition animation

                break;


        }
    }
}


























