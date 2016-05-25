package com.dibs.dibly.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.interfaceUtils.OnPopUpFollowingListener;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;

import greendao.Merchant;
import greendao.ObjectDeal;

/**
 * Created by VuPhan on 4/18/16.
 */
public class HandleFollowingMerchant {


    public static void showPopupConfirmToFollowing(final Context context, final boolean isFollowing, String Organization_name, final long merchantId, final OnPopUpFollowingListener listener) {
        // custom dialog
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_following_merchant);

        StaticFunction.overrideFontsLight(context, dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        ImageView imgView = (ImageView) dialog.findViewById(R.id.imgFollowing);


        if (!isFollowing) {
            imgView.setImageResource(R.drawable.ic_following_merchant);
            txtMessage.setText(BaseActivity.getTitleText(context, "Follow ", Organization_name), TextView.BufferType.SPANNABLE);
        } else {
            imgView.setImageResource(R.drawable.ic_unfollowing_merchant);
            txtMessage.setText(BaseActivity.getTitleText(context, "Unfollow ", Organization_name), TextView.BufferType.SPANNABLE);
        }


        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                followingMerchant(context, isFollowing, merchantId);
                if (listener != null) {
                    listener.onActionStart(merchantId);
                }
                dialog.dismiss();
            }
        });

        lnlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private static void followingMerchant(Context context, boolean isFollowing, long merchantId) {
        if (UserController.isLogin(context)) {
            String consumer_id = UserController.getCurrentUser(context).getConsumer_id() + "";

            if (isFollowing) {
                Intent intentGetDealDetail = new Intent(context, RealTimeService.class);
                intentGetDealDetail.setAction(RealTimeService.ACTION_STOP_FOLLOWING_MERCHANT);
                intentGetDealDetail.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchantId + "");
                context.startService(intentGetDealDetail);

            } else {
                Intent intentGetDealDetail = new Intent(context, RealTimeService.class);
                intentGetDealDetail.setAction(RealTimeService.ACTION_FOLLOWING_MERCHANT);
                intentGetDealDetail.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchantId + "");
                context.startService(intentGetDealDetail);

            }

        } else {
            // showToastInfo(getString(R.string.login_first));
        }
    }
}
