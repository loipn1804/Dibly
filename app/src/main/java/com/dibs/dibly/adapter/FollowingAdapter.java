package com.dibs.dibly.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.DetailMerchantActivity;
import com.dibs.dibly.activity.MerchantDealActivity;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;

import java.util.ArrayList;
import java.util.List;

import greendao.Following;
import greendao.ObjectUser;

/**
 * Created by USER on 7/3/2015.
 */
public class FollowingAdapter extends BaseAdapter {

    private List<Following> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    private Typeface typefaceIt;
    private Typeface typeface;

    public FollowingAdapter(Activity activity, List<Following> list) {
        this.listData = new ArrayList<>();
        this.listData.addAll(list);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        typefaceIt = StaticFunction.light_it(activity);
        typeface = StaticFunction.light(activity);
    }

    public void setData(List<Following> list) {
        this.listData.clear();
        this.listData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = (listData == null) ? 0 : listData.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_following_merchant, null);
            holder = new ViewHolder();

            holder.lnlRoot = (LinearLayout) convertView.findViewById(R.id.lnlRoot);
            holder.txtNo = (TextView) convertView.findViewById(R.id.txtNo);
            holder.txtMerchantName = (TextView) convertView.findViewById(R.id.txtMerchantName);
            holder.txtNumOfNewDeals = (TextView) convertView.findViewById(R.id.txtNumberOfNewVoucher);
//            holder.btnStopFollow = (TextView) convertView.findViewById(R.id.btnStopFollow);
            holder.rltDealDetail = (RelativeLayout) convertView.findViewById(R.id.rltDealDetail);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtNo.setTypeface(typeface);
        holder.txtMerchantName.setTypeface(typeface);
        holder.txtNumOfNewDeals.setTypeface(typefaceIt);

        holder.lnlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentMerchant = new Intent(activity, MerchantDealActivity.class);
//                intentMerchant.putExtra("id", listData.get(position).getMerchant_id());
//                intentMerchant.putExtra("name", listData.get(position).getMerchant_name());
//                activity.startActivity(intentMerchant);

                Intent intentMerchant = new Intent(activity, DetailMerchantActivity.class);
                intentMerchant.putExtra("deal_id", listData.get(position).getMerchant_id());
                intentMerchant.putExtra("merchant_id", listData.get(position).getMerchant_id());
                intentMerchant.putExtra("isDealDetail", false);
                activity.startActivity(intentMerchant);
            }
        });

        holder.rltDealDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMerchant = new Intent(activity, MerchantDealActivity.class);
                intentMerchant.putExtra("id", listData.get(position).getMerchant_id());
                intentMerchant.putExtra("name", listData.get(position).getMerchant_name());
                activity.startActivity(intentMerchant);
            }
        });

        final Following following = listData.get(position);
        String strNo = (position + 1) + ".";
        holder.txtNo.setText(strNo);
        holder.txtMerchantName.setText(following.getMerchant_name());
        String strNoOfNewDeals;
        if (following.getNum_of_new_deals() == 1) {
            strNoOfNewDeals = following.getNum_of_new_deals() + " live deal";
        } else {
            strNoOfNewDeals = following.getNum_of_new_deals() + " live deals";
        }
        holder.txtNumOfNewDeals.setText(strNoOfNewDeals);

//        holder.btnStopFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupConfirmStopFollow(following);
//            }
//        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRoot;
        TextView txtNo;
        TextView txtMerchantName;
        TextView txtNumOfNewDeals;
        //        TextView btnStopFollow;
        RelativeLayout rltDealDetail;
    }

    public void showPopupConfirmStopFollow(final Following following) {
        final Dialog dialog = new Dialog(activity);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);

        ((BaseActivity) activity).overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMessage.setText("Do you wish to stop following this merchant:\n" + following.getMerchant_name() + "?");

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UserController.isLogin(activity)) {
                    ObjectUser user = UserController.getCurrentUser(activity);
                    Intent intentGetComment = new Intent(activity, RealTimeService.class);
                    intentGetComment.setAction(RealTimeService.ACTION_STOP_FOLLOWING_MERCHANT);
                    intentGetComment.putExtra(RealTimeService.EXTRA_CONSUMER_ID, user.getConsumer_id() + "");
                    intentGetComment.putExtra(RealTimeService.EXTRA_MERCHANT_ID, following.getMerchant_id() + "");
                    intentGetComment.putExtra(RealTimeService.EXTRA_FOLLOWING_ID, following.getId() + "");
                    activity.startService(intentGetComment);
                } else {
//                    Intent intentGetComment = new Intent(activity, RealTimeService.class);
//                    intentGetComment.setAction(RealTimeService.ACTION_STOP_FOLLOWING_MERCHANT);
//                    intentGetComment.putExtra(RealTimeService.EXTRA_CONSUMER_ID, 18 + "");
//                    intentGetComment.putExtra(RealTimeService.EXTRA_MERCHANT_ID, following.getMerchant_id() + "");
//                    intentGetComment.putExtra(RealTimeService.EXTRA_FOLLOWING_ID, following.getId() + "");
//                    activity.startService(intentGetComment);
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
}
