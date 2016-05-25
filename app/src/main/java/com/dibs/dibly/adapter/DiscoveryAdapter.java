package com.dibs.dibly.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.CategoryActivity;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import greendao.Discovery;

/**
 * Created by USER on 7/3/2015.
 */
public class DiscoveryAdapter extends BaseAdapter {

    private List<Discovery> listDiscovery;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions options;

    private Typeface typefaceLight;

    public DiscoveryAdapter(Activity activity, List<Discovery> data) {
        this.listDiscovery = new ArrayList<>();
        this.listDiscovery.addAll(data);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;

        initImageLoaderOption();

        typefaceLight = StaticFunction.light(activity);
    }

    public void setDataSource(List<Discovery> data) {
        this.listDiscovery.clear();
        this.listDiscovery.addAll(data);
    }

    public void notifyDataChange() {
        notifyDataSetChanged();
    }

    public void initImageLoaderOption() {
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.logo).showImageOnFail(R.drawable.logo).cacheInMemory(true).cacheOnDisk(true).build();
    }

    @Override
    public int getCount() {
        int count = (listDiscovery == null) ? 0 : listDiscovery.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listDiscovery.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_discovery, null);
            holder = new ViewHolder();

            holder.lnlRoot = (LinearLayout) convertView.findViewById(R.id.lnlRoot);
//            holder.txtDiscover = (TextView) convertView.findViewById(R.id.txtDiscover);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.imageViewDiscovery = (ImageView) convertView.findViewById(R.id.imageDiscovery);
            holder.txtNumDeal = (TextView) convertView.findViewById(R.id.txtNumDeal);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Discovery discovery = listDiscovery.get(position);

        ((BaseActivity) activity).overrideFontsLight(holder.lnlRoot);

//        holder.txtDiscover.setTypeface(StaticFunction.light_it(activity));
        holder.txtName.setTypeface(typefaceLight);
        holder.txtNumDeal.setTypeface(typefaceLight);

        holder.txtName.setText(discovery.getName());
        int num = listDiscovery.get(position).getNum_deal();
        if (num == 1) {
            holder.txtNumDeal.setText(num + " deal");
        } else {
            holder.txtNumDeal.setText(num + " deals");
        }
        imageLoader.displayImage(discovery.getImageLink(), holder.imageViewDiscovery, options);

        holder.lnlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCategory = new Intent(activity, CategoryActivity.class);
                intentCategory.putExtra("id", listDiscovery.get(position).getId());
                intentCategory.putExtra("name", listDiscovery.get(position).getName());
                activity.startActivity(intentCategory);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRoot;
        //        TextView txtDiscover;
        TextView txtName;
        ImageView imageViewDiscovery;
        TextView txtNumDeal;
    }
}
