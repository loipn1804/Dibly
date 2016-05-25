package com.dibs.dibly.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.DealHomeAdapter;
import com.dibs.dibly.adapter.DealMoreMerchantDetailAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.CommentController;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.daocontroller.ReviewController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.interfaceUtils.OnPopUpFollowingListener;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.dibs.dibly.view.NestedListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import greendao.Comment;
import greendao.Merchant;
import greendao.MyLocation;
import greendao.ObjectDeal;
import greendao.Outlet;
import greendao.Review;

/**
 * Created by VuPhan on 4/16/16.
 */
public class DealDetailMerchantActivity extends BaseActivity implements View.OnClickListener {


    public static final String RECEIVER_ADD_COMMENT = "RECEIVER_ADD_COMMENT";
    private Long dealId, merchantId;
    private Typeface typefaceBold;
    private Typeface typefaceLight;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options, options1;

    private TextView txtMerchantName, txtOpenHour, txtDescription,
            txtPhoneNumber, txtWebsite, txtAddress, txtDistance, txtYay, txtNay;
    private TextView txtLabelWebsite, txtLabelOutlet, txtLabelPhoneNumber, txtLabelMoreDeal, txtLabelReview;
    private LinearLayout lnlOutlets, lnlViewMore, lnlReview, lnlOutlet,lnlMoreDeal;
    private ImageView imgCover, imgAvatar, imgFollowing;
    private ImageView imvFacebook, imvTwitter, imvInstagram;
    private RelativeLayout rltBack, rltFollowing;
    private NestedListView nstListView;
    private ScrollView scrollView;
    private Merchant merchant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_detail_merchant);
        initialIntent();
        initialView();
        registerIntentReceiver();
        loadDataMerchant();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
    }

    private void initialView() {
        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.menu_merchant)
                .showImageOnLoading(R.drawable.menu_merchant)
                .cacheOnDisk(true).build();
        options1 = new DisplayImageOptions.Builder().cacheInMemory(false)
                .showImageForEmptyUri(R.drawable.merchant_cover_sm)
                .showImageOnLoading(R.drawable.merchant_cover_sm)
                .cacheOnDisk(true).build();
        typefaceBold = StaticFunction.bold(this);
        typefaceLight = StaticFunction.light(this);
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltFollowing = (RelativeLayout) findViewById(R.id.rltFollowing);
        txtAddress = (TextView) findViewById(R.id.txtOutletAddress);
        txtMerchantName = (TextView) findViewById(R.id.txtMerchantName);
        txtOpenHour = (TextView) findViewById(R.id.txtOpenHour);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        txtWebsite = (TextView) findViewById(R.id.txtWebsite);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        lnlOutlets = (LinearLayout) findViewById(R.id.lnlOutlets);
        lnlMoreDeal= (LinearLayout) findViewById(R.id.lnlMoreDeal);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        imgCover = (ImageView) findViewById(R.id.imvDeal);
        txtLabelOutlet = (TextView) findViewById(R.id.txtLabelOutlet);
        txtLabelPhoneNumber = (TextView) findViewById(R.id.txtLabelPhoneNumber);
        txtLabelWebsite = (TextView) findViewById(R.id.txtLabelWebsite);
        imvFacebook = (ImageView) findViewById(R.id.imvFacebook);
        imvInstagram = (ImageView) findViewById(R.id.imvInstagram);
        imvTwitter = (ImageView) findViewById(R.id.imvTwitter);
        imgFollowing = (ImageView) findViewById(R.id.imgFollowing);
        nstListView = (NestedListView) findViewById(R.id.lstViewDeals);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        lnlViewMore = (LinearLayout) findViewById(R.id.lnlViewReviewMore);
        lnlReview = (LinearLayout) findViewById(R.id.lnlReview);
        txtYay = (TextView) findViewById(R.id.txtYay);
        txtNay = (TextView) findViewById(R.id.txtNay);
        txtLabelMoreDeal = (TextView) findViewById(R.id.txtLabelMoreDeal);
        txtLabelReview = (TextView) findViewById(R.id.txtLabelReview);
        lnlOutlet = (LinearLayout) findViewById(R.id.lnlOutlet);
        txtLabelMoreDeal.setTypeface(typefaceBold);
        txtLabelMoreDeal.setTypeface(typefaceBold);
        rltBack.setOnClickListener(this);
        txtPhoneNumber.setOnClickListener(this);
        rltFollowing.setOnClickListener(this);
        txtWebsite.setOnClickListener(this);
        imvFacebook.setOnClickListener(this);
        imvInstagram.setOnClickListener(this);
        imvTwitter.setOnClickListener(this);
        lnlViewMore.setOnClickListener(this);
    }

    private void initialIntent() {
        if (getIntent().getExtras() != null) {
            dealId = getIntent().getExtras().getLong("deal_id", 0);
            merchantId = getIntent().getLongExtra("merchant_id", 0);
        }
    }

    private void loadDataMerchant() {

        merchant = MerchantController.getById(this, merchantId);

        if (merchant != null)
            setupMerchantData(merchant);

        Intent intentGetMerchant = new Intent(this, RealTimeService.class);
        intentGetMerchant.setAction(RealTimeService.ACTION_GET_MERCHANT);
        intentGetMerchant.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchantId + "");
        intentGetMerchant.putExtra(RealTimeService.EXTRA_DEAL_ID, "");
        startService(intentGetMerchant);

        showCustomProgressDialog();
    }


    private void registerIntentReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_LIKE_DEAL);
            intentFilter.addAction(RealTimeService.RECEIVER_UNLIKE_DEAL);
            intentFilter.addAction(DealDetailActivity.RECEIVER_EXPRITED_TIME);
            intentFilter.addAction(RECEIVER_ADD_COMMENT);
            registerReceiver(activityReceiver, intentFilter);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);

            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_MERCHANT)) {

                if (result.equals(ParallaxService.RESULT_OK)) {

                    merchant = MerchantController.getById(context, merchantId);
                    setupMerchantData(merchant);

                }

                hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT)) {
                if (result.equals(RealTimeService.RESULT_OK)) {
                    merchant = MerchantController.getById(context, merchantId);
                    if (merchant != null) {
                        merchant.setF_follow(false);
                        MerchantController.update(context, merchant);
                    }
                    DealController.updateObjectIdFliked(DealDetailMerchantActivity.this, merchantId, false);

                    setupMerchantData(merchant);

                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastOk(message);
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_FOLLOWING_MERCHANT)) {
                if (result.equals(RealTimeService.RESULT_OK)) {
                    merchant = MerchantController.getById(context, merchantId);
                    if (merchant != null) {
                        merchant.setF_follow(true);
                        MerchantController.update(context, merchant);
                    }

                    DealController.updateObjectIdFliked(DealDetailMerchantActivity.this, merchantId, true);
                    setupMerchantData(merchant);
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastOk(message);
                }
            }

            hideProgressDialog();
            if (result.equals(ParallaxService.RESULT_FAIL)) {
                String message = intent.getStringExtra(ParallaxService.EXTRA_RESULT_MESSAGE);
                showToastError(message);
            } else if (result.equals(ParallaxService.RESULT_NO_INTERNET)) {
                showToastError(getString(R.string.nointernet));
            }
        }

    };


    private void setupMerchantData(Merchant merchant) {
        if (merchant != null) {

            if (merchant.getCover_image() != null && merchant.getCover_image().length() > 0) {
                imageLoader.displayImage(merchant.getCover_image(), imgCover, options1);
            }

            if (merchant.getLogo_image() != null && merchant.getLogo_image().length() > 0) {
                imageLoader.displayImage(merchant.getLogo_image(), imgAvatar, options);
            }

            txtMerchantName.setText(merchant.getOrganization_name());
            txtLabelMoreDeal.setText("MORE DEAL BY " + merchant.getOrganization_name());

            txtPhoneNumber.setText(merchant.getPhone().length() == 3 ? "Not Available" : merchant.getPhone());
            txtWebsite.setText(merchant.getWebsite_url().length() == 0 ? "Not Available" : merchant.getWebsite_url());
            txtDescription.setText(merchant.getDescription());

            if (merchant.getF_follow()) {
                imgFollowing.setImageResource(R.drawable.ic_heart_active);
            } else {
                imgFollowing.setImageResource(R.drawable.ic_heart_inactive);
            }

            txtYay.setText(merchant.getK_likes() + "");
            txtNay.setText(merchant.getK_unlikes() + "");

            // Set OutLetInfo
            List<Outlet> listOutlet = OutletController.getOutletsByMerchantId(DealDetailMerchantActivity.this, String.valueOf(merchantId));

            if (listOutlet != null && listOutlet.size() > 0) {
                Outlet outlet = listOutlet.get(0);
                String address = (outlet.getAddress1().trim().length() > 0) ? outlet.getAddress1() + " " : "";
                address += outlet.getAddress2();
                txtAddress.setText(address.trim());

                final MyLocation mLoc = MyLocationController.getLastLocation(this);

                if (mLoc != null) {

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
                        setListOutlets(mLoc, listOutlet, -1l);

                    } else {
                        txtDistance.setText("Not Available ");
                        txtAddress.setText("Not Available ");
                    }
                } else {
                    txtDistance.setText("Not Available ");
                }
            } else {
                lnlOutlet.setVisibility(View.GONE);
            }

            // Set Share View
            if (merchant.getFacebook_url().length() == 0) {
                imvFacebook.setVisibility(View.GONE);
            } else {
                imvFacebook.setVisibility(View.VISIBLE);
            }
            if (merchant.getTwitter_url().length() == 0) {
                imvTwitter.setVisibility(View.GONE);
            } else {
                imvTwitter.setVisibility(View.VISIBLE);
            }
            if (merchant.getInstagram_url().length() == 0) {
                imvInstagram.setVisibility(View.GONE);
            } else {
                imvInstagram.setVisibility(View.VISIBLE);
            }

            // set more deals
            DealMoreMerchantDetailAdapter dealHomeAdapter;
            List<ObjectDeal> lstObjectDeal = DealController.getDealByMerchantId(this, merchant.getMerchant_id());

            if (lstObjectDeal != null && lstObjectDeal.size() > 3) {

                List<ObjectDeal> lstObjectDealTmp = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    lstObjectDealTmp.add(lstObjectDeal.get(i));
                }
                dealHomeAdapter = new DealMoreMerchantDetailAdapter(this, lstObjectDealTmp);
                lnlViewMore.setVisibility(View.VISIBLE);
            } else {
                dealHomeAdapter = new DealMoreMerchantDetailAdapter(this, lstObjectDeal);
                lnlViewMore.setVisibility(View.GONE);
            }

            if(lstObjectDeal==null||lstObjectDeal.size()==0){
                lnlMoreDeal.setVisibility(View.GONE);
            }

            dealHomeAdapter.setOnPopupFollowingListener(new OnPopUpFollowingListener() {
                @Override
                public void onActionStart(long merchantId) {
                    showProgressDialog();
                }
            });

            nstListView.setFocusable(false);
            nstListView.setAdapter(dealHomeAdapter);

            //set Reviews
            setListReview();
        }
    }

    private void setListOutlets(MyLocation mLoc, List<Outlet> objectDealOutlets, Long idOutletNearest) {


        lnlOutlets.removeAllViews();
        for (final Outlet outlet : objectDealOutlets) {
            if (!outlet.getOutlet_id().equals(idOutletNearest)) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewRow = inflater.inflate(R.layout.row_other_outlet, null);
                LinearLayout root = (LinearLayout) viewRow.findViewById(R.id.root);
                TextView txtOutlet = (TextView) viewRow.findViewById(R.id.txtOutlet);
                TextView txtAddress = (TextView) viewRow.findViewById(R.id.txtAddress);
                TextView txtPhone = (TextView) viewRow.findViewById(R.id.txtPhone);

                txtOutlet.setTypeface(typefaceBold);
                txtAddress.setTypeface(typefaceLight);
                txtPhone.setTypeface(typefaceLight);
                txtOutlet.setText(outlet.getName());
                txtAddress.setText(outlet.getAddress1() + " " + outlet.getAddress2());

                String phone = (outlet.getPhone().trim().length() > 0) ? outlet.getPhone() : "Not available";
                txtPhone.setText(phone);

                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupConfirmToMap(getString(R.string.open_map), outlet);
                    }
                });

                lnlOutlets.addView(viewRow);
            }
        }
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


    public void showPopupConfirmToFollowing() {
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
        txtMessage.setText(getTitleText("Follow ", merchant.getOrganization_name()), TextView.BufferType.SPANNABLE);

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

    public void showPopupConfirmToUnFollowing() {
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
        txtMessage.setText(getTitleText("Unfollow ", merchant.getOrganization_name()), TextView.BufferType.SPANNABLE);

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


    private void viewUrl(int mode) {
        Merchant merchant = MerchantController.getById(this, merchantId);
        if (merchant != null) {
            if (mode == Const.SOCIAL.FACEBOOK) {
                startActivity(newFacebookIntent(getPackageManager(), merchant.getFacebook_url()));
            } else if (mode == Const.SOCIAL.TWITTER) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(merchant.getTwitter_url()));
                startActivity(intent);
            } else if (mode == Const.SOCIAL.INSTAGRAM) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(merchant.getInstagram_url()));
                startActivity(intent);
            } else if (mode == Const.SOCIAL.WEBSITE) {
                if (merchant.getWebsite_url().length() > 0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(merchant.getWebsite_url()));
                    startActivity(intent);
                }
            }
        }
    }

    private Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri;
        try {
            pm.getPackageInfo("com.facebook.katana", 0);
            // http://stackoverflow.com/a/24547437/1048340
            uri = Uri.parse("fb://facewebmodal/f?href=" + url);
        } catch (PackageManager.NameNotFoundException e) {
            uri = Uri.parse(url);
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    private void followingMerchant() {
        if (UserController.isLogin(this)) {
            String consumer_id = UserController.getCurrentUser(this).getConsumer_id() + "";
            Merchant merchant = MerchantController.getById(this, merchantId);
            if (merchant != null) {
                if (merchant.getF_follow()) {
                    Intent intentGetDealDetail = new Intent(this, RealTimeService.class);
                    intentGetDealDetail.setAction(RealTimeService.ACTION_STOP_FOLLOWING_MERCHANT);
                    intentGetDealDetail.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                    intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchantId + "");
                    startService(intentGetDealDetail);
                    showProgressDialog();
                } else {
                    Intent intentGetDealDetail = new Intent(this, RealTimeService.class);
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
    }

    private void setListReview() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Light.otf");
        List<Review> listComment = ReviewController.getReviewByMerchantId(this, merchantId + "");

        int size = listComment.size() > 3 ? 3 : listComment.size();

        lnlReview.removeAllViews();
        for (int i = 0; i < size; i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewRow = inflater.inflate(R.layout.row_review_v2, null);
            ImageView imvAvatar = (ImageView) viewRow.findViewById(R.id.imgAvatar);
            TextView txtUsername = (TextView) viewRow.findViewById(R.id.txtUserName);
            TextView txtDate = (TextView) viewRow.findViewById(R.id.txtDate);
            final TextView txtReView = (TextView) viewRow.findViewById(R.id.txtReView);
            final TextView txtReadmore = (TextView) viewRow.findViewById(R.id.txtReadmore);
            final LinearLayout lnlYayOrNay = (LinearLayout) viewRow.findViewById(R.id.lnlYayOrNay);
            final ImageView imgYayOrNay = (ImageView) viewRow.findViewById(R.id.imgYayOrNay);

            boolean isYay = listComment.get(i).getIs_yay();

            if (listComment.get(i).getProfile_image() != null && listComment.get(i).getProfile_image().length() > 0 && !listComment.get(i).getProfile_image().equals("null")) {
                imageLoader.displayImage(listComment.get(i).getProfile_image(), imvAvatar, options);
            } else {
                if (isYay) {
                    imvAvatar.setImageResource(R.drawable.ic_avatar_blue);
                } else {
                    imvAvatar.setImageResource(R.drawable.ic_avatar_red);
                }
            }

            txtDate.setText(StaticFunction.parseDateToddMMyyyy(listComment.get(i).getCreated_at()));


            if (isYay) {
                imgYayOrNay.setImageResource(R.drawable.ic_yay_white);
                lnlYayOrNay.setBackgroundResource(R.drawable.btn_yay_green);
            } else {
                imgYayOrNay.setImageResource(R.drawable.ic_nya_white);
                lnlYayOrNay.setBackgroundResource(R.drawable.btn_nay_red);
            }

            txtUsername.setText((listComment.get(i).getFullname() != null && !listComment.get(i).getFullname().equals("null")) ? listComment.get(i).getFullname() : "");
            txtReView.setText((listComment.get(i).getReview() != null) ? listComment.get(i).getReview() : "");
            txtReView.setMaxLines(2);


            txtUsername.setTypeface(typeface);
            txtDate.setTypeface(typeface);
            txtReView.setTypeface(typeface);
            txtReadmore.setTypeface(typeface);


            lnlReview.addView(viewRow);
        }

        if (size < 4) {
            lnlViewMore.setVisibility(View.GONE);
        } else {
            lnlViewMore.setVisibility(View.VISIBLE);
        }
    }

    private String changeDateFormat(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        return day + "/" + month + "/" + year;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imvFacebook:
                viewUrl(Const.SOCIAL.FACEBOOK);
                break;
            case R.id.imvTwitter:
                viewUrl(Const.SOCIAL.TWITTER);
                break;
            case R.id.imvInstagram:
                viewUrl(Const.SOCIAL.INSTAGRAM);
                break;
            case R.id.txtWebsite:
                viewUrl(Const.SOCIAL.WEBSITE);
                break;
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltFollowing:
                if (merchant.getF_follow()) {
                    showPopupConfirmToUnFollowing();
                } else {
                    showPopupConfirmToFollowing();
                }

                break;
            case R.id.lnlViewReviewMore:
                Intent intent = new Intent(DealDetailMerchantActivity.this, ReviewsActivity.class);
                intent.putExtra(Const.BUNDLE_EXTRAS.MERCHANT_ID, merchantId);
                startActivity(intent);
                break;
            case R.id.txtPhoneNumber:
                if(merchant.getPhone().trim().length()>3) {
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + merchant.getPhone()));
                    startActivity(intentPhone);
                }
                break;
        }
    }
}
