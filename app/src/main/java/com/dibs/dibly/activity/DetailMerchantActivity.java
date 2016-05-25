package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.dibs.dibly.R;
import com.dibs.dibly.adapter.DetailMerchantPagerAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import greendao.Merchant;
import greendao.ObjectUser;

/**
 * Created by USER on 10/16/2015.
 */
public class DetailMerchantActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;

    private ImageView imvAvatar;
    private TextView txtMerchantName;
    private TextView txtFollowing;
    private TextView txtNewDeal;
    private TextView txtPastDeal;
    private TextView txtLabelNewDeal;
    private TextView txtLabelPastDeal;
    private RelativeLayout rltCover;
    private ImageView imvCover;

    private DetailMerchantPagerAdapter detailMerchantPagerAdapter;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions optionsRound;
    private DisplayImageOptions options;

    private Long merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_merchant);

        optionsRound = new DisplayImageOptions.Builder().cacheInMemory(false)
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.menu_merchant)
                .showImageOnLoading(R.drawable.menu_merchant)
                .cacheOnDisk(true).build();

        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .showImageForEmptyUri(R.drawable.bg_acc_setting1)
                .showImageOnLoading(R.drawable.bg_acc_setting1)
                .cacheOnDisk(true).build();

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_FOLLOWING_MERCHANT);
            registerReceiver(activityReceiver, intentFilter);
        }

        overrideFontsLight(findViewById(R.id.root));

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        DetailMerchantFragment detailMerchantFragment = new DetailMerchantFragment();
//        fragmentTransaction.add(R.id.lnlFragment, detailMerchantFragment, "merchant");
//        fragmentTransaction.commit();

        if (getIntent().hasExtra("merchant_id")) {
            merchantId = getIntent().getLongExtra("merchant_id", 0l);
        }

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        imvAvatar = (ImageView) findViewById(R.id.imvAvatar);
        txtMerchantName = (TextView) findViewById(R.id.txtMerchantName);
        txtFollowing = (TextView) findViewById(R.id.txtFollowing);
        txtNewDeal = (TextView) findViewById(R.id.txtNewDeal);
        txtPastDeal = (TextView) findViewById(R.id.txtPastDeal);
        txtLabelNewDeal = (TextView) findViewById(R.id.txtLabelNewDeal);
        txtLabelPastDeal = (TextView) findViewById(R.id.txtLabelPastDeal);
        rltCover = (RelativeLayout) findViewById(R.id.rltCover);
        imvCover = (ImageView) findViewById(R.id.imvCover);

        rltBack.setOnClickListener(this);
        txtFollowing.setOnClickListener(this);

        txtFollowing.setVisibility(View.INVISIBLE);

        overrideFontsBold(txtNewDeal);
        overrideFontsBold(txtPastDeal);
    }

    private void initData() {
        detailMerchantPagerAdapter = new DetailMerchantPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(detailMerchantPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabStrip.setViewPager(viewPager);
        tabStrip.setTextColor(getResources().getColor(R.color.txt_black_69));
        tabStrip.setDividerColor(getResources().getColor(R.color.transparent));
        tabStrip.setIndicatorColor(getResources().getColor(R.color.orange_main));
        tabStrip.setUnderlineColor(getResources().getColor(R.color.transparent));
        tabStrip.setTypeface(StaticFunction.semi_bold(this), 1);
        tabStrip.setTextSize((int) getResources().getDimension(R.dimen.txt_15sp));
        tabStrip.setBackgroundColor(getResources().getColor(R.color.gray_bg_tab_trip));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtFollowing:
                if (UserController.isLogin(DetailMerchantActivity.this)) {
                    String consumer_id = UserController.getCurrentUser(DetailMerchantActivity.this).getConsumer_id() + "";
                    Merchant merchant = MerchantController.getById(DetailMerchantActivity.this, merchantId);
                    if (merchant != null) {
                        if (merchant.getF_follow()) {
                            Intent intentGetDealDetail = new Intent(DetailMerchantActivity.this, RealTimeService.class);
                            intentGetDealDetail.setAction(RealTimeService.ACTION_STOP_FOLLOWING_MERCHANT);
                            intentGetDealDetail.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                            intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchantId + "");
                            startService(intentGetDealDetail);
                            showProgressDialog();
                        } else {
                            Intent intentGetDealDetail = new Intent(DetailMerchantActivity.this, RealTimeService.class);
                            intentGetDealDetail.setAction(RealTimeService.ACTION_FOLLOWING_MERCHANT);
                            intentGetDealDetail.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                            intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchantId + "");
                            startService(intentGetDealDetail);
                            showProgressDialog();
                        }
                    }
                } else {
                    showToastInfo(getString(R.string.login_first));
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
        exitMixPanel();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_MERCHANT)) {
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    Merchant merchant = MerchantController.getById(DetailMerchantActivity.this, merchantId);
                    if (merchant != null) {
                        txtMerchantName.setText(merchant.getOrganization_name());
                        txtFollowing.setVisibility(View.VISIBLE);
                        if (merchant.getF_follow()) {
                            txtFollowing.setText("Unfollow");
                            txtFollowing.setBackgroundResource(R.drawable.bg_red_follow);
                        } else {
                            txtFollowing.setText("Follow");
                            txtFollowing.setBackgroundResource(R.drawable.bg_green_follow);
                        }

                        imageLoader.displayImage(merchant.getLogo_image(), imvAvatar, optionsRound);
                        imageLoader.displayImage(merchant.getCover_image(), imvCover, options);

//                        try {
//                            JSONArray array_gallery_images = new JSONArray(merchant.getProfile_images());
//                            int num = array_gallery_images.length();
//                            if (num == 0) {
//
//                            } else if (num == 1) {
//                                JSONObject image = array_gallery_images.getJSONObject(0);
//                                imageLoader.displayImage(image.getString("image_thumbnail_url"), imvAvatar, options);
//                            } else {
//                                JSONObject image = array_gallery_images.getJSONObject(0);
//                                imageLoader.displayImage(image.getString("image_thumbnail_url"), imvAvatar, options);
//                            }
//                        } catch (JSONException e) {
//
//                        }

                        if (merchant.getLive_deals() == 1) {
                            txtNewDeal.setText("1");
                            txtLabelNewDeal.setText("Live Deal");
                        } else {
                            txtNewDeal.setText(merchant.getLive_deals() + "");
                            txtLabelNewDeal.setText(" Live Deals");
                        }
                        if (merchant.getPast_deals() == 1) {
                            txtPastDeal.setText("1");
                            txtLabelPastDeal.setText(" Past Deal");
                        } else {
                            txtPastDeal.setText(merchant.getPast_deals() + "");
                            txtLabelPastDeal.setText(" Past Deals");
                        }
                        rltCover.setVisibility(View.GONE);
                    }
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    ObjectUser user = UserController.getCurrentUser(DetailMerchantActivity.this);
                    int currentFollow = user.getK_following();
                    user.setK_following(currentFollow - 1);
                    UserController.update(DetailMerchantActivity.this, user);
                    Merchant merchant = MerchantController.getById(DetailMerchantActivity.this, merchantId);
                    if (merchant != null) {
                        merchant.setF_follow(false);
                        MerchantController.update(DetailMerchantActivity.this, merchant);
                        if (merchant.getF_follow()) {
                            txtFollowing.setText("Unfollow");
                            txtFollowing.setBackgroundResource(R.drawable.bg_red_follow);
                        } else {
                            txtFollowing.setText("Follow");
                            txtFollowing.setBackgroundResource(R.drawable.bg_green_follow);
                        }
                    }
                    enterMixPanel();
//                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
//                    showToastOk(message);
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastInfo(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_FOLLOWING_MERCHANT)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    ObjectUser user = UserController.getCurrentUser(DetailMerchantActivity.this);
                    int currentFollow = user.getK_following();
                    user.setK_following(currentFollow + 1);
                    UserController.update(DetailMerchantActivity.this, user);
                    Merchant merchant = MerchantController.getById(DetailMerchantActivity.this, merchantId);
                    if (merchant != null) {
                        merchant.setF_follow(true);
                        MerchantController.update(DetailMerchantActivity.this, merchant);
                        if (merchant.getF_follow()) {
                            txtFollowing.setText("Unfollow");
                            txtFollowing.setBackgroundResource(R.drawable.bg_red_follow);
                        } else {
                            txtFollowing.setText("Follow");
                            txtFollowing.setBackgroundResource(R.drawable.bg_green_follow);
                        }
                    }
//                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
//                    showToastOk(message);
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastInfo(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideProgressDialog();
            }
        }
    };

    private void enterMixPanel() {
        Merchant merchant = MerchantController.getById(DetailMerchantActivity.this, merchantId);
        if (merchant != null) {
            ObjectUser user = UserController.getCurrentUser(this);
            JSONObject object = new JSONObject();
            try {
                object.put("time", StaticFunction.getCurrentTime());
                object.put("email", user.getEmail());
                object.put("merchant id", merchant.getMerchant_id());
                object.put("merchant name", merchant.getOrganization_name());
                object.put("how many outlets", merchant.getK_outlets());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            trackMixPanel(getString(R.string.Enter_Merchant_Detail), object);
            startDurationMixPanel(getString(R.string.Duration_Merchant_Detail));
        }
    }

    private void exitMixPanel() {
        Merchant merchant = MerchantController.getById(DetailMerchantActivity.this, merchantId);
        if (merchant != null) {
            ObjectUser user = UserController.getCurrentUser(this);
            JSONObject object = new JSONObject();
            try {
                object.put("time", StaticFunction.getCurrentTime());
                object.put("email", user.getEmail());
                object.put("merchant id", merchant.getMerchant_id());
                object.put("merchant name", merchant.getOrganization_name());
                object.put("how many outlets", merchant.getK_outlets());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            trackMixPanel(getString(R.string.Exit_Merchant_Detail), object);
            endDurationMixPanel(getString(R.string.Duration_Merchant_Detail), object);
        }
    }
}
