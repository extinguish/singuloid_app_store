package com.singuloid.singuloidappstore.appfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.singuloid.singuloidappstore.R;
import com.singuloid.singuloidappstore.RecommendationAppListActivity;
import com.singuloid.singuloidappstore.util.AppConstants;

/**
 * Created by scguo on 15/10/21.
 *
 * The fragment of which contains and display the information about the
 * recommendation information from the server
 *
 */
public class RecommendationFragment extends Fragment implements View.OnClickListener {

    private ImageView mAppGame, mAppNewest, mAppTools, mAppNecessary;
    private RecyclerView mRecommendationList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommendation_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAppGame = (ImageView) view.findViewById(R.id.recommendation_game);
        mAppNewest = (ImageView) view.findViewById(R.id.recommendation_newest);
        mAppTools = (ImageView) view.findViewById(R.id.recommendation_tools);
        mAppNecessary = (ImageView) view.findViewById(R.id.recommendation_necessary);

        mAppGame.setOnClickListener(this);
        mAppNewest.setOnClickListener(this);
        mAppNecessary.setOnClickListener(this);
        mAppTools.setOnClickListener(this);

        mRecommendationList = (RecyclerView) view.findViewById(R.id.system_recommendation_app_list);
        setupRecommendationList();
    }

    private void setupRecommendationList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecommendationList.setLayoutManager(layoutManager);
        // TODO: setup adapter

    }

    @Override
    public void onClick(View v) {
        Intent appListActivity = new Intent(getActivity(), RecommendationAppListActivity.class);
        switch (v.getId()) {
            default:
            case R.id.recommendation_game:
                appListActivity.putExtra(AppConstants.RECOMMENDATION_CATEGORY_KEY,
                        AppConstants.RECOMMENDATION_GAME_CATEGORY_VAL);
                startActivity(appListActivity);
                break;
            case R.id.recommendation_newest:
                appListActivity.putExtra(AppConstants.RECOMMENDATION_CATEGORY_KEY,
                        AppConstants.RECOMMENDATION_NEWEST_CATEGORY_VAL);
                startActivity(appListActivity);
                break;
            case R.id.recommendation_tools:
                appListActivity.putExtra(AppConstants.RECOMMENDATION_CATEGORY_KEY,
                        AppConstants.RECOMMENDATION_TOOLS_CATEGORY_VAL);
                startActivity(appListActivity);
                break;
            case R.id.recommendation_necessary:
                appListActivity.putExtra(AppConstants.RECOMMENDATION_CATEGORY_KEY,
                        AppConstants.RECOMMENDATION_NECESSARY_CATEGORY_VAL);
                startActivity(appListActivity);
                break;
        }
    }


}

































