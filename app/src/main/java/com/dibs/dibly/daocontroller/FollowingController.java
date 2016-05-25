package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.Following;
import greendao.FollowingDao;

/**
 * Created by USER on 7/10/2015.
 */
public class FollowingController {

    private static FollowingDao getFollowingDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getFollowingDao();
    }

    public static void insert(Context context, Following following) {
        getFollowingDao(context).insert(following);
    }


    public static List<Following> getAll(Context ctx) {
        return getFollowingDao(ctx).loadAll();
    }

    public static void deleteByID(Context ctx, long id) {
        getFollowingDao(ctx).deleteByKey(id);

    }

    public static void deleteAll(Context context) {
        getFollowingDao(context).deleteAll();
    }
}
