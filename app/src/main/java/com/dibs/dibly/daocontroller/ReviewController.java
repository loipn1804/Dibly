package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.Category;
import greendao.CategoryDao;
import greendao.Outlet;
import greendao.Review;
import greendao.ReviewDao;


/**
 * Created by VuPhan on 4/9/16.
 */
public class ReviewController {

    private static ReviewDao getReviewDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getReviewDao();
    }

    public static void insert(Context context, Review review) {
        getReviewDao(context).insert(review);
    }

    public static List<Review> getAll(Context ctx) {
        return getReviewDao(ctx).loadAll();
    }

    public static List<Review> getReviewByMerchantId(Context ctx, String merchantID) {
        List<Review> reviewList = getReviewDao(ctx).queryRaw("WHERE merchant_id = ?", merchantID);
        return reviewList;
    }

    public static void clearReviewByMerchantId(Context ctx, String merchantID) {
        List<Review> reviewList = getReviewByMerchantId(ctx,merchantID);
        for(Review review:reviewList){
            getReviewDao(ctx).deleteByKey(review.getId());
        }

    }

    public static void deleteAll(Context context) {
        getReviewDao(context).deleteAll();
    }

}
