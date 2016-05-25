package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.Category;
import greendao.CategoryDao;
import greendao.PhoneCode;
import greendao.PhoneCodeDao;


/**
 * Created by VuPhan on 4/9/16.
 */
public class CategoryController {

    private static CategoryDao getCategoryDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCategoryDao();
    }

    public static void insert(Context context, Category category) {
        getCategoryDao(context).insert(category);
    }

    public static List<Category> getAll(Context ctx) {
        return getCategoryDao(ctx).loadAll();
    }

    public static void deleteAll(Context context) {
        getCategoryDao(context).deleteAll();
    }

}
