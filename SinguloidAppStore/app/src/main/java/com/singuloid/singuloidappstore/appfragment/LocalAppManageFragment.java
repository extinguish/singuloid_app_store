package com.singuloid.singuloidappstore.appfragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.singuloid.singuloidappstore.R;
import com.singuloid.singuloidappstore.adapter.LocalInstalledAppListAdapter;
import com.singuloid.singuloidappstore.adapter.apploader.LocalInstalledAppListLoader;
import com.singuloid.singuloidappstore.adapter.bean.LocalInstalledAppEntry;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by scguo on 15/10/17.
 *
 * The Fragment of which representing the local installed app list
 *
 */
public class LocalAppManageFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<LocalInstalledAppEntry>> {
    private static final String TAG = "LocalAppManageFragment";
    // the integer of which using to tag the loader instance we started or managing
    private static final int LOADER_ID = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new EventHandler(this);
    }

    private RecyclerView mAppListView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LocalInstalledAppListAdapter mAppListAdapter;
    private EventHandler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View appManageView = inflater.inflate(R.layout.fragment_app_manage_layout, container, false);

        return appManageView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, " on View created ... ");
        mAppListView = (RecyclerView) view.findViewById(R.id.local_app_list);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mAppListView.setLayoutManager(mLayoutManager);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }


    @Override
    public Loader<List<LocalInstalledAppEntry>> onCreateLoader(int i, Bundle bundle) {
        Log.d(TAG, " on create loader ... ");
        return new LocalInstalledAppListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<LocalInstalledAppEntry>> listLoader, List<LocalInstalledAppEntry> localInstalledAppEntries) {
        Log.d(TAG, " on load finished ... ");
        Message msg = new Message();
        msg.what = ON_DATA_LOADED;
        msg.obj = localInstalledAppEntries;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onLoaderReset(Loader<List<LocalInstalledAppEntry>> listLoader) {
        Log.d(TAG, " on load reset ... ");
        mAppListAdapter.setData(null);
    }

    private static final int ON_DATA_LOADED = 1;

    static class EventHandler extends Handler {

        private final WeakReference<LocalAppManageFragment> mFragment;

        public EventHandler(LocalAppManageFragment fragment) {
            this.mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            LocalAppManageFragment fragment = mFragment.get();
            if (fragment != null) {
                fragment.handleEvents(msg);
            }
        }
    }

    private void handleEvents(Message msg) {
        Log.d(TAG, " we have received the data we need ... ");
        mAppListAdapter = new LocalInstalledAppListAdapter(getActivity(), (List<LocalInstalledAppEntry>) msg.obj);
        mAppListView.setAdapter(mAppListAdapter);
    }
}




















