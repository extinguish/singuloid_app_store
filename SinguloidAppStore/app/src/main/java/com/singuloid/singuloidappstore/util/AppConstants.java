package com.singuloid.singuloidappstore.util;

/**
 * Created by scguo on 15/10/21.
 *
 * Using the interface to store constants can save the class instance creation
 * memory consumption
 *
 */
public interface AppConstants {

    public static final String RECOMMENDATION_CATEGORY_KEY = "recommendation_category";

    public static final String RECOMMENDATION_GAME_CATEGORY_VAL = "category_game";
    public static final String RECOMMENDATION_NEWEST_CATEGORY_VAL = "category_newest";
    public static final String RECOMMENDATION_TOOLS_CATEGORY_VAL = "category_tools";
    public static final String RECOMMENDATION_NECESSARY_CATEGORY_VAL = "category_necessary";

    // define the constants of which using transfer some data between
    // the app list fragment or activity to the AppDetailActivity
    public static final String APP_INFO_KEY = "app_info_key";

    // we could put bitmap into the bundle
    public static final String APP_INFO_ICON = "app_info_icon";
    public static final String APP_INFO_TITLE = "app_info_title";
    public static final String APP_INFO_SIZE = "app_info_size";
    public static final String APP_INFO_DOWNLOAD_NUM = "app_info_download_num";
    public static final String APP_INFO_VERSION = "app_info_version";
    public static final String APP_INFO_UPDATE_TIME = "app_info_update_time";




}
