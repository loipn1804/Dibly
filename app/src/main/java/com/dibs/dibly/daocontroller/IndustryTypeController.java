package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.IndustryType;
import greendao.IndustryTypeDao;

/**
 * Created by USER on 10/20/2015.
 */
public class IndustryTypeController {

    private static IndustryTypeDao getIndustryTypeDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getIndustryTypeDao();
    }

    public static void insert(Context context, IndustryType industryType) {
        getIndustryTypeDao(context).insert(industryType);
    }

    public static void clearById(Context context, Long id) {
        getIndustryTypeDao(context).deleteByKey(id);
    }

    public static List<IndustryType> getAll(Context context) {
        return getIndustryTypeDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getIndustryTypeDao(context).deleteAll();
    }
}
