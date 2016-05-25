package com.dibs.dibly.consts;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by VuPhan on 4/15/16.
 */
public class GeneralMethod {

    public static boolean checkTimeToSync(Context mContext) {

        // Time to sync
        int HOUR_TO_SYNC = 1;

        SharedPreferences prefs = mContext.getSharedPreferences("appSync", 0);
        SharedPreferences.Editor editor = prefs.edit();

        // Get date of first launch sync
        Long date_firstLaunch = prefs.getLong("timeSync", 0);
        if (date_firstLaunch == 0) {
           /* date_firstLaunch = System.currentTimeMillis();
            editor.putLong("timeSync", date_firstLaunch);
            editor.commit(); */
            return true;
        }

        if (System.currentTimeMillis() >= date_firstLaunch +
                (HOUR_TO_SYNC * 60 * 60 * 1000)) {
            return true;
        }

        return false;
    }

    public static void setTimeSync(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("appSync", 0);
        SharedPreferences.Editor editor = prefs.edit();

        Long date_firstLaunch = System.currentTimeMillis();
        editor.putLong("timeSync", date_firstLaunch);
        editor.commit();
    }
}
