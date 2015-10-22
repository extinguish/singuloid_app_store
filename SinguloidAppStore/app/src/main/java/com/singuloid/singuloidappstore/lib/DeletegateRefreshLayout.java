package com.singuloid.singuloidappstore.lib;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by scguo on 15/10/21.
 */
public class DeletegateRefreshLayout extends SwipeRefreshLayout {

    private ViewDelegate mViewDelegate;

    public DeletegateRefreshLayout(Context context) {
        super(context);
    }

    public DeletegateRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewDelegate(ViewDelegate viewDelegate) {
        this.mViewDelegate = viewDelegate;
    }

    @Override
    public boolean canChildScrollUp() {
        if (mViewDelegate != null) {
            return mViewDelegate.isReadyForPull();
        }
        return super.canChildScrollUp();
    }
}
