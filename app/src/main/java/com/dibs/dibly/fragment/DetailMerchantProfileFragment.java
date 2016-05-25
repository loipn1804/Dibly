package com.dibs.dibly.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.activity.MerchantDealActivity;
import com.dibs.dibly.activity.MerchantPictureActivity;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;

import greendao.Merchant;

/**
 * Created by USER on 11/30/2015.
 */
public class DetailMerchantProfileFragment extends Fragment implements View.OnClickListener {

    public static final String RECEIVER_ADD_COMMENT = "RECEIVER_ADD_COMMENT";

    private Long dealId;
    private Long merchantId;
    private boolean isDealDetail;
//    private ObjectDeal objectDeal;

    private LinearLayout btnCallMerchant;
    private TextView txtAddress;
    private TextView txtPhone;
    private LinearLayout btnViewMap;
    private LinearLayout lnlViewMoreImages;
    private TextView txtViewMoreImages;
    private LinearLayout imvFacebook;
    private LinearLayout imvTwitter;
    private LinearLayout imvInstagram;
    private TextView txtDesc;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private DisplayImageOptions optionsPicture;

    private boolean isExprited;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_merchant_profile, container, false);

        isExprited = false;

        ((BaseActivity) getActivity()).overrideFontsLight(view);

        options = new DisplayImageOptions.Builder().cacheInMemory(false).displayer(new RoundedBitmapDisplayer(500)).showImageForEmptyUri(R.color.transparent).showImageOnLoading(R.color.transparent).cacheOnDisk(true).build();
        optionsPicture = new DisplayImageOptions.Builder().cacheInMemory(true).displayer(new RoundedBitmapDisplayer(500)).showImageForEmptyUri(R.drawable.menu_person).showImageOnLoading(R.drawable.menu_person).cacheOnDisk(true).build();

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_MERCHANT);
//            intentFilter.addAction(ParallaxService.RECEIVER_GET_COMMENT_BY_DEAL_ID);
//            intentFilter.addAction(ParallaxService.RECEIVER_GET_COMMENT_BY_MERCHANT_ID);
//            intentFilter.addAction(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT);
//            intentFilter.addAction(RealTimeService.RECEIVER_FOLLOWING_MERCHANT);
            intentFilter.addAction(DealDetailActivity.RECEIVER_EXPRITED_TIME);
            intentFilter.addAction(RECEIVER_ADD_COMMENT);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }

        if (getActivity().getIntent().getExtras() != null) {
            dealId = getActivity().getIntent().getLongExtra("deal_id", 0l);
            merchantId = getActivity().getIntent().getLongExtra("merchant_id", 0l);
            isDealDetail = getActivity().getIntent().getBooleanExtra("isDealDetail", true);

            if (isDealDetail) {
//                Intent intentGetComment = new Intent(getActivity(), ParallaxService.class);
//                intentGetComment.setAction(ParallaxService.ACTION_GET_COMMENT_BY_DEAL_ID);
//                intentGetComment.putExtra(ParallaxService.EXTRA_DEAL_ID, dealId + "");
//                intentGetComment.putExtra(ParallaxService.EXTRA_PAGE, "1");
//                getActivity().startService(intentGetComment);
            } else {
                Intent intentGetComment = new Intent(getActivity(), ParallaxService.class);
                intentGetComment.setAction(ParallaxService.ACTION_GET_COMMENT_BY_MERCHANT_ID);
                intentGetComment.putExtra(ParallaxService.EXTRA_MERCHANT_ID, merchantId + "");
                intentGetComment.putExtra(ParallaxService.EXTRA_PAGE, "1");
                getActivity().startService(intentGetComment);
            }

            Intent intentGetMerchant = new Intent(getActivity(), RealTimeService.class);
            intentGetMerchant.setAction(RealTimeService.ACTION_GET_MERCHANT);
            intentGetMerchant.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchantId + "");
            if (isDealDetail) {
                intentGetMerchant.putExtra(RealTimeService.EXTRA_DEAL_ID, dealId + "");
            } else {
                intentGetMerchant.putExtra(RealTimeService.EXTRA_DEAL_ID, "");
            }
            getActivity().startService(intentGetMerchant);

            ((BaseActivity) getActivity()).showCustomProgressDialog();
        }

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        txtPhone = (TextView) view.findViewById(R.id.txtPhone);
        btnCallMerchant = (LinearLayout) view.findViewById(R.id.btnCallMerchant);
        btnViewMap = (LinearLayout) view.findViewById(R.id.btnViewMap);
        lnlViewMoreImages = (LinearLayout) view.findViewById(R.id.lnlViewMoreImages);
        txtViewMoreImages = (TextView) view.findViewById(R.id.txtViewMoreImages);
        imvFacebook = (LinearLayout) view.findViewById(R.id.imvFacebook);
        imvTwitter = (LinearLayout) view.findViewById(R.id.imvTwitter);
        imvInstagram = (LinearLayout) view.findViewById(R.id.imvInstagram);
        txtDesc = (TextView) view.findViewById(R.id.txtDesc);

        btnCallMerchant.setOnClickListener(this);
        btnViewMap.setOnClickListener(this);
        imvFacebook.setOnClickListener(this);
        imvTwitter.setOnClickListener(this);
        imvInstagram.setOnClickListener(this);
        lnlViewMoreImages.setOnClickListener(DetailMerchantProfileFragment.this);

