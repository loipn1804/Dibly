package com.dibs.dibly.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;
import com.dibs.dibly.R;
import com.dibs.dibly.adapter.DetailDealPagerAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.NotifyController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.LineItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnPublishListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import greendao.ObjectDeal;

/**
 * Created by USER on 6/29/2015.
 */
public class DealDetailActivity extends BaseActivity implements View.OnClickListener {//, Braintree.PaymentMethodCreatedListener

    public static final String RECEIVER_NOTIFY_LIST = "RECEIVER_NOTIFY_LIST";
    public static final String RECEIVER_EXPRITED_TIME = "RECEIVER_EXPRITED_TIME";
    public static final String RECEIVER_CLAIM_SUCCESS = "RECEIVER_CLAIM_SUCCESS";

    private SimpleFacebook mSimpleFacebook;

    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private DetailDealPagerAdapter dealPagerAdapter;

    private RelativeLayout rltBack;
    private RelativeLayout rltCallDib;
    private RelativeLayout rltShare;
    private TextView txtCallDib;

    private TextView txtDesc_1;
    private TextView txtDesc_2;
    private ImageView imvDeal;
    private LinearLayout lnlExclusive;
    private RelativeLayout rltCover;

    private Long dealId;

    private boolean is_from_notify = false;

    private String KEY_CLIENT_TOKEN = "";
//    private Braintree mBraintree;

    private boolean isExprited;

    private boolean canCallDib;
    private boolean canClaim;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_deal);

        options = new DisplayImageOptions.Builder().cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).showImageForEmptyUri(R.color.white).showImageOnLoading(R.color.white).cacheOnDisk(true).build();

        isExprited = false;
        canCallDib = false;
        canClaim = false;

        overrideFontsLight(findViewById(R.id.root));

//        mBraintree = Braintree.restoreSavedInstanceState(this, savedInstanceState);
//        if (mBraintree != null) {
//            // mBraintree is ready to use
//            showToastInfo("mBraintree is ready to use");
//        } else {
//            Braintree.setup(this, KEY_CLIENT_TOKEN, new Braintree.BraintreeSetupFinishedListener() {
//                @Override
//                public void onBraintreeSetupFinished(boolean setupSuccessful, Braintree braintree, String errorMessage, Exception exception) {
//                    if (setupSuccessful) {
//                        // braintree is now setup and available for use
//                        mBraintree = braintree;
//                        mBraintree.addListener(DealDetailActivity.this);
//                        showToastInfo("braintree is now setup and available for use");
//                    } else {
//                        // Braintree could not be initialized, check errors and try again
//                        // This is usually a result of a network connectivity error
//                        showToastInfo("Braintree could not be initialized");
//                    }
//                }
//            });
//        }

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_DETAIL);
            intentFilter.addAction(RealTimeService.RECEIVER_CALL_DIB);
            intentFilter.addAction(RealTimeService.RECEIVER_GET_KEY_CLIENT_TOKEN);
            intentFilter.addAction(RealTimeService.RECEIVER_CREATE_PAYMENT);
            intentFilter.addAction(DealDetailActivity.RECEIVER_EXPRITED_TIME);
            registerReceiver(activityReceiver, intentFilter);
        }

        if (getIntent().hasExtra("deal_id")) {
            dealId = getIntent().getLongExtra("deal_id", 0l);

            if (getIntent().hasExtra("from")) {
                is_from_notify = true;
            }
//            if (getIntent().hasExtra("is_load_all")) {
//                is_from_notify = true;
//            }

//            ObjectDeal deal = DealController.getDealById(DealDetailActivity.this, dealId);
//            if (deal != null) {
//                Intent intentGetDealDetail = new Intent(DealDetailActivity.this, RealTimeService.class);
//                intentGetDealDetail.setAction(RealTimeService.ACTION_GET_DEAL_DETAIL);
//                intentGetDealDetail.putExtra(RealTimeService.EXTRA_DEAL_ID, dealId + "");
//                intentGetDealDetail.putExtra(RealTimeService.EXTRA_DEAL_IS_CLAIMED, deal.getF_claimed());
//                startService(intentGetDealDetail);
//            }
        } else {
            finish();
        }

        initView();
        initData();
    }

    protected void onPause() {
        super.onPause();

//        if (mBraintree != null) {
//            mBraintree.lockListeners();
//            mBraintree.removeListener(this);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
        NotifyController.clearByDealId(DealDetailActivity.this, dealId);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Integer.parseInt(dealId + ""));

        setBtnCalldibs();

