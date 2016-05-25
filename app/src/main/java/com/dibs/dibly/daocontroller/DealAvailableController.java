package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.DealAvailable;
import greendao.DealAvailableDao;

/**
 * Created by USER on 7/14/2015.
 */
public class DealAvailableController {

    private static DealAvailableDao getDealAvailableDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDealAvailableDao();
    }

    public static void insert(Context context, DealAvailable dealAvailable) {
        if (getDealAvailableDao(context).load(dealAvailable.getDeal_id()) == null) {
            getDealAvailableDao(context).insert(dealAvailable);
        }
    }

    public static DealAvailable getById(Context context, Long deal_id) {
        return getDealAvailableDao(context).load(deal_id);
    }

    public static List<DealAvailable> getAll(Context context) {
        return getDealAvailableDao(context).loadAll();
    }

    public static List<DealAvailable> getAllNotExpired(Context context) {
        return getDealAvailableDao(context).queryRaw(" where is_expired=?", "0");
    }

    public static List<DealAvailable> getAllExpired(Context context) {
        return getDealAvailableDao(context).queryRaw(" where is_expired=?", "1");
    }

    public static void clearById(Context context, Long id) {
        getDealAvailableDao(context).deleteByKey(id);
    }

    public static void clearAll(Context context) {
        getDealAvailableDao(context).deleteAll();
    }
}
