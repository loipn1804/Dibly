package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import greendao.ObjectDeal;
import greendao.ObjectDealSearch;
import greendao.ObjectDealSearchDao;

/**
 * Created by Deal on 7/8/2015.
 */
public class SearchDealController {

    private static ObjectDealSearchDao getDealSearchDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getObjectDealSearchDao();
    }

    public static void insert(Context context, ObjectDealSearch objectDeal) {
        if (getDealById(context, objectDeal.getDeal_id()) == null) {
            getDealSearchDao(context).insert(objectDeal);
        }
    }

    public static void update(Context context, ObjectDealSearch objectDeal) {
        getDealSearchDao(context).update(objectDeal);
    }

    public static List<ObjectDealSearch> getAllDeals(Context context) {
        return getDealSearchDao(context).loadAll();
    }

    public static ObjectDealSearch getDealById(Context context, Long deal_id) {
        List<ObjectDealSearch> list = getDealSearchDao(context).queryRaw(" WHERE DEAL_ID = ?", deal_id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }


    public static void clearDeal(Context context) {

        getDealSearchDao(context).deleteAll();

    }

    public static List<ObjectDealSearch> getDealThatALive(Context context) {


        List<ObjectDealSearch> returnDealList = new ArrayList<>();
        List<ObjectDealSearch> dealList = getAllDeals(context);
        long current_time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (ObjectDealSearch objectDeal : dealList) {

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
        getDealSearchDao(context).deleteAll();
    }

    public static ObjectDeal convertObjectDealSearchToObjectDeal(ObjectDealSearch objectDealSearch) {
        ObjectDeal objectDeal = new ObjectDeal();
        objectDeal.setId(objectDealSearch.getId());
        objectDeal.setDeal_id(objectDealSearch.getDeal_id());
        objectDeal.setDescription((objectDealSearch.getDescription()));
        objectDeal.setEnd_at(objectDealSearch.getEnd_at());
        objectDeal.setStart_at(objectDealSearch.getStart_at());
        objectDeal.setImage(objectDealSearch.getImage());
        objectDeal.setDeal_type(2);
        objectDeal.setMax_claim(objectDealSearch.getMax_claim());
        objectDeal.setMerchant_id(objectDealSearch.getMerchant_id());
        objectDeal.setOriginal_price(objectDealSearch.getOriginal_price());
        objectDeal.setOutlet_id(objectDealSearch.getOutlet_id());
        objectDeal.setPurchase_now_price(objectDealSearch.getPurchase_now_price());
        objectDeal.setTerms(objectDealSearch.getTerms());
        objectDeal.setTitle(objectDealSearch.getTitle());
        objectDeal.setType(objectDealSearch.getType());
        objectDeal.setF_claimed(objectDealSearch.getF_claimed());
        objectDeal.setF_call_dibs(objectDealSearch.getF_call_dibs());

        return objectDeal;

    }
}
