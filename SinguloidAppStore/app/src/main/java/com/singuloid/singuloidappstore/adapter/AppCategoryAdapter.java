package com.singuloid.singuloidappstore.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.singuloid.singuloidappstore.R;
import com.singuloid.singuloidappstore.adapter.bean.AppCategoryBean;

import java.util.ArrayList;

/**
 * Created by scguo on 15/10/16.
 *
 * In AppCategoryAdapter implementation, we have data loading priority
 * set, we need to first connect to server to check the server data,
 * and then compare the data with the server, what the server return
 * are just the string data, and we compare the returned data set with
 * the local predefined data set, if conflict, then we need to add one more
 * pair into our local data set
 *
 */
public class AppCategoryAdapter extends BaseAdapter {
    private static final String TAG = "AppCategoryAdapter";

    private Context mContext;
    private ArrayList<AppCategoryBean> mCategoryList;

    public AppCategoryAdapter(Context context, ArrayList<AppCategoryBean> list) {
        this.mContext = context;
        this.mCategoryList = list;
    }

    @Override
    public int getCount() {
        return this.mCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.app_category_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.app_category_item_title);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.app_category_item_icon);
            convertView.setTag(viewHolder);
            Log.d(TAG, " the convert view are null ... ");
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AppCategoryBean item = mCategoryList.get(position);
        viewHolder.text.setText(item.getName());
        Log.d(TAG, " the resource id we get are : " + item.getImgResId());
        viewHolder.img.setImageResource(item.getImgResId());

        return convertView;
    }

    private static class ViewHolder {
        TextView text;
        ImageView img;
    }

}
