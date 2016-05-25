package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.ObjectDealMerchant;
import greendao.ObjectDealMerchantDao;

/**
 * Created by USER on 7/8/2015.
 */
public class DealMerchantController {

    private static ObjectDealMerchantDao getDealMerchantDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getObjectDealMerchantDao();
    }

    public static void insert(Context context, ObjectDealMerchant objectDealMerchant) {
        ObjectDealMerchant objectDelete = getDealMerchantByDealIdAndMerchantId(context, objectDealMerchant.getDeal_id(), objectDealMerchant.getMerchant_id());
        if (objectDelete != null) {
            getDealMerchantDao(context).delete(objectDelete);
        }
        getDealMerchantDao(context).insert(objectDealMerchant);
    }

    public static List<ObjectDealMerchant> getAllDealMerchants(Context context) {
        return getDealMerchantDao(context).loadAll();
    }

    public static ObjectDealMerchant getDealMerchantByDealIdAndMerchantId(Context context, Long deal_id, Long merchant_id) {
        List<ObjectDealMerchant> list = getDealMerchantDao(context).queryRaw(" WHERE DEAL_ID = ? AND MERCHANT_ID = ?", deal_id + "", merchant_id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static String getNameByMerchantId(Context context, Long merchant_id) {
        List<ObjectDealMerchant> list = getDealMerchantDao(context).queryRaw(" WHERE MERCHANT_ID = ?", merchant_id + "");
        if (list.size() > 0) {
            return list.get(0).getOrganization_name();
        } else {
            return "";
        }
    }

    public static void clearAllDealMerchants(Context context) {
        getDealMerchantDao(context).deleteAll();
    }
}
