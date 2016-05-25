package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import greendao.Merchant;
import greendao.MerchantDao;

/**
 * Created by USER on 7/10/2015.
 */
public class MerchantController {

    private static MerchantDao getMerchantDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getMerchantDao();
    }

    public static void insert(Context context, Merchant merchant) {
        getMerchantDao(context).deleteByKey(merchant.getMerchant_id());
        getMerchantDao(context).insert(merchant);
    }

    public static void update(Context context, Merchant merchant) {
        getMerchantDao(context).update(merchant);
    }

    public static Merchant getById(Context context, Long id) {
        return getMerchantDao(context).load(id);
    }

    public static String getNameById(Context context, Long id) {
        Merchant merchant = getById(context, id);
        if (merchant != null) {
            return merchant.getOrganization_name();
        } else {
            return "";
        }
    }

    public static void clearById(Context context, Long id) {
        getMerchantDao(context).deleteByKey(id);
    }

    public static void clearAll(Context context) {
        getMerchantDao(context).deleteAll();
    }
}
