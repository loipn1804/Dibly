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
import com.dibs.dibly.activity.MerchantDealActivity;

import java.util.ArrayList;
import java.util.List;

import greendao.DealClaimed;

/**
 * Created by USER on 7/14/2015.
 */
public class PurchaseDealClaimedAdapter extends BaseAdapter {

    private List<DealClaimed> listDealClaimed;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Typeface typeface;

    public PurchaseDealClaimedAdapter(Activity activity, List<DealClaimed> listDealClaimed) {
        this.listDealClaimed = new ArrayList<>();
        this.listDealClaimed.addAll(listDealClaimed);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNova-Light.otf");
    }

    public void setListData(List<DealClaimed> listDealClaimed) {
        this.listDealClaimed.clear();
        this.listDealClaimed.addAll(listDealClaimed);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = (listDealClaimed == null) ? 0 : listDealClaimed.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listDealClaimed.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_purchase_deal_claimed, null);
            holder = new ViewHolder();

            holder.lnlRoot = (LinearLayout) convertView.findViewById(R.id.lnlRoot);
            holder.txtNum = (TextView) convertView.findViewById(R.id.txtNum);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.txtMerchant = (TextView) convertView.findViewById(R.id.txtMerchant);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtInfo = (TextView) convertView.findViewById(R.id.txtInfo);

            holder.txtNum.setTypeface(typeface);
            holder.txtTitle.setTypeface(typeface);
            holder.txtMerchant.setTypeface(typeface);
            holder.txtDate.setTypeface(typeface);
            holder.txtInfo.setTypeface(typeface);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.lnlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDealDetail = new Intent(activity, DealDetailActivity.class);
                intentDealDetail.putExtra("deal_id", listDealClaimed.get(position).getDeal_id());
                intentDealDetail.putExtra("merchant_id", listDealClaimed.get(position).getMerchant_id());
//                intentDealDetail.putExtra("is_load_all", "yes");
                activity.startActivity(intentDealDetail);
            }
        });

        holder.txtMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMerchant = new Intent(activity, MerchantDealActivity.class);
                intentMerchant.putExtra("id", listDealClaimed.get(position).getMerchant_id());
                intentMerchant.putExtra("name", listDealClaimed.get(position).getOrganization_name());
                activity.startActivity(intentMerchant);
            }
        });

        holder.txtNum.setText((position + 1) + "");
        holder.txtTitle.setText(listDealClaimed.get(position).getTitle());
        holder.txtMerchant.setText(listDealClaimed.get(position).getOrganization_name());

        if (listDealClaimed.get(position).getF_claimed()) {
            holder.txtInfo.setText("Claimed on:");
            holder.txtDate.setText(changeDateFormat(listDealClaimed.get(position).getConsumed_at()));
        } else {
            holder.txtInfo.setText("Not claimed, expired on:");
            holder.txtDate.setText(changeDateFormat(listDealClaimed.get(position).getValidity()));
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRoot;
        TextView txtNum;
        TextView txtTitle;
        TextView txtMerchant;
        TextView txtDate;
        TextView txtInfo;
    }

    private String changeDateFormat(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        return day + "/" + month + "/" + year;
    }
}
