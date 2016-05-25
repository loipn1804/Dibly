package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.IndustrySearch;
import greendao.IndustrySearchDao;

/**
 * Created by USER on 10/20/2015.
 */
public class IndustrySearchController {

    private static IndustrySearchDao getIndustrySearchDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getIndustrySearchDao();
    }

    public static void insert(Context context, IndustrySearch industrySearch) {
        if (getById(context, industrySearch.getId()) == null) {
            getIndustrySearchDao(context).insert(industrySearch);
        }
    }

    public static IndustrySearch getById(Context context, Long id) {
        return getIndustrySearchDao(context).load(id);
    }

    public static void clearById(Context context, Long id) {
        getIndustrySearchDao(context).deleteByKey(id);
    }

    public static List<IndustrySearch> getAll(Context context) {
        return getIndustrySearchDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getIndustrySearchDao(context).deleteAll();
    }
}
