package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.PhoneCode;
import greendao.PhoneCodeDao;


/**
 * Created by VuPhan on 4/9/16.
 */
public class PhoneCodeController {

    private static PhoneCodeDao getPhoneDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getPhoneCodeDao();
    }

    public static void insert(Context context, PhoneCode phoneCode) {
        getPhoneDao(context).insert(phoneCode);
    }

    public static List<PhoneCode> getAll(Context ctx) {
        return getPhoneDao(ctx).loadAll();
    }

    public static void deleteAll(Context context) {
        getPhoneDao(context).deleteAll();
    }

}
