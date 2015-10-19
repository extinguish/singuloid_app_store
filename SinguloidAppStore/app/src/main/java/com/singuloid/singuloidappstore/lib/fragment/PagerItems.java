package com.singuloid.singuloidappstore.lib.fragment;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by scguo on 15/10/16.
 */
public class PagerItems <T extends PagerItem> extends ArrayList<T> {
    private final Context context;

    protected PagerItems(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
