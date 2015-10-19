package com.singuloid.singuloidappstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.singuloid.singuloidappstore.appfragment.AppCategoryFragment;
import com.singuloid.singuloidappstore.appfragment.BaseFragment;
import com.singuloid.singuloidappstore.lib.tabview.STabLayout;
import com.singuloid.singuloidappstore.lib.fragment.FragmentPagerItem;
import com.singuloid.singuloidappstore.lib.fragment.FragmentPagerItemAdapter;
import com.singuloid.singuloidappstore.lib.fragment.FragmentPagerItems;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolBarTitle = (TextView) findViewById(R.id.main_title);
        toolBarTitle.setText(getString(R.string.app_store_title));
        ImageView imgSearch = (ImageView) findViewById(R.id.main_search);
        imgSearch.setOnClickListener(this);

        ViewGroup tabContainer = (ViewGroup) findViewById(R.id.tab);
        LayoutInflater inflater = LayoutInflater.from(this);
        tabContainer.addView(inflater.inflate(R.layout.appstore_tabindicator_layout, tabContainer, false));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        STabLayout viewPagerTab = (STabLayout) findViewById(R.id.viewpagertab);

        // now, we init the ViewPager adapter
        FragmentPagerItems pagerItems = new FragmentPagerItems(this);

        pagerItems.add(FragmentPagerItem.of(getString(R.string.fragment_categoty_title_str), AppCategoryFragment.class));
        pagerItems.add(FragmentPagerItem.of(getString(R.string.fragment_rank_title_str), BaseFragment.class));
        pagerItems.add(FragmentPagerItem.of(getString(R.string.fragment_newest_title_str), BaseFragment.class));
        pagerItems.add(FragmentPagerItem.of(getString(R.string.fragment_manage_title_str), BaseFragment.class));

        FragmentPagerItemAdapter fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pagerItems);

        viewPager.setAdapter(fragmentPagerItemAdapter);
        viewPagerTab.setViewPager(viewPager);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
            case R.id.main_search:
                // TODO: add activity transition animation while we switching into the SearchActivity
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
        }
    }
}
