package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.ArrayList;
import java.util.List;

import greendao.TypeSearch;
import greendao.TypeSearchDao;

/**
 * Created by USER on 7/20/2015.
 */
public class TypeSearchController {

    private static TypeSearchDao getTypeSearchDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getTypeSearchDao();
    }

    public static void insert(Context context, TypeSearch TypeSearch) {
        getTypeSearchDao(context).insert(TypeSearch);
    }

    public static void clearById(Context context, Long id) {
        getTypeSearchDao(context).deleteByKey(id);
    }

    public static List<TypeSearch> getAll(Context context) {
        List<TypeSearch> typeSearchList = new ArrayList<>();
        typeSearchList = getTypeSearchDao(context).loadAll();
        return typeSearchList;
    }

    public static void clearAll(Context context) {
        getTypeSearchDao(context).deleteAll();
    }
}
