package com.dibs.dibly.daocontroller;

import android.content.Context;
import android.location.Location;

import com.dibs.dibly.application.MyApplication;

import java.util.ArrayList;
import java.util.List;

import greendao.MyLocation;
import greendao.Outlet;
import greendao.OutletDao;

/**
 * Created by USER on 7/10/2015.
 */
public class OutletController {

    private static OutletDao getOutletDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getOutletDao();
    }

    public static void insert(Context context, Outlet outlet) {
        Outlet outlet_db = getOutletDao(context).load(outlet.getOutlet_id());
        if (outlet_db == null) {
            getOutletDao(context).insert(outlet);
        } else {
            if (outlet.getZip_code().length() > 0) {
                outlet_db.setZip_code(outlet.getZip_code());

            }
            if (outlet_db.getSecret_code().length() == 0) {
                outlet_db.setSecret_code(outlet.getSecret_code());
            }

            getOutletDao(context).update(outlet_db);
        }
    }

    public static void clearById(Context context, Long id) {
        getOutletDao(context).deleteByKey(id);
    }

    public static void clearAll(Context context) {
        getOutletDao(context).deleteAll();
    }

    public static void clearByMerchantId(Context context, String merchantID) {
        List<Outlet> outletList = getOutletDao(context).queryRaw(" WHERE merchant_id = ?", merchantID);
        for (Outlet outlet : outletList) {
            getOutletDao(context).delete(outlet);
        }
    }

    public static List<Outlet> getOutletsByMerchantId(Context context, String merchantID) {
        List<Outlet> outletList = new ArrayList<Outlet>();
        outletList = getOutletDao(context).queryRaw(" WHERE merchant_id = ?", merchantID);
        return outletList;
    }

    public static Outlet getOutletById(Context context, Long outlet_id) {
        return getOutletDao(context).load(outlet_id);
    }

    public static int getNearestOutletByOutletId(Context context, long outlet_id) {
        MyLocation mLoc = MyLocationController.getLastLocation(context);
        if (mLoc != null) {
            Outlet outlet = getOutletById(context, outlet_id);
            float result[] = new float[3];
            Location.distanceBetween(mLoc.getLatitude(), mLoc.getLongitude(), Double.parseDouble(outlet.getLatitude()), Double.parseDouble(outlet.getLongitude()), result);
            int dis = (int) result[0];
            return dis;
        } else {
            return 0;
        }
    }

    public static List<Outlet> getOutletsByDealOutlets(Context context, String outlets) {
        List<Outlet> outletList = new ArrayList<Outlet>();
        if(outlets.length()>0) {
            String[] outlet_id = outlets.split("/");
            for (int i = 0; i < outlet_id.length; i++) {
                Outlet outlet = getOutletById(context, Long.parseLong(outlet_id[i]));
                if (outlet != null) {
                    outletList.add(outlet);
                }
            }
        }
        return outletList;
    }
}
