package com.dibs.dibly.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.activity.DealDetailV2Activity;
import com.dibs.dibly.activity.OutletDealActivity;
import com.dibs.dibly.consts.TimerDealObj;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.view.CountDownTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import greendao.MyLocation;
import greendao.ObjectDeal;
import greendao.Outlet;

/**
 * Created by USER on 6/30/2015.
 */
public class DealHomeAdapter extends BaseAdapter {

    private List<ObjectDeal> listDeal;
    private List<TimerDealObj> listTime;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private Typeface typeface,typefaceBold;

    public DealHomeAdapter(Activity activity, List<ObjectDeal> listDeal) {
        this.listDeal = new ArrayList<>();
        this.listDeal.addAll(listDeal);
        this.listTime = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        options = new DisplayImageOptions.Builder().cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).showImageForEmptyUri(R.color.white).showImageOnLoading(R.color.white).cacheOnDisk(true).build();
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNova-Light.otf");
        typefaceBold = Typeface.createFromAsset(activity.getAssets(),"fonts/ProximaNova-Bold.otf");
        calculateTime();

    }

    public void myNotifyDataSetChanged(List<ObjectDeal> listDeal) {
        this.listDeal.clear();
        this.listDeal.addAll(listDeal);
        calculateTime();
        notifyDataSetChanged();
    }

    private void calculateTime() {
        listTime.clear();
        for (ObjectDeal deal : listDeal) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date startDate = sdf.parse(deal.getStart_at());
                long startInMilliseconds = startDate.getTime();
                Date endDate = sdf.parse(deal.getEnd_at());
                long endInMilliseconds = endDate.getTime();
                long current_time = System.currentTimeMillis();
                long timeTotal = endInMilliseconds - startInMilliseconds;
                long millisUntilFinished = endInMilliseconds - current_time;
                long percent = 0;
                if (timeTotal == 0) {
                    percent = 100;
                } else {
                    percent = ((timeTotal - millisUntilFinished) * 100) / timeTotal;
                }

                long remainTime = endInMilliseconds - current_time;
                int day = (int) (remainTime / (1000 * 3600 * 24));
                long remainHours = remainTime - (day * 1000 * 3600 * 24);

                String distance = "";
                final MyLocation mLoc = MyLocationController.getLastLocation(activity);
                if (mLoc != null) {
                    Outlet outlet = OutletController.getOutletById(activity, deal.getOutlet_id());
                    if (outlet != null) {
                        float result[] = new float[3];
                        Location.distanceBetween(mLoc.getLatitude(), mLoc.getLongitude(), Double.parseDouble(outlet.getLatitude()), Double.parseDouble(outlet.getLongitude()), result);
                        int dis = (int) result[0];
                        if (dis < 1000) {
                            distance = dis + "m away";
                        } else {
                            float f_dis = (float) dis / 1000;
                            BigDecimal big = new BigDecimal(f_dis).setScale(2, BigDecimal.ROUND_HALF_UP);
                            distance = big + "km away";
                        }
                    } else {
                        distance = "Not Available";
                    }
                } else {
                    distance = "Not Available";
                }

                TimerDealObj timerDealObj = new TimerDealObj((int) percent, day, remainTime, remainHours, distance, endInMilliseconds);
                listTime.add(timerDealObj);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getCount() {
        int count = (listDeal == null) ? 0 : listDeal.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listDeal.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_dealv2, null);
            holder = new ViewHolder();

            holder.lnlRoot = (LinearLayout) convertView.findViewById(R.id.lnlRoot);
            holder.txtDesc_1 = (TextView) convertView.findViewById(R.id.txtDesc_1);
            holder.txtDesc_2 = (TextView) convertView.findViewById(R.id.txtDesc_2);
            holder.txtDistance = (TextView) convertView.findViewById(R.id.txtDistance);
            holder.txtCountdownTimer = (CountDownTextView) convertView.findViewById(R.id.txtCountdownTimer);
            holder.imvDeal = (ImageView) convertView.findViewById(R.id.imvDeal);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.lnlExclusive = (LinearLayout) convertView.findViewById(R.id.lnlExclusive);
            holder.txtExclusive = (TextView) convertView.findViewById(R.id.txtExclusive);
            holder.txtDays = (TextView) convertView.findViewById(R.id.txtDays);
            holder.lnlMoreDeal = (LinearLayout) convertView.findViewById(R.id.lnlMoreDeal);
            holder.txtMoreDeal = (TextView) convertView.findViewById(R.id.txtMoreDeal);
            holder.foot = convertView.findViewById(R.id.foot);


            holder.txtDesc_1.setTypeface(typeface);
            holder.txtDesc_2.setTypeface(typefaceBold);
            holder.txtDistance.setTypeface(typeface);
            holder.txtCountdownTimer.setTypeface(typeface);
            holder.txtStatus.setTypeface(typeface);
            holder.txtExclusive.setTypeface(typeface);
            holder.txtDays.setTypeface(typeface);
            holder.txtMoreDeal.setTypeface(typeface);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.lnlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDealDetail = new Intent(activity, DealDetailV2Activity.class);
                intentDealDetail.putExtra("deal_id", listDeal.get(position).getDeal_id());
                intentDealDetail.putExtra("merchant_id", listDeal.get(position).getMerchant_id());
                activity.startActivity(intentDealDetail);
            }
        });

        imageLoader.displayImage(listDeal.get(position).getImage(), holder.imvDeal, options);

        holder.txtDesc_1.setText(listDeal.get(position).getTitle());
        Outlet outlet = OutletController.getOutletById(activity, listDeal.get(position).getOutlet_id());
        String txtDesc_2 = "";
        if (outlet != null) {
            txtDesc_2 = outlet.getName();
            holder.txtDesc_2.setText(txtDesc_2);
        } else {
            txtDesc_2 = listDeal.get(position).getOrganization_name();
            holder.txtDesc_2.setText(txtDesc_2);
        }

        holder.txtDistance.setText(listTime.get(position).getDistance());

        if (listDeal.get(position).getF_claimed()) {
            holder.txtStatus.setVisibility(View.VISIBLE);
            holder.txtStatus.setText("Claimed");
        } else if (listDeal.get(position).getF_call_dibs()) {
            holder.txtStatus.setVisibility(View.VISIBLE);
            holder.txtStatus.setText("Dibs called");
        } else {
            holder.txtStatus.setVisibility(View.GONE);
            holder.txtStatus.setText("");
        }

        if (listDeal.get(position).getIs_exclusive()) {
            holder.lnlExclusive.setVisibility(View.VISIBLE);
        } else {
            holder.lnlExclusive.setVisibility(View.GONE);
        }

        holder.txtCountdownTimer.setTimeCount(listTime.get(position).getEndInMilliseconds(), false);

        int days = listTime.get(position).getDay();
        if (days > 0) {
            if (days == 1) {
                holder.txtDays.setText("1 day");
            } else {
                holder.txtDays.setText(days + " days");
            }
            holder.txtDays.setVisibility(View.VISIBLE);
        } else {
            holder.txtDays.setVisibility(View.GONE);
        }

        if (listDeal.get(position).getK_deals_by_outlet() != null) {
            int deals = listDeal.get(position).getRefer_deal_infos().size(); //listDeal.get(position).getK_deals_by_outlet();
            if (deals > 1) {
                holder.txtMoreDeal.setText(deals + " more deals from this outlet");
                holder.lnlMoreDeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentMoreDeal = new Intent(activity, OutletDealActivity.class);
                        intentMoreDeal.putExtra("ids", listDeal.get(position).getRefer_deal_ids());
                        intentMoreDeal.putExtra("outlet_id", listDeal.get(position).getOutlet_id() + "");
                        Outlet outlet = OutletController.getOutletById(activity, listDeal.get(position).getOutlet_id());
                        String txtDesc_2 = "";
                        if (outlet != null) {
                            txtDesc_2 = outlet.getName();
                        } else {
                            txtDesc_2 = listDeal.get(position).getOrganization_name();
                        }
                        intentMoreDeal.putExtra("name", txtDesc_2);
                        activity.startActivity(intentMoreDeal);
                    }
                });
                holder.lnlMoreDeal.setVisibility(View.VISIBLE);
            } else {
                holder.lnlMoreDeal.setVisibility(View.GONE);
            }
        } else {
            holder.lnlMoreDeal.setVisibility(View.GONE);
        }

        if (!(position + 1 == getCount())) {
            holder.foot.setVisibility(View.GONE);
        } else {
            holder.foot.setVisibility(View.VISIBLE);
        }



        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRoot;
        TextView txtDesc_1;
        TextView txtDesc_2;
        TextView txtDistance;
        TextView txtStatus;
        CountDownTextView txtCountdownTimer;
        ImageView imvDeal;
        LinearLayout lnlExclusive;
        TextView txtExclusive;
        TextView txtDays;
        LinearLayout lnlMoreDeal;
        TextView txtMoreDeal;
        View foot;
        View head;
    }

}
