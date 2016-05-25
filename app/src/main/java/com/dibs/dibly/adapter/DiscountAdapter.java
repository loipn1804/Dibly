package com.dibs.dibly.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.staticfunction.StaticFunction;

import java.util.ArrayList;
import java.util.List;

import greendao.Discount;

/**
 * Created by USER on 10/22/2015.
 */
public class DiscountAdapter extends BaseAdapter {

    private List<Discount> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    private Typeface typeface;

    public DiscountAdapter(Activity activity, List<Discount> list) {
        this.listData = new ArrayList<>();
        this.listData.addAll(list);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        typeface = StaticFunction.light(activity);
    }

    public void setData(List<Discount> list) {
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
            convertView = this.layoutInflater.inflate(R.layout.row_discount, null);
            holder = new ViewHolder();

            holder.txtNum = (TextView) convertView.findViewById(R.id.txtNum);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtNum.setTypeface(typeface);
        holder.txtName.setTypeface(typeface);

        holder.txtNum.setText((position + 1) + "");
        holder.txtName.setText(listData.get(position).getNote());

        return convertView;
    }

    public class ViewHolder {
        TextView txtNum;
        TextView txtName;
    }
}
