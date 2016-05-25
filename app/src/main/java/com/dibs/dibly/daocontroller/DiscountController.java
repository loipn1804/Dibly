package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.Discount;
import greendao.DiscountDao;

/**
 * Created by USER on 10/21/2015.
 */
public class DiscountController {

    private static DiscountDao getDiscountDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDiscountDao();
    }

    public static void insert(Context context, Discount discount) {
        getDiscountDao(context).insert(discount);
    }

    public static void clearById(Context context, Long id) {
        getDiscountDao(context).deleteByKey(id);
    }

    public static List<Discount> getAll(Context context) {
        return getDiscountDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDiscountDao(context).deleteAll();
    }
}
