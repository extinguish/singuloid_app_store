package com.singuloid.singuloidappstore.lib;

/**
 * Created by scguo on 15/10/21.
 *
 * The delegate of which to indicate whether the listView can scroll or not
 *
 */
public interface ViewDelegate {
    public boolean isReadyForPull();
}
