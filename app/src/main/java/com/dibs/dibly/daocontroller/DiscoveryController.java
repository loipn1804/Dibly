package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.Discovery;
import greendao.DiscoveryDao;

/**
 * Created by USER on 7/10/2015.
 */
public class DiscoveryController {
    private static DiscoveryDao getDiscoveryDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDiscoveryDao();
    }

    public static void insert(Context context, Discovery discovery) {
        getDiscoveryDao(context).insert(discovery);
    }

    public static void update(Context context, Discovery discovery) {
        getDiscoveryDao(context).update(discovery);
    }


    public static List<Discovery> getAll(Context ctx) {
        return getDiscoveryDao(ctx).queryRaw("order by position asc");

    }

    public static Discovery getDiscoveryById(Context ctx, long id) {

        Discovery discovery = null;

        discovery = getDiscoveryDao(ctx).load(id);
        if (discovery != null) {
            return discovery;
        } else {
            return null;
        }
    }


    public static void deleteAll(Context ctx) {
        getDiscoveryDao(ctx).deleteAll();

    }
}
