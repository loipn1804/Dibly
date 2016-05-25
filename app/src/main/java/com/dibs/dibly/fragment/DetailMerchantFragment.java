package com.dibs.dibly.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.AllOutletActivity;
import com.dibs.dibly.activity.AllReviewActivity;
import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.activity.MerchantDealActivity;
import com.dibs.dibly.activity.MerchantPictureActivity;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.CommentController;
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
import org.json.JSONObject;

import java.util.List;

import greendao.Comment;
import greendao.Merchant;

/**
 * Created by USER on 7/1/2015.
 */
public class DetailMerchantFragment extends Fragment implements View.OnClickListener { //, OnMapReadyCallback

    public static final String RECEIVER_ADD_COMMENT = "RECEIVER_ADD_COMMENT";

    private Long dealId;
    private Long merchantId;
    private boolean isDealDetail;
//    private ObjectDeal objectDeal;

    private LinearLayout lnlReview;
    private LinearLayout lnlViewAllReview;
    private LinearLayout lnlViewAllOutlet;
    private LinearLayout lnlFollowing;
    private LinearLayout lnlLike;
    private LinearLayout lnlUnLike;
    private LinearLayout btnCallMerchant;

    private TextView txtAddress;
    private TextView txtPhone;
    private TextView txtLike;
    private TextView txtUnLike;
    private LinearLayout btnViewMap;
    private ImageView imvLike;
    private ImageView imvUnLike;
    private TextView txtYourLike;
    private TextView txtYourUnLike;
    private ImageView imvMerchantPicture;
    private TextView txtMerchantName;
    private TextView txtViewMoreImages;
    private TextView txtFollowing;
    private ImageView imvFacebook;
    private ImageView imvTwitter;
    private ImageView imvInstagram;
    private TextView txtNewDeal;
    private TextView txtLabelNewDeal;
    private TextView txtPastDeal;
    private TextView txtLabelPastDeal;
    private ProgressBar progLike;
    private ProgressBar progUnLike;

    //    private MapFragment mapFragment;
    private ImageView imageMap;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private DisplayImageOptions optionsPicture;

