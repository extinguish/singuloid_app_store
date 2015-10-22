package com.singuloid.singuloidappstore.appfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by scguo on 15/10/21.
 *
 * If we choose to install one app, and after we clicking on the "install" app, then
 * we will start downloading this apk file from the server, and before this apk is installed
 * on the system we need to display this apk information inside this Fragment, and after
 * this apk file is installed successfully, we just remove this item from this fragment.
 *
 */
public class DownloadManageFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
