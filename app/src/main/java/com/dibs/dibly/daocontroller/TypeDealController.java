package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.Category;
import greendao.CategoryDao;
import greendao.TypeDeal;
import greendao.TypeDealDao;


/**
 * Created by VuPhan on 4/9/16.
 */
public class TypeDealController {

    private static TypeDealDao getTypeDealDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getTypeDealDao();
    }

    public static void insert(Context context, TypeDeal typeDeal) {
        getTypeDealDao(context).insert(typeDeal);
    }

    public static List<TypeDeal> getAll(Context ctx) {
        return getTypeDealDao(ctx).loadAll();
    }

    public static void deleteAll(Context context) {
        getTypeDealDao(context).deleteAll();
    }

}
