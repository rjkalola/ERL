package com.app.erl;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import com.app.erl.injection.component.DaggerNetworkComponent;
import com.app.erl.injection.component.DaggerServiceComponent;
import com.app.erl.injection.component.NetworkComponent;
import com.app.erl.injection.component.ServiceComponent;
import com.app.erl.injection.module.AppModule;
import com.app.erl.injection.module.NetworkModule;
import com.app.erl.injection.module.UserAuthenticationModule;
import com.app.erl.util.AppConstant;
import com.app.erl.util.LocaleManager;
import com.app.erl.util.VariantConfig;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OwlManagementApp extends Application {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static NetworkComponent networkComponent;
    public static ServiceComponent serviceComponent;
    private static SharedPreferences.Editor sharedPreferencesEditor;
    private static SharedPreferences sharedPreferences;
    private static Context context;
    private static OwlManagementApp instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setNewLocale(base, "en"));
        CalligraphyContextWrapper.wrap(base);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        Log.e("test","Light Mode");
        LocaleManager.setLocale(this);
//        switch (OwlManagementApp.preferenceGetInteger(AppConstant.SharedPrefKey.THEME_MODE, 0)) {
//            case 0:
//                Log.e("test","Light Mode");
//                AppCompatDelegate.setDefaultNightMode(
//                        AppCompatDelegate.MODE_NIGHT_NO);
//                break;
//            case 1:
//                Log.e("test","Dark Mode");
//                AppCompatDelegate.setDefaultNightMode(
//                        AppCompatDelegate.MODE_NIGHT_YES);
//                break;
//        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        setupFont();

        networkComponent = DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule(VariantConfig.getServerBaseUrl()))
                .appModule(new AppModule(this))
                .build();

        serviceComponent = DaggerServiceComponent.builder()
                .networkComponent(networkComponent)
                .userAuthenticationModule(new UserAuthenticationModule())
                .build();

        sharedPreferencesEditor = networkComponent.provideSharedPreference().edit();
        sharedPreferences = networkComponent.provideSharedPreference();
        OwlManagementApp.instance = this;

//        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//            @Override
//            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                Log.e("test", "onActivityCreated");
//
////                if (OwlManagementApp.preferenceGetInteger(AppConstant.SharedPrefKey.THEME_MODE, 0) == 0)
////                    setTheme(R.style.LightTheme);
////                else
////                    setTheme(R.style.DarkTheme);
//
////                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
////                    setTheme(R.style.DarkTheme);
////                } else {
////                    setTheme(R.style.LightTheme);
////                }
//            }
//
//            @Override
//            public void onActivityStarted(Activity activity) {
//                Log.e("test", "onActivityStarted");
//            }
//
//            @Override
//            public void onActivityResumed(Activity activity) {
//                Log.e("test", "onActivityResumed");
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity) {
//                Log.e("test", "onActivityPaused");
//            }
//
//            @Override
//            public void onActivityStopped(Activity activity) {
//                Log.e("test", "onActivityStopped");
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//                Log.e("test", "onActivitySaveInstanceState");
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity) {
//                Log.e("test", "onActivityDestroyed");
//            }
//        });

        switch (OwlManagementApp.preferenceGetInteger(AppConstant.SharedPrefKey.THEME_MODE, 0)) {
            case 0:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }

//        if (!AppUtils.isMyServiceRunning(context, LocationService.class)) {
//            Intent intent = new Intent(this, LocationService.class);
//            startService(intent);
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                Log.e("test", "startForegroundService");
//                startForegroundService(intent);
//            } else {
//                Log.e("test", "startService");
//                startService(intent);
//            }
//        } else {
//            Log.e("test", "already start service");
//        }
    }

    public static OwlManagementApp get() {
        return instance;
    }

    public NetworkComponent getNetworkComponent() {
        return networkComponent;
    }

    public void setNetworkComponent(NetworkComponent networkComponent) {
        this.networkComponent = networkComponent;
    }

    public static ServiceComponent getServiceComponent() {
        return serviceComponent;
    }

    public void setServiceComponent(ServiceComponent serviceComponent) {
        this.serviceComponent = serviceComponent;
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor() {
        return sharedPreferencesEditor;
    }

    public static void setSharedPreferencesEditor(SharedPreferences.Editor sharedPreferencesEditor) {
        OwlManagementApp.sharedPreferencesEditor = sharedPreferencesEditor;
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void setSharedPreferences(SharedPreferences sharedPreferences) {
        OwlManagementApp.sharedPreferences = sharedPreferences;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        OwlManagementApp.context = context;
    }

    /**
     * Application level preference work.
     */
    public static void preferencePutInteger(String key, int value) {
        sharedPreferencesEditor.putInt(key, value).apply();
    }

    public static int preferenceGetInteger(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void preferencePutBoolean(String key, boolean value) {
        sharedPreferencesEditor.putBoolean(key, value).apply();
    }

    public static boolean preferenceGetBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void preferencePutString(String key, String value) {
        sharedPreferencesEditor.putString(key, value).apply();
    }

    public static String preferenceGetString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void preferencePutLong(String key, long value) {
        sharedPreferencesEditor.putLong(key, value).apply();
    }

    public static long preferenceGetLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static void preferenceRemoveKey(String key) {
        sharedPreferencesEditor.remove(key).apply();
    }

    public void preferencePutFloat(String key, float value) {
        sharedPreferencesEditor.putFloat(key, value).apply();
    }

    public static float preferenceGetFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static void clearPreference() {
        sharedPreferencesEditor.clear().apply();
    }


    private void setupFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_family_regular))
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public void clearData() {
        OwlManagementApp.preferenceRemoveKey(AppConstant.SharedPrefKey.USER_INFO);
        OwlManagementApp.preferenceRemoveKey(AppConstant.SharedPrefKey.PERMISSION_SETTINGS);
        OwlManagementApp.preferenceRemoveKey(AppConstant.SharedPrefKey.NOTIFICATION_SETTINGS);
    }

}
