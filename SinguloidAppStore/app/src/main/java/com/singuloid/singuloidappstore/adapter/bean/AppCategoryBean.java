package com.singuloid.singuloidappstore.adapter.bean;

/**
 * Created by scguo on 15/10/16.
 */
public class AppCategoryBean {
    private final int mName;
    private final int mImgResId;

    public AppCategoryBean(int name, int resId) {
        this.mName = name;
        this.mImgResId = resId;
    }

    public int getName() {
        return this.mName;
    }

    public int getImgResId() {
        return mImgResId;
    }

}
