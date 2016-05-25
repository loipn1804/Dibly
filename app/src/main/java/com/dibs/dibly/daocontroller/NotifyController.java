package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.ArrayList;
import java.util.List;

import greendao.Notify;
import greendao.NotifyDao;

/**
 * Created by USER on 8/4/2015.
 */
public class NotifyController {

    private static NotifyDao getNotifyDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getNotifyDao();
    }

    public static void insert(Context context, Notify notify) {
        if (getNotifyDao(context).load(notify.getDeal_id()) == null) {
            getNotifyDao(context).insert(notify);
        }
    }

    public static List<Notify> getAll(Context context) {
        List<Notify> NotifyList = new ArrayList<>();
        NotifyList = getNotifyDao(context).loadAll();
        return NotifyList;
    }

    public static int getSize(Context context) {
        List<Notify> NotifyList = new ArrayList<>();
        NotifyList = getNotifyDao(context).loadAll();
        return NotifyList.size();
    }

    public static void clearByDealId(Context context, Long dealId) {
        getNotifyDao(context).deleteByKey(dealId);
    }

    public static void clearAll(Context context) {
        getNotifyDao(context).deleteAll();
    }
}
