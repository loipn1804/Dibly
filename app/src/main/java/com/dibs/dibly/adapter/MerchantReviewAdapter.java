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
import android.widget.ImageView;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import greendao.Following;
import greendao.ObjectUser;
import greendao.Review;

/**
 * Created by VuPhan on 4/22/16.
 */
public class MerchantReviewAdapter extends BaseAdapter {
    private List<Review> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    private Typeface typefaceIt;
    private Typeface typeface;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options, options1;


    public MerchantReviewAdapter(Activity activity, List<Review> list) {
        this.listData = new ArrayList<>();
        this.listData.addAll(list);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        typefaceIt = StaticFunction.light_it(activity);
        typeface = StaticFunction.light(activity);

        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(activity.getResources().getColor(R.color.transparent))
                .showImageOnLoading(activity.getResources().getColor(R.color.transparent))
                .cacheOnDisk(true).build();
    }

    public void setData(List<Review> list) {
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
            convertView = this.layoutInflater.inflate(R.layout.row_review_v2, null);
            holder = new ViewHolder();


            holder.imvAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);
            holder.txtUsername = (TextView) convertView.findViewById(R.id.txtUserName);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtReView = (TextView) convertView.findViewById(R.id.txtReView);
            holder.txtReadmore = (TextView) convertView.findViewById(R.id.txtReadmore);
            holder.lnlYayOrNay = (LinearLayout) convertView.findViewById(R.id.lnlYayOrNay);
            holder.imgYayOrNay = (ImageView) convertView.findViewById(R.id.imgYayOrNay);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtUsername.setTypeface(typeface);
        holder.txtDate.setTypeface(typeface);
        holder.txtReView.setTypeface(typeface);
        holder.txtReadmore.setTypeface(typeface);
        holder.txtReView.setMaxLines(Integer.MAX_VALUE);
        holder.txtReView.setEllipsize(null);

        boolean isYay = listData.get(position).getIs_yay();

        if (listData.get(position).getProfile_image() != null && listData.get(position).getProfile_image().length() > 0 && !listData.get(position).getProfile_image().equals("null")) {
            imageLoader.displayImage(listData.get(position).getProfile_image(), holder.imvAvatar, options);
        } else {
            if (isYay) {
                holder.imvAvatar.setImageResource(R.drawable.ic_avatar_blue);
            } else {
                holder.imvAvatar.setImageResource(R.drawable.ic_avatar_red);
            }
        }

        holder.txtDate.setText(listData.get(position).getCreated_at());


        if (isYay) {
            holder.imgYayOrNay.setImageResource(R.drawable.ic_yay_white);
            holder.lnlYayOrNay.setBackgroundResource(R.drawable.btn_yay_green);
        } else {
            holder.imgYayOrNay.setImageResource(R.drawable.ic_nya_white);
            holder.lnlYayOrNay.setBackgroundResource(R.drawable.btn_nay_red);
        }

        holder.txtUsername.setText((listData.get(position).getFullname() != null && !listData.get(position).getFullname().equals("null")) ? listData.get(position).getFullname() : "");
        holder.txtReView.setText((listData.get(position).getReview() != null) ? listData.get(position).getReview() : "");


        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRoot;
        ImageView imvAvatar;
        TextView txtUsername;
        TextView txtDate;
        TextView txtReView;
        TextView txtReadmore;
        LinearLayout lnlYayOrNay;
        ImageView imgYayOrNay;
    }


}