//        SpannableString content = new SpannableString(txtViewMoreImages.getText());
//        content.setSpan(new UnderlineSpan(), 0, txtViewMoreImages.getText().length(), 0);//where first 0 shows the starting and udata.length() shows the ending span.if you want to span only part of it than you can change these values like 5,8 then it will underline part of it.
//        txtViewMoreImages.setText(content);
    }

    private void initData() {
//        setListReview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtFollowing:
                if (UserController.isLogin(getActivity())) {
                    String consumer_id = UserController.getCurrentUser(getActivity()).getConsumer_id() + "";
                    Merchant merchant = MerchantController.getById(getActivity(), merchantId);
                    if (merchant != null) {
                        if (merchant.getF_follow()) {
                            Intent intentGetDealDetail = new Intent(getActivity(), RealTimeService.class);
                            intentGetDealDetail.setAction(RealTimeService.ACTION_STOP_FOLLOWING_MERCHANT);
                            intentGetDealDetail.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                            intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchantId + "");
                            getActivity().startService(intentGetDealDetail);
                            ((BaseActivity) getActivity()).showProgressDialog();
                        } else {
                            Intent intentGetDealDetail = new Intent(getActivity(), RealTimeService.class);
                            intentGetDealDetail.setAction(RealTimeService.ACTION_FOLLOWING_MERCHANT);
                            intentGetDealDetail.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                            intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchantId + "");
                            getActivity().startService(intentGetDealDetail);
                            ((BaseActivity) getActivity()).showProgressDialog();
                        }
                    }
                } else {
                    ((BaseActivity) getActivity()).showToastInfo(getString(R.string.login_first));
                }
                break;
            case R.id.btnViewMap:
                if (!txtAddress.getText().toString().trim().isEmpty() && !txtAddress.getText().toString().equalsIgnoreCase("Not Available")) {
                    StaticFunction.callPhone(getActivity(), txtAddress.getText().toString().trim());
                }
                break;
            case R.id.lnlViewMoreImages:
                Intent intentMerchantPicture = new Intent(getActivity(), MerchantPictureActivity.class);
                intentMerchantPicture.putExtra("merchant_id", merchantId);
                getActivity().startActivity(intentMerchantPicture);
                break;
            case R.id.imvMerchantPicture:
                Intent intentMerchantPicture2 = new Intent(getActivity(), MerchantPictureActivity.class);
                intentMerchantPicture2.putExtra("merchant_id", merchantId);
                getActivity().startActivity(intentMerchantPicture2);
                break;
            case R.id.txtNewDeal:
                Merchant merchantObj1 = MerchantController.getById(getActivity(), merchantId);
                if (merchantObj1 != null) {
                    Intent intentMerchant = new Intent(getActivity(), MerchantDealActivity.class);
                    intentMerchant.putExtra("id", merchantObj1.getMerchant_id());
                    intentMerchant.putExtra("name", merchantObj1.getOrganization_name());
                    getActivity().startActivity(intentMerchant);
                }
                break;
            case R.id.txtLabelNewDeal:
                Merchant merchantObj2 = MerchantController.getById(getActivity(), merchantId);
                if (merchantObj2 != null) {
                    Intent intentMerchant = new Intent(getActivity(), MerchantDealActivity.class);
                    intentMerchant.putExtra("id", merchantObj2.getMerchant_id());
                    intentMerchant.putExtra("name", merchantObj2.getOrganization_name());
                    getActivity().startActivity(intentMerchant);
                }
                break;
            case R.id.imvFacebook:
                viewUrl(1);
                break;
            case R.id.imvTwitter:
                viewUrl(2);
                break;
            case R.id.imvInstagram:
                viewUrl(3);
                break;
            case R.id.btnCallMerchant:
                viewUrl(4);
                break;
        }
    }

    private void viewUrl(int mode) { // 1.fb, 2.tw, 3.in
        Merchant merchant = MerchantController.getById(getActivity(), merchantId);
        if (merchant != null) {
            if (mode == 1) {
                startActivity(newFacebookIntent(getActivity().getPackageManager(), merchant.getFacebook_url()));
            } else if (mode == 2) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(merchant.getTwitter_url()));
                startActivity(intent);
            } else if (mode == 3) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(merchant.getInstagram_url()));
                startActivity(intent);
            } else if (mode == 4) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            ((BaseActivity) getActivity()).showToastInfo("map result");
        }
    }


    private String changeDateFormat(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        return day + "/" + month + "/" + year;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            getActivity().unregisterReceiver(activityReceiver);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_MERCHANT)) {
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    Log.e("merchant", "RECEIVER_GET_MERCHANT");
                    Merchant merchant = MerchantController.getById(getActivity(), merchantId);
                    if (merchant != null) {
                        txtDesc.setText(merchant.getDescription());
                        txtAddress.setText(merchant.getPhone().length() == 0 ? "Not Available" : merchant.getPhone());
                        txtPhone.setText(merchant.getWebsite_url().length() == 0 ? "Not Available" : merchant.getWebsite_url());
//                        if (merchant.getPhone().length() == 0) {
//                            btnViewMap.setVisibility(View.GONE);
//                        }
//                        if (merchant.getWebsite_url().length() == 0) {
//                            btnCallMerchant.setVisibility(View.GONE);
//                        }

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

                        try {
                            JSONArray array_gallery_images = new JSONArray(merchant.getProfile_images());
                            int num = array_gallery_images.length();
                            if (num == 0) {
                                txtViewMoreImages.setText("");
                                lnlViewMoreImages.setVisibility(View.GONE);
                            } else {
                                txtViewMoreImages.setText("View all merchant images (" + num + ")");
                                lnlViewMoreImages.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            txtViewMoreImages.setText("");
                        }
//                        SpannableString content = new SpannableString(txtViewMoreImages.getText());
//                        content.setSpan(new UnderlineSpan(), 0, txtViewMoreImages.getText().length(), 0);//where first 0 shows the starting and udata.length() shows the ending span.if you want to span only part of it than you can change these values like 5,8 then it will underline part of it.
//                        txtViewMoreImages.setText(content);
                    }
                } else if (result.equals(ParallaxService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(ParallaxService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastError(message);
                } else if (result.equals(ParallaxService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
                if (!isDealDetail) {
                    ((BaseActivity) getActivity()).hideCustomProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    Merchant merchant = MerchantController.getById(getActivity(), merchantId);
                    if (merchant != null) {
                        merchant.setF_follow(false);
                        MerchantController.update(getActivity(), merchant);
                    }
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastOk(message);
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastInfo(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
                ((BaseActivity) getActivity()).hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_FOLLOWING_MERCHANT)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    Merchant merchant = MerchantController.getById(getActivity(), merchantId);
                    if (merchant != null) {
                        merchant.setF_follow(true);
                        MerchantController.update(getActivity(), merchant);
                    }
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastOk(message);
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastInfo(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
                ((BaseActivity) getActivity()).hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_EXPRITED_TIME)) {
                isExprited = true;
            } else if (intent.getAction().equalsIgnoreCase(RECEIVER_ADD_COMMENT)) {
                if (isDealDetail) {
                    Intent intentGetComment = new Intent(getActivity(), ParallaxService.class);
                    intentGetComment.setAction(ParallaxService.ACTION_GET_COMMENT_BY_DEAL_ID);
                    intentGetComment.putExtra(ParallaxService.EXTRA_DEAL_ID, dealId + "");
                    intentGetComment.putExtra(ParallaxService.EXTRA_PAGE, "1");
                    getActivity().startService(intentGetComment);
                } else {
                    Intent intentGetComment = new Intent(getActivity(), ParallaxService.class);
                    intentGetComment.setAction(ParallaxService.ACTION_GET_COMMENT_BY_MERCHANT_ID);
                    intentGetComment.putExtra(ParallaxService.EXTRA_MERCHANT_ID, merchantId + "");
                    intentGetComment.putExtra(ParallaxService.EXTRA_PAGE, "1");
                    getActivity().startService(intentGetComment);
                }
            }
        }
    };

    public int dpToPx(Activity activity, int dp) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int screenWidth() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        //float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) dpWidth;
    }
}
