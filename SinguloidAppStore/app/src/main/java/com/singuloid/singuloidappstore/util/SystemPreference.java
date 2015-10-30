package com.singuloid.singuloidappstore.util;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by scguo on 15/10/16.
 *
 * The SystemPreference of which extends from SharedPreference and here we using
 * her to persistent some temp data
 *
 */
public class SystemPreference implements SharedPreferences.OnSharedPreferenceChangeListener {
    package com.singuloid.workphone.settings;

    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.content.SharedPreferences.Editor;
    import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
    import android.preference.PreferenceManager;
    import android.util.Log;

    import com.singuloid.workphone.BuildConfig;
    import com.singuloid.workphone.R;
    import com.singuloid.workphone.appdb.AppDB;
    import com.singuloid.workphone.launcher.IconCache;
    import com.singuloid.workphone.launcher.IconItemInfo;
    import com.singuloid.workphone.launcher.Launcher;
    import com.singuloid.workphone.launcher.ShortcutInfo;
    import com.singuloid.workphone.util.AESEncryptor;
    import com.singuloid.workphone.util.WorkingStateUtil;

    import java.net.URISyntaxException;
    import java.text.Collator;
    import java.util.ArrayList;
    import java.util.Comparator;
    import java.util.LinkedHashMap;
    import java.util.LinkedHashSet;
    import java.util.List;
    import java.util.Set;
    import java.util.concurrent.locks.Lock;
    import java.util.concurrent.locks.ReentrantLock;

    public class SystemPreferences implements SharedPreferences.OnSharedPreferenceChangeListener {

        private final int MODE = Context.MODE_WORLD_READABLE
                + Context.MODE_WORLD_WRITEABLE;

        private final static String EmptyString = "";

        private static SystemPreferences _Current = null;

        private Launcher mLauncher = null;

        private boolean isWorking = true;
        private boolean isWorkingInitialized = false;

        static final String PREF_IS_WORKING = "IsWorking";
        static final String PREF_PASS = "Pass";
        static final String PREF_HOMESCREEN_ENDLESS_LOOP = "EndlessHomescreenLoop";
        static final String PREF_SWIPE_UP_ACTION = "SwipeUpAction";
        static final String PREF_CURRENT_DRAWER_SORT_ORDER = "CurrentDrawerSortOrder";
        static final String PREF_SESSIONID = "SessionId";
        static final String PREF_PWDRULE = "PwdR";
        static final String PREF_NEED_MOD_PWD_FLAG = "NeedModPwd";
        static final String REG_PHONE_NUMBER = "RegPhoneNum";
        static final String IS_MODIFY_USER_PASSWORD = "isModifyUserPassword";
        static final String DEVICE_ID = "DeviceId";
        static final String CURRENT_PAGE = "CurrentPage";
        static final String LOCK_TYPE = "LockType";
        static final String IS_DEFAULT_LAUNCHER = "IsDefaultLauncher";

        static final String USER_NAME_PERSONAL = "UserPhoneNum";
        static final String USER_NAME_ENTERPRISE = "EnterpriseUserEmail";

        // the tag of which using to indicate whether the user close the floating window
        // manually, and if it is closed by the user, then, we should not start the Floating
        // view automatically, otherwise, just open it automatically
        static final String CLOSE_FLOATING_VIEW_MANUL = "closeFloatingViewManul";

        // add the key for keeping the max sync day
        static final String MAX_SYNC_INTERVAL_IN_DAY = "maxSyncIntervalInDay";
        static final String IS_MUST_SYNC_COUNTING_STARTED = "isMustSyncCountingStarted";

        // add the key for sync date and time
        static final String MAX_SYNC_DESTINATION_TIME = "maxSyncDestinationTime";

        // the list of which using to track the auth app list
        static final String AUTHED_APP_SET = "authenticatedAppSet";
        // the set of which using to track the restricted app set
        static final String RESTRICTED_APP_SET = "restrictedAppSet";

        // add the local install app id list string key
        static final String LOCAL_INSTALLED_APP_ID_LIST_STR = "localInstalledAppIdListStr";

        // TODO: the following statement need more testing for availability
        // the key of which use to indicate the
        static final String MAX_LOGIN_COUNT = "maxLoginCount";

        static final String HAS_ENTER_RESTRICT_LOGIN_STATE = "hasEnterRestrictLoginState";

        public static final String IS_TRIGGERED_FROM_FLOATINGWINDOW = "TriggeredFromFloatingWindow";
        public static final String IS_IN_WORKPHONE = "IsInWorkPhoneNow";

        public static final String WORKSPACE_SHORTCUT_COUNT = "workspaceShortcutCount";

        static final String IF_FIRST_ENTER_LOGIN = "isFirstEnterLogin";
        // this key value will be using for dynamic generate the encrypt key based on the current
        // user login information
        static final String ENCRYPT_KEY = "encryptKey";

        // the login user name defined here are using for dynamic generate the encrypt key
        static final String USER_NAME = "login_username";
        static final String ENTERPRISE_SERVER_URL_DEBUG = "EpServerUrlDebug";
        static final String ENTERPRISE_PUSH_SERVER_URL = "EpPushServerUrl";
        static final String ENTERPRISE_DOWNLOAD_SERVER_URL = "EpDownloadServerUrl";

        static final String ENTERPRISE_COMPANY_FLAG = "EnterpriseCompanyFlag";

        // TODO: scguo the following key are added just for each specified company,
        // TODO: if the server really supports the SAAS service, we will not need it anymore
        static final String ENTERPRISE_SERVER_URL_RELEASE = "EpServerUrlRelease";
        static final String PERSONAL_SERVER_URL_DEBUG = "PnServerUrlDebug";
        static final String PERSONAL_PUSH_SERVER_URL_DEBUG = "PnPushServerUrlDebug";


        // the following string fields are using to construct the basic SOCKS5 configuration object
        static final String SOCKS_SERVER_HOST = "socksServerHost";
        static final String SOCKS_SERVER_PORT = "socksServerPort";
        static final String SOCKS_PSW = "socksPsw";
        static final String SOCKS_PROXY_PORT = "socksProxyPort"; // this is also the local port number
        static final String SOCKS_ENCRYPTION_METHOD = "socksEncryptionMethod";
        static final String SOCKS_TIMEOUT_IN_SECS = "socksTimeoutInSecs";

        // as the screen width and height are persistent all over the app,
        // so we need to just calculate it once, and store it and use it forever
        // until we reinstall it on a different device
        static final String DEVICE_SCREEN_WIDTH = "deviceScreenWidth";
        static final String DEVICE_SCREEN_HEIGHT = "deviceScreenHeight";

        // make the SharedPreference of which using to store the locally proxied app
        static final String NATIVE_PROXIED_APP_SET = "nativeProxiedAppList";

        static final String SAAS_URL_HISTORY_SET = "saasUrlHistroySet";

        static final String CLOUD_MODE = "currentCloudMode";
        static final String PRIVATE_CLOUD_MODE_URL = "privateCloudMode";

        // as the requirement of 3rd party apps need to start the Socks proxy and the
        // requirements for switch sock proxy, so we need to process these two events
        // separately
        static final String IS_CUSTOM_APP_SOCKS = "isCustomAppSocksProxy";

        public static SystemPreferences getInstance() {
            if (_Current == null)
                _Current = new SystemPreferences();
            return _Current;
        }

        public void setLauncher(Launcher launcher) {
            if (launcher == null && mPreferences != null) {
                mPreferences.unregisterOnSharedPreferenceChangeListener(this);
                mPreferences = null;
            } else if (launcher != null) {
                mPreferences = PreferenceManager
                        .getDefaultSharedPreferences(launcher);
                mPreferences.registerOnSharedPreferenceChangeListener(this);
                PreferenceManager.setDefaultValues(launcher, R.xml.settings, false);
            }
            mLauncher = launcher;
        }

        public Launcher getLauncher() {
            return mLauncher;
        }

        private Intent getIntent(String key) {
            String val = mPreferences.getString(PREF_SWIPE_UP_ACTION, EmptyString);
            if (val.equals(EmptyString)) {
                try {
                    return Intent.parseUri(val, 0);
                } catch (URISyntaxException e) {
                    return null;
                }
            } else
                return null;
        }

