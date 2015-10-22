package com.singuloid.singuloidappstore.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;

/**
 * Created by scguo on 15/10/20.
 */
public class Utils {

    public static float convertDpToPx(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return dp * (displayMetrics.densityDpi / 160f);
    }

    public static String convertByteToMB(float byteNum) {
        float resultSize = byteNum / 1024 / 1024;
        DecimalFormat resultFormat = new DecimalFormat("0.00");
        return resultFormat.format(resultSize).concat("M");
    }

    /**
     * Create a new drawable based on the given drawable size
     * @param context
     * @param targetDrawable the drawable of which we need to resize to the size we need
     * @return
     */
    public static Drawable resizeDrawable(Context context, Drawable targetDrawable) {
        Drawable sysDefAppIcon = getSystemDefIcon(context);
        final int width = sysDefAppIcon.getIntrinsicWidth();
        final int height = sysDefAppIcon.getIntrinsicHeight();
        Bitmap bitmap = ((BitmapDrawable) targetDrawable).getBitmap();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return new BitmapDrawable(context.getResources(), resizedBitmap);
    }

    /**
     *
     * @param context
     * @return the system default app icon
     */
    public static Drawable getSystemDefIcon(Context context) {
        return context.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
    }


}
