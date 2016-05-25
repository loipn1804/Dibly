package com.dibs.dibly.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.dibs.dibly.R;
import com.dibs.dibly.daocontroller.PermanentController;
import com.dibs.dibly.service.ParallaxService;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.splunk.mint.Mint;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.PermanentDeals;


/**
 * Created by USER on 4/22/2015.
 */
public class MyApplication extends MultiDexApplication {

    public DaoSession daoSession;
    public static Activity CurrentActivity;
    Permission[] permissions = new Permission[]{
            Permission.EMAIL,
            Permission.PUBLISH_ACTION
    };

    @Override
    public void onCreate() {
        MultiDex.install(getApplicationContext());
        super.onCreate();

        Mint.initAndStartSession(getApplicationContext(), "26997fce");

        int current_version = getAppVersion();

        final SharedPreferences preferences = getSharedPreferences("versioncode", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        final int previous_version = preferences.getInt("version", current_version);
        if (previous_version == current_version) {
//            Toast.makeText(AntidoteApplication.this, "old_version", Toast.LENGTH_LONG).show();
            editor.putInt("version", current_version);

            String current_ver_name = getAppVersionName();

            String previous_ver_name = preferences.getString("ver_name", current_ver_name);

            if (previous_ver_name.equals(current_ver_name)) {
                editor.putString("ver_name", current_ver_name);
            } else {
                deleteDatabase();
                editor.putString("ver_name", current_ver_name);
            }
        } else {
//            Toast.makeText(AntidoteApplication.this, "deleteDatabase", Toast.LENGTH_LONG).show();
            deleteDatabase();
            editor.putInt("version", current_version);

            SharedPreferences preferencesFirstRun = getSharedPreferences("first_run", MODE_PRIVATE);
            SharedPreferences.Editor editorFirstRun = preferencesFirstRun.edit();
            editorFirstRun.putBoolean("is_first_start_mix_panel", true);
            editorFirstRun.commit();
        }
        editor.commit();

        setupDatabase();

      /*  ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).
                threadPriority(Thread.NORM_PRIORITY - 1).memoryCacheSizePercentage(50).
                threadPoolSize(3).discCacheFileNameGenerator(new Md5FileNameGenerator()).
                memoryCache(new WeakMemoryCache()).tasksProcessingOrder(QueueProcessingType.LIFO)
                //.imageDecoder(new NutraBaseImageDecoder(true))
                // Remove for release app
                .build(); */


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                        // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        L.writeLogs(false);

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(getString(R.string.facebook_app_id))
                .setNamespace(getString(R.string.app_name))
                .setPermissions(permissions)
                .build();
        SimpleFacebook.setConfiguration(configuration);
    }

    @Override
    public void onTerminate() {
        Log.i("application", "onTerminate");
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "diblydb", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        // initTestGEOfenceData();
//        initAllPermanentData();
    }

    private void initAllPermanentData() {
        Intent intentGetDeal = new Intent(this, ParallaxService.class);
        intentGetDeal.setAction(ParallaxService.ACTION_SEARCH_DEAL);
        intentGetDeal.putExtra(ParallaxService.EXTRA_LATITUDE, "1.303278");
        intentGetDeal.putExtra(ParallaxService.EXTRA_LONGITUDE, "103.834965");
        intentGetDeal.putExtra(ParallaxService.EXTRA_PAGE, "1");

        String keyword = "";
        String deal_type = "";
        String discovery_type = "";
        String min = "";
        String max = "";

        intentGetDeal.putExtra(ParallaxService.EXTRA_KEYWORD, keyword);
        intentGetDeal.putExtra(ParallaxService.EXTRA_DEAL_TYPE, deal_type);
        intentGetDeal.putExtra(ParallaxService.EXTRA_DISCOVERY_TYPE, discovery_type);
        intentGetDeal.putExtra(ParallaxService.EXTRA_MIN, min);
        intentGetDeal.putExtra(ParallaxService.EXTRA_MAX, max);

        this.startService(intentGetDeal);
    }


    public DaoSession getDaoSession() {
        if (daoSession != null) {
            return daoSession;
        } else {
            setupDatabase();
            return daoSession;
        }
    }

    private void deleteDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "diblydb", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoMaster.dropAllTables(db, true);
        daoMaster.createAllTables(db, true);
    }