    private boolean isExprited;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_merchant, container, false);

        isExprited = false;

        ((BaseActivity) getActivity()).overrideFontsLight(view);

        options = new DisplayImageOptions.Builder().cacheInMemory(false).displayer(new RoundedBitmapDisplayer(500)).showImageForEmptyUri(R.color.transparent).showImageOnLoading(R.color.transparent).cacheOnDisk(true).build();
        optionsPicture = new DisplayImageOptions.Builder().cacheInMemory(true).displayer(new RoundedBitmapDisplayer(500)).showImageForEmptyUri(R.drawable.menu_person).showImageOnLoading(R.drawable.menu_person).cacheOnDisk(true).build();

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_MERCHANT);
            intentFilter.addAction(ParallaxService.RECEIVER_GET_COMMENT_BY_DEAL_ID);
            intentFilter.addAction(ParallaxService.RECEIVER_GET_COMMENT_BY_MERCHANT_ID);
            intentFilter.addAction(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_LIKE_DEAL);
            intentFilter.addAction(RealTimeService.RECEIVER_UNLIKE_DEAL);
            intentFilter.addAction(DealDetailActivity.RECEIVER_EXPRITED_TIME);
            intentFilter.addAction(RECEIVER_ADD_COMMENT);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }

        if (getActivity().getIntent().getExtras() != null) {
            dealId = getActivity().getIntent().getLongExtra("deal_id", 0l);
            merchantId = getActivity().getIntent().getLongExtra("merchant_id", 0l);
            isDealDetail = getActivity().getIntent().getBooleanExtra("isDealDetail", true);

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
        lnlReview = (LinearLayout) view.findViewById(R.id.lnlReview);
        lnlViewAllReview = (LinearLayout) view.findViewById(R.id.lnlViewAllReview);
        txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        txtPhone = (TextView) view.findViewById(R.id.txtPhone);
        txtLike = (TextView) view.findViewById(R.id.txtLike);
        txtUnLike = (TextView) view.findViewById(R.id.txtUnLike);
        lnlViewAllOutlet = (LinearLayout) view.findViewById(R.id.lnlViewAllOutlet);
        lnlFollowing = (LinearLayout) view.findViewById(R.id.lnlFollowing);
        lnlLike = (LinearLayout) view.findViewById(R.id.lnlLike);
        lnlUnLike = (LinearLayout) view.findViewById(R.id.lnlUnLike);
        imageMap = (ImageView) view.findViewById(R.id.imageMap);
        btnCallMerchant = (LinearLayout) view.findViewById(R.id.btnCallMerchant);
        btnViewMap = (LinearLayout) view.findViewById(R.id.btnViewMap);
        imvLike = (ImageView) view.findViewById(R.id.imvLike);
        imvUnLike = (ImageView) view.findViewById(R.id.imvUnLike);
        txtYourLike = (TextView) view.findViewById(R.id.txtYourLike);
        txtYourUnLike = (TextView) view.findViewById(R.id.txtYourUnLike);
        imvMerchantPicture = (ImageView) view.findViewById(R.id.imvMerchantPicture);
        txtMerchantName = (TextView) view.findViewById(R.id.txtMerchantName);
        txtViewMoreImages = (TextView) view.findViewById(R.id.txtViewMoreImages);
        txtFollowing = (TextView) view.findViewById(R.id.txtFollowing);
        imvFacebook = (ImageView) view.findViewById(R.id.imvFacebook);
        imvTwitter = (ImageView) view.findViewById(R.id.imvTwitter);
        imvInstagram = (ImageView) view.findViewById(R.id.imvInstagram);
        txtNewDeal = (TextView) view.findViewById(R.id.txtNewDeal);
        txtLabelNewDeal = (TextView) view.findViewById(R.id.txtLabelNewDeal);
        txtPastDeal = (TextView) view.findViewById(R.id.txtPastDeal);
        txtLabelPastDeal = (TextView) view.findViewById(R.id.txtLabelPastDeal);
        progLike = (ProgressBar) view.findViewById(R.id.progLike);
        progUnLike = (ProgressBar) view.findViewById(R.id.progUnLike);

        progLike.setVisibility(View.GONE);
        progUnLike.setVisibility(View.GONE);

        lnlViewAllReview.setOnClickListener(this);
        lnlViewAllOutlet.setOnClickListener(this);
        lnlFollowing.setOnClickListener(this);
//        lnlLike.setOnClickListener(this);
//        lnlUnLike.setOnClickListener(this);
        imageMap.setOnClickListener(this);
        btnCallMerchant.setOnClickListener(this);
        btnViewMap.setOnClickListener(this);
        txtFollowing.setOnClickListener(this);
        imvFacebook.setOnClickListener(this);
        imvTwitter.setOnClickListener(this);
        imvInstagram.setOnClickListener(this);
        txtPhone.setOnClickListener(this);
        txtNewDeal.setOnClickListener(this);
        txtLabelNewDeal.setOnClickListener(this);
//        txtPastDeal.setOnClickListener(this);
//        txtLabelPastDeal.setOnClickListener(this);

        txtFollowing.setVisibility(View.INVISIBLE);

        SpannableString content = new SpannableString(txtViewMoreImages.getText());
        content.setSpan(new UnderlineSpan(), 0, txtViewMoreImages.getText().length(), 0);//where first 0 shows the starting and udata.length() shows the ending span.if you want to span only part of it than you can change these values like 5,8 then it will underline part of it.
        txtViewMoreImages.setText(content);
    }

    private void initData() {
//        setListReview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnlViewAllReview:
                Intent intentViewAllReview = new Intent(getActivity(), AllReviewActivity.class);
                intentViewAllReview.putExtra("deal_id", dealId);
                intentViewAllReview.putExtra("merchant_id", merchantId);
                intentViewAllReview.putExtra("isDealDetail", isDealDetail);
                intentViewAllReview.putExtra("isExprited", isExprited);
                getActivity().startActivityForResult(intentViewAllReview, AllReviewActivity.ALL_REVIEW_REQUEST);
                break;
            case R.id.lnlViewAllOutlet:
                Intent intentOutlet = new Intent(getActivity(), AllOutletActivity.class);
                intentOutlet.putExtra("merchant_id", merchantId + "");
                getActivity().startActivity(intentOutlet);
                break;
            case R.id.lnlFollowing:
//                if (UserController.isLogin(getActivity())) {
//                    String consumer_id = UserController.getCurrentUser(getActivity()).getConsumer_id() + "";
//                    Intent intentGetDealDetail = new Intent(getActivity(), RealTimeService.class);
//                    intentGetDealDetail.setAction(RealTimeService.ACTION_FOLLOWING_MERCHANT);
//                    intentGetDealDetail.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
//                    intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchantId + "");
//                    getActivity().startService(intentGetDealDetail);
//                    ((BaseActivity) getActivity()).showProgressDialog();
//                } else {
//                    ((BaseActivity) getActivity()).showToastInfo(getString(R.string.login_first));
//                }
                break;
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
            case R.id.txtViewMoreImages:
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

    private void setListReview() {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ProximaNova-Light.otf");
        List<Comment> listComment = CommentController.getAll(getActivity());

        int size = listComment.size() > 3 ? 3 : listComment.size();

        lnlReview.removeAllViews();
        for (int i = 0; i < size; i++) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewRow = inflater.inflate(R.layout.row_review, null);
            RelativeLayout rltRoot = (RelativeLayout) viewRow.findViewById(R.id.rltRoot);
            ImageView imvAvatar = (ImageView) viewRow.findViewById(R.id.imvAvatar);
            TextView txtUsername = (TextView) viewRow.findViewById(R.id.txtUsername);
            TextView txtDate = (TextView) viewRow.findViewById(R.id.txtDate);
            final TextView txtReView = (TextView) viewRow.findViewById(R.id.txtReView);
            final TextView txtReadmore = (TextView) viewRow.findViewById(R.id.txtReadmore);
            TextView txtAvatarName = (TextView) viewRow.findViewById(R.id.txtAvatarName);
            TextView txtDealTitle = (TextView) viewRow.findViewById(R.id.txtDealTitle);

            rltRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            imageLoader.displayImage(listComment.get(i).getProfile_image(), imvAvatar, options);

            txtUsername.setText(listComment.get(i).getFirst_name() + " " + listComment.get(i).getLast_name());
            txtDate.setText(changeDateFormat(listComment.get(i).getCreated_at()));
            txtReView.setText(listComment.get(i).getText());
            txtReView.setMaxLines(3);
            txtReadmore.setVisibility(View.GONE);

            if (listComment.get(i).getDeal_id() == 0l) {
                txtDealTitle.setVisibility(View.GONE);
            } else {
                txtDealTitle.setVisibility(View.VISIBLE);
                txtDealTitle.setText(listComment.get(i).getTitle());
            }

            String avatarName;
            if (listComment.get(i).getFirst_name().length() > 0) {
                avatarName = listComment.get(i).getFirst_name().substring(0, 1);
            } else if (listComment.get(i).getLast_name().length() > 0) {
                avatarName = listComment.get(i).getLast_name().substring(0, 1);
            } else {
                avatarName = "";
            }
            avatarName = avatarName.toUpperCase();
            txtAvatarName.setText(avatarName);
            txtAvatarName.setTypeface(StaticFunction.bold(getActivity()));
            if (avatarName.length() > 0) {
                txtAvatarName.setBackgroundResource(StaticFunction.getBackgroundAvatarName(avatarName));
            }

            txtUsername.setTypeface(typeface);
            txtDate.setTypeface(typeface);
            txtReView.setTypeface(typeface);
            txtReadmore.setTypeface(typeface);
            txtDealTitle.setTypeface(StaticFunction.bold(getActivity()));

            lnlReview.addView(viewRow);
        }

        if (size < 3) {
            lnlViewAllReview.setVisibility(View.GONE);
        } else {
            lnlViewAllReview.setVisibility(View.VISIBLE);
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

    private void setLikeAndUnlike(Merchant merchant) {
        txtLike.setText(merchant.getK_likes() + " ");
        txtUnLike.setText(merchant.getK_unlikes() + " ");
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
                        setLikeAndUnlike(merchant);
                        txtMerchantName.setText(merchant.getOrganization_name());

                        txtAddress.setText(merchant.getPhone().length() == 0 ? "Not Available" : merchant.getPhone());
                        txtPhone.setText(merchant.getPhone().length() == 0 ? "Not Available" : merchant.getWebsite_url());

                        txtFollowing.setVisibility(View.VISIBLE);
                        if (merchant.getF_follow()) {
                            txtFollowing.setText("Unfollow");
                        } else {
                            txtFollowing.setText("Follow");
                        }

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
                            } else if (num == 1) {
                                txtViewMoreImages.setText("");
                                JSONObject image = array_gallery_images.getJSONObject(0);
                                imageLoader.displayImage(image.getString("image_thumbnail_url"), imvMerchantPicture, optionsPicture);
                                txtViewMoreImages.setOnClickListener(DetailMerchantFragment.this);
                                imvMerchantPicture.setOnClickListener(DetailMerchantFragment.this);
                            } else {
                                txtViewMoreImages.setText("View more (" + (num - 1) + " images)");
                                JSONObject image = array_gallery_images.getJSONObject(0);
                                imageLoader.displayImage(image.getString("image_thumbnail_url"), imvMerchantPicture, optionsPicture);
                                txtViewMoreImages.setOnClickListener(DetailMerchantFragment.this);
                                imvMerchantPicture.setOnClickListener(DetailMerchantFragment.this);
                            }
                        } catch (JSONException e) {
                            txtViewMoreImages.setText("");
                        }
                        SpannableString content = new SpannableString(txtViewMoreImages.getText());
                        content.setSpan(new UnderlineSpan(), 0, txtViewMoreImages.getText().length(), 0);//where first 0 shows the starting and udata.length() shows the ending span.if you want to span only part of it than you can change these values like 5,8 then it will underline part of it.
                        txtViewMoreImages.setText(content);

                        if (merchant.getLive_deals() == 1) {
                            txtNewDeal.setText("1");
                            txtLabelNewDeal.setText("Live Deal");
                        } else {
                            txtNewDeal.setText(merchant.getLive_deals() + "");
                            txtLabelNewDeal.setText("Live Deals");
                        }
                        if (merchant.getPast_deals() == 1) {
                            txtPastDeal.setText("1");
                            txtLabelPastDeal.setText("Past Deal");
                        } else {
                            txtPastDeal.setText(merchant.getPast_deals() + "");
                            txtLabelPastDeal.setText("Past Deals");
                        }
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
            } else if (intent.getAction().equalsIgnoreCase(ParallaxService.RECEIVER_GET_COMMENT_BY_DEAL_ID)) {
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    setListReview();
                } else if (result.equals(ParallaxService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(ParallaxService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastError(message);
                } else if (result.equals(ParallaxService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
            } else if (intent.getAction().equalsIgnoreCase(ParallaxService.RECEIVER_GET_COMMENT_BY_MERCHANT_ID)) {
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    setListReview();
                } else if (result.equals(ParallaxService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(ParallaxService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastError(message);
                } else if (result.equals(ParallaxService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    Merchant merchant = MerchantController.getById(getActivity(), merchantId);
                    if (merchant != null) {
                        merchant.setF_follow(false);
                        MerchantController.update(getActivity(), merchant);
                        if (merchant.getF_follow()) {
                            txtFollowing.setText("Unfollow");
                        } else {
                            txtFollowing.setText("Follow");
                        }
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
                        if (merchant.getF_follow()) {
                            txtFollowing.setText("Unfollow");
                        } else {
                            txtFollowing.setText("Follow");
                        }
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
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_LIKE_DEAL)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    Merchant merchant = MerchantController.getById(getActivity(), merchantId);
                    if (merchant != null) {
                        setLikeAndUnlike(merchant);
                    }
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_UNLIKE_DEAL)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    Merchant merchant = MerchantController.getById(getActivity(), merchantId);
                    if (merchant != null) {
                        setLikeAndUnlike(merchant);
                    }
                }
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
