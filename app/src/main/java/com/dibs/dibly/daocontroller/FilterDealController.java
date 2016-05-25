package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.DealFilter;
import greendao.DealFilterDao;

/**
 * Created by USER on 7/10/2015.
 */
public class FilterDealController {

    public static final long ID = 1;
    public static final String PURCHASE_TYPE_INAPP = "inapp";
    public static final String PURCHASE_TYPE_INSTORE = "instore";
    public static final String PURCHASE_TYPE_BOTH = "";

    private static DealFilterDao getFilterDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDealFilterDao();
    }

    public static void insert(Context context, DealFilter filter) {
        getFilterDao(context).deleteAll();
        getFilterDao(context).insert(filter);
    }

    public static List<DealFilter> getAll(Context context) {
        return getFilterDao(context).loadAll();
    }

    public static DealFilter getFilter(Context ctx) {
        return getFilterDao(ctx).load(ID);
    }

    public static void clearAll(Context ctx) {
        getFilterDao(ctx).deleteAll();
    }
}
