package com.dibs.dibly.daocontroller;

import android.content.Context;
import android.util.Log;

import com.dibs.dibly.application.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import greendao.ObjectDeal;
import greendao.ObjectDealDao;

/**
 * Created by Deal on 7/8/2015.
 */

public class DealController {

    public static ObjectDealDao getDealDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getObjectDealDao();
    }

    public static long insert(Context context, ObjectDeal objectDeal) {
        if (objectDeal.getRef_merchant_id() != null && objectDeal.getRef_merchant_id() == -1)
            clearDealByDealIdAndDealType(context, objectDeal.getDeal_id(), objectDeal.getDeal_type() + "",objectDeal.getOutlet_id());
        return getDealDao(context).insert(objectDeal);
    }

    public static void update(Context context, ObjectDeal objectDeal) {
        getDealDao(context).update(objectDeal);
    }

    public static List<ObjectDeal> getAllDeals(Context context) {
        return getDealDao(context).loadAll();
    }

    public static ObjectDeal getDealByIdAndDealType(Context context, Long deal_id, int deal_type) {
        List<ObjectDeal> list = getDealDao(context).queryRaw(" WHERE DEAL_ID = ? AND deal_type = ? AND fromDealFollowing = ?", deal_id + "", deal_type + "", "false");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static ObjectDeal getDealById(Context context, Long deal_id) {
        List<ObjectDeal> list = getDealDao(context).queryRaw(" WHERE DEAL_ID = ?", deal_id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static List<ObjectDeal> getListDealById(Context context, Long deal_id,String outlet_id) {
        List<ObjectDeal> list = getDealDao(context).queryRaw(" WHERE DEAL_ID = ? and outlet_id = ?", deal_id + "",outlet_id);
        return list;
    }


    public static ObjectDeal getDealByDealId(Context context, Long deal_id) {
        List<ObjectDeal> list = getDealDao(context).queryRaw(" WHERE DEAL_ID = ?", deal_id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static ObjectDeal getDealByDealId(Context context, Long deal_id,Long outletId) {
        List<ObjectDeal> list = getDealDao(context).queryRaw(" WHERE DEAL_ID = ? and outlet_id = ?", deal_id + "",outletId+"");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static ObjectDeal getDealByDealIdAndDealType(Context context, Long deal_id, int deal_type) {
        List<ObjectDeal> list = getDealDao(context).queryRaw(" WHERE DEAL_ID = ? AND deal_type = ? and ref_merchant_id = ?", deal_id + "", deal_type + "","-1");
        if (list.size() > 0) {

            for (ObjectDeal objectDeal : list) {
                if (objectDeal.getDealReferId() != null && objectDeal.getDealReferId() == -1 && !objectDeal.getFromDealFollowing()) {
                   return objectDeal;
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static ObjectDeal getDealByDealIdAndDealType(Context context, Long deal_id,Long outletId, int deal_type) {
        List<ObjectDeal> list = getDealDao(context).queryRaw(" WHERE DEAL_ID = ? AND deal_type = ? and ref_merchant_id = ? and outlet_id = ?", deal_id + "", deal_type + "","-1",outletId+"");
        if (list.size() > 0) {

            for (ObjectDeal objectDeal : list) {
                if (objectDeal.getDealReferId() != null && objectDeal.getDealReferId() == -1 && !objectDeal.getFromDealFollowing()) {
                    return objectDeal;
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static void clearDealByDealIdAndDealType(Context context, Long deal_id, String deal_type,long outlet_id) {
        List<ObjectDeal> list = getDealDao(context).queryRaw(" WHERE DEAL_ID = ? AND deal_type = ? and outlet_id = ?", deal_id + "", deal_type,outlet_id+"");
        for (ObjectDeal deal : list) {
            getDealDao(context).deleteByKey(deal.getId());
        }
    }

    public static List<ObjectDeal> getListDealByDealId(Context context, Long deal_id) {
        return getDealDao(context).queryRaw(" WHERE DEAL_ID = ?", deal_id + "");
    }

    public static List<ObjectDeal> getListDealByDealId(Context context, Long deal_id,Long outletId) {
        return getDealDao(context).queryRaw(" WHERE DEAL_ID = ? and outlet_id = ?", deal_id + "",outletId+"");
    }

    public static List<ObjectDeal> getDealByDealType(Context context, int deal_type) {
        List<ObjectDeal> tmpObject = getDealDao(context).queryRaw(" WHERE deal_type = ? and ref_merchant_id = ?", deal_type + "", "-1");
        List<ObjectDeal> resultObject = new ArrayList();
        if (tmpObject != null) {

            for (ObjectDeal objectDeal : tmpObject) {
                if (objectDeal.getDealReferId() != null && objectDeal.getDealReferId() == -1 && !objectDeal.getFromDealFollowing()) {
                    resultObject.add(objectDeal);
                }
            }

        }

        return resultObject;
    }

    public static List<ObjectDeal> getDealByDealTypeHistory(Context context, int deal_type) {
        List<ObjectDeal> tmpObject = getDealDao(context).queryRaw(" WHERE deal_type = ?", deal_type + "");
        List<ObjectDeal> resultObject = new ArrayList();
        if (tmpObject != null) {

            for (ObjectDeal objectDeal : tmpObject) {
                if (objectDeal.getDealReferId() != null && objectDeal.getDealReferId() == -1 && !objectDeal.getFromDealFollowing()) {
                    resultObject.add(objectDeal);
                }
            }

        }

        return resultObject;
    }


    public static List<ObjectDeal> getDealDiscoveryByDealType(Context context, int deal_type) {
        List<ObjectDeal> resultObject = getDealDao(context).queryRaw(" WHERE deal_type = ?", deal_type + "");

        return resultObject;
    }


    public static void clearDealFollowing(Context context) {
        List<ObjectDeal> list = getDealsFollowing(context);
        for (ObjectDeal deal : list) {
            getDealDao(context).deleteByKey(deal.getId());
        }
    }

    public static List<ObjectDeal> getDealsFollowing(Context context) {
        List<ObjectDeal> tmpObject = getDealDao(context).loadAll();
        List<ObjectDeal> resultObject = new ArrayList();
        if (tmpObject != null) {

            for (ObjectDeal objectDeal : tmpObject) {
                if (objectDeal.getDealReferId() != null && objectDeal.getDealReferId() == -1 &&objectDeal.getFromDealFollowing()&&objectDeal.getF_liked()) {
                    resultObject.add(objectDeal);
                }
            }

        }

        return resultObject;
    }

    public static List<ObjectDeal> getDealByMerchantId(Context context, long merchantId) {
        List<ObjectDeal> resultObject = getDealDao(context).queryRaw(" WHERE ref_merchant_id = ?", merchantId + "");

        return resultObject;
    }

    public static void updateObjectIdFliked(Context context, long merchantId, boolean fliked) {
        List<ObjectDeal> resultObject = getDealDao(context).queryRaw(" WHERE merchant_id = ?", merchantId + "");
        for (ObjectDeal objectDeal : resultObject) {
            objectDeal.setF_liked(fliked);
            DealController.update(context, objectDeal);
        }
    }

    public static void clearDealByMerchantRef(Context context, long merchantId) {
        List<ObjectDeal> resultObject = getDealByMerchantId(context, merchantId);
        for (ObjectDeal objectDeal : resultObject) {
            getDealDao(context).delete(objectDeal);
        }
    }

    public static void clearDealByMerchantIdAndDealType(Context context, String merchantId,int dealType) {
        List<ObjectDeal> resultObject = getDealDao(context).queryRaw(" WHERE merchant_id = ? and deal_type = ? ", merchantId ,dealType+"");
        for (ObjectDeal objectDeal : resultObject) {
            getDealDao(context).delete(objectDeal);
        }
    }

    public static void clearDealByDealType(Context context, int deal_type) {
        List<ObjectDeal> list = getDealByDealType(context, deal_type);
        for (ObjectDeal objectDeal : list) {
            getDealDao(context).delete(objectDeal);
        }
    }

    public static void clearDealDiscoveryByDealType(Context context, int deal_type) {
        List<ObjectDeal> list = getDealDiscoveryByDealType(context, deal_type);
        for (ObjectDeal objectDeal : list) {
            getDealDao(context).delete(objectDeal);
        }
    }

    public static List<ObjectDeal> getDealThatALive(Context context) {

        List<ObjectDeal> returnDealList = new ArrayList<>();
        List<ObjectDeal> dealList = getAllDeals(context);
        long current_time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (ObjectDeal objectDeal : dealList) {

            try {
                Date startDate = sdf.parse(objectDeal.getEnd_at());
                long endTime = startDate.getTime();
                if (endTime > current_time) {
                    returnDealList.add(objectDeal);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        dealList = null;

        return returnDealList;
    }

    public static void clearByDealId(Context context, Long DealId) {
        ObjectDeal deal = getDealById(context, DealId);
        if (deal != null) {
            getDealDao(context).delete(deal);
        }
    }

    public static void clearAllDeals(Context context) {
        getDealDao(context).deleteAll();
    }
}
