package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;
import com.dibs.dibly.consts.Const;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import greendao.Merchant;
import greendao.MerchantList;
import greendao.MerchantListDao;

/**
 * Created by USER on 10/19/2015.
 */
public class MerchantListController {

    private static MerchantListDao getMerchantListDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getMerchantListDao();
    }

    public static void insert(Context context, MerchantList merchant) {
        getMerchantListDao(context).insert(merchant);
    }

    public static void update(Context context, MerchantList merchant) {
        getMerchantListDao(context).update(merchant);
    }

    public static void updateFFollowing(Context context, long merchantId,boolean f_follow) {
        List<MerchantList> merchantLists = getMerchantListDao(context).queryRaw(" WHERE merchant_id = ? ", merchantId + "");
        for(MerchantList merchantList:merchantLists){
            merchantList.setF_follow(f_follow);
            getMerchantListDao(context).update(merchantList);
        }

    }



    public static List<MerchantList> getBySearchName(Context context, String name) {
        return getMerchantListDao(context).queryRaw(" WHERE organization_name = ? ", "%" + name + "%");
    }



    public static void clearByMerchantId(Context context, Long id,int type) {
       List<MerchantList> lists = getMerchantListDao(context).queryRaw(" WHERE merchant_id = ? and merchant_type = ? ", "" + id ,type+"");
       for(MerchantList merchantList:lists){
           getMerchantListDao(context).delete(merchantList);
       }
    }

    public static List<MerchantList> getAll(Context context, int order) {
        List<MerchantList> list = getMerchantListDao(context).queryRaw(" WHERE merchant_type = ?", Const.MERCHANT_LIST_TYPE.ALL_MERCHANT+"");
        if (order == 0) {
            Collections.sort(list, new Comparator<MerchantList>() {
                @Override
                public int compare(MerchantList object1, MerchantList object2) {
                    return object1.getOrganization_name().toLowerCase().compareTo(object2.getOrganization_name().toLowerCase());
                }
            });
        } else {
            Collections.sort(list, new Comparator<MerchantList>() {
                @Override
                public int compare(MerchantList object1, MerchantList object2) {
                    return object2.getOrganization_name().toLowerCase().compareTo(object1.getOrganization_name().toLowerCase());
                }
            });
        }

        return list;
    }

    public static List<MerchantList> getAllAndFollowed(Context context, int order) {
        List<MerchantList> list = getMerchantListDao(context).queryRaw(" WHERE merchant_type = ?", Const.MERCHANT_LIST_TYPE.FOLLOWING+"");
        if (order == 0) {
            Collections.sort(list, new Comparator<MerchantList>() {
                @Override
                public int compare(MerchantList object1, MerchantList object2) {
                    return object1.getOrganization_name().toLowerCase().compareTo(object2.getOrganization_name().toLowerCase());
                }
            });
        } else {
            Collections.sort(list, new Comparator<MerchantList>() {
                @Override
                public int compare(MerchantList object1, MerchantList object2) {
                    return object2.getOrganization_name().toLowerCase().compareTo(object1.getOrganization_name().toLowerCase());
                }
            });
        }

        List<MerchantList> lisTmp = new ArrayList<>();

        for(MerchantList merchantList:list){
            if(merchantList.getF_follow()){
                lisTmp.add(merchantList);
            }
        }

        return lisTmp;
    }

    public static void clearAllByType(Context context,int type) {
        List<MerchantList> lists = getMerchantListDao(context).queryRaw(" WHERE merchant_type = ?", type+"");
        for(MerchantList merchantList:lists){
            getMerchantListDao(context).delete(merchantList);
        }
    }

    public static void clearAll(Context context) {
        getMerchantListDao(context).deleteAll();
    }
}
