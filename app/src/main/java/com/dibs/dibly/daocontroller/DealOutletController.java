package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.ObjectDealOutlet;
import greendao.ObjectDealOutletDao;

/**
 * Created by USER on 7/8/2015.
 */
public class DealOutletController {

    private static ObjectDealOutletDao getDealOutletDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getObjectDealOutletDao();
    }

    public static void insert(Context context, ObjectDealOutlet objectDealOutlet) {
        List<ObjectDealOutlet> list = getDealOutletByDealIdAndOutletId(context, objectDealOutlet.getDeal_id(), objectDealOutlet.getOutlet_id());
        for (ObjectDealOutlet objectDelete : list) {
            getDealOutletDao(context).delete(objectDelete);
        }
        getDealOutletDao(context).insert(objectDealOutlet);
    }

    public static void update(Context context, ObjectDealOutlet objectDealOutlet) {
        getDealOutletDao(context).update(objectDealOutlet);
    }

    public static List<ObjectDealOutlet> getAllDealOutlets(Context context) {
        return getDealOutletDao(context).loadAll();
    }

    public static List<ObjectDealOutlet> getDealOutletByDealIdAndOutletId(Context context, Long deal_id, Long outlet_id) {
        return getDealOutletDao(context).queryRaw(" WHERE DEAL_ID = ? AND OUTLET_ID = ?", deal_id + "", outlet_id + "");
    }

    public static List<ObjectDealOutlet> getDealOutletByDealId(Context context, Long deal_id) {
        return getDealOutletDao(context).queryRaw(" WHERE DEAL_ID = ?", deal_id + "");
    }

    public static ObjectDealOutlet getDealOutletByOutletId(Context context, Long outlet_id) {
        List<ObjectDealOutlet> list = getDealOutletDao(context).queryRaw(" WHERE OUTLET_ID = ?", outlet_id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }


    public static List<ObjectDealOutlet> getDealOutletByDealIdAndNotYetNotify(Context context, Long deal_id) {
        return getDealOutletDao(context).queryRaw(" WHERE DEAL_ID = ?", deal_id + "");
    }

    public static void clearAllDealOutlets(Context context) {
        getDealOutletDao(context).deleteAll();
    }
}
