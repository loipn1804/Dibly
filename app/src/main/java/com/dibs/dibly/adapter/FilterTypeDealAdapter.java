package com.dibs.dibly.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dibs.dibly.R;

import java.util.ArrayList;
import java.util.List;

import greendao.Category;
import greendao.TypeDeal;

/**
 * Created by USER on 7/16/2015.
 */
public class FilterTypeDealAdapter extends BaseAdapter {

    private List<TypeDeal> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;


    private Typeface typeface;

    public FilterTypeDealAdapter(Activity activity, List<TypeDeal> data) {
        this.listData = new ArrayList<>();
        this.listData.addAll(data);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;

        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNova-Light.otf");
    }

    public void setDataSource(List<TypeDeal> data) {
        this.listData.clear();
        this.listData.addAll(data);
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
            convertView = this.layoutInflater.inflate(R.layout.row_filter, null);
            holder = new ViewHolder();

            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.imgCheck = (ImageView) convertView.findViewById(R.id.imgCheck);
            holder.divider = convertView.findViewById(R.id.divider);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(listData.get(position).getIsSelect()){
            holder.imgCheck.setImageResource(R.drawable.ic_filteractive);
        }else{
            holder.imgCheck.setImageResource(R.drawable.ic_filterinactive);
        }

        holder.txtName.setText(listData.get(position).getText());

        if(position == listData.size()-1){
            holder.divider.setVisibility(View.GONE);
        }else {
            holder.divider.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


    public class ViewHolder {

        TextView txtName;
        ImageView imgCheck;
        View divider;

    }
}