        public boolean getEndlessScrolling() {
            return mPreferences.getBoolean(PREF_HOMESCREEN_ENDLESS_LOOP, true);
        }

        public boolean getIsWorking(Context context) {
            if (isWorkingInitialized)
                return isWorking;
            ensurePreference(context);
            isWorkingInitialized = true;
            isWorking = mPreferences.getBoolean(PREF_IS_WORKING, false);
            return isWorking;
        }

        public void setIsWorking(Context context, boolean value) {
            if (!isWorkingInitialized) {
                isWorkingInitialized = true;
                isWorking = !value;
            }
            if (isWorking == value)
                return;

            isWorking = value;
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(PREF_IS_WORKING, isWorking);
            editor.commit();

            /**
             * when state changed , send a broadcast so that other app can get the new state
             */
            WorkingStateUtil.sendBroadcastWhenWorkingStateChanged(context, value);
        }

        public boolean isInWorkPhoneCurrent(Context context) {
            ensurePreference(context);
            return mPreferences.getBoolean(IS_IN_WORKPHONE, false);
        }

        public void setIsInWorkPhoneCurrent(Context context, boolean whetherIn) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(IS_IN_WORKPHONE, whetherIn);
            editor.commit();
        }

        public boolean isTriggeredFromFloating(Context context) {
            ensurePreference(context);
            return mPreferences.getBoolean(IS_TRIGGERED_FROM_FLOATINGWINDOW, false);
        }