//        if (mBraintree != null) {
//            mBraintree.addListener(this);
//            mBraintree.unlockListeners();
//        }
    }

    private void setBtnCalldibs() {
        if (UserController.isLogin(DealDetailActivity.this)) {
            ObjectDeal deal = DealController.getDealById(DealDetailActivity.this, dealId);
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
                        txtCallDib.setText("I Call Dibs");
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
            Intent intentHome = new Intent(DealDetailActivity.this, HomeActivity.class);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intentHome);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        if (mBraintree != null) {
//            mBraintree.onSaveInstanceState(outState);
//        }
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltCallDib = (RelativeLayout) findViewById(R.id.rltCallDib);
        rltShare = (RelativeLayout) findViewById(R.id.rltShare);
        txtCallDib = (TextView) findViewById(R.id.txtCallDib);
        txtDesc_1 = (TextView) findViewById(R.id.txtDesc_1);
        txtDesc_2 = (TextView) findViewById(R.id.txtDesc_2);
        imvDeal = (ImageView) findViewById(R.id.imvDeal);
        lnlExclusive = (LinearLayout) findViewById(R.id.lnlExclusive);
        rltCover = (RelativeLayout) findViewById(R.id.rltCover);

        rltBack.setOnClickListener(this);
        rltCallDib.setOnClickListener(this);
        rltShare.setOnClickListener(this);
    }

    private void initData() {
        dealPagerAdapter = new DetailDealPagerAdapter(getSupportFragmentManager(), DealDetailActivity.this, dealId);
        viewPager.setAdapter(dealPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabStrip.setViewPager(viewPager);
        tabStrip.setTextColor(getResources().getColor(R.color.txt_black_69));
        tabStrip.setDividerColor(getResources().getColor(R.color.transparent));
        tabStrip.setIndicatorColor(getResources().getColor(R.color.orange_main));
        tabStrip.setUnderlineColor(getResources().getColor(R.color.transparent));
//        tabStrip.setIndicatorHeight(10);
        tabStrip.setTypeface(StaticFunction.semi_bold(this), 1);
        tabStrip.setTextSize((int) getResources().getDimension(R.dimen.txt_15sp));
        tabStrip.setBackgroundColor(getResources().getColor(R.color.gray_bg_tab_trip));

        final ObjectDeal deal = DealController.getDealByDealId(this, dealId);
        if (deal != null) {
            txtDesc_1.setText(deal.getTitle());
            txtDesc_2.setText(deal.getOrganization_name());
            imageLoader.displayImage(deal.getImage(), imvDeal, options);
            if (deal.getIs_exclusive()) {
                lnlExclusive.setVisibility(View.VISIBLE);
            } else {
                lnlExclusive.setVisibility(View.GONE);
            }

            imvDeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deal.getImage() != null && !deal.getImage().isEmpty()) {
                        Intent intent = new Intent(DealDetailActivity.this, PhotoZoomActivity.class);
                        intent.putExtra("URL", deal.getImage());
                        startActivity(intent);
                    }
                }
            });
        }

        setBtnCalldibs();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                onBackPressed();
                finish();
                break;
            case R.id.rltCallDib:
                if (UserController.isLogin(DealDetailActivity.this)) {
                    ObjectDeal deal = DealController.getDealById(DealDetailActivity.this, dealId);
                    if (deal != null) {
                        if (canClaim) {
                            Intent intentQR = new Intent(DealDetailActivity.this, EnterMerchantCodeActivity.class);
                            intentQR.putExtra("deal_id", dealId);
                            startActivity(intentQR);
                        } else if (canCallDib) {
                            showPopupConfirmCallDib(deal.getTerms());
                        }
                    }
                } else {
                    showToastInfo(getString(R.string.login_first));
                }
                break;
            case R.id.rltShare:
