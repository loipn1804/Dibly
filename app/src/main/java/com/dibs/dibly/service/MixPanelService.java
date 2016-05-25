package com.dibs.dibly.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dibs.dibly.R;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import greendao.ObjectUser;

/**
 * Created by USER on 05/24/2016.
 */
public class MixPanelService extends Service {

    public static String last_page = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startWatch();
        return Service.START_STICKY;
    }

    private void startWatch() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        SharedPreferences preferences = getSharedPreferences("MixPanel", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        boolean isRunning = preferences.getBoolean("isRunning", true);
                        if (isRunning) {
                            boolean isFirstRunning = preferences.getBoolean("isFirstRunning", true);
                            if (isFirstRunning) {
                                Log.e("mixpanel", "running");
                                editor.putBoolean("isFirstRunning", false);
                                enterMixPanel();
                            }

                            editor.putBoolean("isFirstStopped", true);
                            editor.commit();
                        } else {
                            boolean isFirstStopped = preferences.getBoolean("isFirstStopped", true);
                            if (isFirstStopped) {
                                Log.e("mixpanel", "stopped");
                                editor.putBoolean("isFirstStopped", false);
                                Log.e("mixpanel", last_page);
                                exitMixPanel();
                            }

                            editor.putBoolean("isFirstRunning", true);
                            editor.commit();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void enterMixPanel() {
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            if (UserController.isLogin(this)) {
                ObjectUser user = UserController.getCurrentUser(this);
                object.put("email", user.getEmail());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Enter_Application), object);
        startDurationMixPanel(getString(R.string.Duration_Application));
    }

    private void exitMixPanel() {
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            object.put("current page", last_page);
            if (UserController.isLogin(this)) {
                ObjectUser user = UserController.getCurrentUser(this);
                object.put("email", user.getEmail());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Exit_Application), object);

        JSONObject objectDuration = new JSONObject();
        try {
            objectDuration.put("time", StaticFunction.getCurrentTime());
            if (UserController.isLogin(this)) {
                ObjectUser user = UserController.getCurrentUser(this);
                objectDuration.put("email", user.getEmail());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        endDurationMixPanel(getString(R.string.Duration_Application), objectDuration);
    }

    public void trackMixPanel(String name, JSONObject object) {
        String projectToken = StaticFunction.MixPanelToken;
        MixpanelAPI mixpanelAPI = MixpanelAPI.getInstance(this, projectToken);
        mixpanelAPI.track(name, object);
    }

    public void startDurationMixPanel(String name) {
        String projectToken = StaticFunction.MixPanelToken;
        MixpanelAPI mixpanelAPI = MixpanelAPI.getInstance(this, projectToken);
        mixpanelAPI.timeEvent(name);
    }

    public void endDurationMixPanel(String name, JSONObject object) {
        String projectToken = StaticFunction.MixPanelToken;
        MixpanelAPI mixpanelAPI = MixpanelAPI.getInstance(this, projectToken);
        if (object == null) {
            mixpanelAPI.track(name);
        } else {
            mixpanelAPI.track(name, object);
        }
    }
}
