package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import greendao.PermanentDeals;
import greendao.PermanentDealsDao;

/**
 * Created by Deal on 7/8/2015.
 */

public class PermanentController {

    private static PermanentDealsDao getPermanentDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getPermanentDealsDao();
    }

    public static void insertOrUpdate(Context context, PermanentDeals objectDeal) {
        PermanentDeals permanentDeals = getDealById(context, objectDeal.getDeal_id());
        if (permanentDeals == null) {
            getPermanentDao(context).insert(objectDeal);
        } else {
            permanentDeals.setTitle(objectDeal.getTitle());
            permanentDeals.setDeal_type(objectDeal.getDeal_type());
            permanentDeals.setLatitude(objectDeal.getLatitude());
            permanentDeals.setLongitude(objectDeal.getLongitude());
//            permanentDeals.setIsNotified(objectDeal.getIsNotified());

            getPermanentDao(context).update(permanentDeals);
        }
    }

    public static void insert(Context context, PermanentDeals objectDeal) {
//        if (getDealById(context, objectDeal.getDeal_id()) != null) {
//            getDealDao(context).deleteByKey(objectDeal.getDeal_id());
//        }
        getPermanentDao(context).insert(objectDeal);
    }

    public static void deleteByDealId(Context context, Long deal_id) {
        PermanentDeals permanentDeals = getDealById(context, deal_id);
        if (permanentDeals != null) {
            getPermanentDao(context).delete(permanentDeals);
        }
    }

    public static PermanentDeals getDealById(Context context, Long deal_id) {
        List<PermanentDeals> list = getPermanentDao(context).queryRaw(" WHERE DEAL_ID = ?", deal_id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static void update(Context context, PermanentDeals objectDeal) {
        getPermanentDao(context).update(objectDeal);
    }

    public static void deleteAll(Context context) {
        getPermanentDao(context).deleteAll();
    }

    public static List<PermanentDeals> getAllDeals(Context context) {
        return getPermanentDao(context).loadAll();
    }

    public static List<PermanentDeals> getAllDealsThatNotYetNotify(Context context) {
        return getPermanentDao(context).queryRaw(" WHERE is_notified = ?", 0 + "");

    }

    public static List<PermanentDeals> getDealThatALive(Context context) {
        List<PermanentDeals> returnDealList = new ArrayList<>();
        List<PermanentDeals> dealList = getAllDealsThatNotYetNotify(context);
        long current_time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (PermanentDeals objectDeal : dealList) {
            try {
                Date startDate = sdf.parse(objectDeal.getEnd_at());
                long endTime = startDate.getTime();
                if (endTime > current_time) {
                    returnDealList.add(objectDeal);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        dealList = null;
        return returnDealList;
    }

    public static void clearAllDeals(Context context) {
        getPermanentDao(context).deleteAll();
    }
}