//                List<String> list = new ArrayList<>();
//                list.add("Facebook");
//                list.add("Twitter");
//                showShareDialog(list);
                showShareDialog();
//                Intent notificationIntent = new Intent(DealDetailActivity.this, ClaimSuccessActivity.class);
//                notificationIntent.putExtra("message", "5% Off Any Purchase");
//                notificationIntent.putExtra("outlet_id", "70");
//                notificationIntent.putExtra("deal_id", "259");
//                notificationIntent.putExtra("merchant_id", "22");
//                startActivity(notificationIntent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
    }

    private void doCallDib() {
        if (UserController.isLogin(DealDetailActivity.this)) {
            ObjectDeal deal = DealController.getDealById(DealDetailActivity.this, dealId);
            if (deal != null) {
                if (deal.getDuration_type().equalsIgnoreCase("short_term")) {
                    callDibs("");
                } else {
                    //// get key client token
                    Intent intentGetKeyToken = new Intent(DealDetailActivity.this, RealTimeService.class);
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
                String paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
//                showPopupPrompt(paymentMethodNonce);

                Intent intentCreatePayment = new Intent(DealDetailActivity.this, RealTimeService.class);
                intentCreatePayment.setAction(RealTimeService.ACTION_CREATE_PAYMENT);
                intentCreatePayment.putExtra(RealTimeService.EXTRA_PAYMENT_NONCE, paymentMethodNonce);
                intentCreatePayment.putExtra(RealTimeService.EXTRA_PAYMENT_AMOUNT, DealController.getDealById(DealDetailActivity.this, dealId).getPurchase_now_price() + "");
                intentCreatePayment.putExtra(RealTimeService.EXTRA_CONSUMER_ID, UserController.getCurrentUser(DealDetailActivity.this).getConsumer_id() + "");
                intentCreatePayment.putExtra(RealTimeService.EXTRA_MERCHANT_ID, DealController.getDealById(DealDetailActivity.this, dealId).getMerchant_id() + "");
                startService(intentCreatePayment);
                showProgressDialog();
            }
        } else if (requestCode == AllReviewActivity.ALL_REVIEW_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("call_dibs")) {
                    doCallDib();
                } else if (data.hasExtra("claim")) {
                    Intent intentQR = new Intent(DealDetailActivity.this, EnterMerchantCodeActivity.class);
                    intentQR.putExtra("deal_id", dealId);
                    startActivity(intentQR);
                }
            }
        }
