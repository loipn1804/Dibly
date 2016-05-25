package com.dibs.dibly.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.activity.EnterMerchantCodeActivity;
import com.dibs.dibly.activity.MerchantDealActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import greendao.DealAvailable;

/**
 * Created by USER on 7/3/2015.
 */
public class PurchaseDealAvailableAdapter extends BaseAdapter {

    private List<DealAvailable> listDealAvailable;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private boolean isAvailable;
    private Typeface typeface;
    private SimpleDateFormat sdf;
    private Date today;

    public PurchaseDealAvailableAdapter(Activity activity, List<DealAvailable> listDealAvailable, boolean isAvailable) {
        this.listDealAvailable = new ArrayList<>();
        this.listDealAvailable.addAll(listDealAvailable);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.isAvailable = isAvailable;
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNova-Light.otf");
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.today = new Date();
    }

    public void setListData(List<DealAvailable> listDealAvailable) {
        this.listDealAvailable.clear();
        this.listDealAvailable.addAll(listDealAvailable);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = (listDealAvailable == null) ? 0 : listDealAvailable.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listDealAvailable.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_purchase_deal_available, null);
            holder = new ViewHolder();

            holder.lnlRoot = (LinearLayout) convertView.findViewById(R.id.lnlRoot);
            holder.btnClaimNow = (TextView) convertView.findViewById(R.id.btnClaimNow);
            holder.txtNum = (TextView) convertView.findViewById(R.id.txtNum);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.txtMerchant = (TextView) convertView.findViewById(R.id.txtMerchant);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.txtInfo = (TextView) convertView.findViewById(R.id.txtInfo);

            holder.btnClaimNow.setTypeface(typeface);
            holder.txtNum.setTypeface(typeface);
            holder.txtTitle.setTypeface(typeface);
            holder.txtMerchant.setTypeface(typeface);
            holder.txtDate.setTypeface(typeface);
            holder.txtTime.setTypeface(typeface);
            holder.txtInfo.setTypeface(typeface);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.lnlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDealDetail = new Intent(activity, DealDetailActivity.class);
                intentDealDetail.putExtra("deal_id", listDealAvailable.get(position).getDeal_id());
                intentDealDetail.putExtra("merchant_id", listDealAvailable.get(position).getMerchant_id());

//                intentDealDetail.putExtra("is_load_all", "yes");
                activity.startActivity(intentDealDetail);
            }
        });

        holder.txtMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMerchant = new Intent(activity, MerchantDealActivity.class);
                intentMerchant.putExtra("id", listDealAvailable.get(position).getMerchant_id());
                intentMerchant.putExtra("name", listDealAvailable.get(position).getOrganization_name());
                activity.startActivity(intentMerchant);
            }
        });

        if (isAvailable) {
            holder.btnClaimNow.setVisibility(View.VISIBLE);
        } else {
            holder.btnClaimNow.setVisibility(View.GONE);
        }

        holder.txtNum.setText((position + 1) + "");
        holder.txtTitle.setText(listDealAvailable.get(position).getTitle());
        holder.txtMerchant.setText(listDealAvailable.get(position).getOrganization_name());
        holder.txtDate.setText(changeDateFormat(listDealAvailable.get(position).getValidity()));
        holder.txtTime.setText(listDealAvailable.get(position).getValidity().substring(11, 16));

        try {
            Date validityDate = sdf.parse(listDealAvailable.get(position).getValidity());
            if (today.after(validityDate)) {
                holder.btnClaimNow.setBackgroundResource(R.drawable.btn_red_sm);
                holder.btnClaimNow.setText("Expired");
                holder.btnClaimNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {
                holder.btnClaimNow.setBackgroundResource(R.drawable.btn_claim_now);
                holder.btnClaimNow.setText("Claim now");
                holder.btnClaimNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentQR = new Intent(activity, EnterMerchantCodeActivity.class);
                        intentQR.putExtra("deal_id", listDealAvailable.get(position).getDeal_id());
                        activity.startActivity(intentQR);
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRoot;
        TextView btnClaimNow;
        TextView txtNum;
        TextView txtTitle;
        TextView txtMerchant;
        TextView txtDate;
        TextView txtTime;
        TextView txtInfo;
    }

    private String changeDateFormat(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        return day + "/" + month + "/" + year;
    }
}
