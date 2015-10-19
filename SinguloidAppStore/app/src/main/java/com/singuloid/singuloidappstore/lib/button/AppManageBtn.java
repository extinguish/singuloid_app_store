package com.singuloid.singuloidappstore.lib.button;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by scguo on 15/10/19.
 */
public class AppManageBtn extends ProcessButton {
    public AppManageBtn(Context context) {
        super(context);
    }

    public AppManageBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppManageBtn(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void drawProgress(Canvas canvas) {
        float scale = (float) getProgress() / (float) getMaxProgress();
        float indicatorWidth = (float) getMeasuredWidth() * scale;

        getProgressDrawable().setBounds(0, 0, (int) indicatorWidth, getMeasuredHeight());
        getProgressDrawable().draw(canvas);
    }
}
