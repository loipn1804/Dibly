package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.fragment.DetailMerchantFragment;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;

import org.json.JSONException;
import org.json.JSONObject;

import greendao.ObjectDeal;
import greendao.ObjectUser;

/**
 * Created by USER on 6/29/2015.
 */
public class ClaimSuccessActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtMessage;
    private TextView txtDealTitle;
    private TextView txtMerchantName;
    private ImageView imvLike;
    private ImageView imvDislike;
    private EditText edtComment;
    private Button btnPostReview;

    private String message;
    private String merchant_id;
    private String deal_id;
    private TextView txtYourLike;
    private TextView txtYourUnLike;
    private ProgressBar progLike;
    private ProgressBar progUnLike;

    private boolean isYayNay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_success);

        overrideFontsLight(findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_DETAIL_TO_PROMPT_CLAIMED_SUCCESS);
            intentFilter.addAction(RealTimeService.RECEIVER_LIKE_DEAL);
            intentFilter.addAction(RealTimeService.RECEIVER_UNLIKE_DEAL);
            intentFilter.addAction(RealTimeService.RECEIVER_ADD_COMMENT_DEAL);
            registerReceiver(activityReceiver, intentFilter);
        }

        if (getIntent().getExtras() != null) {
            message = getIntent().getExtras().getString("message");
            deal_id = getIntent().getExtras().getString("deal_id");
            merchant_id = getIntent().getExtras().getString("merchant_id");

            Intent intentGetDeal = new Intent(ClaimSuccessActivity.this, RealTimeService.class);
            intentGetDeal.setAction(RealTimeService.ACTION_GET_DEAL_DETAIL_TO_PROMPT_CLAIMED_SUCCESS);
            intentGetDeal.putExtra(RealTimeService.EXTRA_DEAL_ID, deal_id);
            startService(intentGetDeal);

            showProgressDialog();
        } else {
            finish();
        }

        initView();
        initData();
    }

    private void initView() {
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        txtDealTitle = (TextView) findViewById(R.id.txtDealTitle);
        txtMerchantName = (TextView) findViewById(R.id.txtMerchantName);
        imvLike = (ImageView) findViewById(R.id.imvLike);
        imvDislike = (ImageView) findViewById(R.id.imvDislike);
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnPostReview = (Button) findViewById(R.id.btnPostReview);
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtYourLike = (TextView) findViewById(R.id.txtYourLike);
        txtYourUnLike = (TextView) findViewById(R.id.txtYourUnLike);
        progLike = (ProgressBar) findViewById(R.id.progLike);
        progUnLike = (ProgressBar) findViewById(R.id.progUnLike);

        progLike.setVisibility(View.GONE);
        progUnLike.setVisibility(View.GONE);

        imvLike.setOnClickListener(this);
        imvDislike.setOnClickListener(this);
        btnPostReview.setOnClickListener(this);
        rltBack.setOnClickListener(this);
    }

    private void initData() {
        isYayNay = false;
        txtMessage.setText(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
            case R.id.imvLike:
                if (UserController.isLogin(ClaimSuccessActivity.this)) {
                    if (deal_id != null && deal_id.length() > 0) {
                        ObjectDeal deal = DealController.getDealByDealId(ClaimSuccessActivity.this, Long.parseLong(deal_id));
                        if (deal != null) {
                            if (!deal.getF_yay()) {
                                String consumer_id = UserController.getCurrentUser(ClaimSuccessActivity.this).getConsumer_id() + "";
                                Intent intentLike = new Intent(ClaimSuccessActivity.this, RealTimeService.class);
                                intentLike.setAction(RealTimeService.ACTION_LIKE_DEAL);
                                intentLike.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                                intentLike.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchant_id);
                                intentLike.putExtra(RealTimeService.EXTRA_DEAL_ID, deal_id);
                                ClaimSuccessActivity.this.startService(intentLike);
//                                    showProgressDialog();
                                progLike.setVisibility(View.VISIBLE);
                                yayMixPanel(deal);
                            }
                        }
                    }
                } else {
                    showToastInfo(getString(R.string.login_first));
                }
                break;
            case R.id.imvDislike:
                if (UserController.isLogin(ClaimSuccessActivity.this)) {
                    if (deal_id != null && deal_id.length() > 0) {
                        ObjectDeal deal = DealController.getDealByDealId(ClaimSuccessActivity.this, Long.parseLong(deal_id));
                        if (deal != null) {
                            if (!deal.getF_nay()) {
                                String consumer_id = UserController.getCurrentUser(ClaimSuccessActivity.this).getConsumer_id() + "";
                                Intent intentUnLike = new Intent(ClaimSuccessActivity.this, RealTimeService.class);
                                intentUnLike.setAction(RealTimeService.ACTION_UNLIKE_DEAL);
                                intentUnLike.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                                intentUnLike.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchant_id);
                                intentUnLike.putExtra(RealTimeService.EXTRA_DEAL_ID, deal_id);
                                ClaimSuccessActivity.this.startService(intentUnLike);
//                                    showProgressDialog();
                                progUnLike.setVisibility(View.VISIBLE);
                                nayMixPanel(deal);
                            }
                        }
                    }
                } else {
                    showToastInfo(getString(R.string.login_first));
                }
                break;
            case R.id.btnPostReview:
                if (isYayNay) {
                    String text = edtComment.getText().toString().trim();
                    if (text.length() > 0) {
                        if (UserController.isLogin(ClaimSuccessActivity.this)) {
                            String consumer_id = UserController.getCurrentUser(ClaimSuccessActivity.this).getConsumer_id() + "";
                            Intent intentCmt = new Intent(ClaimSuccessActivity.this, RealTimeService.class);
                            intentCmt.setAction(RealTimeService.ACTION_ADD_COMMENT_DEAL);
                            intentCmt.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                            intentCmt.putExtra(RealTimeService.EXTRA_DEAL_ID, deal_id);
                            intentCmt.putExtra(RealTimeService.EXTRA_COMMENT_DEAL_TEXT, text);
                            ClaimSuccessActivity.this.startService(intentCmt);
                            showProgressDialog();
                        } else {
                            showToastInfo(getString(R.string.login_first));
                        }
                    } else {
                        showToastError("Please enter your review.");
                    }
                } else {
                    showPopupPrompt("Please choose YAY or NAY to review this deal!");
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
    }

    private void setLikeAndUnlike(ObjectDeal objectDeal) {
        if (UserController.isLogin(ClaimSuccessActivity.this)) {
            if (objectDeal.getF_yay()) {
                imvLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                txtYourLike.setVisibility(View.VISIBLE);
            } else {
                imvLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                txtYourLike.setVisibility(View.INVISIBLE);
            }
            if (objectDeal.getF_nay()) {
                imvDislike.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlike));
                txtYourUnLike.setVisibility(View.VISIBLE);
            } else {
                imvDislike.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlike));
                txtYourUnLike.setVisibility(View.INVISIBLE);
            }
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_DETAIL_TO_PROMPT_CLAIMED_SUCCESS)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    String deal_title = intent.getStringExtra(RealTimeService.EXTRA_RESULT_DEAL_TITLE);
                    String merchant_name = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MERCHANT_NAME);
                    txtDealTitle.setText(deal_title);
                    txtMerchantName.setText(merchant_name);
                    if (deal_id != null && deal_id.length() > 0) {
                        ObjectDeal deal = DealController.getDealByDealId(ClaimSuccessActivity.this, Long.parseLong(deal_id));
                        if (deal != null) {
                            setLikeAndUnlike(deal);
                        }
                    }
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_LIKE_DEAL)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    isYayNay = true;
                    if (deal_id != null && deal_id.length() > 0) {
                        ObjectDeal deal = DealController.getDealByDealId(ClaimSuccessActivity.this, Long.parseLong(deal_id));
                        if (deal != null) {
                            deal.setF_yay(true);
                            deal.setK_likes(deal.getK_likes() + 1);
                            if (deal.getF_nay()) {
                                deal.setF_nay(false);
                                deal.setK_unlikes(deal.getK_unlikes() - 1);
                            }
                            DealController.update(ClaimSuccessActivity.this, deal);
                            setLikeAndUnlike(deal);
                        }
                    }
