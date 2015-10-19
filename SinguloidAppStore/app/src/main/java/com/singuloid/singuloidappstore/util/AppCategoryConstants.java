package com.singuloid.singuloidappstore.util;

import com.singuloid.singuloidappstore.R;

/**
 * Created by scguo on 15/10/16.
 * <p/>
 * Here we using the enum to store the App Category constants
 */
public enum AppCategoryConstants {

//    APP_CATEGORY_UTILITIES(R.string.app_category_tools_str, R.drawable.app_category_utilities),
//    APP_CATEGORY_WALLPAPER(R.string.app_category_theme_wallpaper_str, R.drawable.app_category_utilities),
//    APP_CATEGORY_SOCIAL_COMMUNICATION(R.string.app_category_social_messenger_str, R.drawable.app_category_social_messenger),
//    APP_CATEGORY_SHOOTING_PHOTO(R.string.app_category_photo_beautiful_str, R.drawable.app_category_shooting_photo),
//    APP_CATEGORY_AV_VIDEO(R.string.app_category_multimedia_str, R.drawable.app_category_video),
//    APP_CATEGORY_LIFE_STYLE(R.string.app_category_enjoy_life_str, R.drawable.app_category_lifestyle),
//    APP_CATEGORY_FINIANCIAL(R.string.app_category_finicial_shopping_str, R.drawable.app_category_finanical),
//    APP_CATEGORY_EDUCATION(R.string.app_category_education_str, R.drawable.app_category_education),
//    APP_CATEGORY_ADVISORY(R.string.app_category_news_reading_str, R.drawable.app_category_advisory_news),
//    APP_CATEGORY_TRAVEL(R.string.app_category_travel_str, R.drawable.app_category_travel),
//    APP_CATEGORY_GAME(R.string.app_category_game_str, R.drawable.app_category_game);


    APP_CATEGORY_WALLPAPER(R.string.app_category_theme_wallpaper_str, R.mipmap.category_advisory_news),
    APP_CATEGORY_SOCIAL_COMMUNICATION(R.string.app_category_social_messenger_str, R.mipmap.category_advisory_news),
    APP_CATEGORY_SHOOTING_PHOTO(R.string.app_category_photo_beautiful_str, R.mipmap.category_advisory_news),
    APP_CATEGORY_AV_VIDEO(R.string.app_category_multimedia_str, R.mipmap.category_advisory_news),
    APP_CATEGORY_LIFE_STYLE(R.string.app_category_enjoy_life_str, R.mipmap.category_advisory_news),
    APP_CATEGORY_FINIANCIAL(R.string.app_category_finicial_shopping_str, R.mipmap.category_advisory_news),
    APP_CATEGORY_EDUCATION(R.string.app_category_education_str, R.mipmap.category_advisory_news),
    APP_CATEGORY_ADVISORY(R.string.app_category_news_reading_str, R.mipmap.category_advisory_news),
    APP_CATEGORY_TRAVEL(R.string.app_category_travel_str, R.mipmap.category_advisory_news),
    APP_CATEGORY_GAME(R.string.app_category_game_str, R.mipmap.category_advisory_news);


    public final int mName;
    public final int mImgRes;
    AppCategoryConstants(int categoryName, int categoryImg) {
         this.mName = categoryName;
        this.mImgRes = categoryImg;
    }


}
