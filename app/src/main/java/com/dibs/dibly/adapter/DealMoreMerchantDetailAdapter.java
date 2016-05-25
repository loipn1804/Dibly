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
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.interfaceUtils.OnPopUpFollowingListener;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.dibs.dibly.utils.HandleFollowingMerchant;
import com.dibs.dibly.view.CountDownTextView;
import com.lylc.widget.circularprogressbar.CircularProgressBar;
import com.lylc.widget.circularprogressbar.OnListenerFinish;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import greendao.Merchant;
import greendao.MyLocation;
import greendao.ObjectDeal;
import greendao.Outlet;

/**
 * Created by USER on 6/30/2015.
 */
public class DealMoreMerchantDetailAdapter extends BaseAdapter {

    private List<ObjectDeal> listDeal;
    private List<TimerDealObj> listTime;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private Typeface typeface, typefaceBold;
    private OnPopUpFollowingListener listener;

    public DealMoreMerchantDetailAdapter(Activity activity, List<ObjectDeal> listDeal) {
        this.listDeal = new ArrayList<>();
        this.listDeal.addAll(listDeal);
        this.listTime = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        options = new DisplayImageOptions.Builder().cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).
                bitmapConfig(Bitmap.Config.RGB_565).showImageForEmptyUri(R.drawable.merchant_cover_sm).
                showImageOnLoading(R.color.white).showImageOnFail(R.drawable.merchant_cover_sm).cacheOnDisk(true).build();
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNova-Light.otf");
        typefaceBold = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNova-Bold.otf");
        calculateTime();

    }

    public void setOnPopupFollowingListener(OnPopUpFollowingListener listener) {
        this.listener = listener;
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
                timerDealObj.setTotalTime(timeTotal);
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

            holder.imvDeal = (ImageView) convertView.findViewById(R.id.imvDeal);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.lnlExclusive = (LinearLayout) convertView.findViewById(R.id.lnlExclusive);
            holder.txtExclusive = (TextView) convertView.findViewById(R.id.txtExclusive);
            holder.imgFollowing = (ImageView) convertView.findViewById(R.id.imgFollowing);

            holder.foot = convertView.findViewById(R.id.foot);
            holder.circularProgressBar = (CircularProgressBar) convertView.findViewById(R.id.circularProgress);
            holder.txtDibs = (TextView) convertView.findViewById(R.id.txtDibs);
            holder.txtDesc_1.setTypeface(typeface);
            holder.txtDesc_2.setTypeface(typefaceBold);
            holder.txtDistance.setTypeface(typeface);

            holder.txtStatus.setTypeface(typeface);
            holder.txtExclusive.setTypeface(typeface);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.lnlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDealDetail = new Intent(activity, DealDetailV2Activity.class);
                intentDealDetail.putExtra("deal_id", listDeal.get(position).getDeal_id());
                intentDealDetail.putExtra("outletId", listDeal.get(position).getOutlet_id());
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
        holder.circularProgressBar.setMax(100);
        if (listDeal.get(position).getF_claimed()) {
            holder.txtStatus.setVisibility(View.GONE);
            holder.txtDibs.setVisibility(View.GONE);
            holder.txtStatus.setText("Claimed");
            holder.circularProgressBar.cancelTimer();
            holder.circularProgressBar.setTitle("Claimed");
            holder.circularProgressBar.setSubTitle("");
            holder.circularProgressBar.setProgress(0);
            holder.circularProgressBar.setTitleColor(activity.getResources().getColor(R.color.white));
            holder.circularProgressBar.setSubTitleColor(activity.getResources().getColor(R.color.white));
        } else if (listDeal.get(position).getF_call_dibs()) {
            holder.txtStatus.setVisibility(View.GONE);
            holder.txtStatus.setText("Dibs called");
            holder.txtDibs.setVisibility(View.VISIBLE);
            holder.circularProgressBar.setMax(100);
            holder.circularProgressBar.setTitleColor(activity.getResources().getColor(R.color.orange_main));
            holder.circularProgressBar.setSubTitleColor(activity.getResources().getColor(R.color.orange_main));
            holder.circularProgressBar.setTimerCountDown(listTime.get(position).getEndInMilliseconds(), listTime.get(position).getTotalTime());
            holder.circularProgressBar.setOnListenerFinish(new OnListenerFinish() {
                @Override
                public void onFinish() {
                    StaticFunction.sendBroad(activity, DealDetailActivity.RECEIVER_EXPRITED_TIME);
                }
            });
        } else {
            holder.txtStatus.setVisibility(View.GONE);
            holder.txtDibs.setVisibility(View.GONE);
            holder.txtStatus.setText("");
            holder.circularProgressBar.setMax(100);
            holder.circularProgressBar.setTitleColor(activity.getResources().getColor(R.color.orange_main));
            holder.circularProgressBar.setSubTitleColor(activity.getResources().getColor(R.color.orange_main));
            holder.circularProgressBar.setTimerCountDown(listTime.get(position).getEndInMilliseconds(), listTime.get(position).getTotalTime());
            holder.circularProgressBar.setOnListenerFinish(new OnListenerFinish() {
                @Override
                public void onFinish() {
                    StaticFunction.sendBroad(activity, DealDetailActivity.RECEIVER_EXPRITED_TIME);
                }
            });

        }

        if (listDeal.get(position).getIs_exclusive()) {
            holder.lnlExclusive.setVisibility(View.VISIBLE);
        } else {
            holder.lnlExclusive.setVisibility(View.GONE);
        }

        holder.imgFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Merchant merchant = MerchantController.getById(activity, listDeal.get(position).getMerchant_id());
                boolean isFollowing = (merchant != null) ? merchant.getF_follow() : listDeal.get(position).getF_liked();
                String Organization_name =
                        (merchant != null) ? merchant.getOrganization_name() : listDeal.get(position).getOrganization_name();
                long merchantId =
                        (merchant != null) ? merchant.getMerchant_id() : listDeal.get(position).getMerchant_id();
                HandleFollowingMerchant.showPopupConfirmToFollowing(activity, isFollowing,Organization_name,merchantId ,listener);
            }
        });



        Merchant merchant = MerchantController.getById(activity, listDeal.get(position).getMerchant_id());
        boolean isFollowing = (merchant != null) ? merchant.getF_follow() : listDeal.get(position).getF_liked();
        if (isFollowing) {
            holder.imgFollowing.setImageResource(R.drawable.ic_heart_active);
        } else {
            holder.imgFollowing.setImageResource(R.drawable.ic_heart_inactive);
        }




        if (!(position + 1 == getCount())) {
            holder.foot.setVisibility(View.GONE);
        } else {
            holder.foot.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRoot;
        TextView txtDesc_1;
        TextView txtDesc_2;
        TextView txtDistance;
        TextView txtStatus;
        ImageView imvDeal, imgFollowing;
        LinearLayout lnlExclusive;
        TextView txtExclusive,txtDibs;
        View foot;
        CircularProgressBar circularProgressBar;
    }

}
