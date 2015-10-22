package com.singuloid.singuloidappstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.singuloid.singuloidappstore.lib.DeletegateRefreshLayout;
import com.singuloid.singuloidappstore.lib.ViewDelegate;
import com.singuloid.singuloidappstore.util.AppConstants;

/**
 * Created by scguo on 15/10/21.
 */
public class RecommendationAppListActivity extends Activity implements ViewDelegate, SwipeRefreshLayout.OnRefreshListener {

    private DeletegateRefreshLayout mRefreshLayout;
    private RecyclerView mAppListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_app_list_layout);

        mRefreshLayout = (DeletegateRefreshLayout) findViewById(R.id.recommendation_app_list_container);
        setupRefreshLayout();
        mAppListView = (RecyclerView) findViewById(R.id.recommendation_app_list);
        setupAppListView();
        processIntent(getIntent());
    }

    private void setupRefreshLayout() {
        mRefreshLayout.setViewDelegate(this);
        mRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.refresh_colors));
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void setupAppListView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAppListView.setLayoutManager(layoutManager);
        // TODO: set up the adapter
    }

    /**
     * After process this intent that transferred from the RecommendationFragment,
     * we could choose to request different data source from server, thus we can
     * make the right choice
     *
     * @param intent the intent we received from the RecommendationFragment of which
     *               with the app category string constants append
     */
    private void processIntent(Intent intent) {
        if (null != intent) {
            String category = intent.getStringExtra(AppConstants.RECOMMENDATION_CATEGORY_KEY);
            // since Java-7, the JDK start supports the String constants as the switch case
            // and we could use this feature to abound the if-else jungle
            switch (category) {
                default:
                case AppConstants.RECOMMENDATION_GAME_CATEGORY_VAL:

                    break;
                case AppConstants.RECOMMENDATION_NECESSARY_CATEGORY_VAL:

                    break;
                case AppConstants.RECOMMENDATION_TOOLS_CATEGORY_VAL:

                    break;
                case AppConstants.RECOMMENDATION_NEWEST_CATEGORY_VAL:

                    break;
            }
        }
    }

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(mAppListView, -1);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO: provide a better logic
                // TODO: at here to provide better data set

                // after the logic, we need to update the tag to indicate the
                // refresh process are end
                if (mRefreshLayout != null) {
                    mRefreshLayout.setRefreshing(false);
                }
            }
        }, 9000);
    }
}
