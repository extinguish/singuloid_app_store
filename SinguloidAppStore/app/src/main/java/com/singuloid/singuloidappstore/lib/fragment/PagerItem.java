package com.singuloid.singuloidappstore.lib.fragment;

/**
 * Created by scguo on 15/10/16.
 */
public abstract class PagerItem {
    protected static final float DEFAULT_WIDTH = 1.f;

    private final CharSequence title;
    private final float width;

    protected PagerItem(CharSequence title, float width) {
        this.title = title;
        this.width = width;
    }

    public CharSequence getTitle() {
        return title;
    }

    public float getWidth() {
        return width;
    }
}
