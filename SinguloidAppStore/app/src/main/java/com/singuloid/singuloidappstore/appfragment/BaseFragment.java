package com.singuloid.singuloidappstore.appfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.singuloid.singuloidappstore.R;
import com.singuloid.singuloidappstore.lib.DeletegateRefreshLayout;
import com.singuloid.singuloidappstore.lib.ViewDelegate;
import com.singuloid.singuloidappstore.lib.fragment.FragmentPagerItem;

/**
 * Created by scguo on 15/10/16.
 */
public class BaseFragment extends Fragment implements ViewDelegate {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    private DeletegateRefreshLayout mAppItemListRefreshableContainer;
    private RecyclerView mAppItemList;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = FragmentPagerItem.getPosition(getArguments());
        mAppItemListRefreshableContainer = (DeletegateRefreshLayout) view.findViewById(R.id.app_market_refreshable_container);

        setupRefreshLayout();

        mAppItemList = (RecyclerView) view.findViewById(R.id.app_market_list);
        final LinearLayoutManager appListLayoutManager = new LinearLayoutManager(getActivity());
        mAppItemList.setLayoutManager(appListLayoutManager);

        // and we create different ListView dataSet based on the current
        // fragment index
        setupAdapter(position);

    }

    // TODO: setup the delegate refresh layout
    private void setupRefreshLayout() {
        mAppItemListRefreshableContainer.setColorSchemeColors(getResources().getIntArray(R.array.refresh_colors));
        mAppItemListRefreshableContainer.setOnRefreshListener(new RefreshListener());
        this.mAppItemListRefreshableContainer.setViewDelegate(this);
    }

    private void setupAdapter(final int position) {
        if (mAppItemList != null) {
            // set adapter for this RecyclerListView

        }
    }

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(mAppItemList, -1);
    }

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {

            // TODO: just for testing ...
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO: provide a better logic
                    // TODO: at here to provide better data set

                    // after the logic, we need to update the tag to indicate the
                    // refresh process are end
                    if (mAppItemListRefreshableContainer != null) {
                        mAppItemListRefreshableContainer.setRefreshing(false);
                    }
                }
            }, 9000);
        }
    }


}



































