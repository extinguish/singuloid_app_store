package com.singuloid.singuloidappstore.appfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.singuloid.singuloidappstore.R;
import com.singuloid.singuloidappstore.adapter.AppCategoryAdapter;
import com.singuloid.singuloidappstore.adapter.bean.AppCategoryBean;
import com.singuloid.singuloidappstore.lib.DeletegateRefreshLayout;
import com.singuloid.singuloidappstore.lib.ViewDelegate;
import com.singuloid.singuloidappstore.util.AppCategoryContract;

import java.util.ArrayList;

/**
 * Created by scguo on 15/10/16.
 * <p/>
 * The fragment of which using to display the main category of the
 * AppCategory
 */
public class AppCategoryFragment extends Fragment implements ViewDelegate {

    private static final String TAG = "CategoryFragment";
    private DeletegateRefreshLayout mSwipeRefreshLayout;
    private GridView mGrid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categoty_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: the following logic should not put here, and improve later
        // TODO: if time is sufficient, try the MVP pattern to make this Fragment
        // TODO: even more robust
        mSwipeRefreshLayout = (DeletegateRefreshLayout) view.findViewById(R.id.category_refresh_container);
        setupRefreshLayout();

        mGrid = (GridView) view.findViewById(R.id.app_category_grid);
        ArrayList<AppCategoryBean> list = convertLocalDataToList(readLocalSet());
        AppCategoryAdapter gridAdapter = new AppCategoryAdapter(getActivity(), list);
        mGrid.setAdapter(gridAdapter);

    }

    private void setupRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.refresh_colors));
        mSwipeRefreshLayout.setOnRefreshListener(new OnCategoryUpdatedListener());
        mSwipeRefreshLayout.setViewDelegate(this);
    }

    /**
     * @return the container of which contains all of the basic data
     */
    private static ArrayList<Pair<Integer, Integer>> readLocalSet() {
        ArrayList<Pair<Integer, Integer>> localDataSet = new ArrayList<>();
        for (AppCategoryContract categoryConstants : AppCategoryContract.values()) {
            localDataSet.add(new Pair<>(categoryConstants.mName, categoryConstants.mImgRes));
        }
        return localDataSet;
    }

    private static ArrayList<AppCategoryBean> convertLocalDataToList(ArrayList<Pair<Integer, Integer>> srcData) {
        ArrayList<AppCategoryBean> list = new ArrayList<>();

        if (srcData != null) {
            for (Pair<Integer, Integer> data : srcData) {
                AppCategoryBean item = new AppCategoryBean(data.first, data.second);
                list.add(item);
            }
        }
        return list;
    }

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(mGrid, -1);
    }

    private class OnCategoryUpdatedListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {

            // the following are just test case
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO: do noting here

                    // if the refreshing is over
                    // we need to indicate that the refreshing is over
                    if (null != mSwipeRefreshLayout) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 3000);

        }
    }

}

















