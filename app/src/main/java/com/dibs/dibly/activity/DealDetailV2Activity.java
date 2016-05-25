package com.dibs.dibly.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;
import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.NotifyController;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.LineItem;
import com.lylc.widget.circularprogressbar.CircularProgressBar;
import com.lylc.widget.circularprogressbar.OnListenerFinish;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnPublishListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import greendao.MyLocation;
import greendao.ObjectDeal;
import greendao.ObjectUser;
import greendao.Outlet;

/**
 * Created by USER on 6/29/2015.
 */
public class DealDetailV2Activity extends BaseActivity implements View.OnClickListener {//, Braintree.PaymentMethodCreatedListener

    public static final String RECEIVER_NOTIFY_LIST = "RECEIVER_NOTIFY_LIST";
    public static final String RECEIVER_EXPRITED_TIME = "RECEIVER_EXPRITED_TIME";
    public static final String RECEIVER_CLAIM_SUCCESS = "RECEIVER_CLAIM_SUCCESS";

    private SimpleFacebook mSimpleFacebook;
    private RelativeLayout rltBack;
    private RelativeLayout rltCallDib;
    private RelativeLayout rltLike;
    private TextView txtCallDib;
    private TextView txtDesc_2;
    private ImageView imvDeal, imgAvatar, imgFollowing;
    private LinearLayout lnlExclusive, lnlOutlets, lnlTermAndCondition, lnlAboutMerchant, lnlOtherOutlet, lnlReport;
    private RelativeLayout rltCover;
    private TextView txtDescription, txtDealLeft, txtOutletAddress, txtDistance;
    private TextView txtLabelOther, txtLabelDescription;
    private CircularProgressBar circularProgressBar;

