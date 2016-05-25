package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.MyLocation;
import greendao.MyLocationDao;

/**
 * Created by Deal on 7/8/2015.
 */
public class MyLocationController {

    private static MyLocationDao getMyLocationDao(Context c) {
        MyLocationDao dao = ((MyApplication) c.getApplicationContext()).getDaoSession().getMyLocationDao();
        return dao;
    }

    public static void insertOrUpdateMyLocation(Context context, MyLocation mLoc) {
        getMyLocationDao(context).insertOrReplace(mLoc);
    }

    public static MyLocation getLastLocation(Context ctx) {

        List<MyLocation> myLocationList = getMyLocationDao(ctx).loadAll();
        if (myLocationList.size() > 0) {
            MyLocation myLocation = myLocationList.get(myLocationList.size() - 1);

            long current = System.currentTimeMillis();
            long time = Long.parseLong(myLocation.getLastUpdateTime());
            long diff = (current - time) / (1000 * 60);
            //if (diff < 30) {
            return myLocation;
            //} else {
            //   return null;
            // }


        } else {
            return null;
        }

    }

    public static List<MyLocation> getAll(Context ctx) {
        List<MyLocation> myLocationList = getMyLocationDao(ctx).loadAll();
        return myLocationList;
    }
}
