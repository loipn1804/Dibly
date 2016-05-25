package com.dibs.dibly.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.DealDetailMerchantActivity;
import com.dibs.dibly.activity.DetailMerchantActivity;
import com.dibs.dibly.interfaceUtils.OnPopUpFollowingListener;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.dibs.dibly.utils.HandleFollowingMerchant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import greendao.MerchantList;

/**
 * Created by USER on 10/19/2015.
 */
public class MerchantListAdapter extends BaseAdapter {

    private List<MerchantList> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    private Typeface typefaceBold;
    private Typeface typeface;

    private DisplayImageOptions optionsCover;
    private DisplayImageOptions optionsLogo;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private OnPopUpFollowingListener listener;

    public MerchantListAdapter(Activity activity, List<MerchantList> list) {
        this.listData = new ArrayList<>();
        this.listData.addAll(list);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        typefaceBold = StaticFunction.bold(activity);
        typeface = StaticFunction.light(activity);

        optionsCover = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.merchant_cover_sm)
                .showImageOnLoading(R.drawable.merchant_cover_sm)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();
        optionsLogo = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.menu_merchant)
                .showImageOnLoading(R.drawable.menu_merchant)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();
    }

    public void setOnPopupFollowingListener(OnPopUpFollowingListener listener) {
        this.listener = listener;
    }

    public void setData(List<MerchantList> list) {
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
            convertView = this.layoutInflater.inflate(R.layout.row_merchant_list, null);
            holder = new ViewHolder();

            holder.root = (RelativeLayout) convertView.findViewById(R.id.root);
            holder.txtMerchantName = (TextView) convertView.findViewById(R.id.txtMerchantName);
            holder.txtNumOfNewDeals = (TextView) convertView.findViewById(R.id.txtNumberOfNewVoucher);
            holder.imvCover = (ImageView) convertView.findViewById(R.id.imvCover);
            holder.imvLogo = (ImageView) convertView.findViewById(R.id.imvLogo);
            holder.imgFollow = (ImageView) convertView.findViewById(R.id.imvFollow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtMerchantName.setTypeface(typefaceBold);
        holder.txtNumOfNewDeals.setTypeface(typeface);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMerchant = new Intent(activity, DealDetailMerchantActivity.class);
                intentMerchant.putExtra("merchant_id", listData.get(position).getMerchant_id());
                intentMerchant.putExtra("isDealDetail", false);
                activity.startActivity(intentMerchant);
            }
        });

        holder.txtMerchantName.setText(listData.get(position).getOrganization_name());
        int live_deal = listData.get(position).getK_live_deals();
        if (live_deal == 1) {
            holder.txtNumOfNewDeals.setText("1 live deal");
        } else {
            holder.txtNumOfNewDeals.setText(live_deal + " live deals");
        }
        imageLoader.displayImage(listData.get(position).getCover_image(), holder.imvCover, optionsCover);
        imageLoader.displayImage(listData.get(position).getLogo_image(), holder.imvLogo, optionsLogo);

        if(listData.get(position).getF_follow()){
            holder.imgFollow.setImageResource(R.drawable.ic_heart_active);
        }else{
            holder.imgFollow.setImageResource(R.drawable.ic_heart_inactive);
        }

        holder.imgFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandleFollowingMerchant.showPopupConfirmToFollowing(activity, listData.get(position).getF_follow(), listData.get(position).getOrganization_name(), listData.get(position).getMerchant_id(), listener);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        RelativeLayout root;
        TextView txtMerchantName;
        TextView txtNumOfNewDeals;
        ImageView imvCover;
        ImageView imvLogo,imgFollow;
    }
}
