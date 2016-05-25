package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.DealClaimed;
import greendao.DealClaimedDao;

/**
 * Created by USER on 7/14/2015.
 */
public class DealClaimedController {

    private static DealClaimedDao getDealClaimedDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDealClaimedDao();
    }

    public static void insert(Context context, DealClaimed dealClaimed) {
        if (getDealClaimedDao(context).load(dealClaimed.getDeal_id()) == null) {
            getDealClaimedDao(context).insert(dealClaimed);
        }
    }

    public static List<DealClaimed> getAll(Context context) {
        return getDealClaimedDao(context).loadAll();
    }

    public static void clearById(Context context, Long id) {
        getDealClaimedDao(context).deleteByKey(id);
    }

    public static void clearAll(Context context) {
        getDealClaimedDao(context).deleteAll();
    }
}
