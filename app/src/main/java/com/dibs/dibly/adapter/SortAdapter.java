package com.dibs.dibly.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;

import java.util.List;

/**
 * Created by USER on 8/18/2015.
 */
public class SortAdapter extends BaseAdapter {

    private List<String> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private String sort;

    public SortAdapter(Activity activity, List<String> listData, String sort) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.sort = sort;
    }

    public void setData(List<String> listData) {
        this.listData = listData;
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
            convertView = this.layoutInflater.inflate(R.layout.row_sort, null);
            holder = new ViewHolder();

            holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ((BaseActivity)activity).overrideFontsLight(holder.txt1);

        holder.txt1.setText(listData.get(position));
        if (listData.get(position).equalsIgnoreCase(sort)) {
            holder.txt1.setBackgroundColor(activity.getResources().getColor(R.color.gray_bg_call_dibs));
        } else {
            holder.txt1.setBackgroundColor(activity.getResources().getColor(R.color.white));
        }

        return convertView;
    }

    public class ViewHolder {
        TextView txt1;
    }
}