        public void setIsTriggeredFromFloatingwindow(Context context, boolean isTriggered) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(IS_TRIGGERED_FROM_FLOATINGWINDOW, isTriggered);
            editor.commit();
        }

        public boolean isFloatingViewClosedByManul(Context context) {
            ensurePreference(context);
            return mPreferences.getBoolean(CLOSE_FLOATING_VIEW_MANUL, false);
        }

        public void setIsFloatingViewClosedByManul(Context context, boolean isManul) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(CLOSE_FLOATING_VIEW_MANUL, isManul);
            editor.commit();
        }

        /**
         * if WorkPhone is the default launcher
         * @return
         */
        public boolean isWorkPhoneDefault(Context context){
            ensurePreference(context);
            return mPreferences.getBoolean(IS_DEFAULT_LAUNCHER, false);
        }

        /**
         * set WorkPhone as the default launcher
         */
        public void setWorkPhoneDefault(Context context){

            ensurePreference(context);

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(IS_DEFAULT_LAUNCHER, true);
            editor.commit();
        }

        private void dataEncrypt(String key, String value) {
            SharedPreferences.Editor editor = mPreferences.edit();
            try {
                if (!value.equals("")) {
                    editor.putString(key, AESEncryptor.encrypt(MAK, value));
                } else {
                    editor.putString(key, value);
                }
            } catch (Exception e) {
                editor.putString(key, value);
                e.printStackTrace();
            }
            editor.commit();
        }

        private String dataDecrypt(String key) {
            String str = mPreferences.getString(key, "");
            try {
                if (str != null && !"".equals(str)) {
                    str = AESEncryptor.decrypt(MAK, str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return str;
        }

        public SystemPreferences ensurePreference(Context context) {
            if (mPreferences == null) {
                mPreferences = PreferenceManager
                        .getDefaultSharedPreferences(context);
            }
            return this;
        }

        public void setDeviceId(Context context, String value) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(DEVICE_ID, value);
            editor.commit();
        }

        public String getDeviceId(Context context) {
            ensurePreference(context);
            return mPreferences.getString(DEVICE_ID, "");
        }

        public void setPersonalUserName(Context context, String value)
        {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(USER_NAME_PERSONAL, value);
            editor.commit();
        }

        public String getUserNamePersonal(Context context)
        {
            ensurePreference(context);
            return mPreferences.getString(USER_NAME_PERSONAL, "");
        }

        public void storeAuthedAppSet(Context context, Set<String> appSet) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putStringSet(AUTHED_APP_SET, appSet);
            editor.commit();
        }
        private Set<String> mDefaultAuthedAppSet = new LinkedHashSet<String>();
        public Set<String> getAuthedAppList(Context context) {
            ensurePreference(context);
            // we should check the null set condition, thus to forbid the null pointer exception
            return mPreferences.getStringSet(AUTHED_APP_SET, mDefaultAuthedAppSet);
        }

        public void storeRestictedAppsSet(Context context, Set<String> appsSet) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putStringSet(RESTRICTED_APP_SET, appsSet);
            editor.commit();
        }

        public Set<String> getRestrictedAppsSet(Context context) {
            ensurePreference(context);
            return mPreferences.getStringSet(RESTRICTED_APP_SET, null);
        }

        public void setEnterpriseUserName(Context context, String value)
        {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(USER_NAME_ENTERPRISE, value);
            editor.commit();
        }

        public String getUserNameEnterprise(Context context)
        {
            ensurePreference(context);
            return mPreferences.getString(USER_NAME_ENTERPRISE, "");
        }

        public void setMaxLoginTimes(Context context, final int times)
        {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(MAX_LOGIN_COUNT, times);
            editor.commit();
        }

        public int getMaxLoginTimes(Context context)
        {
            ensurePreference(context);
            // here, we set the default value of the max login times are 3
            // as 3 is the minimal times the user can test, this value is decided by the server
            // the 3 is the default value
            return mPreferences.getInt(MAX_LOGIN_COUNT, 3);
        }

        public void setHasEnterRestrictLoginState(Context context, final boolean state)
        {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(HAS_ENTER_RESTRICT_LOGIN_STATE, state);
            editor.commit();
        }

        public boolean getHasEnterRestrictLoginState(Context context)
        {
            ensurePreference(context);
            return mPreferences.getBoolean(HAS_ENTER_RESTRICT_LOGIN_STATE, false);
        }

        public void setIfFirstEnterLogin(Context context, final boolean val)
        {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(IF_FIRST_ENTER_LOGIN, val);
            editor.commit();
        }

        public void setEncryptKey(Context context, final String keyVal)
        {
            ensurePreference(context);
            dataEncrypt(ENCRYPT_KEY, keyVal);
        }

        public String getEncryptKey(Context context)
        {
            ensurePreference(context);
            // by default, the encrypt key value are ""
            return dataDecrypt(ENCRYPT_KEY);
        }

        public void setLoginUserName(Context context, final String userName)
        {
            ensurePreference(context);
            dataEncrypt(USER_NAME, userName);
        }

        public String getLoginUserName(Context context)
        {
            ensurePreference(context);
            return dataDecrypt(USER_NAME);
        }

        public boolean isFirstEnterLogin(Context context)
        {
            ensurePreference(context);
            return mPreferences.getBoolean(IF_FIRST_ENTER_LOGIN, true);
        }

        static final String RESTRICTION_VAL = "restrictVal";

        public void setRestrictionVal(Context context, final int restrictVal)
        {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(RESTRICTION_VAL, restrictVal);
            editor.commit();
        }

        public int getRestrictValue(Context context)
        {
            ensurePreference(context);
            return mPreferences.getInt(RESTRICTION_VAL, 3);
        }

        public void setMaxSyncIntervalInDay(Context context, final int maxSyncInDay)
        {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(MAX_SYNC_INTERVAL_IN_DAY, maxSyncInDay);
            editor.commit();
        }

        public int getMaxSyncIntervalInDay(Context context)
        {
            ensurePreference(context);
            // the default max interval are 7 days, one week
            return mPreferences.getInt(MAX_SYNC_INTERVAL_IN_DAY, 7);
        }

        public void setIsMustSyncCountingStarted(Context context, final boolean isStarted)
        {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(IS_MUST_SYNC_COUNTING_STARTED, isStarted);
            editor.commit();
        }

        public boolean getIsMustSyncCountingStarted(Context context)
        {
            ensurePreference(context);
            // the default tag are false, that is not started
            return mPreferences.getBoolean(IS_MUST_SYNC_COUNTING_STARTED, false);
        }

        public void setMaxSyncDestinationTime(Context context, final String timeStr)
        {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(MAX_SYNC_DESTINATION_TIME, timeStr);
            editor.commit();
        }

        public String getMaxSyncDestinationTime(Context context)
        {
            ensurePreference(context);
            // the default time we set as "", not null
            return mPreferences.getString(MAX_SYNC_DESTINATION_TIME, "");
        }

        // this is for optimizing the process of committing the native installed app info
        // into the server
        static final String COMMIT_NATIVE_APP_INFO = "commitNativeAppInfo";

        public void setCommitNativeAppInfo(Context context, final String jsonArrayStr)
        {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(COMMIT_NATIVE_APP_INFO, jsonArrayStr);
            editor.commit();
        }

        public String getCommitNativeAppInfo(Context context)
        {
            ensurePreference(context);
            return mPreferences.getString(COMMIT_NATIVE_APP_INFO, "");
        }

        static final String NEED_TO_FORCE_RELOGIN = "needToForceRelogin";

        public void setNeedToForceRelogin(Context context, final boolean isNeeded)
        {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(NEED_TO_FORCE_RELOGIN, isNeeded);
            editor.commit();
        }

        public boolean getIsNeedToForceRelogin(Context context)
        {
            ensurePreference(context);
            // in the initial state, we do not need to force the user login into workPhone
            // and only after the user has turned on the AlarmManager, then, we do need to
            // set this tag as true
            return mPreferences.getBoolean(NEED_TO_FORCE_RELOGIN, false);
        }

        public void setSocksServerHost(Context context, String host) {
            ensurePreference(context);
            dataEncrypt(SOCKS_SERVER_HOST, host);
        }

        public String getSocksServerHost(Context context) {
            ensurePreference(context);
            return dataDecrypt(SOCKS_SERVER_HOST);
        }

        public void setSocksServerPort(Context context, String port) {
            ensurePreference(context);
            dataEncrypt(SOCKS_SERVER_PORT, port);
        }

        public String getSocksServerPort(Context context) {
            ensurePreference(context);
            return dataDecrypt(SOCKS_SERVER_PORT);
        }

        public void setSocksPsw(Context context, String psw) {
            ensurePreference(context);
            dataEncrypt(SOCKS_PSW, psw);
        }

        public String getSocksPsw(Context context) {
            ensurePreference(context);
            return dataDecrypt(SOCKS_PSW);
        }

        public void setSocksProxyPort(Context context, String proxyPort) {
            ensurePreference(context);
            dataEncrypt(SOCKS_PROXY_PORT, proxyPort);
        }

        public String getSocksProxyPort(Context context) {
            ensurePreference(context);
            return dataDecrypt(SOCKS_PROXY_PORT);
        }

        public void setSocksEncryptionMethod(Context context, String method) {
            ensurePreference(context);
            dataEncrypt(SOCKS_ENCRYPTION_METHOD, method);
        }

        public String getSocksEncryptionMethod(Context context) {
            ensurePreference(context);
            return dataDecrypt(SOCKS_ENCRYPTION_METHOD);
        }

        public void setSocksTimeoutInSecs(Context context, String timeouts) {
            ensurePreference(context);
            dataEncrypt(SOCKS_TIMEOUT_IN_SECS, timeouts);
        }

        public String getSocksTimeoutInSecs(Context context) {
            ensurePreference(context);
            return dataDecrypt(SOCKS_TIMEOUT_IN_SECS);
        }

        public void storeCurrentScreenShortcutCount(Context context, int count) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(WORKSPACE_SHORTCUT_COUNT, count);
            editor.commit();
        }

        public int getCurrentScreenShortcutCount(Context context) {
            ensurePreference(context);
            return mPreferences.getInt(WORKSPACE_SHORTCUT_COUNT, 0);
        }

        public void storeScreenWidth(Context context, int width) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(DEVICE_SCREEN_WIDTH, width);
            editor.commit();
        }

        public int getScreenWidth(Context context) {
            ensurePreference(context);
            return mPreferences.getInt(DEVICE_SCREEN_WIDTH, 900);
        }

        public void storeScreenHeight(Context context, int height) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(DEVICE_SCREEN_HEIGHT, height);
            editor.commit();
        }

        public int getScreenHeight(Context context) {
            ensurePreference(context);
            return mPreferences.getInt(DEVICE_SCREEN_HEIGHT, 1600);
        }

        public void storeProxiedAppIntoSet(Context context, Set<String> appSet) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putStringSet(NATIVE_PROXIED_APP_SET, appSet);
            editor.commit();
        }
        private Set<String> mProxyContainer = new LinkedHashSet<String>();
        public Set<String> getProxiedAppSet(Context context) {
            ensurePreference(context);
            return mPreferences.getStringSet(NATIVE_PROXIED_APP_SET, mProxyContainer);
        }

        private Set<String> mSaasUrlHistorySet = new LinkedHashSet<String>();
        public Set<String> getSaasUrlHistorySet(Context context) {
            ensurePreference(context);
            return mPreferences.getStringSet(SAAS_URL_HISTORY_SET, mSaasUrlHistorySet);
        }

        public void storeSaasUrlHistorySet(Context context, Set<String> saasUrlSet) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putStringSet(SAAS_URL_HISTORY_SET, saasUrlSet);
            editor.commit();
        }

        public void setCloudMode(Context context, String mode) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(CLOUD_MODE, mode);
            editor.commit();
        }

        public String getCloudMode(Context context) {
            ensurePreference(context);
            return mPreferences.getString(CLOUD_MODE, "saas");
        }

        public String getPrivateModeEnterpriseAddr(Context context) {
            ensurePreference(context);
            return mPreferences.getString(PRIVATE_CLOUD_MODE_URL, "");
        }

        public void setPrivateModeEnterpriseAddr(Context context, String addr) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(PRIVATE_CLOUD_MODE_URL, addr);
            editor.commit();
        }

        public void setCurrentPage(Context context, int value) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(CURRENT_PAGE, value);
            editor.commit();
        }

        public int getCurrentPage(Context context) {
            ensurePreference(context);
            return mPreferences.getInt(CURRENT_PAGE, 0);
        }

        public void setPass(Context context, String value) {
            ensurePreference(context);
            dataEncrypt(PREF_PASS, value);
        }

        public String getPass(Context context) {
            ensurePreference(context);
            return dataDecrypt(PREF_PASS);
        }

        public void setSessionId(Context context, String value) {
            ensurePreference(context);
            dataEncrypt(PREF_SESSIONID, value);
        }

        public String getSessionId(Context context) {
            ensurePreference(context);
            return dataDecrypt(PREF_SESSIONID);
        }

        public void setPwdRule(Context context, int value) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(PREF_PWDRULE, value);
            editor.commit();
        }

        public int getPwdRule(Context context) {
            ensurePreference(context);
            return mPreferences.getInt(PREF_PWDRULE, 0);
        }

        public void setNeedModPwd(Context context, int value) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(PREF_NEED_MOD_PWD_FLAG, value);
            editor.commit();
        }

        public void setLockType(Context context, String value) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(LOCK_TYPE, value);
            editor.commit();
        }

        public String getLockType(Context context) {
            ensurePreference(context);
            return mPreferences.getString(LOCK_TYPE, "");
        }

        public void isModifiedUserPassword(Context context, boolean isModify) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(IS_MODIFY_USER_PASSWORD, isModify);
        }

        public String getRegPhone(Context context) {
            ensurePreference(context);
            return mPreferences.getString(REG_PHONE_NUMBER, "");
        }

        public void setRegPhoneNumber(Context context, String phoneNum) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(REG_PHONE_NUMBER, phoneNum);
            editor.commit();
        }

        // get and set enterprise push server url
        public void setEnterprisePushServerUrl(Context context, String enterprisePushServerUrl) {
            ensurePreference(context);
            dataEncrypt(ENTERPRISE_PUSH_SERVER_URL, enterprisePushServerUrl);
        }
        public String getEnterprisePushServerUrl() {
            return dataDecrypt(ENTERPRISE_PUSH_SERVER_URL);
        }

        // get and set enterprise test api server url
        public void setEnterpriseServerUrlTest(Context context, String enterpriseTestServerUrl) {
            ensurePreference(context);
            dataEncrypt(ENTERPRISE_SERVER_URL_DEBUG, enterpriseTestServerUrl);
        }
        public String getEnterpriseServerUrlTest() {
            String strUrl = mPreferences.getString(ENTERPRISE_SERVER_URL_DEBUG, enterpriseUrl);
            if(strUrl.equals(enterpriseUrl)){
                return enterpriseUrl;
            }else{
                return urlDecrypt(strUrl);
            }
        }

        public void setCompanyFlag(Context context, String companyFlag) {
            ensurePreference(context);
            dataEncrypt(ENTERPRISE_COMPANY_FLAG, companyFlag);
        }

        public String getCompanyFlag() {
            return dataDecrypt(ENTERPRISE_COMPANY_FLAG);
        }

        // get and set personal test push server url
        public void setPersonalPushServerUrlTest(Context context, String testPushServerUrl) {
            ensurePreference(context);
            dataEncrypt(PERSONAL_PUSH_SERVER_URL_DEBUG, testPushServerUrl);
        }
        public String getPersonalPushServerUrlTest() {
            String strUrl = mPreferences.getString(PERSONAL_PUSH_SERVER_URL_DEBUG, personalPushUrl);
            if(strUrl.equals(personalPushUrl)){
                return personalPushUrl;
            }else{
                return urlDecrypt(strUrl);
            }
        }

        //  get and set personal test api server url
        public void setPersonalServerUrlTest(Context context, String personalTestServerUrl) {
            ensurePreference(context);
            dataEncrypt(PERSONAL_SERVER_URL_DEBUG, personalTestServerUrl);
        }
        public String getPersonalServerUrlTest() {
            String strUrl = mPreferences.getString(PERSONAL_SERVER_URL_DEBUG, apiUrl);
            if(strUrl.equals(apiUrl)){
                return apiUrl;
            }else{
                return urlDecrypt(strUrl);
            }
        }

        // get and set enterprise test api server url
        public void setEnterpriseDownloadUrl(Context context, String enterpriseDownloadTestServerUrl) {
            ensurePreference(context);
            Log.d("scguo_enterprise", " in the sharedPerference : " + enterpriseDownloadTestServerUrl);
            dataEncrypt(ENTERPRISE_DOWNLOAD_SERVER_URL, enterpriseDownloadTestServerUrl);
        }
        public String getEnterpriseDownloadUrl() {
            return dataDecrypt(ENTERPRISE_DOWNLOAD_SERVER_URL);
        }

        // get and set enterprise test download server url
        public void setPersonalDownloadUrlTest(Context context, String personalDownloadTestUrl) {
            ensurePreference(context);
            dataEncrypt(PERSONAL_DOWNLOAD_SERVER_URL_DEBUG, personalDownloadTestUrl);
        }
        public String getPersonalDownloadUrlTest() {
            String strUrl = mPreferences.getString(PERSONAL_DOWNLOAD_SERVER_URL_DEBUG, personalDownloadUrl);
            Log.d("nativeApp_strUrl",strUrl);
            if(strUrl.equals(personalDownloadUrl)){
                return personalDownloadUrl;
            }else{
                return urlDecrypt(strUrl);
            }
        }

        // get enterprise release server url
        public String getEnterpriseReleaseServerUrl() {
            String strUrl = mPreferences.getString(ENTERPRISE_SERVER_URL_RELEASE, enterpriseUrl);
            if(strUrl.equals(enterpriseUrl)) {
                return enterpriseUrl;
            }else{
                return urlDecrypt(strUrl);
            }
        }

        // use to modify the enterprise url
        public void setEnterpriseServerUrlRelease(Context context, String enterpriseUrl)
        {
            ensurePreference(context);
            dataEncrypt(ENTERPRISE_SERVER_URL_RELEASE, enterpriseUrl);
        }


        // get personal release server url
        public String getPersonalReleaseServerUrl() {
            return apiUrl;
        }

        // get release push server url
        public String getPersonalReleasePushServerUrl() {
            return personalPushUrl;
        }

        // get release personal download server url
        public String getPersonalReleaseDownloadServerUrl() {
            return personalDownloadUrl;
        }

        private String urlDecrypt(String strUrl){
            try {
                if (strUrl != null && !"".equals(strUrl)) {
                    strUrl = AESEncryptor.decrypt(MAK, strUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strUrl;
        }

        public void setProxyUrl(Context context, String pushServerUrl) {
            ensurePreference(context);
            dataEncrypt("PROXY_URL", pushServerUrl);
        }

        public void setAppStoreUrl(Context context, String appStoreUrl) {
            ensurePreference(context);
            dataEncrypt("APP_STORE_URL", appStoreUrl);
        }

        public void setAppStoreApiUrl(Context context, String appstoreApiUrl) {
            ensurePreference(context);
            dataEncrypt("APPSTORE_API_URL", appstoreApiUrl);
        }

        public String getAppStoreApiUrl(Context context) {
            return dataDecrypt("APPSTORE_API_URL");
        }

        public Boolean isFirstEnter() {
            if (mPreferences != null) {
                return mPreferences.getBoolean("FIRST_ENTER", true);
            } else {
                return true;
            }
        }

        public void setFirstEnter(Context context, boolean isFirst) {
            ensurePreference(context);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean("FIRST_ENTER", isFirst);
            editor.commit();
        }

        public String getProxyAddress() {
            return dataDecrypt("PROXY_URL");
        }

        /**
         * As only the session Id would be the valid tag to detect whether the current
         * user has loggedIn into the WorkPhone, so we just use the session Id that the
         * user set at onLoginSuccess() method
         *
         * @param context
         * @return
         */
        public boolean getIfLoggedIn(Context context) {
            ensurePreference(context);
            String sid = mPreferences.getString(PREF_SESSIONID,"");
            if (mPreferences != null && (!sid.equals(""))) {
                return true;
            }
            return false;
        }

        /* clear preferences */
        public void clearPass() {
            if (mPreferences != null) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.remove(PREF_PASS);
                editor.remove("FIRST_ENTER");
                editor.commit();
            }
        }

        public void clearSessionId() {
            if (mPreferences != null) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.remove(PREF_SESSIONID);
                editor.commit();
            }
        }

        public void clearPhoneNumAccount() {
            if (mPreferences != null) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.remove("com.workphone.phone_account");
                editor.remove("com.workphone.phone_password");
                editor.commit();
            }
        }

        public void clearModifiedPassword() {
            if (mPreferences != null) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.remove(IS_MODIFY_USER_PASSWORD);
                editor.remove(PREF_NEED_MOD_PWD_FLAG);
                editor.commit();
            }
        }

        private static final int SORT_BY_NAME = 1;
        private static final int SORT_BY_LAUNCH_COUNT = 2;
        private Comparator<IconItemInfo> mCurrentComparator = null;

        private Comparator<IconItemInfo> getAppNameComparator() {
            final IconCache myIconCache = mLauncher.getIconCache();
            final Collator sCollator = Collator.getInstance();
            return new Comparator<IconItemInfo>() {
                @Override
                public final int compare(IconItemInfo a, IconItemInfo b) {
                    return sCollator.compare(a.getTitle(myIconCache),
                            b.getTitle(myIconCache));
                }
            };
        }

        private Comparator<IconItemInfo> getLaunchCountComparator() {
            final AppDB myAppDB = mLauncher.getAppDB();
            return new Comparator<IconItemInfo>() {
                @Override
                public int compare(IconItemInfo a, IconItemInfo b) {
                    int valA = Integer.MAX_VALUE;
                    int valB = Integer.MAX_VALUE;
                    if (a instanceof ShortcutInfo)
                        valA = myAppDB.getLaunchCounter((ShortcutInfo) a);
                    if (b instanceof ShortcutInfo)
                        valB = myAppDB.getLaunchCounter((ShortcutInfo) b);
                    return valB - valA;
                }
            };
        }

        public Comparator<IconItemInfo> getCurrentDrawerComparator() {
            if (mCurrentComparator == null) {
                int currentMode = Integer.parseInt(mPreferences.getString(
                        PREF_CURRENT_DRAWER_SORT_ORDER,
                        String.valueOf(SORT_BY_NAME)));
                switch (currentMode) {
                    case SORT_BY_NAME:
                        mCurrentComparator = getAppNameComparator();
                        break;
                    case SORT_BY_LAUNCH_COUNT:
                        mCurrentComparator = getLaunchCountComparator();
                        break;
                }
            }
            return mCurrentComparator;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                              String key) {
            if (key.equals(PREF_CURRENT_DRAWER_SORT_ORDER) && mLauncher != null) {
                mCurrentComparator = null;
                mLauncher.getAllAppsView().sort();
            }
        }

        // All downloading apps' id list
        private static List<Long> id_list = new ArrayList<Long>();

        private final Lock lock = new ReentrantLock();

        public void addDownloadAppsIdList(long id) {
            Log.d("Test_SystemPreferences_addDownloadAppsIdList()",""+id);
            lock.lock();
            if (!id_list.contains(id)) {
                id_list.add(id);
            }
            lock.unlock();
        }

        public List<Long> getDownloadAppsIdList() {
            List<Long> id_list_copy = new ArrayList<Long>();
            lock.lock();
            for(Long i : id_list){
                id_list_copy.add(i);
            }
            Log.d("Test_SystemPreferences_getDownloadAppsIdList()",""+id_list_copy.size());
            lock.unlock();
            return id_list_copy;
        }



}






    package com.singuloid.workphone.appstore;

    import android.content.Context;
    import android.content.pm.PackageManager;
    import android.text.TextUtils;
    import android.util.Log;

    import com.singuloid.workphone.network.InstalledAppInfoProvider;
    import com.singuloid.workphone.settings.SystemPreferences;

    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.Iterator;
    import java.util.LinkedHashSet;
    import java.util.List;
    import java.util.Set;

    public class AppListCache {
        private List<WosAppInfo> mLocalAllowedAppList = new ArrayList<WosAppInfo>();
        private List<WosAppInfo> mRemoteAllowedAppList = new ArrayList<WosAppInfo>();

        private List<WosAppInfo> mLocalHistoryAppList = new ArrayList<WosAppInfo>();
        private List<WosAppInfo> mRemoteHistoryAppList = new ArrayList<WosAppInfo>();

        private List<WosAppInfo> mLocalInstalledAppList = new ArrayList<WosAppInfo>();

        private List<WosAppInfo> mUpdatingAppList = new ArrayList<WosAppInfo>();

        private static final SystemPreferences sSystemPreference = SystemPreferences.getInstance();


        private static AppListCache instance = new AppListCache();

        private boolean find_update_app = false;

        private AppListCache() {

        }

        public static AppListCache getInstance() {
            return instance;
        }

        public boolean addRemoteAllowedApp(WosAppInfo wosAppInfo) {
            Log.d(TAG_SRC, " adding app : " + wosAppInfo.getAppName() + " into the " +
                    "mRemoteAllowedAppList ... and the mRemoteAllowedAppList are using to full the mLocalAllowedAppList");
            return wosAppInfo != null
                    && !mRemoteAllowedAppList.contains(wosAppInfo)
                    && mRemoteAllowedAppList.add(wosAppInfo);
        }

        public final int getRemoteAllowedAppListSize() {
            return mRemoteAllowedAppList.size();
        }

        public boolean addRemoteHistoryApp(WosAppInfo wosAppInfo) {
            return wosAppInfo != null &&
                    !mRemoteHistoryAppList.contains(wosAppInfo)
                    && mRemoteHistoryAppList.add(wosAppInfo);
        }

        public final int getRemoteHistoryAppListSize() {
            return mRemoteHistoryAppList.size();
        }

        // copy the list from server to local cache
        public void synchronize() {
            synchronizeAllowedAppList();
            synchronizeHistoryAppList();
        }

        public void synchronizeAllowedAppList() {
            Log.d(TAG_SRC, "start sync the appStore data source ... ");
//        mLocalAllowedAppList.clear();
//        mLocalAllowedAppList.addAll(mRemoteAllowedAppList);
            List<WosAppInfo> excludingList = new ArrayList<WosAppInfo>();
            Set<Integer> updatedIndexSet = new HashSet<Integer>();

//        int notificationId = 1;
            // traverse local list at first

            final int localLen = mLocalAllowedAppList.size();
            Log.d(TAG_SRC, " the local allowed app list size are : " + localLen);
            for (int i = 0; i < localLen; i++) {
                WosAppInfo localAppInfo = mLocalAllowedAppList.get(i);
                if (mRemoteAllowedAppList.contains(localAppInfo)) {
                    // update local info
                    int index = mRemoteAllowedAppList.indexOf(localAppInfo);
                    WosAppInfo remoteAppInfo = mRemoteAllowedAppList.get(index);
                    updatedIndexSet.add(index);
                    // check version
                    if (localAppInfo.isNewerVersion(remoteAppInfo)) {
                        Log.d(TAG_SRC, " the following app are the apps that need to update " + localAppInfo.getAppName());
                        localAppInfo.setAppName(remoteAppInfo.getAppName());
                        localAppInfo.setPackageName(remoteAppInfo.getPackageName());
                        localAppInfo.setBasic(remoteAppInfo.isBasic());
                        localAppInfo.setAppType(remoteAppInfo.getAppType());
                        localAppInfo.setIcon(remoteAppInfo.getIcon());
                        localAppInfo.setUrl(remoteAppInfo.getUrl());
                        localAppInfo.setVersionCode(remoteAppInfo.getVersionCode());
                        localAppInfo.setVersionName(remoteAppInfo.getVersionName());

                        localAppInfo.setUpdatingStatus(true);
                    }
                } else {
                    // delete this one
                    excludingList.add(localAppInfo);
                }
            }

            // delete those info not in remote list
            for (WosAppInfo excludingAppInfo : excludingList) {
                mLocalAllowedAppList.remove(excludingAppInfo);
            }

            // add those info in remote list but not in local list
            final int remoteLen = mRemoteAllowedAppList.size();
            for (int i = 0; i < remoteLen; i++) {
                if (!updatedIndexSet.contains(i)) {
                    Log.d(TAG_SRC, "truly adding app list into the mLocalAllowedAppList ... ");
                    WosAppInfo remoteAppInfo = mRemoteAllowedAppList.get(i);
                    remoteAppInfo.setUpdatingStatus(true);
                    mLocalAllowedAppList.add(remoteAppInfo);
                }
            }
        }

        public void synchronizeHistoryAppList() {
            mLocalHistoryAppList.clear();
            mLocalHistoryAppList.addAll(mRemoteHistoryAppList);
        }

        public boolean removeLocalHistoryApp(String packageName) {
            for (WosAppInfo appInfo : mLocalHistoryAppList) {
                if (appInfo.getPackageName().equals(packageName)) {
                    return mLocalHistoryAppList.remove(appInfo);
                }
            }

            return false;
        }

        public boolean addLocalHistoryApp(String packageName) {
            for (WosAppInfo appInfo : mLocalAllowedAppList) {
                if (appInfo.getPackageName().equals(packageName)) {
                    // change updating status, so that it can send notification when next installation
                    appInfo.setUpdatingStatus(true);

                    // remove the app package name in updating app list, if necessary
                    removeUpdatingApp(packageName);
                    return !mLocalHistoryAppList.contains(appInfo) && mLocalHistoryAppList.add(appInfo);
                }
            }
            return false;
        }

        public boolean isAllowed(WosAppInfo wosAppInfo) {
            // TODO: uncomment me later
//        return mLocalAllowedAppList.contains(wosAppInfo);
            return true;
        }

        public boolean isAllowedForPackageName(String packageName) {
            // TODO: fix me later
            return isAllowedForPackageName(packageName, true);
        }

        public boolean isAllowedForPackageName(String packageName, boolean debugAble) {
            if (packageName.equals("com.singuloid.workphone")
                    || packageName.equals("com.singuloid.workphoneent")) {
                return true;
            } else {
                for (WosAppInfo appInfo : mLocalAllowedAppList) {
                    if (appInfo.getPackageName().equals(packageName)) {
                        return true;
                    }
                }
            }

            return debugAble;
        }

        public boolean isAllowedForAppName(String appName) {
            // TODO: fix me later

            //if set second arg true, other app's shortcut will appear on WorkPhone
            return isAllowedForAppName(appName, /*true*/ false);
        }

        public boolean isAllowedForAppName(String appName, boolean debugAble) {
            for (WosAppInfo appInfo : mLocalAllowedAppList) {
                if (appInfo.getAppName().equals(appName)) {
                    return true;
                }
            }

            return debugAble;
        }

        public int numOfAllowedApps() {
            return mLocalAllowedAppList.size();
        }

        public int numOfHistoryApps() {
            return mLocalHistoryAppList.size();
        }

        public List<WosAppInfo> getAllowedAppList() {
            return mLocalAllowedAppList;
        }

        public List<WosAppInfo> getHistoryAppList() {
            return mLocalHistoryAppList;
        }

        public List<WosAppInfo> getProxiedAppList(Context context) {
            List<WosAppInfo> proxiedAppList = new ArrayList<WosAppInfo>();
            Set<String> savedProxiedAppSet = sSystemPreference.getProxiedAppSet(context);
            if (null != savedProxiedAppSet) {
                Log.d("scguo_adding_app_1", " -------------> the proxied app set size we get are : " + savedProxiedAppSet.size());

                for (String appConfigInfo : savedProxiedAppSet) {
                    Log.d(TAG, " ----------------------GETTING INFORMATION FROM SYSTEMPREFERENCE----------------------------");
                    String delims = "[;]";
                    String[] tokens = appConfigInfo.split(delims);

                    // one token represents one complete WosAppInfo
                    String appId = tokens[0];
                    String serverIP = tokens[1];
                    String serverPort = tokens[2];
                    String psw = tokens[3];
                    String proxyPort = tokens[4];
                    String encryptionMethod = tokens[5];
                    String timeoutInSecs = tokens[6];
                    Log.d(TAG, " the token we get are : " + appId + ", " + serverIP + ", " + serverPort +
                            ", " + ", " + proxyPort + ", " + encryptionMethod + ", " + timeoutInSecs);
                    WosAppInfo appInfo = getWosAppInfo(appId);
                    if (appInfo != null) {
                        appInfo.setProxyAddress(serverIP);
                        appInfo.setProxyPort(serverPort);
                        appInfo.setPsw(psw);
                        appInfo.setLocalPort(proxyPort);
                        appInfo.setEncryptionMethod(encryptionMethod);
                        appInfo.setTimeoutInSecs(timeoutInSecs);
                        proxiedAppList.add(appInfo);
                        Log.d(TAG, " -------> we have parsed out one wosAppInfo into the proxied app list ");
                    }
                }
            }
            Log.d("scguo_adding_app_1", " the finally proxied list we parsed from the SystemPreference are : " + proxiedAppList.size());
            return proxiedAppList;
        }

        /**
         * Add wosAppInfo into the ProxiedAppList based on the package name that
         * transferred in
         * @param appPkgName we need to get the relative WosAppInfo based on this single
         *                   appPkgName
         *                   private String serverIp;
         * @param serverIP
         * @param serverPort
         * @param password
         * @param proxyPort
         * @param encryptionMethod
         * @param timeoutInSecs
         *
         */
        public synchronized void addProxiedApp(Context context, String appPkgName, String serverIP,
                                               String serverPort,
                                               String password,
                                               String proxyPort,
                                               String encryptionMethod,
                                               String timeoutInSecs) {

            Set<String> savedAppSet = sSystemPreference.getProxiedAppSet(context);
            Log.d("scguo_adding_app", " THE SET WE GET FROM SYSTEMPREFERENCE ARE ---> : " + savedAppSet.size());
            if (! TextUtils.isEmpty(appPkgName)) {
                StringBuilder proxyStr = new StringBuilder();
                proxyStr.append(appPkgName).append(";").
                        append(serverIP).append(";").
                        append(serverPort).append(";").
                        append(password).append(";").
                        append(proxyPort).append(";").
                        append(encryptionMethod).append(";").
                        append(timeoutInSecs).append(";");
                String proxyConfig = proxyStr.toString();
                Log.d(TAG, " the proxied config string we add are : " + proxyConfig + ", ---->");
                if (savedAppSet != null) {
                    Log.d("scguo_adding_app", "adding apps into the systemPreference ...  ");
                    savedAppSet.add(proxyConfig);
                }
            }
            Log.d("scguo_adding_app", "the finally app set we stored into the SystemPreference are : " + savedAppSet.size());

            sSystemPreference.storeProxiedAppIntoSet(context, savedAppSet);
        }

        public void removeProxiedApp(Context context, WosAppInfo appInfo) {
            Set<String> savedAppsSet = sSystemPreference.getProxiedAppSet(context);
            Log.d(TAG_PROXY, " ----> before we delete elements from it, the size are : " + savedAppsSet.size());
            if (null != appInfo) {
                StringBuilder specifiedAppInfo = new StringBuilder();
                specifiedAppInfo.append(appInfo.getPackageName()).append(";").
                        append(appInfo.getProxyAddress()).append(";").
                        append(appInfo.getProxyPort()).append(";").
                        append(appInfo.getPsw()).append(";").
                        append(appInfo.getLocalPort()).append(";").
                        append(appInfo.getEncryptionMethod()).append(";").
                        append(appInfo.getTimeoutInSecs()).append(";");
                String configedInfo = specifiedAppInfo.toString();
                Iterator<String> appSetIter = savedAppsSet.iterator();
                for ( ; appSetIter.hasNext() ; ) {
                    if (appSetIter.next().equals(configedInfo)) {
                        appSetIter.remove();
                    }
                }
            }
            sSystemPreference.storeProxiedAppIntoSet(context, savedAppsSet);
            Log.d(TAG_PROXY, " ----> after we delete elements from it, the size are : " + sSystemPreference.getProxiedAppSet(context).size());
        }

        /**
         *
         * @param context
         * @param packageName
         * @return
         */
        public boolean isAppNeedProxied(Context context, String packageName) {
            boolean contained = false;
            List<WosAppInfo> proxiedAppList = getProxiedAppList(context);
            final int size = proxiedAppList.size();
            Log.d(TAG_PROXY, " the app list that need to proxied size are : " + size);
            for (int i = 0; i < size; i++) {
                Log.d(TAG_PROXY, " the app that inside the proxy list are : " + proxiedAppList.get(i).getPackageName());
                if (proxiedAppList.get(i).getPackageName().equals(packageName)) {
                    contained = true;
                }
            }
            return contained;
        }


        /**
         * every time we installed new app on WorkPhone, this method will be invoked to
         * update the local app database.
         * As launcher will create the shortcut based on the AppDB, so this method will
         * have influence on the shortcut creation.
         *
         * @param context
         */
        public void updateInstalledAppList(final Context context) {
            refreshInstalledAppList(context);

            String sid = SystemPreferences.getInstance().getSessionId(context);
            // Upload app info list to server
            WosAppInfoCommit appInfo = new WosAppInfoCommit(context);
            appInfo.commitAppInfo(sid);
        }


        public void refreshInstalledAppList(final Context context) {
            // get installed app list in device
            InstalledAppInfoProvider.getInstance().update();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    updateLocalInstalledAppList(context);
                }
            }).start();
        }

        public synchronized boolean updateLocalInstalledAppList(final Context context) {

            List<WosAppInfo> allApps = InstalledAppInfoProvider.getInstance().getAllApps(context);
            if (allApps != null) {
                mLocalInstalledAppList.clear();
                mLocalInstalledAppList.addAll(allApps);
            }
            return true;
        }

        // Every time a new app is installed or uninstalled, this method is called to refresh updating list
        public void checkAppVersion(Context context) {
            mUpdatingAppList.clear();

            // compare version and send notification if necessary
            int notificationId = 1;
            for (WosAppInfo appInfoFromServer : mLocalAllowedAppList) {
                if (mLocalInstalledAppList.contains(appInfoFromServer)) {
                    final int index = mLocalInstalledAppList.indexOf(appInfoFromServer);
                    WosAppInfo installedAppInfo = mLocalInstalledAppList.get(index);
                    if (installedAppInfo.isNewerVersion(appInfoFromServer)) {
                        if (appInfoFromServer.isUpdating()) {
                            // we do not need this notification anymore, as this do not fit our needs anymore.
                            // scguo --> 2014/08/31 remove the showUpdateAppNoficiation() method
                            // TODO: scguo if we need the showUpdateAppNotification in the future, we just uncomment it will work
//                        WorkPhoneNotification.showUpdateAppNotification(context, appInfoFromServer.getAppName(), notificationId++);
                            appInfoFromServer.setUpdatingStatus(false);
                        }

                        WosAppInfo updatingAppInfo = new WosAppInfo();
                        updatingAppInfo.setPackageName(installedAppInfo.getPackageName());
                        updatingAppInfo.setAppName(installedAppInfo.getAppName());
                        updatingAppInfo.setAppType(installedAppInfo.getAppType());
                        updatingAppInfo.setVersionCode(installedAppInfo.getVersionCode());
                        updatingAppInfo.setVersionName(installedAppInfo.getVersionName());
                        updatingAppInfo.setIcon(installedAppInfo.getIcon());
                        updatingAppInfo.setCodeSize(installedAppInfo.getCodeSize());
                        updatingAppInfo.setDataSize(installedAppInfo.getDataSize());
                        updatingAppInfo.setCacheSize(installedAppInfo.getCacheSize());

                        updatingAppInfo.setBasic(appInfoFromServer.isBasic());
                        updatingAppInfo.setUrl(appInfoFromServer.getUrl());
                        updatingAppInfo.setUpdatingStatus(appInfoFromServer.isUpdating());

                        addUpdatingApp(updatingAppInfo);
                    }
                }
            }
        }

        public List<WosAppInfo> getInstalledNativeAppList() {
            return mLocalInstalledAppList;
        }

        // TODO: remove the following statement later, it is just for testing now
        private static final String TAG = "AppListCache";
        private static final String TAG_SRC = "AppStoreDataSrcSync";
        private static final String TAG_PROXY = "WorkphoneProxyConfig";

        // get the detailed wosAppInfo based on the app package name
        // we just iterate over the local installed app list
        public WosAppInfo getWosAppInfo(final String appId) {
            if (appId == null || appId.equals(""))
                return null;

            Log.d(TAG, " the app id we need to transfer are : " + appId);
            for (WosAppInfo appInfo : getInstalledNativeAppList()) {
                if (appInfo.getPackageName().equals(appId)) {
                    return appInfo;
                }
            }
            return null;
        }


        public void clearRemoteHistoryAppList() {
            mRemoteHistoryAppList.clear();
        }

        public void clearRemoteAllowedAppList() {
            mRemoteAllowedAppList.clear();
        }

        public void clearAll() {
            mRemoteAllowedAppList.clear();
            mRemoteHistoryAppList.clear();
            mLocalAllowedAppList.clear();
            mLocalHistoryAppList.clear();
            mUpdatingAppList.clear();
        }

        public List<WosAppInfo> getUpdatingAppList() {
            Log.d(TAG, "" + mUpdatingAppList.size());
            return mUpdatingAppList;
        }

        private void addUpdatingApp(WosAppInfo app) {
            if (!mUpdatingAppList.contains(app)) {
                mUpdatingAppList.add(app);
            }
        }

        private boolean removeUpdatingApp(String packageName) {
            for (WosAppInfo appInfo : mUpdatingAppList) {
                if (appInfo.getPackageName().equals(packageName)) {
                    return mUpdatingAppList.remove(appInfo);
                }
            }

            return false;
        }

        public boolean judge_update_flag() {
            return find_update_app;
        }

        public void set_judge_update_flag(boolean flag) {
            find_update_app = flag;
        }


        private static final String[] ALL_BASIC_APP_NAME_ARR = {
                "com.singuloid.mms", // mms
                "com.singuloid.calendar", // calendar
                "com.singuloid.workphone.apps.fileexplorer", // fileExplorer
                "com.singuloid.browser", // browser
                "com.singuloid.workphone.apps.email", // E-mail
                "com.singuloid.workphone.apps.contacts", // Constacts
                "com.singuloid.gallery3d" // Gallery and camera

        };

        public boolean isAllBasicAppsInstalled(Context context) {
            PackageManager packageManager = context.getPackageManager();
            boolean allInstalled = false;
            final int len = ALL_BASIC_APP_NAME_ARR.length;
            int i = 0;
            for (; i <= len - 1; i++) {
                try {
                    packageManager.getPackageInfo(ALL_BASIC_APP_NAME_ARR[i], PackageManager.GET_ACTIVITIES);
                    allInstalled = true;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    allInstalled = false;
                }
            }
            return allInstalled;
        }

        public boolean isAppInstalled(Context context, final String appPkgName) {
            boolean pkgInstalled = false;
            if (!TextUtils.isEmpty(appPkgName)) {
                PackageManager packageManager = context.getPackageManager();
                try {
                    packageManager.getPackageInfo(appPkgName, PackageManager.GET_ACTIVITIES);
                    pkgInstalled = true;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    pkgInstalled = false;
                }
            }
            return pkgInstalled;
        }
    }
    package com.singuloid.workphone.activity;

    import android.app.ActionBar;
    import android.app.Activity;
    import android.app.AlertDialog;
    import android.content.Context;
    import android.graphics.Bitmap;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.util.Log;
    import android.view.ActionMode;
    import android.view.ContextMenu;
    import android.view.Gravity;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.Window;
    import android.view.WindowManager;
    import android.widget.AdapterView;
    import android.widget.BaseAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.ListView;
    import android.widget.RelativeLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.singuloid.workphone.R;
    import com.singuloid.workphone.appstore.AppListCache;
    import com.singuloid.workphone.appstore.AppStoreServerRetCodeCheck;
    import com.singuloid.workphone.appstore.NativeAppStoreApi;
    import com.singuloid.workphone.appstore.WosAppInfo;
    import com.singuloid.workphone.settings.SystemPreferences;
    import com.singuloid.workphone.util.Utils;

    import java.util.ArrayList;
    import java.util.List;

    /**
     * Created by scguo on 15/8/17.
     * <p/>
     * The activity of which using to config and manage the Socks5 proxy connection
     */
    public class AppSocksManagementActivity extends Activity {
        private static final String TAG = "AppSocksManagementActivity";

        private static final SystemPreferences sSystemPreference = SystemPreferences.getInstance();
        private int mScreenWidth, mScreenHeight;

        private ActionBar mActionBar;

        private ArrayList<WosAppInfo> mProxiedAppList;
        private ListView mProxiedAppListView;
        private ProxiedAppsAdapter mAppAdapter;
        private ActionMode.Callback mCallback;
        private ActionMode mMode;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.app_manage_socks_layout);

            mScreenWidth = sSystemPreference.getScreenWidth(this);
            mScreenHeight = sSystemPreference.getScreenHeight(this);

            mActionBar = this.getActionBar();
            mActionBar.setTitle(R.string.add_apps_socks_title);
            mActionBar.setIcon(R.drawable.desktop_setting);

            mProxiedAppListView = (ListView) findViewById(R.id.listview_proxied_apps);

            mProxiedAppList = (ArrayList<WosAppInfo>) AppListCache.getInstance().getProxiedAppList(AppSocksManagementActivity.this);
            Log.d(TAG, " inside the AppSocksManagementActivity, and the proxiedAppList size we get are : " + mProxiedAppList.size());
            mAppAdapter = new ProxiedAppsAdapter(this, mProxiedAppList);
            mProxiedAppListView.setAdapter(mAppAdapter);
            mProxiedAppListView.setLongClickable(true);
            registerForContextMenu(mProxiedAppListView);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Log.d(TAG, " create the context menu ");
            if (v.getId() == R.id.listview_proxied_apps) {
                Log.d(TAG, " the list view has been clicked on ... ");
                menu.setHeaderTitle(getString(R.string.manage_socks_app));
                menu.add(getString(R.string.menu_delete));
            }
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            Log.d(TAG, " we have selected the item at " + item.getTitle());
            // delete this item from the proxied list
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            mAppAdapter.removeItem(info.position);
            Log.d(TAG, " we need to remove item at : " + info.position);

            return true;
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.app_socks_manage_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            final int id = item.getItemId();
            switch (id) {
                default:
                case R.id.adding_apps_config_socks:
                    Log.d(TAG, " adding apps to config the socks proxy for itself ");
                    // start the Dialog of which indicate that the user should adding
                    // apps that using the socks proxy
                    openAddingDialog();
                    break;
            }

            return super.onOptionsItemSelected(item);
        }

        private void openAddingDialog() {
            final View view = LayoutInflater.from(this).inflate(R.layout.add_app_socks_layout, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            Button btnNext = (Button) view.findViewById(R.id.btn_socks_config_next);
            final EditText etAppName = (EditText) view.findViewById(R.id.et_app_name);
            final EditText etAppPkgName = (EditText) view.findViewById(R.id.et_app_pkgname);
            btnNext.setEnabled(true);
            alertDialog.show();

            Window window = alertDialog.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setContentView(view);

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String appName = etAppName.getText().toString();
                    String appPkgName = etAppPkgName.getText().toString();
                    Log.d(TAG, " the app name : " + appName + ", and the package name : " + appPkgName);
                    // judge whether this application has been install on workPhone
                    if (TextUtils.isEmpty(appName) || TextUtils.isEmpty(appPkgName)) {
                        showMsg(getString(R.string.app_field_null));
                        return;
                    }

                    if (!AppListCache.getInstance().isAppInstalled(AppSocksManagementActivity.this, appPkgName)) {
                        etAppName.requestFocus();
                        etAppPkgName.requestFocus();
                        showMsg(getString(R.string.app_not_installed));
                        return;
                    }

                    // start the socks5 config dialog directly and with this dialog closed
                    openDetailedConfigDialog(appPkgName);
                    alertDialog.dismiss();
                }
            });
        }

        // the dialog of which are just using for demonstration
        private void openConfigInfoDialog(final WosAppInfo configuredApp) {
            View view = LayoutInflater.from(this).inflate(R.layout.config_info_dialog, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            final TextView tvTitle = (TextView) view.findViewById(R.id.tv_info_title);
            final TextView tvServerIpField = (TextView) view.findViewById(R.id.tv_server_ip_info);
            final TextView tvServerPortField = (TextView) view.findViewById(R.id.tv_server_port_info);
            final TextView tvPswField = (TextView) view.findViewById(R.id.tv_socks_password_info);
            final TextView tvSocksProxyPortField = (TextView) view.findViewById(R.id.tv_socks_proxy_port_info);
            final TextView tvEncryptionMethodField = (TextView) view.findViewById(R.id.tv_encryption_method_info);
            final TextView tvTimeoutInSecsField = (TextView) view.findViewById(R.id.tv_timeout_info);
            Button btnSure = (Button) view.findViewById(R.id.bt_config_save);
            btnSure.setEnabled(true);
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(view);

            tvTitle.setText(configuredApp.getAppName());
            // init the relative field in this dialog
            tvServerIpField.setText(configuredApp.getProxyAddress());
            tvServerPortField.setText(configuredApp.getProxyPort());
            tvPswField.setText("******");// this is the password, and it is should not be visible to the user directly
            tvSocksProxyPortField.setText(configuredApp.getLocalPort());
            tvEncryptionMethodField.setText(configuredApp.getEncryptionMethod());
            tvTimeoutInSecsField.setText(configuredApp.getTimeoutInSecs());

            btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }

        private void openDetailedConfigDialog(final String appPkgName) {
            // init the textWatcher for the EditText in current AlertDialog
            View view = LayoutInflater.from(this).inflate(R.layout.config_socks_dialog, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            final EditText etServerIP = (EditText) view.findViewById(R.id.et_enter_server_ip);
            final EditText etServerPort = (EditText) view.findViewById(R.id.et_enter_server_port);
            final EditText etPsw = (EditText) view.findViewById(R.id.et_socks_password);
            final EditText etSocksProxyPort = (EditText) view.findViewById(R.id.et_socks_proxy_port);
            final EditText etEncryptionMethod = (EditText) view.findViewById(R.id.et_encryption_method);
            final EditText etTimeoutSeconds = (EditText) view.findViewById(R.id.et_timeout);
            Button btnSure = (Button) view.findViewById(R.id.bt_config_save);

            // disable the button sure initially
            btnSure.setEnabled(true);
            alertDialog.show();

            Window window = alertDialog.getWindow();
            window.setContentView(view);
            // we need to clear the focus flag for this dialog, only with these flags set, the
            // soft keyboard will pop up
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String serverIP = etServerIP.getText().toString();
                    final String serverPort = etServerPort.getText().toString();
                    final String psw = etPsw.getText().toString();
                    final String socks5ProxyPort = etSocksProxyPort.getText().toString();
                    final String encryptionMethod = etEncryptionMethod.getText().toString();
                    final String timeoutInSecs = etTimeoutSeconds.getText().toString();

                    if (TextUtils.isEmpty(serverIP) || TextUtils.isEmpty(serverPort)
                            || TextUtils.isEmpty(psw) || TextUtils.isEmpty(socks5ProxyPort)
                            || TextUtils.isEmpty(encryptionMethod) || TextUtils.isEmpty(timeoutInSecs)) {
                        showMsg(getString(R.string.app_config_null_warn));
                        return;
                    }

                    Log.d(TAG, " the detailed configuration information are : serverIP " +
                            serverIP + ", and the timeout seconds we get are : " + timeoutInSecs);

                    // store this app into the proxiedAppList just based on the app package name
                    AppListCache.getInstance().addProxiedApp(AppSocksManagementActivity.this, appPkgName,
                            serverIP,
                            serverPort,
                            psw,
                            socks5ProxyPort,
                            encryptionMethod,
                            timeoutInSecs);
                    // we need to stop that process based on the package name
                    Utils.stopSpecifiedProcess(AppSocksManagementActivity.this, appPkgName);
                    showMsg(getString(R.string.config_warning));
                    Log.d(TAG, " after adding the list, and the app list size we get are : " + AppListCache.getInstance().getProxiedAppList(AppSocksManagementActivity.this).size());
                    mAppAdapter.setDataList((ArrayList<WosAppInfo>) AppListCache.getInstance().getProxiedAppList(AppSocksManagementActivity.this));
                    mAppAdapter.notifyDataSetChanged();

                    // and finally dismiss the current dialog
                    alertDialog.dismiss();
                }
            });

        }

        private void showMsg(String showMeg) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast_layout,
                    (ViewGroup) findViewById(R.id.toast_layout_root));
            Button button = (Button) layout.findViewById(R.id.bt_for_toast);
            button.setText(showMeg);
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.setView(layout);
            toast.show();
        }

        /**
         * For the basic item in the ProxiedAppsAdapter, we are using the item of WosAppInfo,
         * as every item that could be installed on WorkPhone are all an instance of the WosAppInfo,
         * so we just using the WosAppInfo will be enough.
         * The only thing we need to add into the WosAppInfo are the proxy server host IP address for
         * the current app.
         */
        public class ProxiedAppsAdapter extends BaseAdapter {
            private static final String TAG = "ProxiedAppsAdapter";

            ArrayList<WosAppInfo> dataList = new ArrayList<WosAppInfo>();
            private Context context;

            public ProxiedAppsAdapter(Context context, ArrayList<WosAppInfo> appList) {
                this.context = context;
                this.dataList = appList;
            }

            public void setDataList(ArrayList<WosAppInfo> appList) {
                this.dataList = appList;
            }

            @Override
            public int getCount() {
                return dataList.size();
            }

            @Override
            public Object getItem(int position) {
                return dataList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final AppViewHolder holder;
                if (convertView == null) {
                    holder = new AppViewHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.app_proxied_item_layout, null);
                    final View layout = convertView;
                    holder.initViews(layout);
                    layout.setTag(holder);
                } else {
                    holder = (AppViewHolder) convertView.getTag();
                }

                final WosAppInfo wosAppInfo = dataList.get(position);
                Bitmap bitmap = wosAppInfo.getIcon();
                if (bitmap != null) {
                    holder.appIcon.setImageBitmap(bitmap);
                }

                holder.appTitle.setText(wosAppInfo.getAppName());
                holder.appIP.setText(wosAppInfo.getProxyAddress());

                holder.appItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, " on item clicked ...");
                        openConfigInfoDialog(wosAppInfo);
                    }
                });
                holder.appItem.setLongClickable(true);

                return convertView;
            }

            public void removeItem(int pos) {
                if (pos < getCount()) {
                    // we need also remove this item from the SystemPreference
                    WosAppInfo appInfo = dataList.get(pos);
                    AppListCache.getInstance().removeProxiedApp(context, appInfo);
                    this.dataList.remove(pos);
                    notifyDataSetChanged();
                }
            }

            public class AppViewHolder {
                public ImageView appIcon;
                public TextView appTitle;
                public TextView appIP;
                public RelativeLayout appItem;

                public void initViews(View layout) {
                    appIcon = (ImageView) layout.findViewById(R.id.iv_logo);
                    appTitle = (TextView) layout.findViewById(R.id.tv_title);
                    appIP = (TextView) layout.findViewById(R.id.tv_ip_address);
                    appItem = (RelativeLayout) layout.findViewById(R.id.rl_item);
                }
            }
        }
    }