//        showToastInfo("onActivityResult");
    }

    private void callDibs(String trans_id) {
        ObjectDeal deal = DealController.getDealById(DealDetailActivity.this, dealId);
        Intent intentCallDib = new Intent(DealDetailActivity.this, RealTimeService.class);
        intentCallDib.setAction(RealTimeService.ACTION_CALL_DIB);
        intentCallDib.putExtra(RealTimeService.EXTRA_DEAL_ID, dealId + "");
        intentCallDib.putExtra(RealTimeService.EXTRA_OUTLET_ID, deal.getOutlet_id() + "");
        intentCallDib.putExtra(RealTimeService.EXTRA_CONSUMER_ID, UserController.getCurrentUser(DealDetailActivity.this).getConsumer_id() + "");
        intentCallDib.putExtra(RealTimeService.EXTRA_PAYMENT_TRANS_ID, trans_id);
        startService(intentCallDib);
        showProgressDialog();
    }

    private void preparePayment() {
        ObjectDeal deal = DealController.getDealById(DealDetailActivity.this, dealId);
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
//                .putExtra(BraintreePaymentActivity.EXTRA_ANDROID_PAY_CART, getAndroidPayCart(false))
//                .putExtra(BraintreePaymentActivity.EXTRA_ANDROID_PAY_IS_BILLING_AGREEMENT, false);
//                            .putExtra(BraintreePaymentActivity.EXTRA_ANDROID_PAY_IS_BILLING_AGREEMENT, Settings.isAndroidPayBillingAgreement(this));
//
            startActivityForResult(intent, 100);
        }
    }

    private Cart getAndroidPayCart(boolean b) {
        if (b) {
            return null;
        } else {
            ObjectDeal deal = DealController.getDealById(DealDetailActivity.this, dealId);
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

    private void showPopupConfirmCallDib(String term) {
        // custom dialog
        final Dialog dialog = new Dialog(DealDetailActivity.this);

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
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_CALL_DIB)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String data = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    Intent intentQR = new Intent(DealDetailActivity.this, EnterMerchantCodeActivity.class);
                    intentQR.putExtra("deal_id", dealId);
                    startActivity(intentQR);
                    hideProgressDialog();
                    List<ObjectDeal> deals = DealController.getListDealByDealId(DealDetailActivity.this, dealId);
                    for (ObjectDeal deal : deals) {
                        deal.setF_call_dibs(true);
                        DealController.update(DealDetailActivity.this, deal);
                    }
                    StaticFunction.sendBroad(DealDetailActivity.this, DealDetailActivity.RECEIVER_NOTIFY_LIST);
//                    finish();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_KEY_CLIENT_TOKEN)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String key = intent.getStringExtra(RealTimeService.EXTRA_RESULT_KEY_CLIENT_TOKEN);
                    KEY_CLIENT_TOKEN = key;

                    preparePayment();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_CREATE_PAYMENT)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    hideProgressDialog();
                    String trans_id = intent.getStringExtra(RealTimeService.EXTRA_RESULT_PAYMENT_TRANS_ID);
                    callDibs(trans_id);
//                    showPopupPrompt(trans_id);
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_EXPRITED_TIME)) {
                isExprited = true;
                setBtnCalldibs();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_DETAIL)) {
                setBtnCalldibs();
                rltCover.setVisibility(View.GONE);
            }
        }
    };

    public void showShareDialog(final List<String> listData) {
        // custom dialog
        final Dialog dialog = new Dialog(DealDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.popup_with_list);
        overrideFontsLight(dialog.findViewById(R.id.root));

        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DealDetailActivity.this, R.layout.row_list_item, listData);
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
        final Dialog dialog = new Dialog(DealDetailActivity.this);
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
                ObjectDeal deal = DealController.getDealByDealId(DealDetailActivity.this, dealId);
                if (deal != null) {
//                    String strShare = "";
//                    strShare += deal.getImage() + "\n";
//                    strShare += deal.getTitle() + "\n";
//                    strShare += deal.getOrganization_name() + "\n";
////                    strShare += "Android: https://play.google.com/store/apps/details?id=com.dibs.dibly" + "\n";
////                    strShare += "iOS: https://itunes.apple.com/us/app/dibly/id1018166197?ls=1&mt=8";
                    shareFacebookOld("http://getdibly.com/");

//                    Feed feed = new Feed.Builder()
////                            .setMessage("Clone it out...")
//                            .setName(deal.getTitle())
//                            .setCaption(deal.getOrganization_name())
//                            .setDescription(deal.getOrganization_name())
//                            .setPicture(deal.getImage_thumbnail())
//                            .setLink("http://getdibly.com/")
//                            .build();
//                    mSimpleFacebook.publish(feed, true, onPublishListener);
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
//        urlToShare = "http://facebook.com/phanngocloi1804";
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

// As fallback, launch sharer.php in a browser
        if (!facebookAppFound) {
            String sharerUrl = "https://twitter.com/intent/tweet?text=" + urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }

        startActivity(intent);
    }

//    @Override
//    public void onPaymentMethodCreated(PaymentMethod paymentMethod) {
//        showToastOk(paymentMethod.getNonce());
//    }
}