    public int getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public String getAppVersionName() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public void clearDaoSession() {
        daoSession = null;
    }

    private void initTestGEOfenceData() {
        PermanentController.deleteAll(this);
        PermanentDeals deal = new PermanentDeals();
        deal.setDeal_id(1001l);
        deal.setType("instore");
        deal.setIsNotified(0);
        deal.setTitle("50% off on Forever 21");
        deal.setOriginal_price(1000f);
        deal.setPurchase_now_price(500f);
        deal.setStart_at("2015-07-23 15:30:30");
        deal.setEnd_at("2015-07-30 23:30:30");
        deal.setLatitude(10.791357);
        deal.setLongitude(106.702283);

        PermanentController.insert(this, deal);


        deal = new PermanentDeals();
        deal.setDeal_id(1001l);
        deal.setType("instore");
        deal.setIsNotified(0);
        deal.setTitle("Buy 1 get 1 free Orchard");
        deal.setOriginal_price(500f);
        deal.setPurchase_now_price(300f);
        deal.setStart_at("2015-07-23 15:30:30");
        deal.setEnd_at("2015-07-30 23:30:30");
        deal.setLatitude(10.792235);
        deal.setLongitude(106.699587);

        PermanentController.insert(this, deal);


        deal = new PermanentDeals();
        deal.setDeal_id(1001l);
        deal.setType("instore");
        deal.setIsNotified(0);
        deal.setTitle("Old machine for sale up to 70%");
        deal.setOriginal_price(100f);
        deal.setPurchase_now_price(70f);
        deal.setStart_at("2015-07-23 15:30:30");
        deal.setEnd_at("2015-07-30 23:30:30");
        deal.setLatitude(10.801608);
        deal.setLongitude(106.711332);

        PermanentController.insert(this, deal);


        deal = new PermanentDeals();
        deal.setDeal_id(1001l);
        deal.setType("instore");
        deal.setIsNotified(0);
        deal.setTitle("D2 cafe discount 30%");
        deal.setOriginal_price(10f);
        deal.setPurchase_now_price(6f);
        deal.setStart_at("2015-07-23 15:30:30");
        deal.setEnd_at("2015-07-30 23:30:30");
        deal.setLatitude(10.804311);
        deal.setLongitude(106.715853);

        PermanentController.insert(this, deal);


        deal = new PermanentDeals();
        deal.setDeal_id(1001l);
        deal.setType("instore");
        deal.setIsNotified(0);
        deal.setTitle("Nike Just do it collection 50% off");
        deal.setOriginal_price(15f);
        deal.setPurchase_now_price(8f);
        deal.setStart_at("2015-07-23 15:30:30");
        deal.setEnd_at("2015-07-30 23:30:30");
        deal.setLatitude(10.811071);
        deal.setLongitude(106.714780);

        PermanentController.insert(this, deal);


        deal = new PermanentDeals();
        deal.setDeal_id(1001l);
        deal.setType("instore");
        deal.setIsNotified(0);
        deal.setTitle("Free beer for you!");
        deal.setOriginal_price(200f);
        deal.setPurchase_now_price(130f);
        deal.setStart_at("2015-07-23 15:30:30");
        deal.setEnd_at("2015-07-30 23:30:30");
        deal.setLatitude(10.816837);
        deal.setLongitude(106.720110);

        PermanentController.insert(this, deal);


        deal = new PermanentDeals();
        deal.setDeal_id(1001l);
        deal.setType("instore");
        deal.setIsNotified(0);
        deal.setTitle("Moon river! free beer");
        deal.setOriginal_price(5000f);
        deal.setPurchase_now_price(4500f);
        deal.setStart_at("2015-07-23 15:30:30");
        deal.setEnd_at("2015-07-30 23:30:30");
        deal.setLatitude(10.821608);
        deal.setLongitude(106.729015);

        PermanentController.insert(this, deal);

    }

}
