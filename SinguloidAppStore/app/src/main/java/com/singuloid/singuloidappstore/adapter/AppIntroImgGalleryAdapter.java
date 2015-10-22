package com.singuloid.singuloidappstore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.singuloid.singuloidappstore.R;

import java.util.ArrayList;

/**
 * Created by scguo on 15/10/22.
 *
 * The adapter for the App introduction image gallery, and we using the
 * {@link android.support.v7.widget.RecyclerView} to implement this gallery
 *
 */
public class AppIntroImgGalleryAdapter extends RecyclerView.Adapter<AppIntroImgGalleryAdapter.IntroGalleryViewHolder> {
    private static final String TAG = "AppIntroImgGalleryAdapter";
    private final LayoutInflater mInflater;
    private final Context mContext;

    // what we receive only are the image url, and then we could
    // use this url to retrieve the detailed image from the server
    private ArrayList<String> mGalleryImgUrlList;
    public AppIntroImgGalleryAdapter(Context context, ArrayList<String> imgUrlList) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mGalleryImgUrlList = imgUrlList;
    }

    @Override
    public IntroGalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IntroGalleryViewHolder(mInflater.inflate(R.layout.app_intro_gallery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(IntroGalleryViewHolder holder, int position) {
        // TODO: in real word, we using the Volley NetworkImageView to bind to the GalleryImgItem
        // TODO: what we need is just the image url will be enough
//        holder.mGalleryImgItem.setImageDrawable();
    }

    @Override
    public int getItemCount() {
        return mGalleryImgUrlList != null ? mGalleryImgUrlList.size() : 0;
    }

    public static class IntroGalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView mGalleryImgItem;

        public IntroGalleryViewHolder(View itemView) {
            super(itemView);
            mGalleryImgItem = (ImageView) itemView.findViewById(R.id.app_intro_item_img);
        }
    }
}