//                    showToastOk(message);
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
//                    showToastInfo(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideProgressDialog();
                progLike.setVisibility(View.GONE);
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_UNLIKE_DEAL)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    isYayNay = true;
                    if (deal_id != null && deal_id.length() > 0) {
                        ObjectDeal deal = DealController.getDealByDealId(ClaimSuccessActivity.this, Long.parseLong(deal_id));
                        if (deal != null) {
                            deal.setF_nay(true);
                            deal.setK_unlikes(deal.getK_unlikes() + 1);
                            if (deal.getF_yay()) {
                                deal.setF_yay(false);
                                deal.setK_likes(deal.getK_likes() - 1);
                            }
                            DealController.update(ClaimSuccessActivity.this, deal);
                            setLikeAndUnlike(deal);
                        }
                    }
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {

                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideProgressDialog();
                progUnLike.setVisibility(View.GONE);
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_ADD_COMMENT_DEAL)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    edtComment.setText("");
                    showToastOk(message);
                    hideProgressDialog();
                    Intent intentThank = new Intent(ClaimSuccessActivity.this, ClaimSuccessThankActivity.class);
                    intentThank.putExtra("id", Long.parseLong(merchant_id));
                    intentThank.putExtra("name", txtMerchantName.getText().toString());
                    startActivity(intentThank);
                    StaticFunction.sendBroad(ClaimSuccessActivity.this, DetailMerchantFragment.RECEIVER_ADD_COMMENT);
                    finish();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            }
        }
    };

    private void yayMixPanel(ObjectDeal deal) {
        ObjectUser user = UserController.getCurrentUser(this);
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            object.put("email", user.getEmail());
            object.put("merchant id", deal.getMerchant_id());
            object.put("merchant name", deal.getOrganization_name());
            object.put("YayOrNay", "Yay");
            object.put("feedback content", edtComment.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Yay_Nay), object);
    }

    private void nayMixPanel(ObjectDeal deal) {
        ObjectUser user = UserController.getCurrentUser(this);
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            object.put("email", user.getEmail());
            object.put("merchant id", deal.getMerchant_id());
            object.put("merchant name", deal.getOrganization_name());
            object.put("YayOrNay", "Nay");
            object.put("feedback content", edtComment.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Yay_Nay), object);
    }
}
