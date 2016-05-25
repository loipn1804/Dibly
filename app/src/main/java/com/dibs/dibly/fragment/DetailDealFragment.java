package com.dibs.dibly.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.dibs.dibly.view.CountDownTextView;
import com.lylc.widget.circularprogressbar.CircularProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import greendao.MyLocation;
import greendao.ObjectDeal;
import greendao.Outlet;

/**
 * Created by USER on 7/1/2015.
 */
public class DetailDealFragment extends Fragment {

    private Long dealId;
    private Long merchantId;
    private ObjectDeal objectDeal;

    private TextView txtDistance;
    private CountDownTextView txtCountdownTimer;
    private TextView txtDesc;
    private TextView txtOutlets;
    private TextView txtDealLeft;
    private TextView txtLabelDealLeft;
    private LinearLayout lnlOutlets;
    private TextView txtAddressOutletNearest;
    private LinearLayout lnlPrice;
    private TextView txtPriceOriginal;
    private TextView txtPricePurchased;

    private CircularProgressBar circularPro;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private boolean is_expired;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_deal, container, false);

        ((BaseActivity) getActivity()).overrideFontsLight(view);

        options = new DisplayImageOptions.Builder().cacheInMemory(false).showImageForEmptyUri(R.color.white).showImageOnLoading(R.color.transparent).cacheOnDisk(true).build();

        if (getActivity().getIntent().getExtras() != null) {
            dealId = getActivity().getIntent().getLongExtra("deal_id", 0l);
            merchantId = getActivity().getIntent().getLongExtra("merchant_id", 0l);

            String outlet_id = "";
            ObjectDeal deal = DealController.getDealByDealId(getActivity(), dealId);
            if (deal != null) {
                outlet_id = deal.getOutlet_id() + "";
            }

            Intent intentGetDealDetail = new Intent(getActivity(), RealTimeService.class);
            intentGetDealDetail.setAction(RealTimeService.ACTION_GET_DEAL_DETAIL);
            intentGetDealDetail.putExtra(RealTimeService.EXTRA_DEAL_ID, dealId + "");
            intentGetDealDetail.putExtra(RealTimeService.EXTRA_OUTLET_ID, outlet_id);
            getActivity().startService(intentGetDealDetail);
        }

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_DETAIL);
            intentFilter.addAction(DealDetailActivity.RECEIVER_EXPRITED_TIME);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }

        is_expired = false;

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        txtDistance = (TextView) view.findViewById(R.id.txtDistance);
        txtCountdownTimer = (CountDownTextView) view.findViewById(R.id.txtCountdownTimer);
        txtDesc = (TextView) view.findViewById(R.id.txtDesc);
        txtOutlets = (TextView) view.findViewById(R.id.txtOutlets);
        txtDealLeft = (TextView) view.findViewById(R.id.txtDealLeft);
        txtLabelDealLeft = (TextView) view.findViewById(R.id.txtLabelDealLeft);
        lnlOutlets = (LinearLayout) view.findViewById(R.id.lnlOutlets);
        txtAddressOutletNearest = (TextView) view.findViewById(R.id.txtAddressOutletNearest);
        lnlPrice = (LinearLayout) view.findViewById(R.id.lnlPrice);
        txtPriceOriginal = (TextView) view.findViewById(R.id.txtPriceOriginal);
        txtPricePurchased = (TextView) view.findViewById(R.id.txtPricePurchased);

        circularPro = (CircularProgressBar) view.findViewById(R.id.circularProgress);

        StaticFunction.overrideFontsBold(getActivity(), txtOutlets);
        StaticFunction.overrideFontsBold(getActivity(), txtDealLeft);
        StaticFunction.overrideFontsBold(getActivity(), txtPriceOriginal);
        StaticFunction.overrideFontsBold(getActivity(), txtPricePurchased);
    }

    private void initData() {
        objectDeal = DealController.getDealById(getActivity(), dealId);
        if (objectDeal != null) {
            if (objectDeal.getType().equalsIgnoreCase("instore")) {
                lnlPrice.setVisibility(View.GONE);
            } else {
                lnlPrice.setVisibility(View.VISIBLE);
                BigDecimal bigOriginal = new BigDecimal(objectDeal.getOriginal_price()).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal bigPurchased = new BigDecimal(objectDeal.getPurchase_now_price()).setScale(2, BigDecimal.ROUND_HALF_UP);
                txtPriceOriginal.setText("S$ " + bigOriginal);
                txtPricePurchased.setText("S$ " + bigPurchased);
                txtPriceOriginal.setPaintFlags(txtPriceOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            txtDesc.setText(objectDeal.getDescription());
            final MyLocation mLoc = MyLocationController.getLastLocation(getActivity());

            if (mLoc != null) {
                final Outlet outlet = OutletController.getOutletById(getActivity(), objectDeal.getOutlet_id());
                if (outlet != null) {
                    float result[] = new float[3];
                    Location.distanceBetween(mLoc.getLatitude(), mLoc.getLongitude(), Double.parseDouble(outlet.getLatitude()), Double.parseDouble(outlet.getLongitude()), result);
                    int dis = (int) result[0];
                    if (dis < 1000) {
                        txtDistance.setText(dis + "m away ");
                    } else {
                        float f_dis = (float) dis / 1000;
                        BigDecimal big = new BigDecimal(f_dis).setScale(2, BigDecimal.ROUND_HALF_UP);
                        txtDistance.setText(big + "km away ");
                    }
                    String address = (outlet.getAddress1().trim().length()>0)?outlet.getAddress1() + " ":"";
                    address += outlet.getAddress2();
                    txtAddressOutletNearest.setText(address);
                    txtAddressOutletNearest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopupConfirmToMap(getString(R.string.open_map), outlet);
                        }
                    });
                    if (objectDeal.getOutlets() != null) {
                        List<Outlet> objectDealOutlets = OutletController.getOutletsByDealOutlets(getActivity(), objectDeal.getOutlets());
                        setListOutlets(mLoc, objectDealOutlets, outlet.getOutlet_id());
                    }
                } else {
                    txtDistance.setText("Not Available ");
                    txtAddressOutletNearest.setText("Not Available ");
                }
            } else {
                txtDistance.setText("Not Available ");
                txtAddressOutletNearest.setText("Not Available ");
            }

            circularPro.setMax(100);
            circularPro.setProgress(100);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date startDate = sdf.parse(objectDeal.getStart_at());
                long startInMilliseconds = startDate.getTime();
                Date endDate = sdf.parse(objectDeal.getEnd_at());
                long endInMilliseconds = endDate.getTime();
                long current_time = System.currentTimeMillis();
                long timeTotal = endInMilliseconds - startInMilliseconds;
                long millisUntilFinished = endInMilliseconds - current_time;
                long percent = 0;
                if (timeTotal == 0) {
                    percent = 100;
                } else {
                    percent = ((timeTotal - millisUntilFinished) * 100) / timeTotal;
                    Log.e("dsadas",percent+"");
                }
                circularPro.setProgress((int) percent);
                txtCountdownTimer.setTimeCount(endInMilliseconds, true);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (objectDeal.getK_rest_claim() != null) {
                if (objectDeal.getK_rest_claim() == 0) {
                    txtDealLeft.setText("No limit");
                } else {
                    if (objectDeal.getMax_claim() == 1) {
                        txtDealLeft.setText(objectDeal.getK_rest_claim() + "");
                        txtLabelDealLeft.setText(" deal left");
                    } else {
                        txtDealLeft.setText(objectDeal.getK_rest_claim() + "");
                        txtLabelDealLeft.setText(" deals left");
                    }
                }
            }
        }
    }

    private void initDataResume() {
//        objectDeal = DealController.getDealById(getActivity(), dealId);
//        if (objectDeal != null) {
//            if (objectDeal.getF_claimed()) {
//                lnlClaimed.setVisibility(View.VISIBLE);
//                txtStatus.setText("Claimed ");
//            } else if (objectDeal.getF_call_dibs()) {
//                lnlClaimed.setVisibility(View.VISIBLE);
//                txtStatus.setText("Dibs called ");
//            } else {
//                lnlClaimed.setVisibility(View.GONE);
//                txtStatus.setText("");
//            }
//        }
    }

    private void setListOutlets(MyLocation mLoc, List<Outlet> objectDealOutlets, Long idOutletNearest) {
        Typeface typefaceBold = StaticFunction.bold(getActivity());
        Typeface typefaceIt = StaticFunction.light_it(getActivity());
        Typeface typefaceLight = StaticFunction.light(getActivity());

        lnlOutlets.removeAllViews();
        for (final Outlet outlet : objectDealOutlets) {
            if (!outlet.getOutlet_id().equals(idOutletNearest)) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewRow = inflater.inflate(R.layout.row_outlet_deal_detail, null);
                LinearLayout root = (LinearLayout) viewRow.findViewById(R.id.root);
                TextView txtOutlet = (TextView) viewRow.findViewById(R.id.txtOutlet);
                TextView txtDistance = (TextView) viewRow.findViewById(R.id.txtDistance);
                TextView txtAddress = (TextView) viewRow.findViewById(R.id.txtAddress);

                txtOutlet.setTypeface(typefaceBold);
                txtDistance.setTypeface(typefaceIt);
                txtAddress.setTypeface(typefaceLight);

                txtOutlet.setText(outlet.getName());
                txtAddress.setText(outlet.getAddress1() + " " + outlet.getAddress2());

                float result[] = new float[3];
                Location.distanceBetween(mLoc.getLatitude(), mLoc.getLongitude(), Double.parseDouble(outlet.getLatitude()), Double.parseDouble(outlet.getLongitude()), result);
                int dis = (int) result[0];
                if (dis < 1000) {
                    txtDistance.setText(dis + "m away");
                } else {
                    float f_dis = (float) dis / 1000;
                    BigDecimal big = new BigDecimal(f_dis).setScale(2, BigDecimal.ROUND_HALF_UP);
                    txtDistance.setText(big + "km away");
                }

                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupConfirmToMap(getString(R.string.open_map), outlet);
                    }
                });

                lnlOutlets.addView(viewRow);
            }
        }

        if (lnlOutlets.getChildCount() == 0) {
            txtOutlets.setVisibility(View.GONE);
        } else {
            txtOutlets.setVisibility(View.VISIBLE);
        }
    }

    public void showPopupConfirmToMap(String message, final Outlet outlet) {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);

        StaticFunction.overrideFontsLight(getActivity(), dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(message);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = outlet.getLatitude() + "," + outlet.getLongitude();
                String uriString = "google.navigation:q=" + query;
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                getActivity().startActivity(intent);
                dialog.dismiss();
            }
        });

        lnlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onDestroy() {
        txtCountdownTimer.cancelTimer();
        super.onDestroy();
        if (activityReceiver != null) {
            getActivity().unregisterReceiver(activityReceiver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initDataResume();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_DETAIL)) {
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    initData();
                } else if (result.equals(ParallaxService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(ParallaxService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastError(message);
                } else if (result.equals(ParallaxService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
                ((BaseActivity) getActivity()).hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_EXPRITED_TIME)) {
                if (!is_expired) {
                    initData();
                    is_expired = true;
                }
            }
        }
    };
}
