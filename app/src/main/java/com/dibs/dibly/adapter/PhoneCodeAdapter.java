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
import greendao.PhoneCode;

/**
 * Created by VuPhan on 4/9/16.
 */
public class PhoneCodeAdapter extends BaseAdapter {

    private List<PhoneCode> listPhoneCode;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Typeface typeface;

    public PhoneCodeAdapter(Activity activity, List<PhoneCode> listPhoneCode) {
        this.listPhoneCode = new ArrayList<>();
        this.listPhoneCode.addAll(listPhoneCode);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    public void setListData(List<PhoneCode> listPhoneCode) {
        this.listPhoneCode.clear();
        this.listPhoneCode.addAll(listPhoneCode);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = (listPhoneCode == null) ? 0 : listPhoneCode.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listPhoneCode.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_phonecode, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtCode);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(listPhoneCode.get(position).getDial_code());
        holder.txtName.setText(listPhoneCode.get(position).getName());
        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRoot;
        TextView txtTitle, txtName;
    }


}