    private Long dealId;
    private Long outletId;
    private boolean is_from_notify = false;
    private String KEY_CLIENT_TOKEN = "";
    private boolean isExprited;
    private boolean canCallDib;
    private boolean canClaim;
    private Typeface typefaceBold;
    private Typeface typefaceLight;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options, options1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dealv2);
        typefaceBold = StaticFunction.bold(this);
        typefaceLight = StaticFunction.light(this);

        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.menu_merchant)
                .showImageOnLoading(R.drawable.menu_merchant)
                .cacheOnDisk(true).build();
        options1 = new DisplayImageOptions.Builder().cacheInMemory(false)
                .showImageForEmptyUri(R.drawable.merchant_cover_sm)
                .showImageOnLoading(R.drawable.merchant_cover_sm)
                .cacheOnDisk(true).build();

        isExprited = false;
        canCallDib = false;
        canClaim = false;

        overrideFontsLight(findViewById(R.id.root));
        registerReciever();
        initialIntent();
        initView();
        getDealDetail();
        initData();
    }

    private void initialIntent() {
        if (getIntent().hasExtra("deal_id")) {
            dealId = getIntent().getLongExtra("deal_id", 0l);
            outletId = getIntent().getLongExtra("outletId", 0l);
            Log.e("outletIddsada", outletId + "");

            if (getIntent().hasExtra("from")) {
                is_from_notify = true;
            }

        } else {
            finish();
        }
    }

    private void registerReciever() {

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_DETAIL);
            intentFilter.addAction(RealTimeService.RECEIVER_CALL_DIB);
            intentFilter.addAction(RealTimeService.RECEIVER_GET_KEY_CLIENT_TOKEN);
            intentFilter.addAction(RealTimeService.RECEIVER_CREATE_PAYMENT);
            intentFilter.addAction(DealDetailV2Activity.RECEIVER_EXPRITED_TIME);
            intentFilter.addAction(RealTimeService.RECEIVER_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_REPORT);
            registerReceiver(activityReceiver, intentFilter);
        }
    }

    private void initView() {

        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltCallDib = (RelativeLayout) findViewById(R.id.rltCallDib);
        rltLike = (RelativeLayout) findViewById(R.id.rltLike);
        txtCallDib = (TextView) findViewById(R.id.txtCallDib);
        txtDesc_2 = (TextView) findViewById(R.id.txtDesc_2);
        imvDeal = (ImageView) findViewById(R.id.imvDeal);
        lnlExclusive = (LinearLayout) findViewById(R.id.lnlExclusive);
        rltCover = (RelativeLayout) findViewById(R.id.rltCover);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtDealLeft = (TextView) findViewById(R.id.txtDealLeft);
        txtOutletAddress = (TextView) findViewById(R.id.txtOutletAddress);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        lnlOutlets = (LinearLayout) findViewById(R.id.lnlOutlets);
        txtLabelOther = (TextView) findViewById(R.id.txtLabelOutlet);
        txtLabelDescription = (TextView) findViewById(R.id.txtLabelDescription);
        lnlAboutMerchant = (LinearLayout) findViewById(R.id.lnlAboutMerchant);
        lnlTermAndCondition = (LinearLayout) findViewById(R.id.lnlTermAndCondition);
        lnlOtherOutlet = (LinearLayout) findViewById(R.id.lnlOtherOutlet);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgress);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        imgFollowing = (ImageView) findViewById(R.id.imgFollowing);
        lnlReport = (LinearLayout) findViewById(R.id.lnlReport);
        txtLabelOther.setTypeface(typefaceBold);
        txtLabelDescription.setTypeface(typefaceBold);
        rltBack.setOnClickListener(this);
        rltCallDib.setOnClickListener(this);
        rltLike.setOnClickListener(this);
        lnlAboutMerchant.setOnClickListener(this);
        lnlTermAndCondition.setOnClickListener(this);
        lnlReport.setOnClickListener(this);
    }


    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
        NotifyController.clearByDealId(DealDetailV2Activity.this, dealId);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Integer.parseInt(dealId + ""));

        setBtnCallDibs();

    }

    private void setBtnCallDibs() {
        if (UserController.isLogin(DealDetailV2Activity.this)) {
            ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
            if (deal != null) {
                if (deal.getF_claimed()) {
                    rltCallDib.setBackgroundResource(R.drawable.btn_disable);
                    txtCallDib.setText("Claimed");
                    canCallDib = false;
                    canClaim = false;
                } else if (deal.getF_call_dibs()) {
                    if (deal.getClaim_validity() != null) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date today = new Date();
                            Date validityDate = sdf.parse(deal.getClaim_validity());

                            if (today.after(validityDate)) {
                                rltCallDib.setBackgroundResource(R.drawable.btn_disable);
                                txtCallDib.setText("Expired");
                                canCallDib = false;
                                canClaim = false;
                            } else {
                                rltCallDib.setBackgroundResource(R.drawable.btn_claim_now_lg);
                                txtCallDib.setText("Claim now");
                                canCallDib = false;
                                canClaim = true;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        rltCallDib.setBackgroundResource(R.drawable.btn_claim_now_lg);
                        txtCallDib.setText("Claim now");
                        canCallDib = false;
                        canClaim = true;
                    }
                } else {
                    if (isExprited) {
                        rltCallDib.setBackgroundResource(R.drawable.btn_disable);
                        txtCallDib.setText("Expired");
                        canCallDib = false;
                        canClaim = false;
                    } else {
                        rltCallDib.setBackgroundResource(R.drawable.btn_organge);
                        txtCallDib.setText("Call Dibs");
                        canCallDib = true;
                        canClaim = false;
                    }
                }
            }
        } else {
            rltCallDib.setBackgroundResource(R.drawable.btn_organge);
            txtCallDib.setText("I Call Dibs");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (is_from_notify) {
            Intent intentHome = new Intent(DealDetailV2Activity.this, HomeActivity.class);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intentHome);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void initData() {

        final ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
        if (deal != null) {
            loadDealDetail(deal);
            txtDesc_2.setText(deal.getOrganization_name());
            imageLoader.displayImage(deal.getImage(), imvDeal, options1);
            if (deal.getIs_exclusive()) {
                lnlExclusive.setVisibility(View.GONE);
            } else {
                lnlExclusive.setVisibility(View.GONE);
            }

            imvDeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deal.getImage() != null && !deal.getImage().isEmpty()) {
                        Intent intent = new Intent(DealDetailV2Activity.this, PhotoZoomActivity.class);
                        intent.putExtra("URL", deal.getImage());
                        startActivity(intent);
                    }
                }
            });
        } else {
            Log.e("nudsda", "dsadas");
        }

        setBtnCallDibs();
    }

    private void loadDealDetail(ObjectDeal deal) {

        txtDescription.setText(deal.getDescription());

        String url_thumb = deal.getLogo_image_url();
        if (url_thumb != null && url_thumb.trim().length() > 0) {
            imageLoader.displayImage(url_thumb, imgAvatar, options);
        }

        if (deal.getK_rest_claim() != null) {
            if (deal.getK_rest_claim() == 0) {
                txtDealLeft.setText("No limit");
                rltCallDib.setVisibility(View.GONE);
            } else {
                if (deal.getMax_claim() == 1) {
                    txtDealLeft.setText(deal.getK_rest_claim() + " deal left");

                } else {
                    txtDealLeft.setText(deal.getK_rest_claim() + " deals left");

                }
                rltCallDib.setVisibility(View.VISIBLE);
            }
        }

        if (deal.getF_liked()) {
            imgFollowing.setImageResource(R.drawable.ic_heart_active);
        } else {
            imgFollowing.setImageResource(R.drawable.ic_heart_inactive);
        }


        final MyLocation mLoc = MyLocationController.getLastLocation(this);

        if (mLoc != null) {
            Log.e("dsadas", deal.getOutlet_id() + "");
            final Outlet outlet = OutletController.getOutletById(this, deal.getOutlet_id());
            if (outlet != null) {
                float result[] = new float[3];
                Location.distanceBetween(mLoc.getLatitude(), mLoc.getLongitude(), Double.parseDouble(outlet.getLatitude()), Double.parseDouble(outlet.getLongitude()), result);
                int dis = (int) result[0];
                if (dis < 1000) {
                    txtDistance.setText(dis + "m");
                } else {
                    float f_dis = (float) dis / 1000;
                    BigDecimal big = new BigDecimal(f_dis).setScale(2, BigDecimal.ROUND_HALF_UP);
                    txtDistance.setText(big + "km");
                }
                String address = (outlet.getAddress1().trim().length() > 0) ? outlet.getAddress1() + " " : "";
                address += outlet.getAddress2();
                txtOutletAddress.setText(address.trim());
                txtOutletAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupConfirmToMap(getString(R.string.open_map), outlet);
                    }
                });
                if (deal.getOutlets() != null && deal.getOutlets().trim().length() > 0) {
                    List<Outlet> objectDealOutlets = OutletController.getOutletsByDealOutlets(this, deal.getOutlets());
                    setListOutlets(mLoc, objectDealOutlets, outlet.getOutlet_id());
                } else {
                    lnlOtherOutlet.setVisibility(View.GONE);
                }
            } else {
                txtDistance.setText("Not Available ");
                txtOutletAddress.setText("Not Available ");
            }
        } else {
            txtDistance.setText("Not Available ");
            txtOutletAddress.setText("Not Available ");
        }


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

            if (deal.getF_claimed()) {
                circularProgressBar.cancelTimer();
                circularProgressBar.setTitle("Claimed");
                circularProgressBar.setSubTitle("");
                circularProgressBar.setProgress(0);
                circularProgressBar.setTitleColor(getResources().getColor(R.color.white));
                circularProgressBar.setSubTitleColor(getResources().getColor(R.color.white));
            } else if (deal.getF_call_dibs()) {
                circularProgressBar.setMax(100);
                circularProgressBar.setProgress((int) percent);
                circularProgressBar.setTimerCountDown(endInMilliseconds, timeTotal);
                circularProgressBar.setOnListenerFinish(new OnListenerFinish() {
                    @Override
                    public void onFinish() {
                        StaticFunction.sendBroad(DealDetailV2Activity.this, DealDetailActivity.RECEIVER_EXPRITED_TIME);
                    }
                });
            } else {
                circularProgressBar.setMax(100);
                circularProgressBar.setProgress((int) percent);
                circularProgressBar.setTimerCountDown(endInMilliseconds, timeTotal);
                circularProgressBar.setOnListenerFinish(new OnListenerFinish() {
                    @Override
                    public void onFinish() {
                        StaticFunction.sendBroad(DealDetailV2Activity.this, DealDetailActivity.RECEIVER_EXPRITED_TIME);
                    }
                });

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void getDealDetail() {

        showProgressDialog();
        Intent intentGetDealDetail = new Intent(this, RealTimeService.class);
        intentGetDealDetail.setAction(RealTimeService.ACTION_GET_DEAL_DETAIL);
        intentGetDealDetail.putExtra(RealTimeService.EXTRA_DEAL_ID, dealId + "");
        intentGetDealDetail.putExtra(RealTimeService.EXTRA_OUTLET_ID, outletId + "");
        startService(intentGetDealDetail);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                onBackPressed();
                finish();
                break;
            case R.id.rltCallDib:
                if (UserController.isLogin(DealDetailV2Activity.this)) {
                    ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
                    if (deal != null) {
                        if (canClaim) {
                            Intent intentQR = new Intent(DealDetailV2Activity.this, EnterMerchantCodeActivity.class);
                            intentQR.putExtra("deal_id", dealId);
                            intentQR.putExtra("outlet_id", outletId);
                            startActivity(intentQR);
                            claimMixPanel();
                        } else if (canCallDib) {
                            showPopupConfirmCallDib(deal.getTerms(), false);
                        }
                    }
                } else {
                    showToastInfo(getString(R.string.login_first));
                }
                break;
            case R.id.rltLike:
                if (UserController.isLogin(DealDetailV2Activity.this)) {
                    ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
                    if (deal != null) {
                        if (deal.getF_liked()) {
                            showPopupConfirmToUnFollowing(deal);
                        } else {
                            showPopupConfirmToFollowing(deal);
                        }
                    }
                } else {
                    showToastInfo(getString(R.string.login_first));
                }
                break;
            case R.id.lnlAboutMerchant:
                ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
                Intent intentMerchant = new Intent(this, DealDetailMerchantActivity.class);
                intentMerchant.putExtra("deal_id", dealId);
                intentMerchant.putExtra("merchant_id", deal.getMerchant_id());
                startActivity(intentMerchant);
                break;
            case R.id.lnlTermAndCondition:
                Intent intent = new Intent(this, DetailTermAndConditionActivity.class);
                intent.putExtra("deal_id", dealId);
                startActivity(intent);
                break;
            case R.id.lnlReport:
                showPopupReport();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
        if (circularProgressBar != null) {
            circularProgressBar.cancelTimer();
        }
        exitMixPanel();
    }

    private void doCallDib() {
        if (UserController.isLogin(DealDetailV2Activity.this)) {
            ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
            if (deal != null) {
                if (deal.getDuration_type().equalsIgnoreCase("short_term")) {
                    callDibs("");
                } else {
                    //// get key client token
                    Intent intentGetKeyToken = new Intent(DealDetailV2Activity.this, RealTimeService.class);
                    intentGetKeyToken.setAction(RealTimeService.ACTION_GET_KEY_CLIENT_TOKEN);
                    startService(intentGetKeyToken);
                    showProgressDialog();
                }
            }
        } else {
            showToastInfo(getString(R.string.login_first));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == BraintreePaymentActivity.RESULT_OK) {
                ObjectDeal objectDeal = DealController.getDealByDealId(this, dealId, outletId);

                String paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
                Intent intentCreatePayment = new Intent(DealDetailV2Activity.this, RealTimeService.class);
                intentCreatePayment.setAction(RealTimeService.ACTION_CREATE_PAYMENT);
                intentCreatePayment.putExtra(RealTimeService.EXTRA_PAYMENT_NONCE, paymentMethodNonce);
                intentCreatePayment.putExtra(RealTimeService.EXTRA_PAYMENT_AMOUNT, objectDeal.getPurchase_now_price() + "");
                intentCreatePayment.putExtra(RealTimeService.EXTRA_CONSUMER_ID, UserController.getCurrentUser(DealDetailV2Activity.this).getConsumer_id() + "");
                intentCreatePayment.putExtra(RealTimeService.EXTRA_MERCHANT_ID, objectDeal.getMerchant_id() + "");
                startService(intentCreatePayment);
                showProgressDialog();
            }
        } else if (requestCode == AllReviewActivity.ALL_REVIEW_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("call_dibs")) {
                    doCallDib();
                } else if (data.hasExtra("claim")) {
                    Intent intentQR = new Intent(DealDetailV2Activity.this, EnterMerchantCodeActivity.class);
                    intentQR.putExtra("deal_id", dealId);
                    intentQR.putExtra("outlet_id", outletId);
                    startActivity(intentQR);
                }
            }
        }

    }

    private void callDibs(String trans_id) {
        ObjectDeal deal = DealController.getDealByDealId(DealDetailV2Activity.this, dealId, outletId);
        Intent intentCallDib = new Intent(DealDetailV2Activity.this, RealTimeService.class);
        intentCallDib.setAction(RealTimeService.ACTION_CALL_DIB);
        intentCallDib.putExtra(RealTimeService.EXTRA_DEAL_ID, dealId + "");
        intentCallDib.putExtra(RealTimeService.EXTRA_OUTLET_ID, deal.getOutlet_id() + "");
        intentCallDib.putExtra(RealTimeService.EXTRA_CONSUMER_ID, UserController.getCurrentUser(DealDetailV2Activity.this).getConsumer_id() + "");
        intentCallDib.putExtra(RealTimeService.EXTRA_PAYMENT_TRANS_ID, trans_id);
        startService(intentCallDib);
        showProgressDialog();
        callDibMixPanel();
    }

    private void preparePayment() {
        ObjectDeal deal = DealController.getDealByDealId(DealDetailV2Activity.this, dealId, outletId);
        if (deal != null) {
            Customization customization = new Customization.CustomizationBuilder()
                    .primaryDescription(deal.getOrganization_name())
                    .secondaryDescription(deal.getTitle())
                    .amount(deal.getPurchase_now_price() + "")
                    .submitButtonText("Buy")
                    .build();

            Intent intent = new Intent(this, BraintreePaymentActivity.class)
                    .putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, KEY_CLIENT_TOKEN)
                    .putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);
            startActivityForResult(intent, 100);
        }
    }

    private Cart getAndroidPayCart(boolean b) {
        if (b) {
            return null;
        } else {
            ObjectDeal deal = DealController.getDealByDealId(DealDetailV2Activity.this, dealId, outletId);
            return Cart.newBuilder()
                    .setCurrencyCode("SGD")
                    .setTotalPrice(deal.getPurchase_now_price() + "")
                    .addLineItem(LineItem.newBuilder()
                            .setCurrencyCode("SGD")
                            .setDescription(deal.getTitle())
                            .setQuantity("1")
                            .setUnitPrice(deal.getPurchase_now_price() + "")
                            .setTotalPrice(deal.getPurchase_now_price() + "")
                            .build())
                    .build();
        }
    }

    private void showPopupConfirmCallDib(String term, boolean isFormTC) {
        // custom dialog
        final Dialog dialog = new Dialog(DealDetailV2Activity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_confirm_call_dibs);

        overrideFontsLight(dialog.findViewById(R.id.root));

        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        TextView txtTermAndCon = (TextView) dialog.findViewById(R.id.txtTermAndCon);
        txtTermAndCon.setText(term);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCallDib();
                dialog.dismiss();
            }
        });

        if (isFormTC) {
            btnOk.setVisibility(View.GONE);
            btnCancel.setBackgroundResource(R.drawable.bg_btn_make_payment);
            btnCancel.setText("OK");
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_CALL_DIB)) {

                if (result.equals(RealTimeService.RESULT_OK)) {
                    hideProgressDialog();
                    List<ObjectDeal> deals = DealController.getListDealByDealId(DealDetailV2Activity.this, dealId);
                    for (ObjectDeal deal : deals) {
                        deal.setF_call_dibs(true);
                        DealController.update(DealDetailV2Activity.this, deal);
                    }
                    StaticFunction.sendBroad(DealDetailV2Activity.this, DealDetailV2Activity.RECEIVER_NOTIFY_LIST);

                    setBtnCallDibs();
                    showPopupCallingDib();
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_KEY_CLIENT_TOKEN)) {

                if (result.equals(RealTimeService.RESULT_OK)) {
                    String key = intent.getStringExtra(RealTimeService.EXTRA_RESULT_KEY_CLIENT_TOKEN);
                    KEY_CLIENT_TOKEN = key;

                    preparePayment();
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_CREATE_PAYMENT)) {
                if (result.equals(RealTimeService.RESULT_OK)) {
                    hideProgressDialog();
                    String trans_id = intent.getStringExtra(RealTimeService.EXTRA_RESULT_PAYMENT_TRANS_ID);
                    callDibs(trans_id);
                }
            } else if (intent.getAction().equalsIgnoreCase(DealDetailV2Activity.RECEIVER_EXPRITED_TIME)) {
                isExprited = true;
                setBtnCallDibs();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_DETAIL)) {
                if (result.equals(ParallaxService.RESULT_OK)) {
                    initData();
                    enterMixPanel();
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT)) {
                if (result.equals(RealTimeService.RESULT_OK)) {
                    ObjectDeal deal = DealController.getDealByDealId(context, dealId, outletId);
                    if (deal != null) {
                        deal.setF_liked(false);
                        DealController.update(context, deal);
                        if (deal.getF_liked()) {
                            imgFollowing.setImageResource(R.drawable.ic_heart_active);
                        } else {
                            imgFollowing.setImageResource(R.drawable.ic_heart_inactive);
                        }
                    }
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastOk(message);
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_FOLLOWING_MERCHANT)) {
                if (result.equals(RealTimeService.RESULT_OK)) {
                    ObjectDeal deal = DealController.getDealByDealId(context, dealId, outletId);
                    if (deal != null) {
                        deal.setF_liked(true);
                        DealController.update(context, deal);
                        if (deal.getF_liked()) {
                            imgFollowing.setImageResource(R.drawable.ic_heart_active);
                        } else {
                            imgFollowing.setImageResource(R.drawable.ic_heart_inactive);
                        }
                    }
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastOk(message);
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_REPORT)) {
                if (result.equals(ParallaxService.RESULT_OK)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);

                    showToastInfo(message);
                }
            }


            hideProgressDialog();
            if (result != null)
                if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);

                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
        }
    };

    public void showShareDialog(final List<String> listData) {
        // custom dialog
        final Dialog dialog = new Dialog(DealDetailV2Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.popup_with_list);
        overrideFontsLight(dialog.findViewById(R.id.root));

        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DealDetailV2Activity.this, R.layout.row_list_item, listData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    shareFacebookOld("");
                } else if (position == 1) {
                    shareTwitter("");
                }

                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void showShareDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(DealDetailV2Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.popup_share);
        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout btnShareFB = (LinearLayout) dialog.findViewById(R.id.btnShareFB);
        LinearLayout btnShareTwitter = (LinearLayout) dialog.findViewById(R.id.btnShareTwitter);
        btnShareTwitter.setVisibility(View.GONE);

        btnShareFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectDeal deal = DealController.getDealByDealId(DealDetailV2Activity.this, dealId);
                if (deal != null) {

                    shareFacebookOld("http://getdibly.com/");

                }
                dialog.dismiss();
            }
        });

        btnShareTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTwitter("http://getdibly.com/");
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    OnPublishListener onPublishListener = new OnPublishListener() {
        @Override
        public void onComplete(String postId) {
            showToastOk("Shared");
        }
    };

    private void shareFacebookOld(String urlToShare) {
        //        urlToShare = "http://facebook.com/phanngocloi1804";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);
        intent.putExtra(Intent.EXTRA_TITLE, "title");

        // See if official Facebook app is found
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

        // As fallback, launch sharer.php in a browser
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }

        startActivity(intent);


    }

    private void shareTwitter(String urlToShare) {
        //urlToShare = "http://facebook.com/phanngocloi1804";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

        // See if official Facebook app is found
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter.android")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }


        if (!facebookAppFound) {
            String sharerUrl = "https://twitter.com/intent/tweet?text=" + urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }

        startActivity(intent);
    }

    public void showPopupConfirmToMap(String message, final Outlet outlet) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);

        StaticFunction.overrideFontsLight(this, dialog.findViewById(R.id.root));

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
                startActivity(intent);
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

    public void showPopupCallingDib() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_calling_dibs);

        StaticFunction.overrideFontsLight(this, dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        final ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            Date endDate = sdf.parse(deal.getEnd_at());
            long endInMilliseconds = endDate.getTime();
            long current_time = System.currentTimeMillis();
            long remainTime = endInMilliseconds - current_time;

            long hours = TimeUnit.MILLISECONDS.toHours(remainTime);
            remainTime -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(remainTime);

            long days = hours / 24;


            String caption = hours % 24 + " h " + minutes + " m";
            if (days > 0) {
                caption = days + " day " + caption;
            }

            SpannableString text2 = new SpannableString(caption + " to claim it");
            text2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange_main)), 0,
                    caption.length(), 0);
            text2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.txt_black_68)), caption.length(),
                    text2.length() - 1, 0);

            TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
            txtMessage.setText(text2, TextView.BufferType.SPANNABLE);

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        TextView txtTCApply = (TextView) dialog.findViewById(R.id.txtTCApply);
        txtTCApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupConfirmCallDib(deal.getTerms(), true);
            }
        });

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setListOutlets(MyLocation mLoc, List<Outlet> objectDealOutlets, Long idOutletNearest) {


        boolean isExist = false;
        lnlOutlets.removeAllViews();
        for (final Outlet outlet : objectDealOutlets) {
            if (!outlet.getOutlet_id().equals(idOutletNearest)) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewRow = inflater.inflate(R.layout.row_other_outlet, null);
                LinearLayout root = (LinearLayout) viewRow.findViewById(R.id.root);
                TextView txtOutlet = (TextView) viewRow.findViewById(R.id.txtOutlet);
                TextView txtAddress = (TextView) viewRow.findViewById(R.id.txtAddress);
                TextView txtPhone = (TextView) viewRow.findViewById(R.id.txtPhone);
                TextView txtVieMap = (TextView) viewRow.findViewById(R.id.txtVieMap);
                txtOutlet.setTypeface(typefaceBold);
                txtAddress.setTypeface(typefaceLight);
                txtPhone.setTypeface(typefaceLight);
                txtOutlet.setText(outlet.getName());
                txtAddress.setText(outlet.getAddress1() + " " + outlet.getAddress2());

                String phone = (outlet.getPhone().trim().length() > 0) ? outlet.getPhone() : "Not available";
                txtPhone.setText(phone);

                txtVieMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupConfirmToMap(getString(R.string.open_map), outlet);
                    }
                });

                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DealDetailV2Activity.this.outletId = outlet.getOutlet_id();
                        getDealDetail();
                    }
                });

                lnlOutlets.addView(viewRow);
                isExist = true;
            }
        }

        if (!isExist) {
            lnlOtherOutlet.setVisibility(View.GONE);
        } else {
            lnlOtherOutlet.setVisibility(View.VISIBLE);
        }
    }

    private void followingMerchant() {
        if (UserController.isLogin(this)) {
            String consumer_id = UserController.getCurrentUser(this).getConsumer_id() + "";
            ObjectDeal deal = DealController.getDealByDealId(DealDetailV2Activity.this, dealId, outletId);
            if (deal != null) {

                if (deal.getF_liked()) {
                    Intent intentGetDealDetail = new Intent(this, RealTimeService.class);
                    intentGetDealDetail.setAction(RealTimeService.ACTION_STOP_FOLLOWING_MERCHANT);
                    intentGetDealDetail.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                    intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, deal.getMerchant_id() + "");
                    startService(intentGetDealDetail);
                    showProgressDialog();
                    unFollowMerchantMixPanel();
                } else {
                    Intent intentGetDealDetail = new Intent(this, RealTimeService.class);
                    intentGetDealDetail.setAction(RealTimeService.ACTION_FOLLOWING_MERCHANT);
                    intentGetDealDetail.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                    intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, deal.getMerchant_id() + "");
                    startService(intentGetDealDetail);
                    showProgressDialog();
                    followMerchantMixPanel();
                }
            }
        } else {
            showToastInfo(getString(R.string.login_first));
        }
    }

    public void showPopupConfirmToFollowing(ObjectDeal deal) {

        // custom dialog
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_following_merchant);

        StaticFunction.overrideFontsLight(this, dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(getTitleText("Follow ", deal.getOrganization_name()), TextView.BufferType.SPANNABLE);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followingMerchant();
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

    public void showPopupConfirmToUnFollowing(ObjectDeal deal) {

        // custom dialog
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_unfollowing_merchant);

        StaticFunction.overrideFontsLight(this, dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(getTitleText("Unfollow ", deal.getOrganization_name()), TextView.BufferType.SPANNABLE);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followingMerchant();
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

    private void showPopupReport() {

        final ObjectDeal objectDeal = DealController.getDealByDealId(this, dealId, outletId);
        if (objectDeal != null) {
            // custom dialog
            final Dialog dialog = new Dialog(DealDetailV2Activity.this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.popup_report);

            overrideFontsLight(dialog.findViewById(R.id.root));

            LinearLayout btnSubmit = (LinearLayout) dialog.findViewById(R.id.lnlSubmit);


            final TextView txtDealName = (TextView) dialog.findViewById(R.id.txtDealName);
            final EditText edtComment = (EditText) dialog.findViewById(R.id.edtComment);
            edtComment.setTypeface(typefaceLight);
            txtDealName.setText(objectDeal.getOrganization_name());

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String comment = edtComment.getText().toString().trim();

                    if (comment.length() > 0) {
                        Intent intentGetDealDetail = new Intent(DealDetailV2Activity.this, RealTimeService.class);
                        intentGetDealDetail.setAction(RealTimeService.ACTION_REPORT);
                        intentGetDealDetail.putExtra(RealTimeService.EXTRA_DEAL_ID, objectDeal.getDeal_id());
                        intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, objectDeal.getMerchant_id());
                        intentGetDealDetail.putExtra(RealTimeService.EXTRA_OUTLET_ID, objectDeal.getOutlet_id());
                        intentGetDealDetail.putExtra(RealTimeService.EXTRA_REPORT_TEXT, edtComment.getText().toString().trim());
                        startService(intentGetDealDetail);
                        showProgressDialog();
                        dialog.dismiss();
                    } else {
                        showToastInfo("Your comment is empty");
                    }
                }
            });

            dialog.show();
        }
    }

    private void enterMixPanel() {
        final ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
        if (deal != null) {
            ObjectUser user = UserController.getCurrentUser(this);
            JSONObject object = new JSONObject();
            try {
                object.put("time", StaticFunction.getCurrentTime());
                object.put("email", user.getEmail());
                object.put("deal category", deal.getGroup_name());
                object.put("merchant name", deal.getOrganization_name());
                object.put("deal title", deal.getTitle());
                object.put("isExclusive", deal.getIs_exclusive());
                object.put("deal duration", deal_duration(deal));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            trackMixPanel(getString(R.string.Enter_Deal_Detail), object);
            startDurationMixPanel(getString(R.string.Duration_Deal_Detail));
        }
    }

    private void exitMixPanel() {
        final ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
        if (deal != null) {
            ObjectUser user = UserController.getCurrentUser(this);
            JSONObject object = new JSONObject();
            try {
                object.put("time", StaticFunction.getCurrentTime());
                object.put("email", user.getEmail());
                object.put("deal category", deal.getGroup_name());
                object.put("merchant name", deal.getOrganization_name());
                object.put("deal title", deal.getTitle());
                object.put("isExclusive", deal.getIs_exclusive());
                object.put("deal duration", deal_duration(deal));
                object.put("isCallDibs", deal.getF_call_dibs());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            trackMixPanel(getString(R.string.Exit_Deal_Detail), object);
            endDurationMixPanel(getString(R.string.Duration_Deal_Detail), object);
        }
    }

    private void callDibMixPanel() {
        final ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
        if (deal != null) {
            ObjectUser user = UserController.getCurrentUser(this);
            JSONObject object = new JSONObject();
            try {
                object.put("time", StaticFunction.getCurrentTime());
                object.put("email", user.getEmail());
                object.put("deal category", deal.getGroup_name());
                object.put("merchant name", deal.getOrganization_name());
                object.put("deal title", deal.getTitle());
                object.put("isExclusive", deal.getIs_exclusive());
                object.put("deal duration", deal_duration(deal));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            trackMixPanel(getString(R.string.Call_Dibs), object);
        }
    }

    private void claimMixPanel() {
        final ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
        if (deal != null) {
            ObjectUser user = UserController.getCurrentUser(this);
            JSONObject object = new JSONObject();
            try {
                object.put("time", StaticFunction.getCurrentTime());
                object.put("email", user.getEmail());
                object.put("deal category", deal.getGroup_name());
                object.put("merchant name", deal.getOrganization_name());
                object.put("deal title", deal.getTitle());
                object.put("isExclusive", deal.getIs_exclusive());
                object.put("deal duration", deal_duration(deal));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            trackMixPanel(getString(R.string.Claim), object);
        }
    }

    private void followMerchantMixPanel() {
        final ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
        if (deal != null) {
            ObjectUser user = UserController.getCurrentUser(this);
            JSONObject object = new JSONObject();
            try {
                object.put("time", StaticFunction.getCurrentTime());
                object.put("email", user.getEmail());
                object.put("merchant id", deal.getMerchant_id());
                object.put("merchant name", deal.getOrganization_name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            trackMixPanel(getString(R.string.Follow_Merchant), object);
        }
    }

    private void unFollowMerchantMixPanel() {
        final ObjectDeal deal = DealController.getDealByDealId(this, dealId, outletId);
        if (deal != null) {
            ObjectUser user = UserController.getCurrentUser(this);
            JSONObject object = new JSONObject();
            try {
                object.put("time", StaticFunction.getCurrentTime());
                object.put("email", user.getEmail());
                object.put("merchant id", deal.getMerchant_id());
                object.put("merchant name", deal.getOrganization_name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            trackMixPanel(getString(R.string.Un_Follow_Merchant), object);
        }
    }

    private int deal_duration(ObjectDeal deal) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        Date endDate = null;
        int hour = 0;
        try {
            startDate = format.parse(deal.getStart_at());
            endDate = format.parse(deal.getEnd_at());
            long mil = endDate.getTime() - startDate.getTime();
            hour = (int) (mil / (60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hour;
    }
}
