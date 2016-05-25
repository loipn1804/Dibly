package com.dibs.dibly.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.SearchActivity;
import com.dibs.dibly.base.BaseActivity;

import java.util.List;

/**
 * Created by USER on 7/14/2015.
 */
public class SuggestLocationAdapter extends BaseAdapter {

    private List<SearchActivity.PlaceAutocomplete> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;


    public SuggestLocationAdapter(Activity activity, List<SearchActivity.PlaceAutocomplete> listData) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    public void setData(List<SearchActivity.PlaceAutocomplete> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.row_suggest_location, null);
            holder = new ViewHolder();

            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.txtStreetName = (TextView) convertView.findViewById(R.id.txtStreetName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ((BaseActivity) activity).overrideFontsLight(holder.root);

        /*holder.root.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });*/


        holder.txtStreetName.setText(listData.get(position).description);


        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        TextView txtStreetName;
    }
}
